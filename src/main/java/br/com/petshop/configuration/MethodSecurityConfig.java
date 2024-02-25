package br.com.petshop.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

/**
 * Classe de configuração de níveis de acessos aos endpoints (ADMIN, OWNER, MANAGER, USER).
 */
@Configuration
@EnableMethodSecurity(
        prePostEnabled = true,
        securedEnabled = true,
        jsr250Enabled = true)
public class MethodSecurityConfig{
}
