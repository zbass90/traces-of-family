package com.tof.tracesoffamily.config;

import com.tof.tracesoffamily.security.CustomUserDetailsService;
import com.tof.tracesoffamily.security.JwtTokenFilter;
import com.tof.tracesoffamily.security.RestAuthenticationEntryPoint;
import com.tof.tracesoffamily.security.oauth2.CustomOAuth2UserService;
import com.tof.tracesoffamily.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository;
import com.tof.tracesoffamily.security.oauth2.OAuth2AuthenticationFailureHandler;
import com.tof.tracesoffamily.security.oauth2.OAuth2AuthenticationSuccessHandler;
import com.tof.tracesoffamily.web.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
//HttpSecurity가 authorizeRequests()를 deprecate 해서 생긴 문제...
//    .authorizeRequests() → .authorizeHttpRequests()
//.antMatchers() → .requestMatchers()
//.access("hasAnyRole('ROLE_A','ROLE_B')") → .hasAnyRole("A", "B")
    private final CustomUserDetailsService customUserDetailsService;
    private final UserService userService;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;

    @Value("${jwt.secret}")
    private String secretKey;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        return httpSecurity
                .httpBasic().disable() // ui가 아닌 토큰 인증
                .csrf().disable() // 크로스 사이트 비설정
                .cors().and()
                .formLogin().disable()
                .authorizeHttpRequests()
                .requestMatchers("/",
                        "/error",
                        "/favicon.ico",
                        "/**/*.png",
                        "/**/*.gif",
                        "/**/*.svg",
                        "/**/*.jpg",
                        "/**/*.html",
                        "/**/*.css",
                        "/**/*.js")
                .permitAll() // join, login은 언제나 가능
                .requestMatchers(
                        "/auth/**", "/oauth2/**"
//                        , "/api/mission/**", "/api/post/**"
                )
                .permitAll()
                .requestMatchers(HttpMethod.POST, "/api/**").authenticated() // api/** 모두 검증해야함
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // jwt사용하는 경우 씀
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(new RestAuthenticationEntryPoint())
                .and()
                .oauth2Login()
                .authorizationEndpoint()
                .baseUri("/oauth2/authorize")
                .authorizationRequestRepository(cookieAuthorizationRequestRepository())
                .and()
                .userInfoEndpoint()
                .userService(customOAuth2UserService)
                .and()
                .successHandler(oAuth2AuthenticationSuccessHandler)
                .failureHandler(oAuth2AuthenticationFailureHandler)
                .and()
                .addFilterBefore(new JwtTokenFilter(secretKey, userService), UsernamePasswordAuthenticationFilter.class) //UserNamePasswordAuthenticationFilter적용하기 전에 JWTTokenFilter를 적용 하라는 뜻 입니다.
                .build();
    }

//    @Bean
//    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
//        authenticationManagerBuilder
//                .userDetailsService(customUserDetailsService)
//                .passwordEncoder(passwordEncoder());
//    }
    @Autowired
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder
                .userDetailsService(customUserDetailsService);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public HttpCookieOAuth2AuthorizationRequestRepository cookieAuthorizationRequestRepository() {
        return new HttpCookieOAuth2AuthorizationRequestRepository();
    }
}
