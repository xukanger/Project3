package just.yt.controller;

import just.yt.model.Examinee;
import just.yt.model.TestMark;
import just.yt.service.ExamineeService;
import just.yt.service.TestMarkService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import tool.DefaultResult;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;


@Controller
@RequestMapping("/retest")
public class RetestController {

    @Resource
    ExamineeService examineeService;

    @Resource
    TestMarkService testMarkService;

    public final static String LOGIN_URL =  "/test/login";

    public final static String TEST_URL = "/test/test";

    public final static String FINISH_URL = "/test/finish";

    public final static String NOREAPEAT_URL = "/test/noRepeat";

    public final static String WAIT_URL = "/test/wait";

    public final static String CHOOSE_URL = "/test/choose";

    public final static String CHECK_CHOOSE_URL = "/test/checkChoose";

    public final static String REDIRCT = "redirect:";

    public final static long TOTAL_SEC = 13*60;


    /*
     *登陆页面
     *
     */
    @RequestMapping(value ="/login",method= RequestMethod.GET)
    public  String index() {
        return "/WEB-INF/velocity/test/login.vm";
    }



    /*
     *检测账号
     */
    @RequestMapping(value ="/doLogin",method= RequestMethod.POST)
    public  @ResponseBody
    DefaultResult firstLogin(@RequestParam String id,
                             @RequestParam String username, HttpSession session) {
        List<Examinee> list = examineeService.selectByIdAndName(id,username);
        DefaultResult defaultResult = DefaultResult.result(true,WAIT_URL,null);
        if(list.isEmpty()){
            defaultResult = DefaultResult.result(false,
                    "请核对输入内容，如输入内容准确无误仍无法登陆，请举手示意",null);
        }else{
            Examinee examinee = list.get(0);
            session.setAttribute("user",list.get(0));
            if(isTestEnded(examinee, session)){
                session.setAttribute("user",null);
                return DefaultResult.result(true,FINISH_URL,null);
            }

            if(isTesting(examinee, session)){
                defaultResult = DefaultResult.result(true,TEST_URL,null);
            }
        }
        return defaultResult;
    }

    /*
     *等待
     */
    @RequestMapping(value ="/wait",method= RequestMethod.GET)
    public ModelAndView waitTest(HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("examinee",session.getAttribute("user"));
        modelAndView.setViewName("/WEB-INF/velocity/test/wait.vm");
        return modelAndView;
    }

    /*
     *选择
     */
    @RequestMapping(value ="/choose",method= RequestMethod.GET)
    public ModelAndView choose(HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("examinee",session.getAttribute("user"));
        modelAndView.setViewName("/WEB-INF/velocity/test/choose.vm");
        return modelAndView;
    }


    /*
     *做出选择
     */
    @RequestMapping(value ="/doChoose",method= RequestMethod.POST)
    public  @ResponseBody DefaultResult doChoose(HttpSession session,
                                @RequestParam String type,
                                @RequestParam Integer quesNum) {
        DefaultResult defaultResult = DefaultResult.result(true,CHECK_CHOOSE_URL,null);
        if(!checkType(type)){
            defaultResult = DefaultResult.result(false,"参数错误",null);
        }
        Examinee examinee = (Examinee) session.getAttribute("user");
        if(isTestEnded(examinee, session)){
            defaultResult = DefaultResult.result(true,FINISH_URL,null);
        }else if(isTesting(examinee, session)){
            defaultResult = DefaultResult.result(true,TEST_URL,null);
        }
        else{
            session.setAttribute("quesNum",quesNum);
            session.setAttribute("type",type);
        }
        return defaultResult;
    }

    @RequestMapping(value ="/checkChoose",method= RequestMethod.GET)
    public ModelAndView checkChoosePage(HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        Integer quesNum = (Integer) session.getAttribute("quesNum");
        String type = (String) session.getAttribute("type");
        Examinee examinee = (Examinee) session.getAttribute("user");
        if(quesNum == null || type == null){
            modelAndView.setViewName(REDIRCT+CHOOSE_URL);
        }
        modelAndView.addObject("examinee",examinee);
        modelAndView.addObject("quesNum",quesNum);
        modelAndView.addObject("type",type);
        modelAndView.setViewName("/WEB-INF/velocity/test/checkChoose.vm");
        return modelAndView;
    }


    /*
     *检查选择
     */
    @RequestMapping(value ="/finishChoose",method = RequestMethod.GET)
    public ModelAndView checkChoose(HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        Integer quesNum = (Integer) session.getAttribute("quesNum");
        String type = (String) session.getAttribute("type");
        Examinee examinee = (Examinee) session.getAttribute("user");
        if(quesNum == null || type == null){
            modelAndView.setViewName(REDIRCT+CHOOSE_URL);
        }else if(isTesting(examinee, session)){
            modelAndView.setViewName(REDIRCT+TEST_URL);
        }else if(isTestEnded(examinee, session)) {
            modelAndView.setViewName(REDIRCT+FINISH_URL);
        }else{
            TestMark testMark = new TestMark();
            testMark.setIdentity(examinee.getIdentity());
            testMark.setName(examinee.getName());
            testMark.setType((String) session.getAttribute("type"));
            testMark.setNum((Integer) session.getAttribute("quesNum"));
            testMark.setStart(new Date().getTime());
            testMarkService.insert(testMark);
            session.setAttribute("testMark",testMark);
            modelAndView.setViewName(REDIRCT+TEST_URL);
        }

        return modelAndView;
    }


    /*
    * 考试页面
    *
    * */
    @RequestMapping(value ="/test",method= RequestMethod.GET)
    public  ModelAndView test(HttpSession session) {
        ModelAndView modelAndView = new ModelAndView();
        TestMark testMark = getTestMark(session,"A");

        Examinee examinee = (Examinee) session.getAttribute("user");
        if(isTestEnded(examinee, session)){
            modelAndView.setViewName(REDIRCT+NOREAPEAT_URL);
            return modelAndView;
        }

        long usedSec = getUsedSec(testMark.getStart());
        long restSec = 0;
        if(TOTAL_SEC>usedSec){
            restSec = TOTAL_SEC - usedSec;
        }

        modelAndView.addObject("restSec",restSec);
        modelAndView.addObject("examinee",examinee);
        modelAndView.addObject("answer",testMark.getContent());
        modelAndView.setViewName("/WEB-INF/velocity/test/examination.vm");
        return modelAndView;
    }

    /*
     *考试提交
     */
    @RequestMapping(value ="/submitAnswer",method= RequestMethod.POST)
    public  @ResponseBody DefaultResult submitAnswer(HttpSession session,@RequestParam String answer) {
        ModelAndView mav = new ModelAndView();
        TestMark testMark = getTestMark(session,"A");
        Examinee examinee = (Examinee) session.getAttribute("user");
        if(isTestEnded(examinee, session)){
            return DefaultResult.result(true,NOREAPEAT_URL,null);
        }
        testMark.setContent(answer);
        testMarkService.update(testMark);
        mav.addObject("answer",answer);
        examinee.setEnd((byte) 1);
        examineeService.update(examinee);
        return DefaultResult.result(true,"/test/checkAnswer",null);
    }

    @RequestMapping(value ="/checkAnswer",method = RequestMethod.GET)
    public  ModelAndView checkAns(HttpSession session) {
        ModelAndView modelAndView = new ModelAndView();
        TestMark testMark = getTestMark(session,"A");
        modelAndView.addObject("answer",testMark.getContent());
        modelAndView.setViewName("/WEB-INF/velocity/test/checkAnswer.vm");
        return modelAndView;
    }

    @RequestMapping(value ="/doCheckAnswer",method= RequestMethod.POST)
    public  String confirmAnswer(HttpSession session) {
            TestMark testMark =getTestMark(session,"A");
            testMark.setConfirm(1);
            testMarkService.update(testMark);
            return REDIRCT+FINISH_URL;
    }

    @RequestMapping(value ="/finish",method= RequestMethod.GET)
    public  String finish(HttpSession session) {
        return "/WEB-INF/velocity/test/finish.vm";
    }

    @RequestMapping(value ="/noRepeat",method= RequestMethod.GET)
    public  String noRepeat(HttpSession session) {
        return "/WEB-INF/velocity/test/noRepeat.vm";
    }


    //返回最新的testMark记录，如果不存在返回null
    private TestMark getTestMark(HttpSession session,String type){
        TestMark testMark = (TestMark) session.getAttribute("testMark");
        if(testMark!=null&&testMark.getType().equals(type))
            return testMark;
        else {
            testMark = null;
        }
        List<TestMark> testMarkList =  testMarkService.getByIdAndNameAndType
                ((Examinee) session.getAttribute("user"),type);
        if(!testMarkList.isEmpty()){
            testMark = testMarkList.get(0);
        }
        return testMark;
    }

    private boolean isTestEnded(Examinee examinee, HttpSession session) {
        TestMark testtMark = getTestMark(session, "A");
        if(testtMark!=null && getUsedSec(testtMark.getStart()) > (TOTAL_SEC+10))
            return true;
        return examinee.getEnd() == 1;
    }

    private boolean isTesting(Examinee examinee, HttpSession session){
        TestMark testMark = getTestMark(session,"A");
        return testMark!=null;
    }

    private boolean checkType(String type){
        if(type == null)
            return false;
        return type.equals("A") || type.equals("B");
    }

    private long getUsedSec(long startTime){
        long usedSec =(new Date().getTime() - startTime)/1000;
        return usedSec;
    }





}
