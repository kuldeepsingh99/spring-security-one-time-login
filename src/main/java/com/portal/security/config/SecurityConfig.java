package com.portal.security.config;

import com.portal.security.service.DBOneTimeTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.ott.JdbcOneTimeTokenService;
import org.springframework.security.authentication.ott.OneTimeTokenService;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    DBOneTimeTokenService dbOneTimeTokenService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(new AntPathRequestMatcher("/custom/**")).permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(Customizer.withDefaults())
                .oneTimeTokenLogin((ott) -> {
                    ott.loginProcessingUrl("/custom/submit-token"); // custom login processing url
                    ott.defaultSubmitPageUrl("/custom/submit"); // custom success url
                    ott.tokenGeneratingUrl("/custom/generate-token"); // custom token generating url
                    ott.tokenService(dbOneTimeTokenService);
                })
               .build();
    }


    @Bean
    InMemoryUserDetailsManager inMemoryUserDetailsManager() {
        var userDetails = User.withUsername("kuldeep").password("kuldeep@123").build();
        return new InMemoryUserDetailsManager(userDetails);
    }

}
