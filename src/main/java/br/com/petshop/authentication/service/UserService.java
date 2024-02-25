package br.com.petshop.authentication.service;

import br.com.petshop.customer.repository.CustomerRepository;
import br.com.petshop.authentication.model.enums.Message;
import br.com.petshop.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Classe responsável pelas funcionalidades da autenticação
 */
@Service
public class UserService {
    @Autowired private CustomerRepository customerRepository;
    @Autowired private UserRepository systemUserRepository;

    /**
     * Método que retorna o objeto UserDetails a partir de um username
     * @return
     */
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) {
                String prefix = username.substring(0,3);
                username = username.substring(4);
                if (prefix.equals("app"))
                    return customerRepository.findByUsernameAndActiveIsTrue(username)
                        .orElseThrow(() -> new UsernameNotFoundException(Message.AUTH_NOT_FOUND_ERROR.get()));
                else
                    return systemUserRepository.findByUsernameAndActiveIsTrue(username)
                            .orElseThrow(() -> new UsernameNotFoundException(Message.AUTH_NOT_FOUND_ERROR.get()));
            }
        };
    }
}
