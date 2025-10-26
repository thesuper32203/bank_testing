package com.chat.finicity.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String helloWorld() {

        String userId ="testing_value_" + UUID.randomUUID();
        return userId.toString();

    }
}
