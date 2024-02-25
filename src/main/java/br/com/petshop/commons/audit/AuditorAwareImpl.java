package br.com.petshop.commons.audit;

import br.com.petshop.user.model.entity.UserEntity;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

/**
 * Classe responsável pela configuração da auditoria do JPA
 */
public class AuditorAwareImpl implements AuditorAware<UserEntity> {

    /**
     * Método que retorna o usuário que está praticando a ação
     * @return
     */
    @Override
    public Optional<UserEntity> getCurrentAuditor() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            return Optional.empty();
        } else if (auth.getPrincipal().toString().equalsIgnoreCase("anonymousUser")) {
            return Optional.empty();
        }
        return Optional.ofNullable((UserEntity) auth.getPrincipal());
    }
}
