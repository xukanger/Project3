package just.yt.controller;

import just.yt.model.TUser;
import just.yt.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by yt on 2016/12/13.
 */
@Controller
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;

    @RequestMapping("/showUser")
    public String toIndex(HttpServletRequest request, Model model) {
        //int userId = Integer.parseInt(request.getParameter("id"));
        TUser user = this.userService.getUser();
        model.addAttribute("user", user);
        return "showUser";
    }
}