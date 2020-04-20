package ink.o.w.o.resource.core.dot.service;

import ink.o.w.o.resource.core.dot.domain.Dot;
import ink.o.w.o.server.io.service.ServiceResult;

public interface DotService {
  String test(Dot dot);

  ServiceResult<Dot> create(Dot dot);

  ServiceResult<Dot> fetch(String dotId);
}
