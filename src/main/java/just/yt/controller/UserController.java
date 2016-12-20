package just.yt.controller;

import just.yt.controller.vo.User;
import just.yt.model.TUser;
import just.yt.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * Created by yt on 2016/12/13.
 */
@Controller
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;
    @RequestMapping(value="/login",method= RequestMethod.POST)
    public @ResponseBody TUser login(@RequestBody User tuser) {
        TUser user = this.userService.getUser(tuser.getName(),tuser.getPassword());
        return user;
    }

//    @RequestMapping(value="/signIn",method= RequestMethod.POST)
//    public @ResponseBody TUser signIn(@RequestBody User tuser) {
//        System.out.println(tuser);
//        TUser user = this.userService.insert(tuser);
//        return user;
//    }
}