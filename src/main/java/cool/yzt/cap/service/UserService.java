package cool.yzt.cap.service;

import cool.yzt.cap.entity.User;

import java.util.Map;

public interface UserService {
    User findById(int id);
    User findByUsername(String username);
    int checkRegisterUser(User user);
    int register(User user);
    int activation(int id,String code);
    int login(String username,String password,int expiredSeconds,String ticket);
    int changeHeader(int id,String headerUrl);
    int changePassword(int id, String oldPassword, String newPassword);
    User findInCacheById(int id);
    void saveInCache(User user);
    void deleteInCache(int id);
}
