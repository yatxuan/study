// package com.yat.social.justauth;
//
// import lombok.RequiredArgsConstructor;
// import me.zhyd.oauth.cache.AuthStateCache;
// import me.zhyd.oauth.config.AuthConfig;
// import me.zhyd.oauth.config.AuthDefaultSource;
// import me.zhyd.oauth.config.AuthSource;
// import me.zhyd.oauth.enums.AuthResponseStatus;
// import me.zhyd.oauth.exception.AuthException;
// import me.zhyd.oauth.request.*;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Component;
//
// import java.util.List;
// import java.util.stream.Collectors;
//
// /**
//  * <p>Description: 描述 </p>
//  *
//  * @Created with IDEA
//  * @author: Yat
//  * @Date: 2020/4/9
//  * @Time: 9:53
//  */
// @Component
// @RequiredArgsConstructor(onConstructor_ = @Autowired)
// public class AuthRequestFactory {
//
//
//     private final JustAuthProperties properties;
//     private final AuthStateCache authStateCache;
//
//     /**
//      * 返回当前Oauth列表
//      *
//      * @return Oauth列表
//      */
//     public List<String> oauthList() {
//         return properties.getType().keySet().stream().map(Enum::name).collect(Collectors.toList());
//     }
//
//     /**
//      * 返回AuthRequest对象
//      *
//      * @param source {@link AuthSource}
//      * @return {@link AuthRequest}
//      */
//     public AuthRequest get(AuthDefaultSource source) {
//         AuthConfig config = properties.getType().get(source);
//         switch (source) {
//             case GITHUB:
//                 return new AuthGithubRequest(config, authStateCache);
//             case WEIBO:
//                 return new AuthWeiboRequest(config, authStateCache);
//             case GITEE:
//                 return new AuthGiteeRequest(config, authStateCache);
//             case DINGTALK:
//                 return new AuthDingTalkRequest(config, authStateCache);
//             case BAIDU:
//                 return new AuthBaiduRequest(config, authStateCache);
//             case CSDN:
//                 return new AuthCsdnRequest(config, authStateCache);
//             case CODING:
//                 return new AuthCodingRequest(config, authStateCache);
//             case TENCENT_CLOUD:
//                 return new AuthTencentCloudRequest(config, authStateCache);
//             case OSCHINA:
//                 return new AuthOschinaRequest(config, authStateCache);
//             case ALIPAY:
//                 return new AuthAlipayRequest(config, authStateCache);
//             case QQ:
//                 return new AuthQqRequest(config, authStateCache);
//             case TAOBAO:
//                 return new AuthTaobaoRequest(config, authStateCache);
//             case GOOGLE:
//                 return new AuthGoogleRequest(config, authStateCache);
//             case FACEBOOK:
//                 return new AuthFacebookRequest(config, authStateCache);
//             case DOUYIN:
//                 return new AuthDouyinRequest(config, authStateCache);
//             case LINKEDIN:
//                 return new AuthLinkedinRequest(config, authStateCache);
//             case MICROSOFT:
//                 return new AuthMicrosoftRequest(config, authStateCache);
//             case MI:
//                 return new AuthMiRequest(config, authStateCache);
//             case TOUTIAO:
//                 return new AuthToutiaoRequest(config, authStateCache);
//             case TEAMBITION:
//                 return new AuthTeambitionRequest(config, authStateCache);
//             case RENREN:
//                 return new AuthRenrenRequest(config, authStateCache);
//             case PINTEREST:
//                 return new AuthPinterestRequest(config, authStateCache);
//             case STACK_OVERFLOW:
//                 return new AuthStackOverflowRequest(config, authStateCache);
//             case HUAWEI:
//                 return new AuthHuaweiRequest(config, authStateCache);
//             case WECHAT_ENTERPRISE:
//                 return new AuthWeChatEnterpriseRequest(config, authStateCache);
//             case GITLAB:
//                 return new AuthGitlabRequest(config, authStateCache);
//             case KUJIALE:
//                 return new AuthKujialeRequest(config, authStateCache);
//             default:
//                 throw new AuthException(AuthResponseStatus.UNSUPPORTED);
//         }
//     }
// }
