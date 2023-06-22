package com.tof.tracesoffamily.web.controller.member;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/members")
public class MemberController {
    @PostMapping("/login")
    public ResponseEntity<String> login(){
        return ResponseEntity.ok().body("token");
    }
}
