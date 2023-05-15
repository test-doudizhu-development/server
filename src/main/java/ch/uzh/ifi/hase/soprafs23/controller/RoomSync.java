package ch.uzh.ifi.hase.soprafs23.controller;


import ch.uzh.ifi.hase.soprafs23.config.WebSocketConfigOne;
import ch.uzh.ifi.hase.soprafs23.core.GameContext;
import ch.uzh.ifi.hase.soprafs23.model.Result;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import lombok.Data;
import org.springframework.stereotype.Component;

import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.*;

/**
 * the class for room synchronization
 */
@Component
@ServerEndpoint(value = "/ws/room/sync/{token}")
@Data
public class RoomSync {


    private static final Map<String, Session> sessionMap = Maps.newConcurrentMap();

    private String token;

    @OnOpen
    public void onOpen(Session session, @PathParam(value = "token") String token) {
        this.token = token;
        Gson gson = new Gson();

        Result<Collection<GameContext>> success = Result.success(CardsController.GAME_ROOM.values());

        try {
            if (Objects.nonNull(session)) {
                session.getBasicRemote().sendText(gson.toJson(success));
            }
        } catch (IOException e) {
        }
        if(Objects.nonNull(session)) {
            sessionMap.put(token,session);
        }


    }


    /**
     * synchronize the room code
     */
    public void push() {
        Gson gson = new Gson();
        Result<Collection<GameContext>> success = Result.success(CardsController.GAME_ROOM.values());
        WebSocketConfigOne.executor.execute(() -> {
            for (Session value : sessionMap.values()) {
                if (Objects.nonNull(value)) {
                    try {
                        value.getBasicRemote().sendText(gson.toJson(success));
                    }
                    catch (IOException e) {
                    }
                }
            }
        });
    }


}
