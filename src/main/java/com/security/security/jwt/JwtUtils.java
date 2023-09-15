package com.security.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Component
@Slf4j // anotacion para mostrar logs
public class JwtUtils {

    @Value("${jwt.secret.key}")
    private String secretKey;

    @Value("${jwt.time.expiration}")
    private String timeExpiration;

    // generar token de acceso
    public String generateAccessToken(String username){
        return Jwts.builder()
                .setSubject(username) // usuario que creo el token
                .setIssuedAt(new Date(System.currentTimeMillis())) // fecha de creacion del token
                .setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(timeExpiration))) // hora o fecha en el que se va expirar el token
                .signWith(getSignatureKey(), SignatureAlgorithm.HS256) // firma del metodo
                .compact();
    }

    // validar token de acceo
    public boolean isTokenValid(String token){
        try {
            Jwts.parserBuilder()// lee el token o lo decodifica
                    .setSigningKey(getSignatureKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return true;
        }catch (Exception e){
            log.error("Token invalido, error: ".concat(e.getMessage()));
            return false;
        }
    }

    // obtener el username del token
    public String getUserNameFromToken(String token){
        return getClaim(token, Claims::getSubject);
    }
    // obtener un solo claim
    public <T> T getClaim(String token, Function<Claims, T> claimsTFunction){
        Claims claims = extracAllClaims(token);
        return claimsTFunction.apply(claims);
    }

    // obtener todos los claims (payload) del token
    public Claims extracAllClaims(String token){
        return Jwts.parserBuilder()// lee el token o lo decodifica
                .setSigningKey(getSignatureKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Obtener firma del token
    public Key getSignatureKey(){
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
