package ink.o.w.o.resource.ink.service.handler.ext;

import ink.o.w.o.resource.ink.constant.InkType;
import ink.o.w.o.resource.ink.domain.InkBasic;
import ink.o.w.o.server.domain.ServiceResult;

public abstract class AbstractInkHandler {

  abstract public String handle(InkBasic ink);

  abstract public ServiceResult<InkBasic> fetch(String inkId, InkType inkType);

  abstract public ServiceResult<InkBasic> create(InkBasic ink);

}