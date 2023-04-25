package ch.uzh.ifi.hase.soprafs23.model;

import ch.uzh.ifi.hase.soprafs23.entity.User;
import lombok.Data;

@Data
public class UserReqVo extends User {
    private String repeatPassword;
}
