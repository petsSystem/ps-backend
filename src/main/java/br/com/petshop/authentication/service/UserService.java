package br.com.petshop.authentication.service;

import br.com.petshop.dao.repository.AppUserRepository;
import br.com.petshop.dao.repository.WebUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired private AppUserRepository appUserRepository;
    @Autowired private WebUserRepository webUserRepository;
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) {
                String prefix = username.substring(0,3);
                username = username.substring(4);
                if (prefix.equals("app"))
                    return appUserRepository.findByEmail(username)
                        .orElseThrow(() -> new UsernameNotFoundException("User app not found"));
                else
                    return webUserRepository.findByEmail(username)
                            .orElseThrow(() -> new UsernameNotFoundException("User web not found"));
            }
        };
    }
}
