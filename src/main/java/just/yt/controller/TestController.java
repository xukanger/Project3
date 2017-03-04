package just.yt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by yt on 2017/3/3.
 */
@Controller
@RequestMapping("/test")
public class TestController {

    @RequestMapping(value="/data",method= RequestMethod.GET)
    public  ModelAndView data() {
        ModelAndView mav= new ModelAndView();
        mav.addObject("city","test");
        mav.setViewName("helloworld");
        return mav;
    }

}
