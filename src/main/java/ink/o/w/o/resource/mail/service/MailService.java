package ink.o.w.o.resource.mail.service;

import ink.o.w.o.server.domain.ServiceResult;

public interface MailService {
  ServiceResult<String> sendEmail(String sender, String receiver, String title, String text);

  ServiceResult<String> sendSystemEmail(String receiver, String title, String text);
}
