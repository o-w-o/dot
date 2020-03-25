package ink.o.w.o.resource.dot.service.handler.ex;

import ink.o.w.o.resource.dot.constant.DotType;
import ink.o.w.o.resource.dot.domain.Dot;
import ink.o.w.o.resource.dot.domain.DotSpace;
import ink.o.w.o.server.domain.ServiceResult;

public abstract class AbstractDotHandler {

  abstract public String handle(Dot dot);

  abstract public ServiceResult<Dot> fetch(String dotId, DotType dotType);
  abstract public ServiceResult<DotSpace> fetchSpace(String dotSpaceId, DotType dotType);

  abstract public ServiceResult<Dot> create(Dot dot);

}
