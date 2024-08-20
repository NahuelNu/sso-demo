package com.example.ssodemo.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
public class MetodosPruebaController {

    @GetMapping("/test")
    public String testing() {
        return new String("Ingresaste: ");
    }

    @GetMapping("/test/login")
    public void initiateLogin(HttpServletResponse response) throws IOException {
        // "http://localhost:6080/realms/realmTest/protocol/openid-connect/auth?client_id=sso-proxy&redirect_uri=http://localhost:8080/test/callback&response_type=code&scope=openid";
        String authorizationUri = UriComponentsBuilder
                                    .fromUriString("http://localhost:6080/realms/realmTest/protocol/openid-connect/auth")
                                    .queryParam("client_id", "sso-proxy")
                                    .queryParam("redirect_uri", "http://localhost:8080/test/callback")
                                    .queryParam("response_type", "code")
                                    .queryParam("scope", "openid")
                                    .queryParam("state", "random123")
                                    .queryParam("nonce", "unique456")
                                    .build().toUriString();
        response.sendRedirect(authorizationUri);
    }
    @GetMapping("/test/callback")
    public String handleCallback(@RequestParam String code, @RequestParam String state) throws IOException {
        // Verificar el estado (CSRF protection)
        if (!state.equals("random123")) {
            throw new IllegalStateException("Estado inválido");
        }

       

        // Intercambiar el código por tokens
        OAuth2AccessTokenResponse tokenResponse = this.exchangeCodeForTokens(code);
        return "Code: "+code+" state: "+state + " ---------- "+ tokenResponse.getAccessToken().getTokenValue();
        // // Agregar los tokens al modelo o establecer cookies/sesiones
        // model.addAttribute("idToken", tokenResponse.getIdToken());
        // model.addAttribute("accessToken", tokenResponse.getAccessToken());

        // // Redirigir al usuario a la aplicación cliente
        // return "redirect:https://cliente-app.com/home";s
    }

    private OAuth2AccessTokenResponse exchangeCodeForTokens(String code) {
        RestTemplate restTemplate = new RestTemplate();

        // Configuración de los headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        // Cuerpo de la solicitud (form data)
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("code", code);
        body.add("redirect_uri", "http://localhost:8080/test/callback");
        body.add("client_id", "sso-proxy");
        body.add("client_secret", "sGmM6WVQQEYU3nGQzSRqJugKrnH8aObl");

        // Construcción de la entidad HTTP
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);

        try {
            // Realización de la solicitud POST
            ResponseEntity<Map> response = restTemplate.exchange(
                "http://localhost:6080/realms/realmTest/protocol/openid-connect/token", 
                HttpMethod.POST, 
                requestEntity, 
                Map.class
            );
        
            // Verificación del código de estado y manejo de la respuesta
            if (response.getStatusCode() == HttpStatus.OK) {
                Map<String, Object> tokens = response.getBody();

                String accessToken = (String) tokens.get("access_token");
                String refreshToken = (String) tokens.get("refresh_token");

                // for (Map.Entry<String, Object> entrada : tokens.entrySet()) {
                //     System.out.println("Clave: " + entrada.getKey() + ", Valor: " + entrada.getValue());
                // }

                OAuth2AccessTokenResponse oAuth2AccessTokenResponse= 
                        OAuth2AccessTokenResponse   .withToken(accessToken)
                                                    .tokenType(OAuth2AccessToken.TokenType.BEARER)
                                                    .refreshToken(refreshToken).build();
                return oAuth2AccessTokenResponse;
            } else {
                System.out.println("Error en la respuesta: " + response.getStatusCode());
            }
        
        } catch (RestClientException e) {
            // Manejo de excepciones
            System.err.println("Error realizando la solicitud: " + e.getMessage());
        }
        return null;
    }

    
}
