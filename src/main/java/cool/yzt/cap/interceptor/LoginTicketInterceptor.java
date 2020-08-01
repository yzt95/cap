package cool.yzt.cap.interceptor;

import cool.yzt.cap.entity.LoginTicket;
import cool.yzt.cap.entity.User;
import cool.yzt.cap.service.LoginTicketService;
import cool.yzt.cap.service.UserService;
import cool.yzt.cap.util.CookieUtil;
import cool.yzt.cap.util.UserHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Component
public class LoginTicketInterceptor implements HandlerInterceptor {

    @Autowired
    private LoginTicketService loginTicketService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserHolder userHolder;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String ticket = CookieUtil.getValue(request,"loginCode");
        if(ticket!=null) {
            LoginTicket loginTicket = loginTicketService.findByTicket(ticket);
            if(loginTicket!=null && loginTicket.getStatus()==1 && loginTicket.getExpired().after(new Date())) {
                User user = userService.findInCacheById(loginTicket.getUserId());
                if(user==null) {
                    user = userService.findById(loginTicket.getUserId());
                    if(user!=null) {
                        userService.saveInCache(user);
                    }
                }
                if(user!=null) {
                    userHolder.hold(user);
                }
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        User user = userHolder.get();
        if(modelAndView!=null && user!=null) {
            modelAndView.addObject("loggedInUser",user);
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        userHolder.clear();
    }
}