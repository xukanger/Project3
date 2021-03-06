package just.yt.controller;

import just.yt.model.BopsUser;
import just.yt.model.Examinee;
import just.yt.service.BopsService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import tool.DefaultResult;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;
import java.util.Map;
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
        return "/WEB-INF/velocity/bops/bopslogin.vm";
    }

     @RequestMapping(value ="/login/login",method= RequestMethod.GET)
     public ModelAndView login() {
            ModelAndView mav = new ModelAndView();
            mav.setViewName("bops/bopslogin");
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
        mav.setViewName("bops/bopsindex");
        return mav;
    }

    @RequestMapping(value ="/bopsUser/add",method= RequestMethod.GET)
    public ModelAndView addUser(BopsUser user,HttpSession session) {
        ModelAndView mav = new ModelAndView();
        if(Objects.isNull(session.getAttribute("user"))) {
            mav.setViewName("redirect:/bops/");
        }
        mav.setViewName("bops/addbopsuser");
        return  mav;
    }



    @RequestMapping(value ="/bopsUser/save",method= RequestMethod.POST)
    public @ResponseBody DefaultResult saveUser(@RequestParam  String account,@RequestParam String name, @RequestParam String password,HttpSession session) {
        if(Objects.isNull(session.getAttribute("user"))) {
            return  DefaultResult.failResult("请先登录！！");
        }
        BopsUser bopsUser =new BopsUser();
        bopsUser.setAccount(account);
        bopsUser.setName(name);
        bopsUser.setPassword(password);
        bopsUser.setStatus(0);
        bopsUser.setGmtCreate(new Date());
        bopsUser.setGmtModified(new Date());
        bopsService.save(bopsUser);
        return  DefaultResult.successResult();
    }

    @RequestMapping(value ="/bopsUser/importoutput",method= RequestMethod.GET)
    public ModelAndView importExaminee() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("bops/IO");
        return  mav;
    }

    @RequestMapping(value ="/bopsUser/excelUpload",method= RequestMethod.POST)
    public  ModelAndView excelUpload(Map<String, Object> model, @RequestParam  MultipartFile file, HttpSession session) {
        BopsUser user =(BopsUser) session.getAttribute("user");
        DefaultResult res = null;
        ModelAndView mav = new ModelAndView();
        mav.setViewName("bops/IO");
        if(Objects.isNull(user)) {
            res = DefaultResult.failResult("用户未登录");
            mav.addObject("res",res);
            return  mav;
        }
        System.out.println(file.getOriginalFilename());
        System.out.println(file.getName());
        if (!(file.getOriginalFilename().endsWith(".xls")||file.getOriginalFilename().endsWith(".xlsx"))){
            res = DefaultResult.failResult("文件格式不正确");
            mav.addObject("res",res);
            return  mav;
        }
        res = bopsService.importExaminee(file);
        mav.addObject("res",res);
        return  mav;
    }

    @RequestMapping(value ="/bopsUser/outputExamineeA",method= RequestMethod.POST)
    public byte[] outputExamineeA(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        BopsUser user =(BopsUser) session.getAttribute("user");
        if(Objects.isNull(user)) {
            return null;
        }
        response.setContentType("application/vnd.ms-excel");
        String fileName = "default.xls";
        try {
             fileName = URLEncoder.encode("初试考生信息.xls", "UTF-8");
        }catch (Exception e){
            e.printStackTrace();
        }
        response.setHeader( "Content-Disposition", "attachment;filename=" + fileName);
        OutputStream os = null;

            //for zip file
        response.setContentType("application/zip");
        try {
            os = response.getOutputStream();
            bopsService.outputTestMarks("A",os);
            os.flush();
            os.close();
        }catch (Exception e){
            e.printStackTrace();
           return  null;
        }
        return null;
    }

    @RequestMapping(value ="/bopsUser/outputExamineeB",method= RequestMethod.POST)
    public ModelAndView outputExamineeB(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        if(Objects.isNull(session.getAttribute("user"))) {
            return null;
        }
        response.setContentType("application/vnd.ms-excel");
        String fileName = "default.xls";
        try {
            fileName = URLEncoder.encode("复试考生信息.xls", "UTF-8");
        }catch (Exception e){
            e.printStackTrace();
        }
        response.setHeader( "Content-Disposition", "attachment;filename=" +fileName );
        OutputStream os = null;

        try {
            os = response.getOutputStream();
            bopsService.outputTestMarks("B",os);
            os.flush();
            os.close();
        }catch (Exception e){
            e.printStackTrace();
            return  null;
        }
        return null;
    }

    @RequestMapping(value ="/bopsUser/downloadcontent",method= RequestMethod.POST)
    public ModelAndView downloadContent(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        if(Objects.isNull(session.getAttribute("user"))) {
            return null;
        }
        response.setHeader( "Content-Disposition", "attachment;filename=" +"score.zip" );
        OutputStream os = null;
        response.setContentType("application/zip");
        try {
            os = response.getOutputStream();
            bopsService.outputContent(os);
            os.flush();
            os.close();
        }catch (Exception e){
            e.printStackTrace();
            return  null;
        }
        return null;
    }

    @RequestMapping(value ="/bopsUser/delete",method= RequestMethod.POST)
    public @ResponseBody DefaultResult downloadContent(HttpSession session) {
        if(Objects.isNull(session.getAttribute("user"))) {
            return DefaultResult.failResult("用户未登录！");
        }
        DefaultResult rs = bopsService.deleteAll();
        return  rs;
    }

    @RequestMapping(value ="/bopsUser/deleteAll",method= RequestMethod.GET)
    public ModelAndView deleteAll(HttpSession session) {
        if (Objects.isNull(session.getAttribute("user"))) {
            return null;
        }
        ModelAndView mav = new ModelAndView();
        mav.setViewName("bops/del");
        return  mav;
    }

    @RequestMapping(value ="/bopsUser/listAll",method= RequestMethod.GET)
    public ModelAndView listAll(HttpSession session,String identity) {
        if (Objects.isNull(session.getAttribute("user"))) {
            return null;
        }
        ModelAndView mav = new ModelAndView();
        List<Examinee> examinees = bopsService.listExaminee(identity);
        mav.setViewName("bops/listAll");
        mav.addObject("list",examinees);
        mav.addObject("identity",identity);
        return  mav;
    }

    @RequestMapping(value ="/bopsUser/retest",method= RequestMethod.GET)
    public ModelAndView retest(@RequestParam Long id, HttpSession session) {
        if (Objects.isNull(session.getAttribute("user"))) {
            return null;
        }
        if(id != null){
            bopsService.resetEnd(id);
        }
        ModelAndView mav = new ModelAndView();
        List<Examinee> examinees = bopsService.listExaminee();
        mav.setViewName("bops/listAll");
        mav.addObject("list",examinees);
        return  mav;
    }

    @RequestMapping(value ="/bopsUser/delMark",method= RequestMethod.GET)
    public ModelAndView delMark(@RequestParam String identity, HttpSession session) {
        if (Objects.isNull(session.getAttribute("user"))) {
            return null;
        }
        if (StringUtils.isNotEmpty(identity)){
            bopsService.delMark(identity);
        }
        ModelAndView mav = new ModelAndView();
        List<Examinee> examinees = bopsService.listExaminee();
        mav.setViewName("bops/listAll");
        mav.addObject("list",examinees);
        return  mav;
    }
}
