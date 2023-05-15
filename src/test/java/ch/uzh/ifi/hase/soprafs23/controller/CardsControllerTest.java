package ch.uzh.ifi.hase.soprafs23.controller;

import ch.uzh.ifi.hase.soprafs23.config.WebSocketConfigOne;
import ch.uzh.ifi.hase.soprafs23.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs23.core.GameContext;
import ch.uzh.ifi.hase.soprafs23.core.PokerCombination;
import ch.uzh.ifi.hase.soprafs23.entity.User;
//import ch.uzh.ifi.hase.soprafs23.interceptor.SessionInterceptor;
import ch.uzh.ifi.hase.soprafs23.model.Poker;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(CardsController.class)
class CardsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RoomSync mockRoomSync;

    private Gson gson = new Gson();


    User user;
    @BeforeEach
    public void initUser(){
        user = new User();
        user.setName("Firstname Lastname");
        user.setUsername("firstname@lastname");
        user.setPassword("firstname@123");
        user.setToken("1");
        user.setId(1);
        user.setStatus(UserStatus.OFFLINE);
        WebSocketConfigOne.executor = new ThreadPoolExecutor(4,40,60l, TimeUnit.SECONDS,new LinkedBlockingQueue<>(8));
        GameContext gameContext = new GameContext();
        gameContext.prepare(user);
        CardsController.GAME_ROOM.put(0,gameContext);

    }
    @Test
    void testCreateGame1() throws Exception {
        // Setup
        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(post("/cards") .content(gson.toJson(user))
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the resultS
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        verify(mockRoomSync).push();
    }

    @Test
    void testAddUser() throws Exception {
        // Setup
        // Run the test
        mockMvc.perform(post("/cards/addUser")
                        .param("roomCode", "0")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                //.andExpect(MockMvcResultMatchers.jsonPath("$.email").value("123456@qq.com"))
                .andDo(MockMvcResultHandlers.print()).andReturn();
    }

    @Test
    void testCreateGame2() throws Exception {
        // Setup
        // Run the test
        mockMvc.perform(delete("/cards/{roomCode}", 0)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
//                 .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("123456@qq.com"))
                .andDo(MockMvcResultHandlers.print()).andReturn();
    }

    @Test
    void testContend() throws Exception {
        // Setup
        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(post("/cards/contend")
                        .param("roomCode", "0")
                        .param("isContend", "false")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void testPay1() throws Exception {
        // Setup
        PokerCombination pokerCombination  = new PokerCombination();
        pokerCombination.setCard(Lists.newArrayList(Poker.builder().type(1).value(2).build()));
        pokerCombination.setUserId(1);

        // Run the test
        mockMvc.perform(post("/cards/pay/{roomCode}", 0)
                        // 参数类型为JSON
                        .contentType(MediaType.ALL)
                        .content("content").contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(pokerCombination))
                        .accept(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isOk())
//                 .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("123456@qq.com"))
                .andDo(MockMvcResultHandlers.print()).andReturn();

    }

    @Test
    void testPay2() throws Exception {
        // Setup
        // Run the test
        mockMvc.perform(post("/cards/pass/{roomCode}", 0)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
//                 .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("123456@qq.com"))
                .andDo(MockMvcResultHandlers.print()).andReturn();

        // Verify the results
    }

    @Test
    void testContinueGame() throws Exception {
        // Setup
        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(put("/cards/{roomCode}", 0)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }
}
