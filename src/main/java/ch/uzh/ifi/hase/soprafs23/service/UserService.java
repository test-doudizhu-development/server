package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.model.UserReqVo;
import ch.uzh.ifi.hase.soprafs23.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;
import java.util.UUID;

/**
 * User Service This class is the "worker" and responsible for all functionality related to the user
 * (e.g., it creates, modifies, deletes, finds). The result will be passed back to the caller.
 */
@Service
@Transactional
public class UserService {

  private final Logger log = LoggerFactory.getLogger(UserService.class);

  @Autowired
  private UserRepository userRepository;

  public User createUser(User newUser) {
    newUser.setToken(UUID.randomUUID().toString());
    newUser.setStatus(UserStatus.OFFLINE);
    checkIfUserExists(newUser);
    // saves the given entity but data is only persisted in the database once
    // flush() is called
    userRepository.save(newUser);

    log.debug("Created Information for User: {}", newUser);
    return newUser;
  }

    /***
     * login(check if the username is not null and password is correct)
     * @param userName
     * @param password
     * @return
     */
    public User login(String userName, String password){
      User user = userRepository.findByUsername(userName);
      if (Objects.nonNull(user) && user.getPassword().equals(password)){
          user.setToken(UUID.randomUUID().toString());
          user.setStatus(UserStatus.ONLINE);
          return user;
      }
      throw new RuntimeException("Password is not correct");
    }


    /**
     * offline(set status to offline)
     * @param old
     */
    public void offline(User old){
        User byId = userRepository.findById(old.getId()).get();
        byId.setStatus(UserStatus.OFFLINE);
        userRepository.save(byId);
    }

    /**
     * update password
     * @param userReqVo
     * @param old
     * @return
     */
    public User updatePassword(UserReqVo userReqVo, User old){
        User byId = userRepository.findById(old.getId()).get();
        if(userReqVo.getPassword().equals(userReqVo.getRepeatPassword())){
            byId.setPassword(userReqVo.getRepeatPassword());
            User save = userRepository.save(byId);
            return save;
        }
        throw new RuntimeException("The password is different!");
    }
    public User getUserByToken(String token){
        return userRepository.findByToken(token);
    }

  /**
   * This is a helper method that will check the uniqueness criteria of the username and the name
   * defined in the User entity. The method will do nothing if the input is unique and throw an
   * error otherwise.
   *
   * @param userToBeCreated
   * @throws ResponseStatusException
   * @see User
   */
  //fix #43
  private void checkIfUserExists(User userToBeCreated) {
    User userByUsername = userRepository.findByUsername(userToBeCreated.getUsername());
    User userByName = userRepository.findByName(userToBeCreated.getName());

    String baseErrorMessage =
        "The %s provided %s not unique. Therefore, the user could not be created!";
    if (userByUsername != null && userByName != null) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, String.format(baseErrorMessage, "username and the name", "are"));
    } else if (userByUsername != null) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, String.format(baseErrorMessage, "username", "is"));
    } else if (userByName != null) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, String.format(baseErrorMessage, "name", "is"));
    }
  }

    /**
     * 修改资料
     * @param userReqVo
     * @return
     */
    public User updateDetail(UserReqVo userReqVo,User old){
        User byId = userRepository.findById(old.getId()).get();
        byId.setName(userReqVo.getName());
        if(userReqVo.getPassword().equals(userReqVo.getRepeatPassword())){
            byId.setPassword(userReqVo.getRepeatPassword());
            User save = userRepository.save(byId);
            return save;
        }
        throw new RuntimeException("Make sure the passwords are consistent! ");
    }
}
