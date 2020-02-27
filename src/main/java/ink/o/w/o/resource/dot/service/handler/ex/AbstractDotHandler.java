package ink.o.w.o.resource.dot.service.handler.ex;

import ink.o.w.o.resource.dot.constant.DotType;
import ink.o.w.o.resource.dot.domain.DotBasic;
import ink.o.w.o.server.domain.ServiceResult;

public abstract class AbstractDotHandler {

  abstract public String handle(DotBasic dot);

  abstract public ServiceResult<DotBasic> fetch(String inkId, DotType dotType);

  abstract public ServiceResult<DotBasic> create(DotBasic dot);

}
