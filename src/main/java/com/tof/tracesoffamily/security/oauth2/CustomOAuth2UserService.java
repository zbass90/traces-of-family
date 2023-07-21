package com.tof.tracesoffamily.security.oauth2;

import com.tof.tracesoffamily.exception.OAuth2AuthenticationProcessingException;
import com.tof.tracesoffamily.security.UserPrincipal;
import com.tof.tracesoffamily.security.oauth2.user.OAuth2UserInfo;
import com.tof.tracesoffamily.security.oauth2.user.OAuth2UserInfoFactory;
import com.tof.tracesoffamily.web.repository.user.AuthProvider;
import com.tof.tracesoffamily.web.repository.user.User;
import com.tof.tracesoffamily.web.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    public static final String DEFAULT_USER_IMAGE_URL = ""; //https://image.daily-mission.com/%EA%B8%B0%EB%B3%B8%EC%9D%B4%EB%AF%B8%EC%A7%80/User_Defualt_Image_150_150.png
    public static final String DEFAULT_USER_THUMBNAIL_URL = ""; //https://image.daily-mission.com/%EA%B8%B0%EB%B3%B8%EC%9D%B4%EB%AF%B8%EC%A7%80/User_Default_Image_40_40.png

    @Autowired
    private UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);

        try {
            return processOAuth2User(oAuth2UserRequest, oAuth2User);
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            // Throwing an instance of AuthenticationException will trigger the OAuth2AuthenticationFailureHandler
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(oAuth2UserRequest.getClientRegistration().getRegistrationId(), oAuth2User.getAttributes());
        if(StringUtils.isEmpty(oAuth2UserInfo.getEmail())) {
            throw new OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider");
        }

        Optional<User> userOptional = userRepository.findByEmailAndProvider(oAuth2UserInfo.getEmail(), AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()) );
        User user;
        if(userOptional.isPresent()) {
            user = userOptional.get();
            if(!user.getProvider().equals(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()))) {
                throw new OAuth2AuthenticationProcessingException("Looks like you're signed up with " +
                        user.getProvider() + " account. Please use your " + user.getProvider() +
                        " account to login.");
            }
            /**
             * 업데이트 제외 (2020-03-10)
             * */
            // user = updateExistingUser(user, oAuth2UserInfo);
        } else {
            user = registerNewUser(oAuth2UserRequest, oAuth2UserInfo);
        }

        return UserPrincipal.create(user, oAuth2User.getAttributes());
    }

    private User registerNewUser(OAuth2UserRequest oAuth2UserRequest, OAuth2UserInfo oAuth2UserInfo) {
        User user = new User();

        user.setProvider(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()));
        user.setProviderId(oAuth2UserInfo.getId());
        user.setName(oAuth2UserInfo.getName());
        user.setEmail(oAuth2UserInfo.getEmail());

        if(oAuth2UserInfo.getImageUrl()!=null){
            user.setImageUrl(oAuth2UserInfo.getImageUrl());
            user.setThumbnailUrl(oAuth2UserInfo.getImageUrl());
            user.setThumbnailUrlUserInfo(oAuth2UserInfo.getImageUrl());
        }else{
            user.setImageUrl(DEFAULT_USER_IMAGE_URL);
            user.setThumbnailUrl(DEFAULT_USER_THUMBNAIL_URL);
            user.setThumbnailUrlUserInfo(DEFAULT_USER_IMAGE_URL);
        }

        return userRepository.save(user);
    }

    /**
     * OAuth provider 에서 변경되었다고해서 변경 x
     * 사용자가 직접 (이름,이미지) 수정 하도록 변경 o
     * */
//    private User updateExistingUser(User existingUser, OAuth2UserInfo oAuth2UserInfo) {
//        existingUser.setName(oAuth2UserInfo.getName());
//
//        // null -> not null
//        if(existingUser.getImageUrl().equals(DEFAULT_USER_IMAGE_URL) && oAuth2UserInfo.getImageUrl()!=null){
//            existingUser.setImageUrl(oAuth2UserInfo.getImageUrl());
//        }
//        // not null -> null
//        else if(!existingUser.getImageUrl().equals(DEFAULT_USER_IMAGE_URL) && oAuth2UserInfo.getImageUrl()==null){
//            existingUser.setImageUrl(DEFAULT_USER_IMAGE_URL);
//        }
//
//        return userRepository.save(existingUser);
//    }

}