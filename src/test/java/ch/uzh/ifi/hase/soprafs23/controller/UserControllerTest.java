package ch.uzh.ifi.hase.soprafs23.controller;

import ch.uzh.ifi.hase.soprafs23.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.model.UserReqVo;
import ch.uzh.ifi.hase.soprafs23.service.UserService;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;





/**
 * UserControllerTest
 * This is a WebMvcTest which allows to test the UserController i.e. GET/POST
 * request without actually sending them over the network.
 * This tests if the UserController works.
 */
@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;


    private String token;

    private Gson gson = new Gson();
        User user;

    @BeforeEach
    public void initUser(){
        user = new User();
        user.setName("Firstname Lastname");
        user.setUsername("firstname@lastname");
        user.setPassword("firstname@123");
        user.setToken("1");
        user.setStatus(UserStatus.OFFLINE);

    }

    @Test
    public void loginTest() throws Exception {

        given(userService.login("firstname@lastname","firstname@123")).willReturn(user);

        mockMvc
                // 请求方式 + 请求路径(不用谢虚拟路径)
                .perform(MockMvcRequestBuilders.post("/user/login")
                        .accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
                        // 参数类型为JSON
                        .contentType(MediaType.ALL)
                        // 参数数据(JSON)
                        .param("username","firstname@lastname").param("password","firstname@123")
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
//                 .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("123456@qq.com"))
                .andDo(MockMvcResultHandlers.print()).andReturn();
    }

    @Test
    public void register() throws Exception {

        given(userService.createUser(any(User.class))).willReturn(user);

        mockMvc
                // 请求方式 + 请求路径(不用谢虚拟路径)
                .perform(MockMvcRequestBuilders.post("/user/register")
                        .accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
                        // 参数类型为JSON
                        .contentType(MediaType.APPLICATION_JSON)
                        // 参数数据(JSON)
                        .content(gson.toJson(user)))
                .andExpect(MockMvcResultMatchers.status().isOk())
//                 .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("123456@qq.com"))
                .andDo(MockMvcResultHandlers.print()).andReturn();
    }
    @Test
    void testGetUserDetail() throws Exception {
        // Setup
        // Run the test
        mockMvc.perform(get("/user/userDetail")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
//                 .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("123456@qq.com"))
                .andDo(MockMvcResultHandlers.print()).andReturn();
    }

    @Test
    void testUpdatePass() throws Exception {
        // Setup
        // Configure UserService.updatePassword(...).
        final User user = new User(0, "name", "password", "username", "token", UserStatus.ONLINE);
        when(userService.updatePassword(new UserReqVo(),
                new User(0, "name", "password", "username", "token", UserStatus.ONLINE))).thenReturn(user);

        // Run the test
        mockMvc.perform(post("/user/updatePass")
                        .content("content").contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).content(gson.toJson(user)))
                .andExpect(MockMvcResultMatchers.status().isOk())
//                 .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("123456@qq.com"))
                .andDo(MockMvcResultHandlers.print()).andReturn();
    }

    @Test
    void testUpdateDetail() throws Exception {
        // Setup
        // Configure UserService.updateDetail(...).
        final User user = new User(0, "name", "password", "username", "token", UserStatus.ONLINE);
        when(userService.updateDetail(new UserReqVo(),
                new User(0, "name", "password", "username", "token", UserStatus.ONLINE))).thenReturn(user);

        // Run the test
        mockMvc.perform(put("/user/updateDetail")
                        .content("content").contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(user)))
                .andExpect(MockMvcResultMatchers.status().isOk())
//                 .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("123456@qq.com"))
                .andDo(MockMvcResultHandlers.print()).andReturn();

    }

    @Test
    void testOffline() throws Exception {
        // Setup
        // Run the test
        mockMvc.perform(put("/user/offline")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
//                 .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("123456@qq.com"))
                .andDo(MockMvcResultHandlers.print()).andReturn();
    }
}
