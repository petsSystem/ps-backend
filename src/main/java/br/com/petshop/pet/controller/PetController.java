package br.com.petshop.pet.controller;

import br.com.petshop.pet.model.dto.request.PetRequest;
import br.com.petshop.user.app.model.entity.AppUserEntity;
import br.com.petshop.user.app.service.PetService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/app/pets")
@Tag(name = "Pets Services")
public class PetController {

    @Autowired private PetService petService;

    @PostMapping()
    @ResponseStatus(HttpStatus.OK)
    public void create(@AuthenticationPrincipal UserDetails user,
            @RequestBody PetRequest request) {
        String email = ((AppUserEntity) user).getEmail();
        petService.create(request, email);
    }
}
