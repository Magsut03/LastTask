package com.example.lasttask.config;

import com.example.lasttask.exception.jwt.AuthEntryPointJwt;
import com.example.lasttask.security.filter.JWTokenFilter;
import com.nimbusds.jose.Header;
import com.sun.net.httpserver.Headers;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        prePostEnabled = true,
        securedEnabled = true
)
public class SecurityConfig {

    private final JWTokenFilter jwTokenFilter;
    private final AuthEntryPointJwt authEntryPointJwt;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .cors()
                .configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues())
                .and()
                .exceptionHandling().authenticationEntryPoint(authEntryPointJwt)
                .and()
                .sessionManagement().sessionCreationPolicy(STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/api/auth/**").permitAll()
                .antMatchers("/api/collection/**").permitAll()
                .antMatchers("/api/admin/**").permitAll()
                .antMatchers("/api/user/**").permitAll()
                .antMatchers("/api/item/**").permitAll()
                .antMatchers("/swagger-ui.html**", "/swagger-resources/**",
                        "/v2/api-docs**", "/webjars/**", "/swagger-ui/**").permitAll()
                .anyRequest().authenticated();
        http
                .addFilterBefore(jwTokenFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
     }


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "OPTIONS", "DELETE", "PUT", "PATCH"));
        config.setAllowCredentials(true);
        config.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));

        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }

}



