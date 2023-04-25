package ch.uzh.ifi.hase.soprafs23.core;

import ch.uzh.ifi.hase.soprafs23.config.WebSocketConfigOne;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.model.Poker;
import ch.uzh.ifi.hase.soprafs23.model.Result;
import ch.uzh.ifi.hase.soprafs23.model.UserVo;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 游戏牌局上下文：
 * 初始化牌组，加入房间等等
 */

@Data
public class GameContext implements Serializable {

    /**
     * 牌局用户 用户id，用户对象
     */
    private List<UserVo> userList = Lists.newArrayList();

    private List<Poker> pokers;

    private String code;

    /**
     * 是否结束
     */
    private volatile boolean isGameOver;

    /**
     * 当前操作用户
     */
    private Integer now;

    /**
     * 庄
     */
    private Integer start;

    /**
     * 1 等待  2 抢地主 3 出牌 4 结束
     */
    private int gameStatus = 1;


    /**
     * 上一副牌
     */
    private PokerCombination last;


    /**
     * 当前出牌对象
     */
    private UserVo user;

    /**
     * 获胜者id
     */
    private Integer winner;



    /**
     * 初始化牌组
     * initialize the card deck
     */
    public void initPokers(){
        pokers = Lists.newArrayList(Poker.builder().value(14).build(),
                Poker.builder().value(15).build());
        for (int i = 1; i < 14; i++) {
            for (int j = 1; j < 5; j++) {
                pokers.add(Poker.builder()
                        .value(i)
                        .type(j)
                        .build());
            }
        }
    }

    //同步
    public void sync(){

        Gson gson = new Gson();
        Result<GameContext> success = Result.success(this);
        WebSocketConfigOne.executor.execute(()->{
            //同步数据
            this.userList.forEach(userVo -> {
                if(Objects.isNull(userVo.getSession())){
                    return;
                }
                try {
                    synchronized(userVo){
                        userVo.getSession().getBasicRemote().sendText(gson.toJson(success));
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            });
        });
    }

    //同步
    public void sync(Integer id){

        Gson gson = new Gson();
        Result<GameContext> success = Result.success(this);

        UserVo user = getUser(id);
        WebSocketConfigOne.executor.execute(()-> {
            if (Objects.nonNull(user.getSession()))
                user.getSession().getAsyncRemote().sendText(gson.toJson(success));
        });
    }


    /**
     * This method is used to prepare a player for the game.
     * 准备，确定当前用户，判断用户是否存在
     * @param userId the ID of the player who wants to prepare
     */
    public synchronized void continueGame(Integer userId){
        // Initialize the user's information and mark them as ready
        UserVo user = getUser(userId);
        user.init();
        user.setContinue(true);

        // Check if all players are ready
        List<UserVo> collect = this.userList.stream().filter(UserVo::isContinue).collect(Collectors.toList());
        if(collect.size()==3){
            // If all players are ready, restart the game and deal the cards
            restart();
            sendCard();
        }
        sync();
    }

    public UserVo getUser(Integer id){
        for (UserVo userVo : this.userList) {
            if( userVo.getId().equals(id)){
                return userVo;
            }
        }
        throw new RuntimeException("Unknown User.");
    }
    public UserVo exist(Integer id){
        for (UserVo userVo : this.userList) {
            if( userVo.getId().equals(id)){
                return userVo;
            }
        }
        return null;
    }

    /**
     * let the user join a room
     */
    public synchronized void prepare(User user){

        UserVo userVo = exist(user.getId());
        if(Objects.nonNull(userVo)){
            return;
        }
        if(isGameOver){
            throw new RuntimeException("the game already starts, you can not join in");
        }
        if(this.userList.size()>=3) {
            throw new RuntimeException("the room is already full");
        }
        this.userList.add(new UserVo(user));
        sync();
    }

    /**
     * Quit the Game Room
     * @param userId
     * @return "Is the room empty?"
     */
    public synchronized boolean quitGame(Integer userId){
        UserVo user = this.getUser(userId);
        this.userList.remove(user);
        sync();
        return this.userList.size()>0;
    }

    /**
     * Get Poker Combination
     * @param id
     * @return Null
     */
    public List<PokerCombination> getPokerCombination(Integer id){
        return null;
    }

    /**
     * This method is used to play a poker combination for a player in the game.
     * 出牌出到错误牌型，显示出牌错误
     * @param pokerCombination the poker combination to be played
     * @param userId the ID of the player who wants to play the combination
     * @return true if the play is successful, false otherwise
     * @throws RuntimeException if it's not the player's turn or the poker combination is invalid
     */
    public boolean pay(PokerCombination pokerCombination,Integer userId){
        pokerCombination.initType();
        if(!this.userList.get(this.now).getId().equals(userId)){
            throw new RuntimeException("It's not your turn.");
        }
        //比较牌型
        if(Objects.nonNull(this.last) && !this.last.compareTo(pokerCombination)){
            throw new RuntimeException("Invalid card combination.");
        }
        // 扣减手牌
        this.last = pokerCombination;
        UserVo userVo = getUser(pokerCombination.getUserId());
        userVo.getHandCard().removeAll(pokerCombination.getCard());
        // 判断是否还有手牌
        if(userVo.getHandCard().size()==0){
            this.winner = this.now;
            gameOver();
        }else {
            // 更换出牌对象
            next();
        }
        sync();
        return true;
    }

    /**
     * This method allows a player to pass their turn without playing a card.
     * 跳过
     * @param id the ID of the player who wants to pass their turn
     * @throws RuntimeException if it's not the player's turn or if it's the first turn of the game
     */
    public void pass(Integer id){
        if(!this.userList.get(this.now).getId().equals(id)){
            throw new RuntimeException("Not your turn.");
        }
        if(Objects.isNull(last)){
            throw new RuntimeException("First turn, Cannot pass.");
        }
        // Move on to the next player's turn
        next();
        //如果最后一手牌和下一位是同一个用户 则重置上一首牌
        // If the last played combination belongs to the next player, reset it to null
        if(now == this.userList.indexOf( getUser(this.last.getUserId()))){
            this.last = null;
        }
        //同步 Update the game state
        sync();
    }

    /**
     * 发牌
     */
    private void sendCard(){

        this.start = (int) (Math.random() * 3);

        initPokers();
        //发牌
        int i = this.start;

        while (pokers.size()!=3) {

            UserVo userVo = userList.get(i);
            //随机抽取一张底牌
            int num = (int) (Math.random() * pokers.size());

            Poker poker = pokers.get(num);

            pokers.remove(poker);
            //加入手牌
            userVo.getHandCard().add(poker);

            if(++i == 3){
                i = 0;
            }
        }

        gameStatus = 2;

        this.now = start;
        //同步牌
        sync();

    }

    /**
     * 通知抢地主
     */
    private void contend(){
        List<UserVo> collect = this.userList.stream()
                .filter(userVo -> Objects.isNull(userVo.getContend())).collect(Collectors.toList());
        //第二轮抢地主 如果还有两位或者三位争取 只看首位是否需要
        if(CollectionUtils.isEmpty(collect)){
            UserVo userVo = this.userList.get(this.start);
            this.now = this.start;
            if(!userVo.getContend()){
                next();
            }
        }
    }

    /**
     * 抢地主
     * @param id
     * @param isContend
     */
    public void contend(Integer id,boolean isContend){
        //修改用户抢地主状态
        List<UserVo> collect = this.userList.stream()
                .filter(user->user.getId().equals(id))
                .collect(Collectors.toList());
        for (UserVo userVo : collect) {
            //如果已经存在状态 则表示为第二轮选举 直接选为地主 其他玩家放弃选择
            if(Objects.nonNull(userVo.getContend()) && isContend){
                this.userList.stream()
                        .filter(user->
                                !user.getId().equals(id))
                        .forEach(user->
                                user.setContend(false));
                //放弃则 上一玩家为地主
            }
            userVo.setContend(isContend);
        }

        next();

        //通知地主分配
        elect();

        sync();
    }

    /**
     * 下一个
     */
    private void next() {
        this.now = this.now+1;
        if(this.now>2){
            this.now=0;
        }
    }

    /**
     * 分配地主
     */
    private void elect(){

        for (UserVo userVo : this.userList) {
            if(Objects.isNull(userVo.getContend())){
                //通知下一个
                contend();
                return;
            }
        }

        //判断是否可以直接获取地主
        List<UserVo> collect = this.userList.stream()
                .filter(UserVo::getContend).collect(Collectors.toList());
        if(collect.size() != 1){
            //通知下一个
            contend();
            return;
        }
        UserVo userVo = collect.get(0);
        //添加到牌组
        userVo.getHandCard().addAll(this.pokers);

        this.start = this.userList.indexOf(userVo);

        this.now = this.start;

        this.gameStatus =3;

        sync();
    }

    /***
     * 游戏结束
     */
    private void gameOver(){
        this.isGameOver = true;
        this.userList.forEach(list->{
            list.setContend(null);
            list.setContinue(false);
        });
        this.gameStatus = 4;
    }

    /***
     * 重新开始
     */
    private void restart(){
        this.gameStatus = 1;
        this.last = null;
        this.winner = null;
    }


}
