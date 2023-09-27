package br.com.petshop.configuration;

import br.com.petshop.system.audit.AuditorAwareImpl;
import br.com.petshop.system.user.model.entity.SysUserEntity;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class JpaConfiguration {

    @Bean
    public AuditorAware<SysUserEntity> auditorAware() {
        return new AuditorAwareImpl();
    }
}
