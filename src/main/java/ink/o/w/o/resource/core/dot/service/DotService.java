package ink.o.w.o.resource.core.dot.service;

import ink.o.w.o.resource.core.dot.domain.Dot;
import ink.o.w.o.server.io.service.ServiceResult;

import java.util.List;

public interface DotService {
  ServiceResult<Dot> create(Dot dot);

  ServiceResult<Dot> update(Dot dot);

  ServiceResult<Dot> retrieve(String dotId);

  ServiceResult<List<Dot>> retrieve(String[] dotIds);

  ServiceResult<Dot> delete(Dot dot);
}
