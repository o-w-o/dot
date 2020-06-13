package o.w.o.resource.integration.email.service.impl;

import o.w.o.server.io.service.ServiceResult;
import o.w.o.resource.integration.email.service.MailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


@Service
public class MailServiceImpl implements MailService {
  @Resource
  private JavaMailSender javaMailSender;

  @Value("${spring.mail.username}")
  private String systemSender;

  /**
   * 简单的邮件发送功能
   * @param sender 发送方
   * @param receiver 接收方
   * @param title 发送的标题
   * @param text 发送的内容
   * @return -
   */
  @Override
  public ServiceResult<String> sendEmail(String sender, String receiver, String title, String text) {
    // 建立邮件消息
    SimpleMailMessage mainMessage = new SimpleMailMessage();

    mainMessage.setFrom(sender);
    mainMessage.setTo(receiver);
    mainMessage.setSubject(title);
    mainMessage.setText(text);

    javaMailSender.send(mainMessage);

    return ServiceResult.success();
  }

  @Override
  public ServiceResult<String> sendSystemEmail(String receiver, String title, String text) {
    return ServiceResult.success(sendEmail(systemSender, receiver, title, text).guard());
  }
}
