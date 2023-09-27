package br.com.petshop.system.audit;

import br.com.petshop.system.user.model.entity.SysUserEntity;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<SysUserEntity> {

    @Override
    public Optional<SysUserEntity> getCurrentAuditor() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            return Optional.empty();
        }
        return Optional.ofNullable((SysUserEntity) auth.getPrincipal());
    }
}
