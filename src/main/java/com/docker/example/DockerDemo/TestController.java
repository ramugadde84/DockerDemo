package com.docker.example.DockerDemo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
class TestController {
    @GetMapping("/hello")
    public String sayHello() {
        return "Ramu updated code After Container stop code fix !";
    }
}
