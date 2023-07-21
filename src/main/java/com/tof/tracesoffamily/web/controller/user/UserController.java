package com.tof.tracesoffamily.web.controller.user;

import com.tof.tracesoffamily.web.dto.LoginRequestDto;
import com.tof.tracesoffamily.web.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequestDto dto){
        //dto 아이디, 패스워드
        return ResponseEntity.ok().body(userService.login(dto.getUserName(),""));
    }
}
