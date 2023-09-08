package br.com.petshop.user.controller.web;

import br.com.petshop.model.dto.request.AppUserCreateRequest;
import br.com.petshop.model.dto.response.AppUserResponse;
import br.com.petshop.user.service.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/register/petshop/web")
@RequiredArgsConstructor
public class WebPetShopRegisterController {

    private final AppUserService registerService;

    @PostMapping("/signup")
    public ResponseEntity<AppUserResponse> signup(
            @RequestBody AppUserCreateRequest request) {
        return ResponseEntity.ok(registerService.create(request));
    }
}
