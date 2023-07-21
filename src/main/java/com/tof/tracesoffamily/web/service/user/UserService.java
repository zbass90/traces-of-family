package com.tof.tracesoffamily.web.service.user;

import com.tof.tracesoffamily.util.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Value("${jwt.secret}")
    private String secretKey;

    private Long expiredMs = 1000 * 60 * 60L; //60초 * 60 = 3600초 = 1시간
    public String login(String userName, String ㅃpassword){
        //인증 과정 생략
        return JwtUtil.createJwt(userName, secretKey, expiredMs);
    }
}
