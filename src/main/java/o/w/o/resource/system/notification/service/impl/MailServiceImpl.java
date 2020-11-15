package o.w.o.resource.system.notification.service.impl;

import o.w.o.resource.system.notification.domain.MailTemplate;
import o.w.o.resource.system.notification.service.MailService;
import o.w.o.server.definition.ServiceResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
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

  @Override
  public ServiceResult<Boolean> send(MailTemplate template) {
    var email = new SimpleMailMessage();

    email.setFrom(systemSender);
    email.setTo(template.getReceiver());
    email.setSubject(template.getSubject());
    email.setText(template.getBody());

    try {
      javaMailSender.send(email);
    } catch (MailException e) {
      return ServiceResult.error(e.getMessage());
    }

    return ServiceResult.success(true);
  }
}
