package br.com.petshop.user.service;

import br.com.petshop.authentication.service.JwtService;
import br.com.petshop.dao.entity.AppUserEntity;
import br.com.petshop.dao.repository.AppUserRepository;
import br.com.petshop.model.dto.request.AppUserRequest;
import br.com.petshop.model.dto.response.JwtAuthenticationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AppUserService {
    @Autowired private AppUserRepository appUserRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private JwtService jwtService;
    @Autowired private ConvertService convert;
    public JwtAuthenticationResponse create(AppUserRequest request) {
        request.setPassword(passwordEncoder.encode(request.getPassword()));
        UserDetails user = convert.convertAppUserRequestIntoEntity(request);
        save((AppUserEntity) user);

        var jwt = jwtService.generateToken(user);
        return JwtAuthenticationResponse.builder().token(jwt).build();
    }

    public AppUserEntity findByEmail(String email) {
        AppUserEntity entity = appUserRepository.findByEmail(email).orElse(null);

        if (entity == null)
            throw new RuntimeException("User not found.");

        return entity;
    }

    public AppUserEntity save (AppUserEntity entity) {
        return appUserRepository.save(entity);
    }
}
