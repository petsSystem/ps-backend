package br.com.petshop.user.controller.app;

import br.com.petshop.dao.entity.AppUserEntity;
import br.com.petshop.model.dto.request.PetRequest;
import br.com.petshop.model.dto.response.JwtAuthenticationResponse;
import br.com.petshop.user.service.AppUserService;
import br.com.petshop.user.service.PetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/app/pet")
@RequiredArgsConstructor
public class PetController {

    private final PetService petService;

    @PostMapping()
    @ResponseStatus(HttpStatus.OK)
    public void create(@AuthenticationPrincipal UserDetails user,
            @RequestBody PetRequest request) {
        String email = ((AppUserEntity) user).getEmail();
        petService.create(request, email);
    }
}
