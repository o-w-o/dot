package ink.o.w.o.resource.core.dot.service.handler;

import ink.o.w.o.resource.core.dot.domain.Dot;
import ink.o.w.o.resource.core.dot.domain.DotSpace;
import ink.o.w.o.resource.core.dot.domain.DotType;
import ink.o.w.o.server.io.service.ServiceResult;

public abstract class DotSpaceHandler {

  abstract public String handle(Dot dot);

  abstract public ServiceResult<Dot> retrieve(String dotId, DotType dotType);

  abstract public ServiceResult<DotSpace> retrieveSpace(String dotSpaceId, DotType dotType);

  abstract public ServiceResult<Dot> create(Dot dot);

}
