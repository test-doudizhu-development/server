package ch.uzh.ifi.hase.soprafs23.core;

import ch.uzh.ifi.hase.soprafs23.config.WebSocketConfigOne;
import ch.uzh.ifi.hase.soprafs23.constant.CombinationType;
import ch.uzh.ifi.hase.soprafs23.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.model.Poker;
import ch.uzh.ifi.hase.soprafs23.model.UserVo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class GameContextTest {

    private GameContext gameContextUnderTest;
    User user;

    @BeforeEach
    void setUp() {
        gameContextUnderTest = new GameContext();
        user = new User(0, "name", "password", "username", "token", UserStatus.ONLINE);
        User user1 = new User(1, "name", "password", "username", "token", UserStatus.ONLINE);
        User user2 = new User(2, "name", "password", "username", "token", UserStatus.ONLINE);
        WebSocketConfigOne.executor = new ThreadPoolExecutor(4,40,60l, TimeUnit.SECONDS,new LinkedBlockingQueue<>(8));

        gameContextUnderTest.prepare(user);
        gameContextUnderTest.prepare(user1);
        gameContextUnderTest.prepare(user2);
        gameContextUnderTest.initPokers();
    }


    @Test
    @DirtiesContext
    void testPay() {
        //准备 READY
        gameContextUnderTest.continueGame(0);
        gameContextUnderTest.continueGame(1);
        gameContextUnderTest.continueGame(2);

        //测试抢地主 CONTEND FOR THE LANDLORD
        gameContextUnderTest.contend(0, false);
        gameContextUnderTest.contend(1, false);
        gameContextUnderTest.contend(2, false);

        // 出牌 DEAL CARDS
        final PokerCombination pokerCombination = new PokerCombination();
        pokerCombination.setCombinationType(CombinationType.ONE);
        pokerCombination.setCard(List.of(Poker.builder()
                .value(1)
                .type(1)
                .build()));
        pokerCombination.setUserId(0);


        // Run the test
        boolean result = gameContextUnderTest.pay(pokerCombination, gameContextUnderTest.getNow());
        //pass
        gameContextUnderTest.pass(gameContextUnderTest.getNow());
        // Verify the results
        assertThat(result).isTrue();

        final UserVo resultUser = gameContextUnderTest.exist(0);

        // Verify the results
        assertNotNull(resultUser);


        //退出游戏 EXIT THE GAME
        result = gameContextUnderTest.quitGame(0);
        assertThat(result).isTrue();
    }

}
