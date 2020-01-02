package ir.rayan.data.sql;

import ir.rayan.dev.util.CommonUtils;
import ir.rayan.dev.util.DateUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.function.Function;

/**
 * Created by mj.rahmati on 12/29/2019.
 */
public enum SQLUtilityType {
    TODAY_JALALI("date.today-jalali", SQLUtilityType::getTodayJalali),
    CUSTOMER_ID("app.customer-id", SQLUtilityType::getCustomerId);

    private String name;
    private Function<HttpServletRequest, String> funtion;

    SQLUtilityType(String name, Function<HttpServletRequest, String> funtion) {
        this.name = name;
        this.funtion = funtion;
    }

    public static String execute(String sqlUtilityTypeName, HttpServletRequest request) {
        String name = sqlUtilityTypeName;
        String type = "s";
        String strings[];
        if((strings = sqlUtilityTypeName.split(":")).length > 1) {
            name = strings[0];
            type = strings[1];
        }
        for (SQLUtilityType sqlUtilityType : SQLUtilityType.values()) {
            if (sqlUtilityType.name.equals(name)) {
                return type.equals("s") ? "'" + sqlUtilityType.funtion.apply(request) + "'" : sqlUtilityType.funtion.apply(request);
            }
        }
        return "null";
    }

    private static String getTodayJalali(HttpServletRequest request) {
        return DateUtils.getTodayJalali();
    }

    private static String getCustomerId(HttpServletRequest request) {
        return CommonUtils.getCurrentUser(request.getSession());//.getAppuserId().toString();
    }
}
