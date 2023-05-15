package ch.uzh.ifi.hase.soprafs23.controller;

import ch.uzh.ifi.hase.soprafs23.config.WebSocketConfigOne;
import ch.uzh.ifi.hase.soprafs23.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs23.core.GameContext;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.stereotype.Component;

import javax.websocket.server.ServerEndpoint;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@WebMvcTest(GameSyncController.class)
class GameSyncControllerTest {
    GameSyncController gameSyncController = new GameSyncController();

    @MockBean
    private UserService userService;
    User user;
    @BeforeEach
    public void initUser() {
        user = new User();
        user.setName("Firstname Lastname");
        user.setUsername("firstname@lastname");
        user.setPassword("firstname@123");
        user.setToken("1");
        user.setId(1);
        user.setStatus(UserStatus.OFFLINE);
        WebSocketConfigOne.executor = new ThreadPoolExecutor(4, 40, 60l, TimeUnit.SECONDS, new LinkedBlockingQueue<>(8));
        GameContext gameContext = new GameContext();
        gameContext.prepare(user);
        CardsController.GAME_ROOM.put(0, gameContext);
        CardsController.GAME_ROOM.put(0, gameContext);
        gameSyncController.setUserService(userService);

    }

    @Test
    void testOnOpen() {
        // Setup
        given(userService.getUserByToken("1")).willReturn(user);

        gameSyncController.onOpen(null,"1",0);
        // Verify the results
    }

    @Test
    void testOnClose() {
        // Setup
        // Run the test

        // Verify the results
    }
}