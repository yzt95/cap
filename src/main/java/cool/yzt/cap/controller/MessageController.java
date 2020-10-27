package cool.yzt.cap.controller;

import cn.hutool.http.HtmlUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cool.yzt.cap.annotation.LoginRequired;
import cool.yzt.cap.dto.PageBean;
import cool.yzt.cap.entity.Message;
import cool.yzt.cap.entity.User;
import cool.yzt.cap.service.MessageService;
import cool.yzt.cap.service.SystemNoticeService;
import cool.yzt.cap.service.UserService;
import cool.yzt.cap.util.GeneralUtil;
import cool.yzt.cap.util.UserHolder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

@Controller
public class MessageController {
    @Autowired
    private UserHolder userHolder;

    @Autowired
    private MessageService messageService;

    @Autowired
    private SystemNoticeService systemNoticeService;

    @Autowired
    private UserService userService;


    @LoginRequired
    @GetMapping("/letter/list")
    public String redirectToMessageList() {
        return "redirect:/letter/list/1";
    }

    @LoginRequired
    @GetMapping("/letter/list/{page}")
    public String getMessageListPage(@PathVariable("page") Integer start, Integer limit,Model model) {
        int userId = userHolder.get().getId();

        start = start==null ? 1 : start;
        limit = limit==null ? 10 : limit;

        PageBean pageBean = messageService.getMessageList(userId,start,limit);
        int messageUnreadCount = messageService.findAllUnreadCount(userId);
        int noticeUnreadCount = systemNoticeService.findAllUnreadCount(userId);
        model.addAttribute("pageBean",pageBean);
        model.addAttribute("messageUnreadCount",messageUnreadCount);
        model.addAttribute("noticeUnreadCount",noticeUnreadCount);
        return "site/letter";
    }

    @LoginRequired
    @GetMapping("/letter/detail/{conversationId}")
    public String redirectToMessageDetail(@PathVariable("conversationId") String conversationId) {
        return ("redirect:/letter/detail/"+conversationId+"/1");
    }

    @LoginRequired
    @GetMapping("/letter/detail/{conversationId}/{page}")
    public String getMessageDetailPage(@PathVariable("conversationId") String conversationId,Model model,
                                       @PathVariable("page") Integer start, Integer limit) {
        User self  = userHolder.get();
        if(!messageService.userHaveConversation(self.getId(),conversationId)) {
            return "redirect:/letter/list";
        }

        start = start==null ? 1 : start;
        limit = limit==null ? 15 : limit;
        PageBean pageBean = messageService.getMessageDetail(conversationId,start,limit);
        if (pageBean==null) return "redirect:/letter/list";


        int id1 = ((Message)pageBean.getContents().get(0).get("message")).getFromId();
        int id2 = ((Message)pageBean.getContents().get(0).get("message")).getToId();

        int friendId = self.getId()==id1 ? id2 : id1;
        User friend = userService.findById(friendId);

        model.addAttribute("pageBean",pageBean);
        model.addAttribute("friend",friend);
        model.addAttribute("self",self);
        //model.addAttribute("conversationId",conversationId);

        messageService.updateStatus(self.getId(),conversationId,1);

        return "site/letter-detail";
    }

    @ResponseBody
    @PostMapping("/letter/send")
    public String send(String toName,String content){
        User toUser = userService.findByUsername(toName);
        User fromUser = userHolder.get();
        if(fromUser==null) {
            JSONObject json = JSONUtil.createObj().set("code",1).set("msg","发送失败，请先登录！");
            return json.toString();
        }
        if(toUser==null) {
            JSONObject json = JSONUtil.createObj().set("code",1).set("msg","发送失败，目标用户不存在！");
            return json.toString();
        }
        if(StringUtils.isBlank(content)) {
            JSONObject json = JSONUtil.createObj().set("code",1).set("msg","发送失败，不可以发送空白消息！");
            return json.toString();
        }
        if(toUser.getId()==fromUser.getId()) {
            JSONObject json = JSONUtil.createObj().set("code",1).set("msg","发送失败，不可以给自己发送私信！");
            return json.toString();
        }
        Message message = new Message();
        message.setFromId(fromUser.getId());
        message.setToId(toUser.getId());
        message.setContent(HtmlUtil.escape(content));

        StringBuilder sb = new StringBuilder();
        if (fromUser.getId() > toUser.getId()){
            sb.append(toUser.getId()).append("_").append(fromUser.getId());
        }else {
            sb.append(fromUser.getId()).append("_").append(toUser.getId());
        }

        String conversationId = GeneralUtil.md5(sb.toString());
        message.setConversationId(conversationId);
        message.setStatus(0);
        message.setCreateTime(new Date());
        messageService.add(message);

        JSONObject json = JSONUtil.createObj().set("code",0).set("msg","发送成功！");
        System.out.println(json.toString());
        return json.toString();

    }
}
