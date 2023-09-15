package com.security.security.filters;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.security.entities.UserEntity;
import com.security.security.jwt.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    // inject class
    @Autowired
    private JwtUtils jwtUtils;

    // hacer la inyecccion por constructor
    /*public JwtAuthenticationFilter(JwtUtils jwtUtils){
        this.jwtUtils = jwtUtils;
    }*/

    // metodo que se ejecuta cuando un usuario se intenta autenticar
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        // recuperar los datos del usuario
        UserEntity userEntity = null;
        String username;
        String password;

        try {
            userEntity = new ObjectMapper().readValue(request.getInputStream(), UserEntity.class);

            username = userEntity.getUsername();
            password = userEntity.getPassword();

        }catch (StreamReadException e) {
            throw new RuntimeException(e);
        } catch (DatabindException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);

        return getAuthenticationManager().authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain,
            Authentication authResult) throws IOException, ServletException {

        // obtener los datalles del usuario
        User user = (User) authResult.getPrincipal();

        // generar token
        String token = jwtUtils.generateAccessToken(user.getUsername());

        // responder a la peticion
        response.addHeader("Authorization", token);

        Map<String, Object> httpResponse = new HashMap<>();

        httpResponse.put("token", token);
        httpResponse.put("username", user.getUsername());
        httpResponse.put("message", "Autenticacion correcra");

        // responder en formato jso usando jackson
        response.getWriter().write(new ObjectMapper().writeValueAsString(httpResponse));
        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().flush(); // con el metodo flush() nos aseguramos de que se apliquen las configuraciones o se escriban bien

        super.successfulAuthentication(request, response, chain, authResult);
    }
}
