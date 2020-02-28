package ink.o.w.o.resource.mail.service;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class MailServiceTest {
  @Autowired
  private MailService mailService;

  @Value("${spring.mail.username}")
  private String systemSender;

  @Value("${my.mail}")
  private String myEmail;

  @Test
  public void sendEmail() {
    Assert.assertNotNull(
        mailService.sendEmail(
            systemSender,
            myEmail,
            "测试邮件",
            "---------------------------"
        )
    );
  }

  @Test
  public void sendSystemEmail() {
    // TODO TEST
  }
}