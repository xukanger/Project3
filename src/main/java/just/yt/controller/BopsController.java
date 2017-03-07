package just.yt.controller;

import just.yt.service.BopsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import tool.DefaultResult;

import java.util.Map;

/**
 * Created by llf on 2017/3/5.
 */
    @Controller
    @RequestMapping("/bops")
    public class BopsController {
      BopsService bopsService;
     @RequestMapping(value ="/login/login",method= RequestMethod.GET)
     public ModelAndView login() {
            ModelAndView mav = new ModelAndView();
            mav.setViewName("bopslogin");
            return mav;
      }
    @RequestMapping(value ="/login/dologin",method= RequestMethod.POST)
    public ModelAndView doLogin(Map<String, Object> model, String account, String password) {
        DefaultResult result = bopsService.login(account, password);
        ModelAndView mav = new ModelAndView();
        if (result.isSuccess()){
            mav.setViewName("bopsindex");
        }else {
            mav.setViewName("bopslogin");
        }
        return mav;
    }
}
