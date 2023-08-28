package br.com.petshop.authentication.service;

import br.com.petshop.model.dto.request.SigninRequest;
import br.com.petshop.model.dto.response.JwtAuthenticationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    @Autowired private JwtService jwtService;
    @Autowired private AuthenticationManager authenticationManager;

    public JwtAuthenticationResponse login(SigninRequest request) {

        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        var jwt = jwtService.generateToken((UserDetails)auth.getPrincipal());
        return JwtAuthenticationResponse.builder().token(jwt).build();
    }
}
