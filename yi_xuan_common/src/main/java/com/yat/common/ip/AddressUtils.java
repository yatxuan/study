package com.yat.common.ip;

import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.http.useragent.Engine;
import cn.hutool.http.useragent.OS;
import cn.hutool.http.useragent.Platform;
import cn.hutool.http.useragent.UserAgentUtil;
import com.yat.common.refactoring.toolkit.FileUtil;
import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.UserAgent;
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
 * <p>Description: Ip工具类 </p>
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


    /**
     * 获取ip地址
     *
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
        UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
        Browser browser = userAgent.getBrowser();
        return browser.getName();
    }

    /**
     * 获取浏览器版本
     */
    public static String getBrowserVersion(HttpServletRequest request) {
        cn.hutool.http.useragent.UserAgent userAgent = UserAgentUtil.parse(request.getHeader("User-Agent"));
        return userAgent.getVersion();
    }

    /**
     * 判断终端是否为移动终端
     */
    public static boolean isMobile(HttpServletRequest request) {
        cn.hutool.http.useragent.UserAgent userAgent = UserAgentUtil.parse(request.getHeader("User-Agent"));
        return userAgent.isMobile();
    }

    /**
     * 获取系统类型
     *
     * @return 系统类型
     */
    public static OS getOs(HttpServletRequest request) {
        cn.hutool.http.useragent.UserAgent userAgent = UserAgentUtil.parse(request.getHeader("User-Agent"));
        return userAgent.getOs();
    }

    /**
     * 获取引擎类型
     *
     * @return 引擎类型
     */
    public static Engine getEngine(HttpServletRequest request) {
        cn.hutool.http.useragent.UserAgent userAgent = UserAgentUtil.parse(request.getHeader("User-Agent"));
        return userAgent.getEngine();
    }

    /**
     * 获取引擎版本
     *
     * @return 引擎版本
     */
    public static String getEngineVersion(HttpServletRequest request) {
        cn.hutool.http.useragent.UserAgent userAgent = UserAgentUtil.parse(request.getHeader("User-Agent"));
        return userAgent.getEngineVersion();
    }

    /**
     * 获取平台类型
     *
     * @return 平台类型
     */
    public static Platform getPlatform(HttpServletRequest request) {
        cn.hutool.http.useragent.UserAgent userAgent = UserAgentUtil.parse(request.getHeader("User-Agent"));
        return userAgent.getPlatform();
    }

    /**
     * 判断当前ip是否属于当前网段
     *
     * @param ip   IP地址：192.168.1.12
     * @param cidr 网段：192.168.1.0/26
     * @return true-属于，false-不属于
     */
    public static boolean isInRange(String ip, String cidr) {
        String[] ips = ip.split("\\.");
        int ipAddr = (Integer.parseInt(ips[0]) << 24)
                | (Integer.parseInt(ips[1]) << 16)
                | (Integer.parseInt(ips[2]) << 8) | Integer.parseInt(ips[3]);
        int type = Integer.parseInt(cidr.replaceAll(".*/", ""));
        int mask = 0xFFFFFFFF << (32 - type);
        String cidrIp = cidr.replaceAll("/.*", "");
        String[] cidrIps = cidrIp.split("\\.");
        int cidrIpAddr = (Integer.parseInt(cidrIps[0]) << 24)
                | (Integer.parseInt(cidrIps[1]) << 16)
                | (Integer.parseInt(cidrIps[2]) << 8)
                | Integer.parseInt(cidrIps[3]);

        return (ipAddr & mask) == (cidrIpAddr & mask);
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
