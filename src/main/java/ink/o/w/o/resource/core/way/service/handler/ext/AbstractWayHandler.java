package ink.o.w.o.resource.core.way.service.handler.ext;

import ink.o.w.o.resource.core.way.domain.Way;
import ink.o.w.o.resource.core.way.domain.WaySpace;
import ink.o.w.o.resource.core.way.domain.WayType;
import ink.o.w.o.server.io.service.ServiceResult;

public abstract class AbstractWayHandler {

  abstract public String handle(Way way);

  abstract public ServiceResult<Way> fetch(String wayId, WayType.WayTypeEnum wayType);
  abstract public ServiceResult<WaySpace> fetchSpace(String waySpaceId, WayType.WayTypeEnum wayType);

  abstract public ServiceResult<Way> create(Way way);

}
