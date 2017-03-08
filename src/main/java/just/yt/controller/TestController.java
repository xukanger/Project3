package just.yt.controller;

import just.yt.model.Examinee;
import just.yt.model.ExamineeExample;
import just.yt.service.ExamineeService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Objects;

/**
 * Created by yt on 2017/3/3.
 */
@Controller
@RequestMapping("/test")
public class TestController {

    @Resource
    ExamineeService examineeService;

    @RequestMapping(value ="/**",method= RequestMethod.GET)
    public  String exception() {
        return "redirect:/test/";
    }

    @RequestMapping(value ="/",method= RequestMethod.GET)
    public  String index() {
        return "login";
    }

    @RequestMapping(value ="/test",method= RequestMethod.GET)
    public  String test() {
        return "examination";
    }

    @RequestMapping(value ="/doLogin",method= RequestMethod.POST)
    public  ModelAndView firstLogin(@RequestParam String id,@RequestParam String username, HttpSession session) {
        ExamineeExample examineeExample = new ExamineeExample();
        examineeExample.or().andIdentityEqualTo(id).andNameEqualTo(username);
        List<Examinee> list=examineeService.selectByExamlpe(examineeExample);

        ModelAndView mav = new ModelAndView();

        if(list.size()==0){//query fails
            //TODO
            mav.setViewName("redirect:/test/");
        }else{
            session.setAttribute("user",list.get(0));
            mav.setViewName("wait");
        }

        return mav;
    }

    @RequestMapping(value ="/confirm",method= RequestMethod.GET)
    public  String confirmRecord(HttpSession session) {
        if(Objects.isNull(session.getAttribute("user")))
            return "redirect:/test/";
        else
            return "choose";
    }

    @RequestMapping(value ="/doChoose",method= RequestMethod.POST)
    public  String choose(HttpSession session,@RequestParam String quesNum) {
        if(Objects.isNull(session.getAttribute("user")))
            return "redirect:/test/";
        else {
            //TODO
            return "examination";
        }
    }

    @RequestMapping(value ="/submit",method= RequestMethod.POST)
    public  ModelAndView submitAnswer(HttpSession session,@RequestParam String answer) {
        ModelAndView mav = new ModelAndView();
        if(Objects.isNull(session.getAttribute("user")))
            mav.setViewName("redirect:/test/");
        else {
            //TODO
            session.setAttribute("answer",answer);
            mav.addObject("answer",answer);
            mav.setViewName("checkAnswer");
        }
        return mav;
    }

    @RequestMapping(value ="/modify",method= RequestMethod.POST)
    public  ModelAndView modify(HttpSession session) {
        ModelAndView mav = new ModelAndView();
        if(Objects.isNull(session.getAttribute("user")))
            mav.setViewName("redirect:/test/");
        else {
            //TODO
            mav.addObject("answer",session.getValue("answer"));
            mav.setViewName("examination");
        }
        return mav;
    }


    @RequestMapping(value ="/checkAnswer",method= RequestMethod.POST)
    public  String confirmAnswer(HttpSession session) {
        if(Objects.isNull(session.getAttribute("user")))
            return "redirect:/test/";
        else
            //TODO
            return "finish";
    }
}
