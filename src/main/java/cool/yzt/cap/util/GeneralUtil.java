package cool.yzt.cap.util;

import cn.hutool.core.util.IdUtil;
import cn.hutool.crypto.digest.DigestUtil;

public class GeneralUtil {

    public static String getUUId() {
        return IdUtil.simpleUUID();
    }

    public static String getUUId(int charNum) {
        return IdUtil.simpleUUID().substring(0,charNum);
    }

    public static String md5(String str) {
        return DigestUtil.md5Hex(str);
    }

    public static boolean passwordIsLegal(String password) {
        if(password==null) return false;
        /*8-20位，字母、数字组成，区分大小写*/
        String regex = "^(?=.*[a-zA-Z])(?=.*[0-9])[A-Za-z0-9]{8,20}$";
        return password.matches(regex);
    }

    public static boolean usernameIsLegal(String username) {
        if(username==null) return false;
        /*5-16位，字母，数字，下划线组成*/
        String regex = "^[a-zA-Z0-9_]{5,16}$";
        return username.matches(regex);
    }



}
