package com.tof.tracesoffamily.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
//HttpSecurity가 authorizeRequests()를 deprecate 해서 생긴 문제...
//    .authorizeRequests() → .authorizeHttpRequests()
//.antMatchers() → .requestMatchers()
//.access("hasAnyRole('ROLE_A','ROLE_B')") → .hasAnyRole("A", "B")
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        return httpSecurity
                .httpBasic().disable() // ui가 아닌 토큰 인증
                .csrf().disable() // 크로스 사이트 비설정
                .cors().and()
                .authorizeHttpRequests()
                .requestMatchers("/api/v1/members/join", "/api/v1/members/login").permitAll() // join, login은 언제나 가능
                .requestMatchers(HttpMethod.POST, "/api/v1/**").authenticated() // join, login은 언제나 가능
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // jwt사용하는 경우 씀
                .and()
//                .addFilterBefore(new JwtTokenFilter(userService, secretKey), UsernamePasswordAuthenticationFilter.class) //UserNamePasswordAuthenticationFilter적용하기 전에 JWTTokenFilter를 적용 하라는 뜻 입니다.
                .build();
    }
}
