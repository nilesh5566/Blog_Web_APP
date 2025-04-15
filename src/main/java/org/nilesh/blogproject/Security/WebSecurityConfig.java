package org.nilesh.blogproject.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {

    private static final String[] WHITELIST = {
        "/", 
        "/login", 
        "/register",
        "/profile/**", 
        "/admin/**",
        "/editor/**",
        "/h2-console/**", 
        "/db-console/**", 
        "/css/**", 
        "/js/**", 
        "/fonts/**", 
        "/images/**",
        "/post/**",
        "/add_post/**",
        "/post_edit/**",
        "/forgot-me/**",
        "/reset_password/**",
        "/verify-otp/**",
        "/reset-password-form/**"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(WHITELIST).permitAll()
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .usernameParameter("email") // make sure your form uses "email"
                .passwordParameter("password")
                .defaultSuccessUrl("/", true).failureUrl("/login?error")
                .permitAll()
            )
          .logout(logout -> logout
    .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))
    .logoutSuccessUrl("/login?logout")
) // delete cookies if any
            .csrf(csrf -> csrf
                .ignoringRequestMatchers("/h2-console/**", "/db-console/**")
            )
            .headers(headers -> headers
                .frameOptions().disable() // Needed for H2 Console
            ).rememberMe().rememberMeParameter("remember-me");

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}