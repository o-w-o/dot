package o.w.o.domain.core.notification.service;

import o.w.o.domain.core.notification.domain.MailTemplate;
import o.w.o.infrastructure.definition.ServiceResult;

/**
 * MailService
 *
 * @author symbols@dingtalk.com
 * @date 2020/11/15
 */
public interface MailService {
  /**
   * 发送 Email
   *
   * @param template 邮件模板 {@link MailTemplate}
   * @return status
   * @author symbols@dingtalk.com
   * @date 2020/11/15
   */
  ServiceResult<Boolean> send(MailTemplate template);
}
