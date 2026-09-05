package com.aiims.service;

import com.aiims.dto.request.LoginRequestDto;
import com.aiims.dto.response.LoginResponseDto;
import com.aiims.dto.response.SignupResponseDto;
import com.aiims.entity.User;
import com.aiims.entity.type.AuthProviderType;
import com.aiims.expection.custom.UserAlreadyExistsException;
import com.aiims.mapper.UserMapper;
import com.aiims.repository.UserRepository;
import com.aiims.security.AuthUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final AuthUtil authUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDto.getUsername(), loginRequestDto.getPassword())
        );

        User user = (User) authentication.getPrincipal();
        String token = authUtil.generateAccessToken(user);
        return userMapper.toLoginResponseDto(user, token);
    }

    public User signUpInternal(LoginRequestDto signupRequestDto, AuthProviderType authProviderType, String providerId) {
        if (signupRequestDto == null || signupRequestDto.getUsername() == null || signupRequestDto.getUsername().isBlank()) {
            throw new IllegalArgumentException("Username is required");
        }

        // Check existing user
        userRepository.findByUsername(signupRequestDto.getUsername()).ifPresent(u -> {
            throw new UserAlreadyExistsException("User with username '" + signupRequestDto.getUsername() + "' already exists");
        });

        User user = User.builder()
                .username(signupRequestDto.getUsername())
                .providerId(providerId)
                .providerType(authProviderType)
                .build();

        if (authProviderType == AuthProviderType.EMAIL && signupRequestDto.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(signupRequestDto.getPassword()));
        }

        User saved = userRepository.save(user);
        log.info("Created user id={} username={} provider={}{}", saved.getId(), saved.getUsername(), saved.getProviderType(), providerId != null ? " providerId=" + providerId : "");
        return saved;
    }

    // signup controller
    public SignupResponseDto signup(LoginRequestDto signupRequestDto) {
        User user = signUpInternal(signupRequestDto, AuthProviderType.EMAIL, null);
        return userMapper.toSignupResponseDto(user);
    }

    @Transactional
    public ResponseEntity<LoginResponseDto> handleOAuth2LoginRequest(OAuth2User oAuth2User, String registrationId) {
        AuthProviderType providerType = authUtil.getProviderTypeFromRegistrationId(registrationId);
        String providerId = authUtil.determineProviderIdFromOAuth2User(oAuth2User, registrationId);

        String email = oAuth2User.getAttribute("email");

        // Try to find by provider link first
        User user = userRepository.findByProviderIdAndProviderType(providerId, providerType).orElse(null);

        if (user == null && email != null && !email.isBlank()) {
            // If an account exists with the same email but different provider, prevent linking
            User existingByEmail = userRepository.findByUsername(email).orElse(null);
            if (existingByEmail != null) {
                throw new BadCredentialsException("Email '" + email + "' is already registered with provider " + existingByEmail.getProviderType());
            }
        }

        if (user == null) {
            // Create new user for this oauth provider
            String username = authUtil.determineUsernameFromOAuth2User(oAuth2User, registrationId, providerId);

            LoginRequestDto dto = new LoginRequestDto();
            dto.setUsername(username);
            dto.setPassword(null);
            user = signUpInternal(dto, providerType, providerId);
        } else {
            // existing linked user: ensure email is synced
            if (email != null && !email.isBlank() && !email.equals(user.getUsername())) {
                user.setUsername(email);
                userRepository.save(user);
            }
        }

        LoginResponseDto loginResponseDto = new LoginResponseDto(authUtil.generateAccessToken(user), user.getId());
        return ResponseEntity.ok(loginResponseDto);
    }

}

















