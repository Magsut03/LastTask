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
                .anyRequest().authenticated();
        http
                .addFilterBefore(jwTokenFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
     }


//    public void performSignIn() {
//
//        Header headers = new Header();
//
//        headers.append('Content-Type', 'application/json');
//        headers.append('Accept', 'application/json');
//
//        headers.append('Access-Control-Allow-Origin', 'http://localhost:3000');
//        headers.append('Access-Control-Allow-Credentials', 'true');
//
//        headers.append('GET', 'POST', 'OPTIONS');
//
//        headers.append('Authorization', 'Basic ' + base64.encode(username + ":" + password));
//
//        fetch(sign_in, {
//                //mode: 'no-cors',
//                credentials: 'include',
//                method: 'POST',
//                headers: headers
//    })
//    .then(response => response.json())
//    .then(json => console.log(json))
//    .catch(error => console.log('Authorization failed : ' + error.message));
//    }

//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.setAllowedOriginPatterns(new ArrayList<>(Arrays.asList("*")));
//        configuration.setAllowCredentials(true);
//        configuration.setAllowedHeaders(Arrays.asList("Access-Control-Allow-Headers",
//                "Access-Control-Allow-Origin", "Access-Control-Request-Method", "Access-Control-Request-Headers",
//                "Origin", "Cache-Control", "Content-Type", "Authorization", "Ack", "ack", "/**", "Accept"));
//        configuration.setAllowedMethods(Arrays.asList("DELETE", "GET", "POST", "PATCH", "PUT"));
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration);
//        return source;
//    }

}



