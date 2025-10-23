package com.example.sso_demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.ldap.DefaultSpringSecurityContextSource;
import org.springframework.security.ldap.authentication.ad.ActiveDirectoryLdapAuthenticationProvider;

/**
 * Configuración de la autenticación LDAP para la aplicación.
 * 
 * Esta clase define los beans necesarios para la autenticación contra 
 * un servidor LDAP utilizando el protocolo Active Directory.
 * 
 * La configuración permite autenticar a los usuarios del dominio 'example.local' 
 * a través del puerto 389 (LDAP). Se establece la conexión con el servidor 
 * y se proporciona un usuario administrador para la autenticación.
 * 
 * @TODO 
 * Credenciales y url deben ser extraidas de variables de entorno
 * Manejo de excepciones
 * 
 * 
 * Componentes principales:
 * - {@link DefaultSpringSecurityContextSource}: Define la conexión con el servidor LDAP.
 * - {@link ActiveDirectoryLdapAuthenticationProvider}: Configura el proveedor de autenticación 
 *   para permitir la verificación de credenciales de los usuarios en el dominio.
 * 
 * @author Nahuel Nuñez
 * @version 0.1
 */
@Configuration
public class LdapConfig {

    /**
     * Configura la fuente de contexto LDAP (LDAP Context Source) necesaria para conectarse al servidor LDAP.
     * 
     * @return Un objeto {@link DefaultSpringSecurityContextSource} que contiene la URL del servidor LDAP, 
     *         el DN (Distinguished Name) del usuario admin y la contraseña.
     */
    @Bean
    public DefaultSpringSecurityContextSource contextSource() {
        // Establece la URL del servidor LDAP con su dominio base (base DN)
        DefaultSpringSecurityContextSource contextSource = 
            new DefaultSpringSecurityContextSource("ldap://192.168.100.34:389/dc=example,dc=local");
        // Usuario y contraseña de un admin 
        contextSource.setUserDn("cn=agustin,dc=example,dc=local"); 
        contextSource.setPassword("agustin"); 
        return contextSource;
    }

    /**
     * Configura el proveedor de autenticación Active Directory para validar las credenciales de los usuarios.
     * 
     * @return Un objeto {@link ActiveDirectoryLdapAuthenticationProvider} que permite autenticar a los 
     *         usuarios contra el dominio "example.local" utilizando el servidor LDAP.
     */
    @Bean
    public ActiveDirectoryLdapAuthenticationProvider authenticationProvider() {
        // dominio , url 
        ActiveDirectoryLdapAuthenticationProvider provider =
            new ActiveDirectoryLdapAuthenticationProvider("example.local", "ldap://192.168.100.34:389");

        provider.setConvertSubErrorCodesToExceptions(true); 
        provider.setUseAuthenticationRequestCredentials(true);

        return provider;
    }
}
