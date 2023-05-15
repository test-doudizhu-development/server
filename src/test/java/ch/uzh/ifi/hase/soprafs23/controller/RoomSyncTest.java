package ch.uzh.ifi.hase.soprafs23.controller;

import ch.uzh.ifi.hase.soprafs23.config.WebSocketConfigOne;
import ch.uzh.ifi.hase.soprafs23.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs23.core.GameContext;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import javax.websocket.Session;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertNotNull;






@WebMvcTest(RoomSync.class)
class RoomSyncTest {
    private RoomSync roomSyncUnderTest;

    @BeforeEach
    void setUp(){
        roomSyncUnderTest = new RoomSync();
        roomSyncUnderTest.setToken("1");

    }

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
    void testOnOpen() {
        // Setup
        Session session = null;
        // Run the test
        roomSyncUnderTest.onOpen(session, "token");
        roomSyncUnderTest.push();
        assertNotNull(roomSyncUnderTest.getToken());
    }
}
