package cool.yzt.cap.controller;

import cool.yzt.cap.dto.PageBean;
import cool.yzt.cap.entity.User;
import cool.yzt.cap.service.DiscussPostService;

import cool.yzt.cap.service.MessageService;
import cool.yzt.cap.service.SystemNoticeService;
import cool.yzt.cap.util.UserHolder;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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


    @GetMapping({"/","/index"})
    public String redirectToIndex() {
        return "forward:/index/newest/1";
    }

    @GetMapping("/index/{mode}/{page}")
    public String getIndexPage(@PathVariable("page") Integer page, @PathVariable("mode") String mode, Integer pageSize, Model model) {
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
        return "index";
    }

    @GetMapping("/error")
    public String getErrorPage() {
        return "/error/500";
    }

}
