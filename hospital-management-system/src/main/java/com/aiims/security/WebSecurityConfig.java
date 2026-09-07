package com.aiims.security;


import com.aiims.entity.type.RoleType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import static com.aiims.entity.type.RoleType.ADMIN;
import static com.aiims.entity.type.RoleType.DOCTOR;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@RequiredArgsConstructor
@Slf4j
@EnableMethodSecurity
public class WebSecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final HandlerExceptionResolver handlerExceptionResolver;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .authorizeHttpRequests( auth -> auth
                        .requestMatchers("/public/**","/auth/**").permitAll()
                        .requestMatchers("/admin/**").hasRole(ADMIN.name())
                        .requestMatchers("/doctors/**").hasAnyRole(DOCTOR.name(), ADMIN.name())
//                        .requestMatchers("/patients/**").hasRole("PATIENT")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .oauth2Login(OAuth2 -> OAuth2
                                .failureHandler((request, response, exception) -> {
                                    log.error("OAuth2 login failed: {}", exception.getMessage());
                                    handlerExceptionResolver.resolveException(request, response, null, exception);//passing to global exception handler
                                })
                        .successHandler(oAuth2SuccessHandler)

                )
                .exceptionHandling(exceptionHandlingConfigurer ->
                        exceptionHandlingConfigurer.accessDeniedHandler((request, response, accessDeniedException) -> {
                            handlerExceptionResolver.resolveException(request, response, null, accessDeniedException);//passing to global exception handler
                        }));



//                .formLogin();
        return httpSecurity.build();
    }

}