package ch.uzh.ifi.hase.soprafs23.model;

import ch.uzh.ifi.hase.soprafs23.entity.User;
import com.github.houbb.heaven.util.lang.BeanUtil;
import com.google.common.collect.Lists;
import lombok.Data;

import javax.websocket.Session;
import java.util.List;
import java.util.Objects;

@Data
public class UserVo extends User{

    public UserVo(User user){
        BeanUtil.copyProperties(user,this);
        // BeanUtils.copyProperties(user,this);
        this.handCard = Lists.newArrayList();
    }

    /**
     * cards of the plyaer
     */
    private List<Poker> handCard;


    /***
     * whether to ask for landlord's right
     */
    private Boolean contend;

    /**
     * whether to continue
     */
    private boolean isContinue;

    /**
     * session
     */
    private transient  Session session;

    public void  init(){
        handCard = Lists.newArrayList();
        contend = null;
        isContinue = false;
    }

    /**
     * if the id is equal, then the two users are equal
     * @param obj
     * @return
     */

    @Override
    public boolean equals(Object obj) {
        if(Objects.isNull(obj)){
            return false;
        }
        if(obj instanceof UserVo){
            UserVo userVo = (UserVo) obj;
            return this.getId().equals(userVo.getId());
        }
        return false;
    }

}
