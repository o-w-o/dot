package ink.o.w.o.resource.dot.service;

import ink.o.w.o.resource.dot.domain.DotBasic;
import ink.o.w.o.server.domain.ServiceResult;

public interface DotService {
  String test(DotBasic dot);

  ServiceResult<DotBasic> create(DotBasic dot);

  ServiceResult<DotBasic> fetch(String dotId);
}
