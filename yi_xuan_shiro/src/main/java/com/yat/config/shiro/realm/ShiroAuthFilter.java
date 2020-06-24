package com.yat.config.shiro.realm;

import com.google.gson.Gson;
import com.yat.common.constant.HttpStatus;
import com.yat.common.utils.ResultResponse;
import com.yat.common.utils.StringUtils;
import com.yat.config.shiro.jwt.JwtToken;
import com.yat.config.shiro.jwt.JwtUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <p>Description: 自定义shiro权限过滤器：oauth2过滤器  </p>
 *
 * @author Yat-Xuan
 * @date 2020/6/20 18:05
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ShiroAuthFilter extends AuthenticatingFilter {

    private String token;
    private final JwtUtil jwtUtil;


    /**
     * 这里我们详细说明下为什么重写
     * 可以对比父类方法：父类是直接从shiro的缓存里取出subject（存储用户信息数据的字段），来进行接口权限的验证
     * 因为我们这里用来无状态令牌验证，在这里我们只需要验证令牌是否正确
     * 对于接口权限的验证，我们在后面会进行验证，所以这里不做处理
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {

        // 获取请求token，如果token不存在，直接返回401
        HttpServletRequest httpRequest = WebUtils.toHttp(request);
        this.token = jwtUtil.getToken(httpRequest);
        if (StringUtils.isBlank(token)) {
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            httpResponse.setContentType("application/json;charset=utf-8");
            httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
            httpResponse.setHeader("Access-Control-Allow-Origin", "*");
            httpResponse.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
            String json = new Gson().toJson(ResultResponse.error(HttpStatus.FORBIDDEN, "请先登录"));
            httpResponse.getWriter().print(json);
            return false;
        }

        return executeLogin(request, response);
    }

    /**
     * 进行AccessToken登录认证授权
     * 这里重新父类的executeLogin方法，目的是减少不必要的判断流程操作，
     * 因为方法每次进来都是在onLoginFailure（登陆认证）里进行令牌认证，
     * 所以，后面没有必要进行token验证，直接取用即可
     */
    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
        JwtToken token = new JwtToken(this.token);
        try {
            // 提交给UserRealm进行认证，如果错误他会抛出异常并被捕获
            Subject subject = getSubject(request, response);
            // 调用login方法 会进入到自定义的登陆验证方法（ShiroRealm-doGetAuthenticationInfo）中去，
            // 在doGetAuthenticationInfo中获取的数据，就是这里传递的参数值
            subject.login(token);
            // 如果没有抛出异常则代表登入成功，返回true
            return onLoginSuccess(token, subject, request, response);
        } catch (AuthenticationException e) {
            return onLoginFailure(token, e, request, response);
        }
    }

    /**
     * <p>重写createToken方法，只是防止发生不必要的因素，</p>
     * <p>因为前面已经进行了token的处理，基本上不会进来这个方法内部</p>
     * <p>所以createToken方法可以不用关心内部的处理方式</p>
     */
    @Override
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {
        HttpServletRequest httpRequest = WebUtils.toHttp(request);
        this.token = jwtUtil.getToken(httpRequest);
        return StringUtils.isBlank(token) ? null : new JwtToken(token);
    }

    /**
     * <p>登陆失败的处理</p>
     */
    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setContentType("application/json;charset=utf-8");
        httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
        httpResponse.setHeader("Access-Control-Allow-Origin", "*");
        httpResponse.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        try {
            //处理登录失败的异常
            Throwable throwable = e.getCause() == null ? e : e.getCause();
            String json = new Gson().toJson(ResultResponse.error(HttpStatus.FORBIDDEN, throwable.getMessage()));
            httpResponse.getWriter().print(json);
        } catch (IOException e1) {
            e1.getMessage();
        }
        return false;
    }

    /**
     * 这里我们详细说明下为什么最终返回的都是true，即允许访问
     * 例如我们提供一个地址 GET /article
     * 登入用户和游客看到的内容是不同的
     * 如果在这里返回了false，请求会被直接拦截，用户看不到任何东西
     * 所以我们在这里返回true，Controller中可以通过 subject.isAuthenticated() 来判断用户是否登入
     * 如果有些资源只有登入用户才能访问，我们只需要在方法上面加上 @RequiresAuthentication 注解即可
     * 但是这样做有一个缺点，就是不能够对GET,POST等请求进行分别过滤鉴权(因为我们重写了官方的方法)，但实际上对应用影响不大
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        return ((HttpServletRequest) request).getMethod().equals(RequestMethod.OPTIONS.name());
    }
}
