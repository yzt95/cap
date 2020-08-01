package cool.yzt.cap.service;

import cool.yzt.cap.entity.LoginTicket;

public interface LoginTicketService {
    int save(LoginTicket loginTicket);
    LoginTicket findByTicket(String ticket);
    void logout(String ticket);
}
