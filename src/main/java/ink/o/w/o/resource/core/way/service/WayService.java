package ink.o.w.o.resource.core.way.service;

import ink.o.w.o.resource.core.way.domain.Way;
import ink.o.w.o.server.io.service.ServiceResult;

public interface WayService {
  String test(Way way);

  ServiceResult<Way> create(Way way);

  ServiceResult<Way> fetch(String wayId);
}
