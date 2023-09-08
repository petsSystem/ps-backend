package br.com.petshop.authentication.service;

import br.com.petshop.model.dto.request.SigninRequest;
import br.com.petshop.model.dto.response.JwtAuthenticationResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthenticationService {

    Logger log = LoggerFactory.getLogger(AuthenticationService.class);

    @Autowired private JwtService jwtService;
    @Autowired private AuthenticationManager authenticationManager;

    public JwtAuthenticationResponse login(SigninRequest request) {
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

            var jwt = jwtService.generateToken((UserDetails) auth.getPrincipal());
            return JwtAuthenticationResponse.builder().token(jwt).build();
        } catch (BadCredentialsException ex) {
            log.error("Bad Credentials: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, "Usuário ou senha estão incorretos.", ex);
        } catch (Exception ex) {
            log.error("Bad Request: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Erro ao efetuar login. Tente novamente mais tarde.", ex);
        }
    }
}
