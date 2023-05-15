package ch.uzh.ifi.hase.soprafs23.controller;

import ch.uzh.ifi.hase.soprafs23.controller.base.BaseController;
import ch.uzh.ifi.hase.soprafs23.core.GameContext;
import ch.uzh.ifi.hase.soprafs23.core.PokerCombination;
import ch.uzh.ifi.hase.soprafs23.model.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@RequestMapping("/cards")
@RestController
public class CardsController extends BaseController {
    public static final Map<Integer,GameContext> GAME_ROOM= new ConcurrentHashMap<>();

    public static final AtomicInteger add= new AtomicInteger();


    @Autowired
    private RoomSync roomSync;


    //create room
    @PostMapping
    public Result createGame(){
        GameContext e = new GameContext();
        int roomCode = add.getAndIncrement();
        GAME_ROOM.put(roomCode,e);
        e.setCode(roomCode+"");
        roomSync.push();
        return Result.success(roomCode);
    }

    //let the user join a game room
    @PostMapping("/addUser")
    public Result addUser(Integer roomCode){
        GameContext gameContext = GAME_ROOM.get(roomCode);
        if(Objects.isNull(gameContext)){
            return Result.error("未找到此房间 There is no found room");
        }
        gameContext.prepare(getUser());
        return Result.success();
    }

    //退出房间
    //quit room
    @DeleteMapping("/{roomCode}")
    public Result createGame(@PathVariable Integer roomCode){
        GameContext gameContext = GAME_ROOM.get(roomCode);
        if (!gameContext.quitGame(getUser().getId())) {
            GAME_ROOM.remove(roomCode);
            roomSync.push();
        }
        return Result.success();
    }

    //出牌
    //play the cards
    @PostMapping("/pay/{roomCode}")
    public Result pay(@PathVariable Integer roomCode, @RequestBody PokerCombination pokerCombination){
        //去重
        if(!CollectionUtils.isEmpty(pokerCombination.getCard())){
            pokerCombination.setCard(pokerCombination.getCard().stream().distinct().collect(Collectors.toList()));
        }else {
            throw new RuntimeException("Please select the cards to play.");
        }
        GameContext gameContext = GAME_ROOM.get(roomCode);
        gameContext.pay(pokerCombination, getUser().getId());
        return Result.success();
    }

    //跳过
    // Execute pass operation
    @PostMapping("/pass/{roomCode}")
    public Result pay(@PathVariable Integer roomCode){
        GameContext gameContext = GAME_ROOM.get(roomCode);
        gameContext.pass(getUser().getId());
        return Result.success();
    }

    //准备,这里是跳过以后继续游戏
    // continue the game
    @PutMapping("/{roomCode}")
    public Result continueGame(@PathVariable Integer roomCode){
        GameContext gameContext = GAME_ROOM.get(roomCode);
        gameContext.continueGame(getUser().getId());
        return Result.success();
    }


    //抢地主
    @PostMapping("/contend")
    public Result contend(Integer roomCode,boolean isContend){
        GameContext gameContext = GAME_ROOM.get(roomCode);
        gameContext.contend(getUser().getId(), isContend);
        return Result.success();
    }
}
