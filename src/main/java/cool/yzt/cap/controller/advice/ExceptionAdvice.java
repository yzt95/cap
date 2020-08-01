package cool.yzt.cap.controller.advice;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@ControllerAdvice(annotations = Controller.class)
public class ExceptionAdvice {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionAdvice.class);

    @ExceptionHandler(Exception.class)
    public void exceptionHandler(Exception e, HttpServletRequest request, HttpServletResponse response) {
        logger.error("服务器发生异常：" + e.getMessage());
        for(StackTraceElement element : e.getStackTrace()) {
            logger.error(element.toString());
        }

        String xRequestedWith = request.getHeader("x-requested-with");
        if("XMLHttpRequest".equals(xRequestedWith)) {
            response.setContentType("application/json;charset=utf-8");
            JSONObject json = JSONUtil.createObj().set("msg","服务器遇到一些问题，请稍后重试，或者联系管理员~");
            try {
                response.getWriter().write(json.toString());
            } catch (IOException ioException) {
                logger.error("服务器发生异常：" + ioException.getMessage());
            }
        } else {
            try {
                response.sendRedirect(request.getContextPath() + "/error");
            } catch (IOException ioException) {
                logger.error("服务器发生异常：" + ioException.getMessage());
            }
        }
    }
}
