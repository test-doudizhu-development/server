package ch.uzh.ifi.hase.soprafs23.controller;

import ch.uzh.ifi.hase.soprafs23.core.GameContext;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.model.Result;
import ch.uzh.ifi.hase.soprafs23.model.UserVo;
import ch.uzh.ifi.hase.soprafs23.service.UserService;
import cn.hutool.extra.spring.SpringUtil;
import com.google.gson.Gson;
import lombok.Data;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Objects;

@Component
@ServerEndpoint(value = "/ws/ddz/sync/{roomCode}/{token}")
@Data
public class GameSyncController {


    private Session session;

    private UserService userService;

    private UserVo userVo;

    private GameContext gameContext;

    private String token;

    private String roomCode;

    private Gson gson = new Gson();

    public GameSyncController() {
        try{
            userService = SpringUtil.getBean(UserService.class);
        }catch (Exception e){
        }
    }

    /**
     * actions when open the session
     * @param session
     */
    @OnOpen
    public void onOpen(Session session, @PathParam(value = "token") String token, @PathParam(value = "roomCode") Integer roomCode) {
        this.session = session;
        this.token = token;
        //get the room
        gameContext = CardsController.GAME_ROOM.get(roomCode);
        User user = userService.getUserByToken(token);
        userVo = gameContext.getUser(user.getId());
        if(Objects.isNull(userVo)){
            session.getAsyncRemote().sendText(gson.toJson(Result.error("not in the room yet")));
        }
        userVo.setSession(session);
        gameContext.sync(userVo.getId());
    }




    /**
     * 断开连接时调用
     * @param session
     */
    @OnClose
    public void onClose(Session session){
        userVo.setSession(null);
    }

}