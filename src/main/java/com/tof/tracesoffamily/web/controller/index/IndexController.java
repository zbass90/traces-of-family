package com.tof.tracesoffamily.web.controller.index;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

//@RequiredArgsConstructor
@RestController
public class IndexController {
    @GetMapping("/")
    public String index(){
        return "";
    }

    @GetMapping("/nowij")
    public String nowij(){
        return "Spring Boot test";
    }
}
