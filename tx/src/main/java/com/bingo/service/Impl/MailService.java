package com.bingo.service.Impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

@Service
public class MailService {
    private final Logger LOGGER = LoggerFactory.getLogger(MailService.class);

    @Resource
    private JavaMailSender sender;

    @Value("${spring.mail.username}")
    private String username;

    /**
     * @description 发送纯文本的简单邮件
     * @param to 邮件接收者
     * @param subject 主题
     * @param content 内容
     */
    void sendSimpleMail(String to,String subject,String content){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(username);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);
        try {
            sender.send(message);
            LOGGER.info("发送给  " + to + "邮件发送成功");
        } catch (Exception ex){
            LOGGER.info("发送给 " + to + "邮件发送失败！" + ex.getMessage());
        }
    }

    /**
     * @description 发送html格式的邮件
     * @param to 邮件接收者
     * @param subject 主题
     * @param content 内容
     */
    void sendHtmlMail(String to,String subject,String content) throws MessagingException {
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom(username);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(content, true);
        sender.send(message);
        LOGGER.info("发送给  " + to + "html格式的邮件发送成功");
    }

    /**
     * @description 发送带附件的邮件
     * @param to 邮件接收者
     * @param subject 主题
     * @param content 内容
     * @param filePath 附件路径
     */
    void sendAttachmentsMail(String to,String subject,String content,String filePath) throws MessagingException {
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom(username);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(content, true);
        FileSystemResource file = new FileSystemResource(new File(filePath));
        String fileName = filePath.substring(filePath.lastIndexOf(File.separator));
        helper.addAttachment(fileName, file);
        sender.send(message);
        LOGGER.info("发送给  " + to + "带附件邮件发送成功");
    }

    /**
     * @description 发送嵌入静态资源（一般是图片）的邮件
     * @param to 邮件接收者
     * @param subject 主题
     * @param content 邮件内容，需要包括一个静态资源的id，比如：<img src=\"cid:rscId01\" >
     * @param rscPath 静态资源路径和文件名
     * @param rscId 静态资源id
     */
    void sendInlineResourceMail(String to,String subject,String content,String rscPath,String rscId) throws MessagingException {
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom(username);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(content, true);
        FileSystemResource res = new FileSystemResource(new File(rscPath));
        helper.addInline(rscId, res);
        sender.send(message);
        LOGGER.info("发送给  " + to + "嵌入静态资源的邮件发送成功");
    }
}
