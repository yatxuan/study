package com.yat.reptile.rule;

import com.yat.reptile.config.AntiReptileProperties;
import eu.bitwalker.useragentutils.DeviceType;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Yat-Xuan
 * @since 2019/7/17 10:13
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "anti.reptile.manager.ua-rule", value = "enabled", havingValue = "true", matchIfMissing = true)
public class UaRule extends AbstractRule {

    private final AntiReptileProperties properties;

    @Override
    protected boolean doExecute(HttpServletRequest request, HttpServletResponse response) {
        AntiReptileProperties.UaRule uaRule = properties.getUaRule();
        UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
        OperatingSystem os = userAgent.getOperatingSystem();
        OperatingSystem osGroup = userAgent.getOperatingSystem().getGroup();
        DeviceType deviceType = userAgent.getOperatingSystem().getDeviceType();
        if (DeviceType.UNKNOWN.equals(deviceType)) {
            log.info("Intercepted request, uri: " + request.getRequestURI() + " Unknown device, User-Agent: " + userAgent.toString());
            return true;
        } else if (OperatingSystem.UNKNOWN.equals(os)
                || OperatingSystem.UNKNOWN_MOBILE.equals(os)
                || OperatingSystem.UNKNOWN_TABLET.equals(os)) {
            log.info("Intercepted request, uri: " + request.getRequestURI() + " Unknown OperatingSystem, User-Agent: " + userAgent.toString());
            return true;
        }
        if (!uaRule.isAllowedLinux()) {
            if (OperatingSystem.LINUX.equals(osGroup) || OperatingSystem.LINUX.equals(os)) {
                log.info("Intercepted request, uri: " + request.getRequestURI() + " Not Allowed Linux request, User-Agent: " + userAgent.toString());
                return true;
            }
        }
        if (!uaRule.isAllowedMobile()) {
            if (DeviceType.MOBILE.equals(deviceType) || DeviceType.TABLET.equals(deviceType)) {
                log.info("Intercepted request, uri: " + request.getRequestURI() + " Not Allowed Mobile Device request, User-Agent: " + userAgent.toString());
                return true;
            }
        }
        if (!uaRule.isAllowedPc() && DeviceType.COMPUTER.equals(deviceType)) {
            log.info("Intercepted request, uri: " + request.getRequestURI() + " Not Allowed PC request, User-Agent: " + userAgent.toString());
            return true;
        }
        if (!uaRule.isAllowedIot()) {
            if (DeviceType.DMR.equals(deviceType) || DeviceType.GAME_CONSOLE.equals(deviceType) || DeviceType.WEARABLE.equals(deviceType)) {
                log.info("Intercepted request, uri: " + request.getRequestURI() + " Not Allowed Iot Device request, User-Agent: " + userAgent.toString());
                return true;
            }
        }
        if (!uaRule.isAllowedProxy() && OperatingSystem.PROXY.equals(os)) {
            log.info("Intercepted request, uri: " + request.getRequestURI() + " Not Allowed Proxy request, User-Agent: " + userAgent.toString());
            return true;
        }
        return false;
    }

    @Override
    public void reset(HttpServletRequest request, String realRequestUri) {
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
