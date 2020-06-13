package o.w.o.resource.core.way.service.handler.ext;

import o.w.o.resource.core.way.domain.Way;
import o.w.o.resource.core.way.domain.WaySpace;
import o.w.o.resource.core.way.domain.WayType;
import o.w.o.server.io.service.ServiceResult;

public abstract class AbstractWayHandler {

  abstract public String handle(Way way);

  abstract public ServiceResult<Way> fetch(String wayId, WayType.TypeEnum wayType);
  abstract public ServiceResult<WaySpace> fetchSpace(String waySpaceId, WayType.TypeEnum wayType);

  abstract public ServiceResult<Way> create(Way way);

}
