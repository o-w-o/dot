package ink.o.w.o.resource.mail.service.impl;

import ink.o.w.o.server.domain.ServiceResult;
import ink.o.w.o.server.domain.ServiceResultFactory;
import ink.o.w.o.resource.mail.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
public class MailServiceImpl implements MailService {
  private final JavaMailSender javaMailSender;

  @Autowired
  public MailServiceImpl(JavaMailSender javaMailSender) {
    this.javaMailSender = javaMailSender;
  }

  @Override
  public ServiceResult<String> sendEmail(String sender, String receiver, String title, String text) {
    // 建立邮件消息
    SimpleMailMessage mainMessage = new SimpleMailMessage();
    // 发送方
    mainMessage.setFrom(sender);
    // 接收方
    mainMessage.setTo(receiver);
    // 发送的标题
    mainMessage.setSubject(title);
    // 发送的内容
    mainMessage.setText(text);

    javaMailSender.send(mainMessage);

    return ServiceResultFactory.success();
  }

  @Override
  public ServiceResult<String> sendSystemEmail(String receiver, String title, String text) {
    return ServiceResultFactory.success(sendEmail("", receiver, title, text).guard());
  }
}
