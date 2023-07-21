package com.tof.tracesoffamily.web.repository.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tof.tracesoffamily.web.repository.common.BaseTimeEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "user")
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

    @Column(nullable = false, length = 2000)
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
}
