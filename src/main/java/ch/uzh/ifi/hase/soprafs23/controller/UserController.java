package ch.uzh.ifi.hase.soprafs23.controller;

import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.model.Result;
import ch.uzh.ifi.hase.soprafs23.model.UserReqVo;
import ch.uzh.ifi.hase.soprafs23.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ch.uzh.ifi.hase.soprafs23.controller.base.BaseController;



/**
 * User Controller
 * This class is responsible for handling all REST request that are related to
 * the user.
 * The controller will receive the request and delegate the execution to the
 * UserService and finally return the result.
 */
@RestController
@RequestMapping("/user")
public class UserController extends BaseController{

  @Autowired
  private UserService userService;


    //register
    @PostMapping("/register")
    public Object register(@RequestBody User user){
        userService.createUser(user);
        return user;
    }

    //login
    @PostMapping("/login")
    public Result login(String username, String password){
        User login = userService.login(username, password);
        login.setPassword(null);
        return Result.success(login);
    }

    /**
     * 查看资料
     * @return
     */
    @GetMapping("userDetail")
    public Result<User> getUserDetail(){return Result.success(getUser());}




    //修改密码
    @PostMapping("/updatePass")
    public Result updatePass(@RequestBody UserReqVo user){
        userService.updatePassword(user, getUser());
        return Result.success();
    }



//修改资料
    @PutMapping("/updateDetail")
    public Result updateDetail(@RequestBody UserReqVo user ){
        userService.updateDetail(user,getUser());
        return Result.success();
    }


    @PutMapping("/offline")
    public Result offline(){
        userService.offline(getUser());
        return Result.success();
    }
}
