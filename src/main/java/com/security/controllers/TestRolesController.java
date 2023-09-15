package com.security.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class TestRolesController {

    @GetMapping("/accessAdmin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> accessAdmin(){
        Map<String, String> httpResponse = new HashMap<>();

        httpResponse.put("message", "Has accedido con el rol de ADMIN");

        return ResponseEntity.ok(httpResponse);
    }

    @GetMapping("/accessUser")
    //@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Map<String, String>> accessUser(){
        Map<String, String> httpResponse = new HashMap<>();

        httpResponse.put("message", "Has accedido con el rol de USER");

        return ResponseEntity.ok(httpResponse);
    }

    @GetMapping("/accessInvited")
    @PreAuthorize("hasRole('INVITED')")
    public ResponseEntity<Map<String, String>> accessInvited(){
        Map<String, String> httpResponse = new HashMap<>();

        httpResponse.put("message", "Has accedido con el rol de INVITED");

        return ResponseEntity.ok(httpResponse);
    }
}
