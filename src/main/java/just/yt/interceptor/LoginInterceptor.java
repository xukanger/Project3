package just.yt.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by yt on 2017/3/8.
 */
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {

        //获取请求的URL
        String url = request.getRequestURI();

        if(url.contains("style")||url.contains("login")||url.contains("Login"))
            return true;
        //获取Session
        HttpSession session = request.getSession();

        if(session.getAttribute("user") != null){
            return true;
        }else if(url.contains("bops")||url.contains("Bops")){
            request.getRequestDispatcher("/bops/login/login").forward(request, response);
        }else {
            request.getRequestDispatcher("/test/login").forward(request, response);
        }
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
