package cool.yzt.cap.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author : yzt
 */
public class DateUtil {
    public static Date getYMD(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date ret = null;
        try {
            ret = sdf.parse(sdf.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return  ret;
    }
    public static Date parse(String str) {
        return parse(str,"yyyy-MM-dd");
    }

    public static Date parse(String str,String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        Date ret = null;
        try {
            ret = sdf.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return  ret;
    }
}
