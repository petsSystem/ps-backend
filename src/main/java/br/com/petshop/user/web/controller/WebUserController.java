package br.com.petshop.user.web.controller;

import br.com.petshop.user.app.model.dto.request.AppUserCreateRequest;
import br.com.petshop.user.app.model.dto.response.AppUserResponse;
import br.com.petshop.user.app.service.AppUserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/web/users")
@Tag(name = "Web Users Services")
public class WebUserController {

    @Autowired private AppUserService registerService;

    @PostMapping()
    public ResponseEntity<AppUserResponse> signup(
            @RequestBody AppUserCreateRequest request) {
        return ResponseEntity.ok(registerService.create(request));
    }
}
