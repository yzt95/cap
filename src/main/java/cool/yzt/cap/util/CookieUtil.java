package cool.yzt.cap.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class CookieUtil {
    public static String getValue(HttpServletRequest request,String key) throws RuntimeException {
        if(request==null || key == null) {
            throw new IllegalArgumentException("获取cookie参数不合法");
        }
        Cookie[] cookies = request.getCookies();
        if(cookies!=null) {
            for(Cookie cookie : cookies) {
                if(cookie.getName().equals(key)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
