package com.example.OvitoCourseWork.config;

import com.example.OvitoCourseWork.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig
{
    @Autowired
    private UserService userService;
    @Autowired
    private SecurityBeansConfig securityBeansConfig;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception
    {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/",
                                "/styleMainPage.css",
                                "/styleAuth.css",
                                "/Logo.ico",
                                "/MainPage",
                                "/reg",
                                "/log",
                                "/AuthPage",
                                "/RegPage"
                        ).permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/AuthPage")
                        .loginProcessingUrl("/login")
                        .usernameParameter("login")
                        .passwordParameter("simplePassword")
                        .defaultSuccessUrl("/MainSearchPage", true)
                        .failureUrl("/AuthPage?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                );

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception
    {
        return authConfig.getAuthenticationManager();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception
    {
        auth.userDetailsService(userService).passwordEncoder(securityBeansConfig.bCryptPasswordEncoder());
    }
}