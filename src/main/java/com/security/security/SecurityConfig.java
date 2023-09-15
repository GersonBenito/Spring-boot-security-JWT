package com.security.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    // metodo que configure la cadena de filtros
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.csrf(config -> config.disable()) // desabilitar crosside para el manejo de formularios desde el frontend
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/api/v1/hello").permitAll(); // permitir que todos los usuarios puedan consumir este endpoint
                    auth.anyRequest().authenticated(); // cualquier otro request diferete de /hello debe de estar autenticado
                })
                .sessionManagement(session -> { // administrador de las sesiones
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                })
                .httpBasic()
                .and()
                .build();
    }

    // crear un usuario en memoria para realizar pruebas
    @Bean
    public UserDetailsService userDetailsService(){
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(
                User.withUsername("GersonBenito")
                        .password("12345")
                        .roles()
                        .build()
        );

        return manager;
    }

    // Encriptar contraseña
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(); // algoritmo de encriptacion
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity httpSecurity, PasswordEncoder passwordEncoder) throws Exception {
        return httpSecurity.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailsService())
                .passwordEncoder(passwordEncoder)
                .and()
                .build();
    }

}
