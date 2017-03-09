package just.yt.controller;

import just.yt.model.Examinee;
import just.yt.model.ExamineeExample;
import just.yt.model.TestMark;
import just.yt.service.ExamineeService;
import just.yt.service.TestMarkService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Date;
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

    @Resource
    TestMarkService testMarkService;


    /*
     *登陆页面
     *
     */
    @RequestMapping(value ="/login",method= RequestMethod.GET)
    public  String index() {
        return "login";
    }



    /*
     *检测账号
     */
    @RequestMapping(value ="/doLogin",method= RequestMethod.POST)
    public  ModelAndView firstLogin(@RequestParam String id,@RequestParam String username, HttpSession session) {
        ExamineeExample examineeExample = new ExamineeExample();
        examineeExample.or().andIdentityEqualTo(id).andNameEqualTo(username);
        List<Examinee> list=examineeService.selectByExamlpe(examineeExample);

        ModelAndView mav = new ModelAndView();

        if(list.size()==0){
            mav.setViewName("redirect:/test/");
        }else{
            session.setAttribute("user",list.get(0));
            mav.setViewName("wait");
        }

        return mav;
    }

    /*
     *确认广播无误
     */
    @RequestMapping(value ="/confirm",method= RequestMethod.GET)
    public  String confirmRecord(HttpSession session) {
            return "choose";
    }


    /*
     *选择题号
     */
    @RequestMapping(value ="/doChoose",method= RequestMethod.POST)
    public  ModelAndView choose(HttpSession session,@RequestParam Integer quesNum) {
        ModelAndView modelAndView = new ModelAndView();
        Boolean isTesting= (Boolean) session.getAttribute("isTesting");
        if(isTesting==null||!isTesting){
            TestMark testMark = new TestMark();
            Examinee examinee = (Examinee) session.getAttribute("user");
            testMark.setIdentity(examinee.getIdentity());
            testMark.setName(examinee.getName());
            testMark.setNum(quesNum);
            testMark.setStart(new Date().getTime());
            testMarkService.insert(testMark);
            session.setAttribute("testMark",testMark);
            session.setAttribute("isTesting",true);
        }
        modelAndView.setViewName("redirect:/test/test");
        return modelAndView;
    }


    /*
    * 考试页面
    * */
    @RequestMapping(value ="/test",method= RequestMethod.GET)
    public  ModelAndView test(HttpSession session) {
        ModelAndView modelAndView = new ModelAndView();
        TestMark testMark = (TestMark) session.getAttribute("testMark");
        if(!Objects.isNull(testMark.getConfirm())&&testMark.getConfirm()==1){
            modelAndView.setViewName("noRepeat");
            return modelAndView;
        }
        modelAndView.addObject("startTime",testMark.getStart());
        modelAndView.addObject("answer",testMark.getContent());
        modelAndView.setViewName("examination");
        return modelAndView;
    }

    /*
     *考试提交
     */
    @RequestMapping(value ="/submit",method= RequestMethod.POST)
    public  ModelAndView submitAnswer(HttpSession session,@RequestParam String answer) {
        ModelAndView mav = new ModelAndView();
        TestMark testMark = (TestMark) session.getAttribute("testMark");
        if(!Objects.isNull(testMark.getConfirm())&&testMark.getConfirm()==1){
            mav.setViewName("redirect:/test/noRepeat");
            return mav;
        }
        testMark.setContent(answer);
        testMarkService.update(testMark);
        mav.addObject("answer",answer);

        mav.addObject("startTime",((TestMark) session.getAttribute("testMark")).getStart());
        mav.setViewName("checkAnswer");

        return mav;
    }

    /*
     *修改答案
     *
     */
    @RequestMapping(value ="/modify",method= RequestMethod.POST)
    public  ModelAndView modify(HttpSession session) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("redirect:/test/test");
        return mav;
    }


    @RequestMapping(value ="/checkAnswer",method= RequestMethod.POST)
    public  String confirmAnswer(HttpSession session) {
            TestMark testMark =(TestMark) session.getAttribute("testMark");
            testMark.setConfirm(1);
            testMarkService.update(testMark);
            return "redirect:/test/finish";
    }

    @RequestMapping(value ="/finish",method= RequestMethod.GET)
    public  String finish(HttpSession session) {
        return "finish";
    }

    @RequestMapping(value ="/noRepeat",method= RequestMethod.GET)
    public  String noRepeat(HttpSession session) {
        return "noRepeat";
    }


}
