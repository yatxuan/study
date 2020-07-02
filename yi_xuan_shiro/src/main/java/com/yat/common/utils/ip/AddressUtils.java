package com.yat.common.utils.ip;

import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.http.useragent.Browser;
import cn.hutool.http.useragent.OS;
import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import com.yat.common.utils.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.lionsoul.ip2region.DataBlock;
import org.lionsoul.ip2region.DbConfig;
import org.lionsoul.ip2region.DbSearcher;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.Date;

/**
 * <p>Description: 获取地址类 </p>
 *
 * @author Yat-Xuan
 * @date 2020/3/27 11:24
 */
@Slf4j
public class AddressUtils {

    /**
     * 用于IP定位转换
     */
    private final static String REGION = "内网IP|内网IP";

    private static final String UNKNOWN = "unknown";


    public static void main(String[] args) {
        String cityInfo = getCityInfo("117.132.192.68");
        System.out.println(cityInfo);
    }

    /**
     * 获取ip地址
     * @param request /
     * @return /
     */
    public static String getIpAddr(HttpServletRequest request) {
        if (request == null) {
            return UNKNOWN;
        }
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Forwarded-For");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }

        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        String comma = ",";
        String localhost = "127.0.0.1";
        if (ip.contains(comma)) {
            ip = ip.split(",")[0];
        }
        if (localhost.equals(ip)) {
            // 获取本机真正的ip地址
            try {
                ip = InetAddress.getLocalHost().getHostAddress();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }
        return ip;
    }


    /**
     * 根据ip获取详细地址
     */
    public static String getCityInfo(String ip) {
        DbSearcher searcher = null;
        try {
            String path = "ip2region/ip2region.db";
            String name = "ip2region.db";
            DbConfig config = new DbConfig();
            File file = FileUtil.inputStreamToFile(new ClassPathResource(path).getStream(), name);
            searcher = new DbSearcher(config, file.getPath());
            Method method;
            method = searcher.getClass().getMethod("btreeSearch", String.class);
            DataBlock dataBlock;
            dataBlock = (DataBlock) method.invoke(searcher, ip);
            String address = dataBlock.getRegion().replace("0|", "");
            char symbol = '|';
            if (address.charAt(address.length() - 1) == symbol) {
                address = address.substring(0, address.length() - 1);
            }
            return address.equals(REGION) ? "内网IP" : address;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (searcher != null) {
                try {
                    searcher.close();
                } catch (IOException ignored) {
                }
            }

        }
        return "";
    }

    /**
     * 获取浏览器信息
     */
    public static String getBrowser(HttpServletRequest request) {
        UserAgent userAgent = UserAgentUtil.parse(request.getHeader("User-Agent"));
        Browser browser = userAgent.getBrowser();
        return browser.getName();
    }

    /**
     * 判断终端是否为移动终端
     */
    public static boolean isMobile(HttpServletRequest request) {
        UserAgent userAgent = UserAgentUtil.parse(request.getHeader("User-Agent"));
        return userAgent.isMobile();
    }

    /**
     * 获取系统类型
     *
     * @return 系统类型
     */
    public OS getOs(HttpServletRequest request) {
        UserAgent userAgent = UserAgentUtil.parse(request.getHeader("User-Agent"));
        return userAgent.getOs();
    }

    /**
     * 获得当天是周几
     */
    public static String getWeekDay() {
        String[] weekDays = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());

        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0) {
            w = 0;
        }
        return weekDays[w];
    }
}
