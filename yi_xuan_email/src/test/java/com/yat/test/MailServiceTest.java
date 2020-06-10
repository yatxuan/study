package com.yat.test;

import cn.hutool.core.io.resource.ResourceUtil;
import com.yat.email.service.MailService;
import lombok.RequiredArgsConstructor;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;

import javax.mail.MessagingException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <p>Description: 邮件测试 </p>
 *
 * @author Yat-Xuan
 * @date 2020/4/8 10:51
 */
@RequiredArgsConstructor
public class MailServiceTest extends SpringBootDemoEmailApplicationTests {

    private final MailService mailService;
    private final TemplateEngine templateEngine;
    private final ApplicationContext context;

    String to = "yatxuan@163.com";

    @Test
    public void text() {
        mailService.testText();
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss:SS");
        String workDate = simpleDateFormat.format(date);

        System.out.println("程序执行完，时间：" + workDate);
    }

    /**
     * 测试简单邮件
     */
    @Test
    public void sendSimpleMail() {
        mailService.sendSimpleMail(to, "这是一封简单邮件", "这是一封普通的SpringBoot测试邮件");
    }

    /**
     * 测试HTML邮件
     */
    @Test
    public void sendHtmlMail() {
        Context context = new Context();
        context.setVariable("project", "Spring Boot Demo");
        context.setVariable("author", "YangXuan");
        context.setVariable("url", "https://github.com/yatxuan/study/tree/master/yi_xuan_email");
        String emailTemplate = templateEngine.process("welcome", context);
        mailService.sendHtmlMail(to, "这是一封模板HTML邮件", emailTemplate);
    }

    /**
     * 测试HTML邮件，自定义模板目录
     */
    @Test
    public void sendHtmlMail2() {

        SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
        templateResolver.setApplicationContext(context);
        templateResolver.setCacheable(false);
        templateResolver.setPrefix("classpath:/email/");
        templateResolver.setSuffix(".html");

        templateEngine.setTemplateResolver(templateResolver);

        Context context = new Context();
        context.setVariable("project", "Spring Boot Demo");
        context.setVariable("author", "YatXuan");
        context.setVariable("url", "https://github.com/yatxuan/study/tree/master/yi_xuan_email");

        String emailTemplate = templateEngine.process("test", context);
        mailService.sendHtmlMail(to, "这是一封模板HTML邮件", emailTemplate);
    }

    /**
     * 测试附件邮件
     *
     * @throws MessagingException 邮件异常
     */
    @Test
    public void sendAttachmentsMail() throws MessagingException {
        String url = "static/ceShi.png";
        URL resource = ResourceUtil.getResource(url);
        if (resource == null) {
            System.err.println("获取不到文件----------------------------------》");
            return;
        }
        mailService.sendAttachmentsMail(to, "这是一封带附件的邮件", "邮件中有附件，请注意查收！", resource.getPath());
    }

    /**
     * 测试静态资源邮件
     *
     * @throws MessagingException 邮件异常
     */
    @Test
    public void sendResourceMail() throws MessagingException {
        String rscId = "xkcoding";
        String content = "<html><body>这是带静态资源的邮件<br/><img src=\'cid:" + rscId + "\' ></body></html>";
        URL resource = ResourceUtil.getResource("static/ceShi.png");
        mailService.sendResourceMail(to, "这是一封带静态资源的邮件", content, resource.getPath(), rscId);
    }
}
