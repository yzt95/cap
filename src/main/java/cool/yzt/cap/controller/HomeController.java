package cool.yzt.cap.controller;

import cool.yzt.cap.dto.PageBean;
import cool.yzt.cap.service.DiscussPostService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class HomeController {

    @Autowired
    private DiscussPostService discussPostService;


    @GetMapping({"/","/index"})
    public String redirectToIndex() {
        return "redirect:/index/page/1";
    }

    @GetMapping("/index/page/{page}")
    public String getIndexPage(@PathVariable("page") Integer page,Integer pageSize,Model model) {
        page = page==null ? 1 : page;
        pageSize = pageSize==null ? 15 : pageSize;

        PageBean pageBean = discussPostService.getPageBean(page,pageSize);
        model.addAttribute("pageBean",pageBean);
        return "index";
    }

    @GetMapping("/error")
    public String getErrorPage() {
        return "/error/500";
    }

}
