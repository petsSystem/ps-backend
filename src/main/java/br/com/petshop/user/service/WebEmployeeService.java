package br.com.petshop.user.service;

import br.com.petshop.authentication.service.JwtService;
import br.com.petshop.dao.entity.AppUserEntity;
import br.com.petshop.dao.repository.AppUserRepository;
import br.com.petshop.dao.repository.WebUserRepository;
import br.com.petshop.model.dto.request.AppUserRequest;
import br.com.petshop.model.dto.response.JwtAuthenticationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class WebEmployeeService {
    @Autowired private AppUserRepository appUserRepository;
    @Autowired private WebUserRepository webUserRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private JwtService jwtService;
    @Autowired private AuthenticationManager authenticationManager;
    @Autowired private ConvertService convert;
    public JwtAuthenticationResponse create(AppUserRequest request) {
        request.setPassword(passwordEncoder.encode(request.getPassword()));
//        UserDetails user = null;
//        if (channel == Channel.APP) {
//            user = convert.convertAppUserRequestIntoEntity(request);
//
//            appUserRepository.save((AppUserEntity) user);
//        }
//         else {
//            user = WebUserEntity.builder().firstName(request.getFirstName()).lastName(request.getLastName())
//                    .email(request.getEmail()).password(passwordEncoder.encode(request.getPassword()))
//                    .role(Role.USER).build();
//
//            webUserRepository.save((WebUserEntity) user);
//        }

//        var jwt = jwtService.generateToken(user);
//        return JwtAuthenticationResponse.builder().token(jwt).build();
        return null;
    }

    public AppUserEntity get (String email) {
        Optional<AppUserEntity> op = appUserRepository.findByEmail(email);
        return op.get();
    }
}
