package uk.techreturners.VirtuArt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        // Allow public access Artworks and the test H2 console
                        .requestMatchers(HttpMethod.GET, "/api/v1/artworks/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/search/**").permitAll()
                        .requestMatchers("/h2-console/**").permitAll()
                        // Secure all Exhibition related endpoints for only authenticated users.
                        .requestMatchers("/api/v1/exhibitions/**").authenticated()
                        .anyRequest().authenticated() // Requires authentication for all other unlisted requests
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> {}) // Default JWT validation
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .headers(headers -> headers
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::disable)
        );
        return httpSecurity.build();
    }
}