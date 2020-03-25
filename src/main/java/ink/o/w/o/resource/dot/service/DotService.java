package ink.o.w.o.resource.dot.service;

import ink.o.w.o.resource.dot.domain.Dot;
import ink.o.w.o.server.domain.ServiceResult;

public interface DotService {
  String test(Dot dot);

  ServiceResult<Dot> create(Dot dot);

  ServiceResult<Dot> fetch(String dotId);
}
