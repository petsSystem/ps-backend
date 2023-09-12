package br.com.petshop.pet.controller;

import br.com.petshop.pet.model.dto.request.PetCreateRequest;
import br.com.petshop.pet.model.dto.request.PetRequest;
import br.com.petshop.pet.model.dto.response.PetResponse;
import br.com.petshop.pet.service.PetService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/app/pets")
@Tag(name = "Pets Services")
public class PetController {

    @Autowired private PetService petService;

    @PostMapping()
    @ResponseStatus(HttpStatus.OK)
    public PetResponse create(Principal authentication,
                              @RequestBody PetCreateRequest request) {
        return petService.create(authentication, request);
    }
}
