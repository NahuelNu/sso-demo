package com.example.ssodemo.controller;

import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletResponse;

@Controller
public class LoginController {
    
    // @GetMapping("/login")
    // public void initiateLogin(HttpServletResponse response) throws IOException {
    //     String authorizationUri = "";
    //     //Ejemplo:
    //     "http://localhost:6080/realms/realmTest/protocol/openid-connect/auth?client_id=sso-proxy&redirect_uri=http://localhost:8080/callback&response_type=code&scope=openid";
    //     response.sendRedirect(authorizationUri);
    // }

    @GetMapping("/callback")
    public void handleCallback(@RequestParam String code, String state) {
        // Validar el state y luego intercambiar el código por tokens
        // Implementar el intercambio de código por tokens mediante una llamada HTTP POST al endpoint /token de Keycloak
    }

}
