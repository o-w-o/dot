package o.w.o.resource.system.notification.domain;

import lombok.Builder;
import lombok.Data;

/**
 * MailTemplate
 *
 * @author symbols@dingtalk.com
 * @date 2020/11/15
 */
@Data
@Builder
public class MailTemplate {
  private String sender;
  private String receiver;
  private String subject;
  private String body;
}
