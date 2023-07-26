package com.tof.tracesoffamily.web.repository.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tof.tracesoffamily.web.repository.common.BaseTimeEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "user")
@NoArgsConstructor
@ToString
public class User extends BaseTimeEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false , length = 20)
    @Size(min = 1, max = 20)
    private String name;

    @Email
    @Column(nullable = false)
    private String email;

    @Column(nullable = false, length = 2000)
    private String imageUrl;

    @Column(nullable = false, length = 2000)
    private String thumbnailUrl;

    @Column(length = 2000) //nullable = false,
    private String thumbnailUrlUserInfo;

    @Column(name = "ORIGINAL_FILE_NAME")
    private String originalFileName;

    @Column(name = "FILE_EXTENSION")
    private String fileExtension;

    @Column(nullable = false)
    private Boolean emailVerified = false;

    @JsonIgnore
    private String password;

    @NotNull
    @Enumerated(EnumType.STRING)
    private AuthProvider provider;

    private String providerId;

//    @OneToMany(mappedBy = "user")
//    @JsonIgnore
//    private List<Post> posts = new ArrayList<>();
//
//    @OneToMany(mappedBy = "user")
//    @JsonIgnore
//    private List<Mission> missions = new ArrayList<>();
//
//    @OneToMany(mappedBy = "user")
//    private List<Participant> participants = new ArrayList<>();

    // 테스트용 빌더
    @Builder
    public User(String name, String email, String imageUrl, AuthProvider provider, String providerId){
        this.name = name;
        this.email = email;
        this.imageUrl = imageUrl;
        this.provider = provider;
        this.providerId = providerId;
    }

    public boolean isValidUpdateName(String name){

        // null name
        if(name==null){
            return false;
        }

        // check same name
        if(this.name.equals(name)){
            return false;
        }

        //이름 허용 최소, 최대값 비교
        if(this.name.length() < name.length()){
           throw new IllegalArgumentException("USER_NAME_SMALLER_THEN_MIN_LENGTH 길이보다 작습니다");
        }

        if(this.name.length() > name.length()){
            throw new IllegalArgumentException("USER_NAME_SMALLER_THEN_MIN_LENGTH 길이보다 큽니다");
        }

        return true;
    }

    public void updateName(String name){
        this.name = name;
    }

    // 이미지 업데이트
    public void updateImage(String imageUrl){
        // 이미지
        this.imageUrl = imageUrl;
        // 썸네일 -> 재생성
        this.thumbnailUrl = imageUrl;
        this.thumbnailUrlUserInfo = imageUrl;
    }

    // 썸네일(Profile) 업데이트
    public void updateThumbnail(String thumbnailUrl){
        this.thumbnailUrl = thumbnailUrl;
    }
}
