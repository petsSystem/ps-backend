package br.com.petshop.configuration;

import br.com.petshop.commons.audit.AuditorAwareImpl;
import br.com.petshop.user.model.entity.UserEntity;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Classe de configuração de auditoria.
 */
@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class JpaConfiguration {

    @Bean
    public AuditorAware<UserEntity> auditorAware() {
        return new AuditorAwareImpl();
    }
}
