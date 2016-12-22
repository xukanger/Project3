package just.yt.controller;

import just.yt.controller.vo.Page;
import just.yt.model.TUser;
import just.yt.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by yt on 2016/12/13.
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;
    @RequestMapping(value="/login",method= RequestMethod.POST)
    public @ResponseBody TUser login(@RequestBody TUser tuser) {
        TUser user = this.userService.getUser(tuser.getName(),tuser.getPassword());
        return user;
    }

    @RequestMapping(value="/signIn",method= RequestMethod.POST)
    public @ResponseBody TUser signIn(@RequestBody TUser tuser) {
        TUser user = this.userService.insert(tuser);
        return user;
    }

    @RequestMapping(value="/getAll",method= RequestMethod.GET)
    public @ResponseBody List<TUser> getAll() {
        List<TUser> users = this.userService.getAll();
        return users;
    }

    @RequestMapping(value="/delete/{id}", method=RequestMethod.POST)
    public @ResponseBody String delete(@PathVariable Integer id) {
        userService.delete(id);
        return null;
    }

    @RequestMapping(value="/update", method=RequestMethod.POST)
    public @ResponseBody String update(@RequestBody TUser tuser) {
        userService.update(tuser);
        return null;
    }

    @RequestMapping(value="/save", method=RequestMethod.POST)
    public @ResponseBody String save(@RequestBody TUser tuser) {
        userService.save(tuser);
        return null;
    }

    @RequestMapping(value="/count", method=RequestMethod.POST)
    public @ResponseBody Integer count() {
        return userService.count();
    }

    @RequestMapping(value="/{id}",method= RequestMethod.GET)
    public @ResponseBody TUser getById(@PathVariable Integer id) {
       return this.userService.getById(id);
    }

    @RequestMapping(value="/getByPage",method= RequestMethod.GET)
    public @ResponseBody List<TUser> getByPage(@RequestBody Page page) {
        List<TUser> users = this.userService.getByPage(page.getPageSize(),page.getPageNo());
        return users;
    }




}