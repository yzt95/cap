package cool.yzt.cap.util;

import cool.yzt.cap.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserHolder {
    private final ThreadLocal<User> users = new ThreadLocal<>();

    public void hold(User user) {
        users.set(user);
    }

    public User get() {
        return users.get();
    }

    public void clear() {
        users.remove();
    }
}
