package com.security.security;

import com.security.security.filters.JwtAuthenticationFilter;
import com.security.security.filters.JwtAuthorizationFilter;
import com.security.security.jwt.JwtUtils;
import com.security.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true) // habilitar la verificacion de roles
public class SecurityConfig {

    @Autowired
    public JwtUtils jwtUtils;

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    JwtAuthorizationFilter authorizationFilter;

    // metodo que configure la cadena de filtros
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, AuthenticationManager authenticationManager) throws Exception {

        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtUtils);
        jwtAuthenticationFilter.setAuthenticationManager(authenticationManager);
        jwtAuthenticationFilter.setFilterProcessesUrl("/login"); // cambiar la url por defecto que maneja spring security

        return httpSecurity.csrf(config -> config.disable()) // desabilitar crosside para el manejo de formularios desde el frontend
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/api/v1/hello").permitAll(); // permitir que todos los usuarios puedan consumir este endpoint
                    auth.anyRequest().authenticated(); // cualquier otro request diferete de /hello debe de estar autenticado
                })
                .sessionManagement(session -> { // administrador de las sesiones
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                })
                .addFilter(jwtAuthenticationFilter) // agregar el primer filtro
                .addFilterBefore(authorizationFilter, UsernamePasswordAuthenticationFilter.class) // se debe de ejecutar antes
                .build();
    }

    // crear un usuario en memoria para realizar pruebas
    /*@Bean
    public UserDetailsService userDetailsService(){
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(
                User.withUsername("GersonBenito")
                        .password("12345")
                        .roles()
                        .build()
        );

        return manager;
    }*/

    // Encriptar contraseña
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(); // algoritmo de encriptacion
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity httpSecurity, PasswordEncoder passwordEncoder) throws Exception {
        return httpSecurity.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder)
                .and()
                .build();
    }

}
