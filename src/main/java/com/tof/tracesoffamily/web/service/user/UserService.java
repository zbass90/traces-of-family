package com.tof.tracesoffamily.web.service.user;

import com.tof.tracesoffamily.exception.ResourceNotFoundException;
import com.tof.tracesoffamily.security.CurrentUser;
import com.tof.tracesoffamily.security.UserPrincipal;
import com.tof.tracesoffamily.util.JwtUtil;
import com.tof.tracesoffamily.web.dto.user.UserResponseDto;
import com.tof.tracesoffamily.web.repository.user.User;
import com.tof.tracesoffamily.web.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {

    @Value("${jwt.secret}")
    private String secretKey;
    private final UserRepository userRepository;


    private Long expiredMs = 1000 * 60 * 60L; //60초 * 60 = 3600초 = 1시간
    public String login(String userName, String ㅃpassword){
        //인증 과정 생략
        return JwtUtil.createJwt(userName, secretKey, expiredMs);
    }

    /**
     * 설명 : 본인의 유저 정보를 가져온다.
     *        아이디, 이름, 이메일
     * */
    @Transactional(readOnly = true)
    public UserResponseDto getCurrentUser(@CurrentUser UserPrincipal userPrincipal){

        /**
         * 설명 : @CurrentUser UserPrincipal 정보를 가져올때 UserRepository.findById 와 같은 조회 query 를 수행한다.
         * */
        // user entity
        User user = userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userPrincipal.getId()));

        // user response dto
        UserResponseDto userResponseDto = UserResponseDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .thumbnailUrl(user.getThumbnailUrl())
                .thumbnailUrlUserInfo(user.getThumbnailUrlUserInfo())
                .build();

        // return response dto
        return userResponseDto;
    }
}
