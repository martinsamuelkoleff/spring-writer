package io.github.martinsamuelkoleff.spring_writer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
	        .csrf(csrf -> csrf
	                .ignoringRequestMatchers("/blog/*/comments")
	            )
            .httpBasic(Customizer.withDefaults())
            .formLogin(Customizer.withDefaults())
            .logout(logout -> logout
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/")
                    .permitAll()
                )
            .sessionManagement(session -> session
                    .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                )
            .authorizeHttpRequests((authorize) -> authorize
            		.requestMatchers(
            			    "/",
            			    "/home",
            			    "/blog/**",
            			    "/css/**",
            			    "/js/**",
            			    "/images/**",
            		        "/sitemap.xml",
            		        "/robots.txt"
            			).permitAll()
                .anyRequest().authenticated()
            );

        return http.build();
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
}