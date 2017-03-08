package just.yt.controller;

import just.yt.model.BopsUser;
import just.yt.service.BopsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import tool.DefaultResult;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Objects;

/**
 * Created by llf on 2017/3/5.
 */
    @Controller
    @RequestMapping("/bops")
    public class BopsController {
         @Resource
        BopsService bopsService;

    @RequestMapping(value ="/",method= RequestMethod.GET)
    public  String index() {
        return "bopslogin";
    }

     @RequestMapping(value ="/login/login",method= RequestMethod.GET)
     public ModelAndView login() {
            ModelAndView mav = new ModelAndView();
            mav.setViewName("bopslogin");
            return mav;
      }

    @RequestMapping(value ="/login/dologin",method= RequestMethod.POST)
    public ModelAndView doLogin(String account, String password,HttpSession session) {
        DefaultResult result = bopsService.login(account, password);
        ModelAndView mav = new ModelAndView();
        if (result.isSuccess()){
            session.setAttribute("user",result.getData());
            mav.setViewName("redirect:/bops/index");
        }else {
            mav.setViewName("redirect:/bops/");
        }
        return mav;
    }

    @RequestMapping(value ="/index",method= RequestMethod.GET)
    public ModelAndView index(HttpSession session) {
        ModelAndView mav = new ModelAndView();
        if(Objects.isNull(session.getAttribute("user"))) {
            mav.setViewName("redirect:/bops/");
        }
        mav.addObject("user",session.getAttribute("user"));
        mav.setViewName("bopsindex");
        return mav;
    }

    @RequestMapping(value ="/bopsUser/add",method= RequestMethod.GET)
    public ModelAndView addUser(BopsUser user,HttpSession session) {
        ModelAndView mav = new ModelAndView();
        if(Objects.isNull(session.getAttribute("user"))) {
            mav.setViewName("redirect:/bops/");
        }
        mav.setViewName("addbopsuser");
        return  mav;
    }

    @RequestMapping(value ="/bopsUser/save",method= RequestMethod.GET)
    public ModelAndView saveUser(BopsUser user,HttpSession session) {
        ModelAndView mav = new ModelAndView();
        if(Objects.isNull(session.getAttribute("user"))) {
            mav.setViewName("redirect:/bops/");
        }
        mav.setViewName("redirect:/bops/index");
        return  mav;
    }

    @RequestMapping(value ="/bopsUser/importoutput",method= RequestMethod.GET)
    public ModelAndView importExaminee() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("IO");
        return  mav;
    }

    @RequestMapping(value ="/bopsUser/excelUpload",method= RequestMethod.GET)
    public ModelAndView excelUpload() {
        return  null;
    }

    @RequestMapping(value ="/bopsUser/outputExamineeA",method= RequestMethod.GET)
    public ModelAndView outputExamineeA() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("addbopsuser");
        return  mav;
    }

    @RequestMapping(value ="/bopsUser/outputExamineeB",method= RequestMethod.GET)
    public ModelAndView outputExamineeB() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("addbopsuser");
        return  mav;
    }
}
