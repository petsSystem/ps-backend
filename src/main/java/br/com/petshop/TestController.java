package br.com.petshop;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/test")
@Hidden
@Component
public class TestController {

    @GetMapping("/hello/sec")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String test() {
        return "Hello World!";
    }

    @GetMapping("/hello")
    public String test1(Principal principal) {
        return "Hello World!";
    }
}
