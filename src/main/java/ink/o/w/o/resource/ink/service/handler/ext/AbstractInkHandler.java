package ink.o.w.o.resource.ink.service.handler.ext;

import ink.o.w.o.resource.ink.constant.InkType;
import ink.o.w.o.resource.ink.domain.Ink;
import ink.o.w.o.resource.ink.domain.InkSpace;
import ink.o.w.o.server.domain.ServiceResult;

public abstract class AbstractInkHandler {

  abstract public String handle(Ink ink);

  abstract public ServiceResult<Ink> fetch(String inkId, InkType inkType);
  abstract public ServiceResult<InkSpace> fetchSpace(String inkSpaceId, InkType inkType);

  abstract public ServiceResult<Ink> create(Ink ink);

}
