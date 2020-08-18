package o.w.o.resource.integration.email.service;

import lombok.extern.slf4j.Slf4j;
import o.w.o.resource.ResourceServiceTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
class MailServiceTest extends ResourceServiceTest {
  @Autowired
  private MailService mailService;

  @Value("${spring.mail.username}")
  private String systemSender;

  @Value("${my.mail}")
  private String myEmail;

  @Test
  public void sendEmail() {
    assertNotNull(
        this.mailService.sendEmail(
            this.systemSender,
            this.myEmail,
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