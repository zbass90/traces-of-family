package com.tof.tracesoffamily.web.dto.user;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.util.ArrayList;
/**
 * 설명 : 유저 정보
 * */
@Getter
public class UserResponseDto implements Serializable {

    private Long id;
    private String name;
    private String email;
    private String thumbnailUrl;
    private String thumbnailUrlUserInfo;

    @Builder
    public UserResponseDto(Long id, String name, String email, String thumbnailUrl, String thumbnailUrlUserInfo){
        this.id = id;
        this.name = name;
        this.email = email;
        this.thumbnailUrl = thumbnailUrl;
        this.thumbnailUrlUserInfo = thumbnailUrlUserInfo;
    }
}
