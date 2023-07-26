package com.tof.tracesoffamily.web.controller.user;

import com.tof.tracesoffamily.security.CurrentUser;
import com.tof.tracesoffamily.security.UserPrincipal;
import com.tof.tracesoffamily.web.dto.LoginRequestDto;
import com.tof.tracesoffamily.web.dto.user.UserResponseDto;
import com.tof.tracesoffamily.web.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 설명 : 본인의 유저 정보를 가져온다. (아이디, 이름, 이메일)
     * */
    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
    public UserResponseDto getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {

        return userService.getCurrentUser(userPrincipal);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequestDto dto){
        //dto 아이디, 패스워드
        return ResponseEntity.ok().body(userService.login(dto.getUserName(),""));
    }
}
