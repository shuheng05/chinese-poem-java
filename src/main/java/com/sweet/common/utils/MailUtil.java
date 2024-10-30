package com.sweet.common.utils;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.SecureRandom;

/**
 * 验证码工具类
 * @author shuheng
 */
@Slf4j
public class MailUtil {

    private final JavaMailSender javaMailSender;

    private final String from;

    public MailUtil(JavaMailSender javaMailSender, String from) {
        this.javaMailSender = javaMailSender;
        this.from = from;
    }

    /**
     * 发送验证码
     */
    public String sendCode(@RequestParam String email) {
        String code = generateVerificationCode();
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        try {
            helper.setSubject("诗岛探幽");
            helper.setText(
                    "<h2 style='color : #49766b'>\"诗岛探幽平台欢迎您！</h2>" +
                    "验证码: <span style='color : #49766b'>" + code + "</span><br>",
                    true);
            helper.setFrom(from, "诗岛探幽官方");
            helper.setTo(email);
        } catch (Exception e) {
            e.printStackTrace();
        }
        javaMailSender.send(mimeMessage);
        return code;
    }

    /**
     * 生成4位随机验证码
     */
    private static String generateVerificationCode() {
        String characters = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        SecureRandom random = new SecureRandom();
        StringBuilder code = new StringBuilder(4);
        for (int i = 0; i < 4; i++) {
            int randomIndex = random.nextInt(characters.length());
            char selectedChar = characters.charAt(randomIndex);
            // 将字符转换为大写或小写
            if (random.nextBoolean()) {
                selectedChar = Character.toLowerCase(selectedChar);
            } else {
                selectedChar = Character.toUpperCase(selectedChar);
            }
            code.append(selectedChar);
        }
        return code.toString();
    }
}
