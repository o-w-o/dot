package o.w.o.resource.core.way.service;

import o.w.o.resource.core.way.domain.Way;
import o.w.o.server.io.service.ServiceResult;

public interface WayService {
  String test(Way way);

  ServiceResult<Way> create(Way way);

  ServiceResult<Way> fetch(String wayId);
}
