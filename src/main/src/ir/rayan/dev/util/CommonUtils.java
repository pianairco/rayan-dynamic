package ir.rayan.dev.util;

import javax.servlet.http.HttpSession;

/**
 * Created by mj.rahmati on 12/31/2019.
 */
public class CommonUtils {
    public static String getCurrentUser(HttpSession session) {
        return "123";
    }

    public static boolean isNull(String str){
        return (str == null || "".equals(str) || "null".equals(str));
    }

    public static boolean isNull(Object obj){
        if (obj == null)
            return true;
        String str = obj.toString();
        return (str == null || "".equals(str) || "null".equals(str));
    }
}
