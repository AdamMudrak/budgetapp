package com.example.budgetingapp.config;

import static com.example.budgetingapp.constants.config.ConfigConstants.FRONT_END_LOCAL_ALLOWED;
import static com.example.budgetingapp.constants.config.ConfigConstants.FRONT_END_REMOTE_ALLOWED;
import static com.example.budgetingapp.constants.config.ConfigConstants.POSTMAN_REMOTE_ALLOWED;
import static com.example.budgetingapp.constants.config.ConfigConstants.SWAGGER_REMOTE_ALLOWED;
import static com.example.budgetingapp.constants.security.SecurityConstants.STRENGTH;
import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

import com.example.budgetingapp.constants.config.ConfigConstants;
import com.example.budgetingapp.security.JwtAuthenticationFilter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

@EnableMethodSecurity
@RequiredArgsConstructor
@Configuration
public class SecurityConfig {
    private final UserDetailsService userDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    @Value(FRONT_END_LOCAL_ALLOWED)
    private String frontendLocalAllowed;
    @Value(FRONT_END_REMOTE_ALLOWED)
    private String frontendRemoteAllowed;
    @Value(POSTMAN_REMOTE_ALLOWED)
    private String postmanRemoteAllowed;
    //TODO to be deleted before production
    @Value(SWAGGER_REMOTE_ALLOWED)
    private String swaggerRemoteAllowed;

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder(STRENGTH);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.setAllowedOrigins(List.of(frontendLocalAllowed,
                            frontendRemoteAllowed,
                            postmanRemoteAllowed,
                            swaggerRemoteAllowed,
                            "http://localhost:8080" //TODO needs to be commented before deploy build
                    ));
                    config.setAllowCredentials(true);
                    config.setAllowedMethods(List.of(ConfigConstants.ALLOWED_METHODS));
                    config.setAllowedHeaders(List.of(ConfigConstants.ALLOWED_HEADERS));
                    return config;
                }))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        auth -> auth
                                .requestMatchers(
                                        antMatcher(ConfigConstants.AUTH_MATCHER),
                                        antMatcher(ConfigConstants.SWAGGER_MATCHER),
                                        antMatcher(ConfigConstants.SWAGGER_DOCS_MATCHER),
                                        antMatcher(ConfigConstants.ERRORS_MATCHER)
                                )
                                .permitAll()
                                .anyRequest()
                                .authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class)
                .userDetailsService(userDetailsService)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
