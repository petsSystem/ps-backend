package br.com.petshop.authentication.service;

import br.com.petshop.authentication.model.dto.request.AuthenticationRequest;
import br.com.petshop.authentication.model.dto.response.AuthenticationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

/**
 * Classe responsável pelas regras do login
 */
@Service
public class AuthenticationService {

    @Autowired private JwtService jwtService;
    @Autowired private AuthenticationManager authenticationManager;

    /**
     * Método que efetua login
     * @param request
     * @return
     */
    public AuthenticationResponse login(AuthenticationRequest request) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        var jwt = jwtService.generateToken((UserDetails) auth.getPrincipal());
        return AuthenticationResponse.builder().token(jwt).build();
    }
}
