package o.w.o.resource.symbol.way.service;

import o.w.o.resource.symbol.way.domain.Way;
import o.w.o.server.io.service.ServiceResult;

public interface WayService {
  String test(Way way);

  ServiceResult<Way> create(Way way);

  ServiceResult<Way> fetch(String wayId);
}
