package cool.yzt.cap.controller;

import cool.yzt.cap.dto.PageBean;
import cool.yzt.cap.entity.User;
import cool.yzt.cap.service.DiscussPostService;

import cool.yzt.cap.service.MessageService;
import cool.yzt.cap.service.SystemNoticeService;
import cool.yzt.cap.service.StatisticService;
import cool.yzt.cap.util.CookieUtil;
import cool.yzt.cap.util.GeneralUtil;
import cool.yzt.cap.util.UserHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class HomeController {

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private SystemNoticeService systemNoticeService;

    @Autowired
    private UserHolder userHolder;

    @Autowired
    private StatisticService statisticService;


    @GetMapping({"/","/index"})
    public String redirectToIndex() {
        return "forward:/index/newest/1";
    }

    @GetMapping("/index/{mode}/{page}")
    public String getIndexPage(@PathVariable("page") Integer page, @PathVariable("mode") String mode,
                               HttpServletRequest request, HttpServletResponse response,
                               Integer pageSize, Model model) {
        page = page==null ? 1 : page;
        pageSize = pageSize==null ? 15 : pageSize;
        mode = mode==null ? "newest" : mode;
        PageBean pageBean;
        if(mode.equals("newest")) {
            pageBean = discussPostService.getPageBean(page,pageSize,0);
        }else if(mode.equals("hottest")) {
            pageBean = discussPostService.getPageBean(page,pageSize,1);
        }else {
            return "error/404";
        }
        model.addAttribute("pageBean",pageBean);
        model.addAttribute("mode",mode);

        String statisticToken = CookieUtil.getValue(request,"statistic");
        if(statisticToken==null) {
            statisticToken = GeneralUtil.getUUId();
            Cookie cookie = new Cookie("statistic", statisticToken);
            cookie.setPath(request.getContextPath());
            cookie.setMaxAge(3600*24);
            response.addCookie(cookie);
        }
        statisticService.recordUV(statisticToken);
        User user = userHolder.get();
        if(user!=null) statisticService.recordDAU(user.getId());
        return "index";
    }

    @GetMapping("/error")
    public String getErrorPage() {
        return "error/500";
    }

}
