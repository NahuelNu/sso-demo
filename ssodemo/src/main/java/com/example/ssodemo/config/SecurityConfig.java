package com.example.ssodemo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.InMemoryOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig{

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
            .authorizeHttpRequests(authorizeRequests ->
                authorizeRequests.anyRequest()
                //.requestMatchers("/test/**")
                .permitAll()
            )
            .httpBasic(Customizer.withDefaults());
            // .oauth2Login(oauth2Login ->
            //     oauth2Login  
            //         .defaultSuccessUrl("/home", true)
            // )
            // .logout(logout ->
            //     logout
            //         .logoutSuccessUrl("/logout-success")
            //         .invalidateHttpSession(true)
            //         .clearAuthentication(true)
            // )
            ;
        return http.build();
    }

    // @Bean
    // OAuth2AuthorizedClientService authorizedClientService(ClientRegistrationRepository clientRegistrationRepository) {
    //     return new InMemoryOAuth2AuthorizedClientService(clientRegistrationRepository);
    // }

}
