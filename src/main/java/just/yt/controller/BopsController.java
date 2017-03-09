package just.yt.controller;

import just.yt.model.BopsUser;
import just.yt.service.BopsService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import tool.DefaultResult;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Date;
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
    public ModelAndView saveUser(@RequestParam  String account,@RequestParam String name, @RequestParam String password,HttpSession session) {
        ModelAndView mav = new ModelAndView();
        if(Objects.isNull(session.getAttribute("user"))) {
            mav.setViewName("redirect:/bops/");
        }
        BopsUser bopsUser =new BopsUser();
        bopsUser.setAccount(account);
        bopsUser.setName(name);
        bopsUser.setPassword(password);
        bopsUser.setStatus(0);
        bopsUser.setGmtCreate(new Date());
        bopsUser.setGmtModified(new Date());

        if (bopsUser == null){
            mav.setViewName("addbopsuser");
            return mav;
        }
        bopsService.save(bopsUser);
        mav.setViewName("redirect:/bops/index");
        return  mav;
    }

    @RequestMapping(value ="/bopsUser/importoutput",method= RequestMethod.GET)
    public ModelAndView importExaminee() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("IO");
        return  mav;
    }

    @RequestMapping(value ="/bopsUser/excelUpload",method= RequestMethod.POST)
    public @ResponseBody  DefaultResult excelUpload(Map<String, Object> model, @RequestParam  MultipartFile file, HttpSession session) {
        if(Objects.isNull(session.getAttribute("user"))) {
            return DefaultResult.failResult("用户未登录");
        }
        System.out.println(file.getOriginalFilename());
        System.out.println(file.getName());
        if (!(file.getOriginalFilename().endsWith(".xls")||file.getOriginalFilename().endsWith(".xlsx"))){
            return DefaultResult.failResult("文件格式不正确");
        }
        DefaultResult res = bopsService.importExaminee(file);
        return  res;
    }

    @RequestMapping(value ="/bopsUser/outputExamineeA",method= RequestMethod.POST)
    public byte[] outputExamineeA(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        if(Objects.isNull(session.getAttribute("user"))) {
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

        //for zip file
        response.setContentType("application/zip");
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



}
