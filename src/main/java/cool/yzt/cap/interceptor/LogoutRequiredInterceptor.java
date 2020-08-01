package cool.yzt.cap.interceptor;

import cool.yzt.cap.annotation.LoginRequired;
import cool.yzt.cap.annotation.LogoutRequired;
import cool.yzt.cap.util.UserHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

@Component
public class LogoutRequiredInterceptor implements HandlerInterceptor {
    @Autowired
    private UserHolder userHolder;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(handler instanceof HandlerMethod) {
            Method method = ((HandlerMethod)handler).getMethod();
            LogoutRequired logoutRequired = method.getAnnotation(LogoutRequired.class);
            if(logoutRequired!=null && userHolder.get()!=null) {
                response.sendRedirect(request.getContextPath()+"/index");
                return false;
            }
        }
        return true;
    }
}
