package com.yat.email.service.impl;

import cn.hutool.core.util.ArrayUtil;
import com.yat.email.service.MailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * <p>Description: 邮件接口 </p>
 *
 * @author Yat-Xuan
 * @date 2020/4/8 10:43
 */
@Slf4j
@Service
public class MailServiceImpl implements MailService {

    @Autowired
    private JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String from;

    /**
     * 发送文本邮件
     *
     * @param to      收件人地址
     * @param subject 邮件主题
     * @param content 邮件内容
     * @param cc      抄送地址
     */
    @Override
    public void sendSimpleMail(String to, String subject, String content, String... cc) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);
        if (ArrayUtil.isNotEmpty(cc)) {
            message.setCc(cc);
        }
        mailSender.send(message);
    }

    /**
     * 发送HTML邮件
     *
     * @param to      收件人地址
     * @param subject 邮件主题
     * @param content 邮件内容
     * @param cc      抄送地址
     */
    @Override
    public void sendHtmlMail(String to, String subject, String content, String... cc) {
        MimeMessage message = mailSender.createMimeMessage();
        getHelper(message, to, subject, content, cc);
        mailSender.send(message);
    }

    /**
     * 发送带附件的邮件
     *
     * @param to       收件人地址
     * @param subject  邮件主题
     * @param content  邮件内容
     * @param filePath 附件地址
     * @param cc       抄送地址
     * @throws MessagingException 邮件发送异常
     */
    @Override
    public void sendAttachmentsMail(String to, String subject, String content, String filePath, String... cc)
            throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();

        MimeMessageHelper helper = getHelper(message, to, subject, content, cc);
        FileSystemResource file = new FileSystemResource(new File(filePath));
        helper.addAttachment(file.getFilename(), file);
        mailSender.send(message);
    }


    /**
     * 发送正文中有静态资源的邮件
     *
     * @param to      收件人地址
     * @param subject 邮件主题
     * @param content 邮件内容
     * @param rscPath 静态资源地址
     * @param rscId   静态资源id
     * @param cc      抄送地址
     * @throws MessagingException 邮件发送异常
     */
    @Override
    public void sendResourceMail(String to, String subject, String content, String rscPath, String rscId, String... cc)
            throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();

        MimeMessageHelper helper = getHelper(message, to, subject, content, cc);
        FileSystemResource res = new FileSystemResource(new File(rscPath));
        helper.addInline(rscId, res);

        mailSender.send(message);
    }


    /**
     * 数据封装
     *
     * @param message 、
     * @param to      收件人地址
     * @param subject 邮件主题
     * @param content 邮件内容
     * @param cc      抄送地址
     */
    private MimeMessageHelper getHelper(MimeMessage message, String to, String subject, String content, String... cc) {
        MimeMessageHelper helper = null;
        try {
            helper = new MimeMessageHelper(message, true);

            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);
            if (ArrayUtil.isNotEmpty(cc)) {
                helper.setCc(cc);
            }
        } catch (MessagingException e) {
            log.error("邮件发送异常");
            e.printStackTrace();
        }
        return helper;
    }


    /**
     * 定时任务，指定 5 分钟后执行该任务
     *
     * @param str 验证码
     */
    private void timedDestruction(String str) {
        int expiration = 5;
        //以下示例为程序调用结束继续运行
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        try {
            executorService.schedule(() -> System.out.println(str), expiration * 60 * 1000L, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
