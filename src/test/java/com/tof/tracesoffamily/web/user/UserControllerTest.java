package com.tof.tracesoffamily.web.controller.user;

import com.tof.tracesoffamily.security.UserPrincipal;
import com.tof.tracesoffamily.web.repository.user.User;
import com.tof.tracesoffamily.web.repository.user.UserRepository;
import com.tof.tracesoffamily.web.user.UserSetup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * - 모든 Bean 을 올리고 테스트를 진행하기 때문에 쉽게 테스트 진행 가능합니다.
 * - API 를 테스트할 경우 요청부터 응답까지 전체적인 테스트 진행 가능합니다.
 * - @Transactional 트랜잭션 어노테이션을 추가하면 테스트코드의 데이터베이스 정보가 자동으로 Rollback 됩니다.
 * - 요청에 대한 메서드를 requestSignUp(...)으로 분리해서 재사용성을 높입니다.
 * - 해당 메서드로 validate 를 실패하는 케이스도 작성합니다.
 * - andDo(print()) 메서드를 추가해서 해당 요청에 대한 출력을 확인합니다. 디버깅에 매우 유용합니다.
 * - 모든 response 에 대한 andExpect 를 작성합니다.
 * - response 에 하나라도 빠지거나 변경되면 API 변경이 이루어진 것이고 그 변경에 맞게 테스트 코드도 변경되어야 합니다.
 * - 회원 조회 테스트 강은 경우 memberSetup.save(); 메서드로 테스트전에 데이터베이스에 insert 합니다.
 * - 데이터베이스에 미리 있는 값을 검증하는 것은 데이터베이스 상태에 의존한 코드가 되며 누군가가 회원 정보를 변경하게 되면 테스트 코드가 실패하게 됩니다.
 * - 테스트 전에 데이터를 insert 하지 않는다면 테스트 코드 구동 전에 .sql 으로 미리 데이터베이스를 준비시킵니다.
 * - 중요한 것은 데이터베이스 상태에 너무 의존적인 테스트는 향후 로직의 문제가 없더라도 테스트가 실패하는 상황이 자주 만나게 됩니다.
 * */

@ExtendWith(SpringExtension.class)
@SpringBootTest(properties = "spring.config.location=" +
        "classpath:/application.yml" +
        ",classpath:/application-oauth.yml")
//        ",classpath:/aws.yml")
@Transactional
@WebAppConfiguration
class UserControllerTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private UserRepository userRepository;

    private User user;
    private MockMvc mvc;

    @BeforeEach
    public void setup(){

        // mvc
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
//
//        // save user
        user = userRepository.save(UserSetup.builder().build().get());
    }

    /**
     * 설명 : 본인의 유저 정보를 가져온다.
     *        아이디, 이름, 이메일, 썸네일, 참여중인 미션 목록 및 당일 포스트 제출여부
     *        {
     *          "id":21,
     *          "name" : "USER_NAME",
     *          "email" : "EMAIL@gmail.com",
     *          "thumbnailUrl": "THUMBNAIL_URL.jpg",
     *          "missions" : [ ]
     *        }
     * */
    @Test
    public void user_me_success() throws Exception {
        // given
        final UserPrincipal userPrincipal = UserPrincipal.create(user);

        // when
        final ResultActions resultActions = getRequest(userPrincipal);

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(user.getId()))
                .andExpect(jsonPath("name").value(user.getName()))
                .andExpect(jsonPath("email").value(user.getEmail()))
                .andExpect(jsonPath("thumbnailUrl").value(user.getThumbnailUrl()));

    }

    /**
     * 설명 : UserDetails 를 with(user(***)) 에 넘겨, Authenticated User 를 사용한다.
     * */
    private ResultActions getRequest(UserPrincipal userPrincipal) throws Exception {
        return  mvc.perform(
                    get("/api/user/me")
                                .with(user(userPrincipal)))
                .andDo(print());
    }
}