package o.w.o.resource.symbol.dot.service.handler;

import o.w.o.server.io.service.ServiceResult;
import o.w.o.resource.symbol.dot.domain.Dot;
import o.w.o.resource.symbol.dot.domain.DotSpace;
import o.w.o.resource.symbol.dot.domain.DotType;

public abstract class DotSpaceHandler {

  abstract public String handle(Dot dot);

  abstract public ServiceResult<Dot> retrieve(String dotId, DotType dotType);

  abstract public ServiceResult<DotSpace> retrieveSpace(String dotSpaceId, DotType dotType);

  abstract public ServiceResult<Dot> create(Dot dot);

}
