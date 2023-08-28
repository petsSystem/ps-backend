package br.com.petshop.authentication.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/resource/app")
@RequiredArgsConstructor
public class AppAuthorizationController {
    @GetMapping
    public ResponseEntity<String> sayHello() {
        return ResponseEntity.ok("Here is your app resource");
    }
}
