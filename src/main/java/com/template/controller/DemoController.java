package com.template.controller;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/demo")
@Hidden
public class DemoController {

    @GetMapping("/test")

    public String test(){
        return "Hello World!!";
    }
}
