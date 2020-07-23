package com.yat.reptile.interceptor;

import com.google.gson.Gson;
import com.yat.common.constant.HttpStatus;
import com.yat.common.util.ResultResponse;
import com.yat.common.util.spring.SpringUtils;
import com.yat.reptile.annotation.AntiReptile;
import com.yat.reptile.config.AntiReptileProperties;
import com.yat.reptile.module.VerifyImageDTO;
import com.yat.reptile.rule.RuleActuator;
import com.yat.reptile.util.CrossUtil;
import com.yat.reptile.util.VerifyImageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;

/**
 * @author Yat-Xuan
 * @since 2020/2/4 17:45
 */
@Slf4j
@Component
public class AntiReptileInterceptor extends HandlerInterceptorAdapter {

    private RuleActuator actuator;

    private List<String> includeUrls;

    private boolean globalFilterMode;

    private VerifyImageUtil verifyImageUtil;

    private AtomicBoolean initialized = new AtomicBoolean(false);

    private void init() {
        this.actuator = SpringUtils.getBean(RuleActuator.class);
        this.verifyImageUtil = SpringUtils.getBean(VerifyImageUtil.class);
        this.includeUrls = SpringUtils.getBean(AntiReptileProperties.class).getIncludeUrls();
        this.globalFilterMode = SpringUtils.getBean(AntiReptileProperties.class).isGlobalFilterMode();
        if (this.includeUrls == null) {
            this.includeUrls = new ArrayList<>();
        }
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!initialized.get()) {
            init();
            initialized.set(true);
        }
        HandlerMethod handlerMethod;
        try {
            handlerMethod = (HandlerMethod) handler;
        } catch (ClassCastException e) {
            return true;
        }
        Method method = handlerMethod.getMethod();
        AntiReptile antiReptile = AnnotationUtils.findAnnotation(method, AntiReptile.class);
        boolean isAntiReptileAnnotation = antiReptile != null;
        String requestUrl = request.getRequestURI();
        if (isIntercept(requestUrl, isAntiReptileAnnotation) && !actuator.isAllowed(request, response)) {
            CrossUtil.setCrossHeader(response);
            response.setStatus(HttpStatus.BANDWIDTH_LIMIT_EXCEEDED);
            VerifyImageDTO verifyImage = verifyImageUtil.generateVerifyImg(null);
            verifyImageUtil.saveVerifyCodeToRedis(verifyImage);
            Map<String, String> map = new HashMap<>(2);
            map.put("verifyId", verifyImage.getVerifyId());
            map.put("verifyImgStr", verifyImage.getVerifyImgStr());
            String json = new Gson().toJson(ResultResponse.error(HttpStatus.BANDWIDTH_LIMIT_EXCEEDED, "访问频率过快", map));
            response.getWriter().print(json);
            response.getWriter().close();
            return false;
        }
        return true;
    }

    /**
     * 是否拦截
     *
     * @param requestUrl              请求uri
     * @param isAntiReptileAnnotation 是否有AntiReptile注解
     * @return 是否拦截
     */
    private boolean isIntercept(String requestUrl, Boolean isAntiReptileAnnotation) {
        if (this.globalFilterMode || isAntiReptileAnnotation || this.includeUrls.contains(requestUrl)) {
            return true;
        } else {
            for (String includeUrl : includeUrls) {
                if (Pattern.matches(includeUrl, requestUrl)) {
                    return true;
                }
            }
            return false;
        }
    }
}
