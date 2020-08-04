package cool.yzt.cap.controller;

import cool.yzt.cap.annotation.LoginRequired;
import cool.yzt.cap.dto.PageBean;
import cool.yzt.cap.entity.Message;
import cool.yzt.cap.entity.User;
import cool.yzt.cap.service.MessageService;
import cool.yzt.cap.service.SystemNoticeService;
import cool.yzt.cap.service.UserService;
import cool.yzt.cap.util.UserHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

/** 系统通知
 * @author : yzt
 */
@Controller
public class NoticeController {
    @Autowired
    private SystemNoticeService systemNoticeService;

    @Autowired
    private UserHolder userHolder;

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;


    @LoginRequired
    @GetMapping("/notice/list")
    public String getNoticeListPage(Model model) {
        int userId = userHolder.get().getId();
        Map<String, Map<String, Object>> noticeList = systemNoticeService.getNoticeList(userId);
        int noticeUnreadCount = systemNoticeService.findAllUnreadCount(userId);
        int messageUnreadCount = messageService.findAllUnreadCount(userId);
        model.addAttribute("noticeList",noticeList);
        model.addAttribute("noticeUnreadCount",noticeUnreadCount);
        model.addAttribute("messageUnreadCount",messageUnreadCount);
        return "site/notice";
    }


    @LoginRequired
    @GetMapping("/notice/{type}/{page}")
    public String getMessageDetailPage(@PathVariable("type") int type,Model model,
                                       @PathVariable("page") Integer start, Integer limit) {
        User user  = userHolder.get();

        if(systemNoticeService.findAllCountByType(user.getId(),type)==0) {
            return "redirect:/notice/list";
        }

        start = start==null ? 1 : start;
        limit = limit==null ? 15 : limit;
        PageBean pageBean = systemNoticeService.getNoticeDetailPage(user.getId(),type,start,limit);
        if (pageBean==null) return "redirect:/notice/list";
        model.addAttribute("pageBean",pageBean);
        model.addAttribute("type",type);
        System.out.println(pageBean.getContents());
        System.out.println(pageBean.getTotalContent());

        systemNoticeService.updateStatus(user.getId(),type);
        return "site/notice-detail";
    }


}
