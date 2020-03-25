package ink.o.w.o.resource.ink.service;

import ink.o.w.o.resource.ink.domain.Ink;
import ink.o.w.o.server.domain.ServiceResult;

public interface InkService {
  String test(Ink ink);

  ServiceResult<Ink> create(Ink ink);

  ServiceResult<Ink> fetch(String inkId);
}
