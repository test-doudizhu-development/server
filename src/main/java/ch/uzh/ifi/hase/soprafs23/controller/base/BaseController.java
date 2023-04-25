package ch.uzh.ifi.hase.soprafs23.controller.base;

import ch.uzh.ifi.hase.soprafs23.Interceptor.SessionInterceptor;
import ch.uzh.ifi.hase.soprafs23.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs23.entity.User;

import java.util.Objects;


//this is just because I can not change the commit message before, this class is used for corresponding issue

public class BaseController {
    /**
     * 获取当前登录用户
     * @return
     */
    public User getUser(){
        User user = SessionInterceptor.SESSION_USER.get();
        if(Objects.isNull(user)){
            user = new User();
            user.setName("Firstname, Lastname");
            user.setUsername("firstname@lastname");
            user.setPassword("firstname@123");
            user.setToken("1");
            user.setStatus(UserStatus.OFFLINE);
            user.setId(1);
            SessionInterceptor.SESSION_USER.set(user);
        }
        return user;
    }


}
