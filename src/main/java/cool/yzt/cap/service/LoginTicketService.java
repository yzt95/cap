package cool.yzt.cap.service;

import cool.yzt.cap.entity.LoginTicket;

public interface LoginTicketService {
    int save(LoginTicket loginTicket, int expiredSeconds);
    LoginTicket findByTicket(String ticket);
    void logout(String ticket);
}
