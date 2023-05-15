package ch.uzh.ifi.hase.soprafs23;

import ch.uzh.ifi.hase.soprafs23.config.WebSocketConfigOne;
import ch.uzh.ifi.hase.soprafs23.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs23.controller.CardsController;
import ch.uzh.ifi.hase.soprafs23.controller.RoomSync;
import ch.uzh.ifi.hase.soprafs23.controller.UserController;
import ch.uzh.ifi.hase.soprafs23.core.GameContext;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.model.Result;
import ch.uzh.ifi.hase.soprafs23.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs23.service.UserService;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Objects;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@Slf4j
@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IntegrationTest {


    @Autowired
    private UserService userService;

    @Autowired
    private UserController userController;

    @MockBean
    private RoomSync mockRoomSync;

    private Gson gson = new Gson();

    private  User user;

    @Autowired
    private UserRepository userRepository;

    public static final AtomicInteger add= new AtomicInteger();


    @Test
    @DirtiesContext
    public void testLogin() throws Exception {
        // Setup

        int i = add.get();
        user = new User();
        user.setName("Firstname Lastname");
        user.setUsername("firstname@lastname");
        user.setPassword("firstname@123");
        user.setStatus(UserStatus.OFFLINE);
        try {
            user =  userService.createUser(user);
        }catch (Exception e){
            log.info(e.getMessage());
        }

        assertNotNull(user.getToken());

        Result login = userController.login("firstname@lastname", "firstname@123");

        assertEquals(login.status,200);
        assertNotNull(login.getData());


        User loginUser = userService.login(user.getUsername(),user.getPassword());
        assertNotNull(loginUser.getId());
        assertEquals(loginUser.getName(), user.getName());
        assertEquals(loginUser.getUsername(), user.getUsername());

        // when
        User found = userRepository.findByUsername("firstname@lastname");
        // then
        assertNotNull(found.getId());
        assertEquals(found.getName(), user.getName());
        assertEquals(found.getUsername(), user.getUsername());


        GameContext gameContextUnderTest = new GameContext();
        user = new User(0, "name", "password", "username", "token", UserStatus.ONLINE);
        User user1 = new User(1, "name", "password", "username", "token", UserStatus.ONLINE);
        User user2 = new User(2, "name", "password", "username", "token", UserStatus.ONLINE);
        WebSocketConfigOne.executor = new ThreadPoolExecutor(4,40,60l, TimeUnit.SECONDS,new LinkedBlockingQueue<>(8));

        gameContextUnderTest.prepare(user);
        gameContextUnderTest.prepare(user1);
        gameContextUnderTest.prepare(user2);
        gameContextUnderTest.initPokers();

        gameContextUnderTest.continueGame(0);
        gameContextUnderTest.continueGame(1);
        gameContextUnderTest.continueGame(2);

        assertEquals(gameContextUnderTest.getGameStatus(),2);


    }
}
