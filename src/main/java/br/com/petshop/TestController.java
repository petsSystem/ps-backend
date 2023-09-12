package br.com.petshop;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/test")
@Hidden
public class TestController {

    @GetMapping("/hello")
    public String test() {
        return "Hello World!";
    }
}
