package br.com.petshop.authentication.service;

import br.com.petshop.app.user.repository.AppUserRepository;
import br.com.petshop.authentication.model.enums.Message;
import br.com.petshop.system.user.repository.SysUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired private AppUserRepository appUserRepository;
    @Autowired private SysUserRepository systemUserRepository;
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) {
                String prefix = username.substring(0,3);
                username = username.substring(4);
                if (prefix.equals("app"))
                    return appUserRepository.findByEmailAndActiveIsTrue(username)
                        .orElseThrow(() -> new UsernameNotFoundException(Message.AUTH_NOT_FOUND.get()));
                else
                    return systemUserRepository.findByUsernameAndActiveIsTrue(username)
                            .orElseThrow(() -> new UsernameNotFoundException(Message.AUTH_NOT_FOUND.get()));
            }
        };
    }
}
