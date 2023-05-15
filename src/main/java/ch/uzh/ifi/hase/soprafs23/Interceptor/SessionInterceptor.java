package ch.uzh.ifi.hase.soprafs23.Interceptor;


import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;


@Component
public class SessionInterceptor implements HandlerInterceptor {
    public static final ThreadLocal<User> SESSION_USER = new ThreadLocal<>();
    public static final  String TOKEN_KEY = "Authorization";

    @Autowired(required = false)
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler){

        String token = request.getParameter(TOKEN_KEY);
        if(StringUtils.isEmpty(token)){
            token = request.getHeader(TOKEN_KEY);
        }
        if(StringUtils.isEmpty(token)){
            return true;
        }

        User userByToken = userService.getUserByToken(token);

        if(Objects.nonNull(userByToken)){
            SESSION_USER.set(userByToken);
        }
        return true;
    }
}
