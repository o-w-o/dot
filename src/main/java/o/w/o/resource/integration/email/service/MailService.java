package o.w.o.resource.integration.email.service;

import o.w.o.server.io.service.ServiceResult;

public interface MailService {
  ServiceResult<String> sendEmail(String sender, String receiver, String title, String text);

  ServiceResult<String> sendSystemEmail(String receiver, String title, String text);
}
