package ch.uzh.ifi.hase.soprafs23.model;

import ch.uzh.ifi.hase.soprafs23.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserReqVoTest {
    @Test
    public void testUserReqVo(){
        User user = new User();
        user.setName("Firstname Lastname");
        user.setUsername("firstname@lastname");
        user.setPassword("firstname@123");
        user.setStatus(UserStatus.ONLINE);
        user.setToken("Token");
        user.setId(1);

        UserReqVo userReqVo = new UserReqVo();
        userReqVo.setName(user.getName());
        userReqVo.setUsername(user.getUsername());
        userReqVo.setPassword(user.getPassword());
        userReqVo.setStatus(user.getStatus());
        userReqVo.setToken(user.getToken());
        userReqVo.setId(user.getId());
        userReqVo.setRepeatPassword("password");

        assertEquals(user.getName(),userReqVo.getName());
        assertEquals(user.getUsername(), userReqVo.getUsername());
        assertEquals(user.getPassword(), userReqVo.getPassword());
        assertEquals(user.getStatus(), userReqVo.getStatus());
        assertEquals(user.getToken(), userReqVo.getToken());
        assertEquals(user.getId(), userReqVo.getId());
        assertEquals("password", userReqVo.getRepeatPassword());

    }

    @Test
    public void testUserReqVoWithNullRepeatPassword() {
        // Test the case where repeatPassword is null
        UserReqVo userReqVo = new UserReqVo();
        assertNull(userReqVo.getRepeatPassword());
    }

    @Test
    public void testUserReqVoWithNonNullRepeatPassword() {
        // Test the case where repeatPassword is not null
        UserReqVo userReqVo = new UserReqVo();
        userReqVo.setRepeatPassword("password");
        assertEquals("password", userReqVo.getRepeatPassword());
    }

}