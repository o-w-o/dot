package o.w.o.resource.symbol.dot.service;

import o.w.o.server.io.service.ServiceResult;
import o.w.o.resource.symbol.dot.domain.Dot;

import java.util.List;

public interface DotService {
  ServiceResult<Dot> create(Dot dot);

  ServiceResult<Dot> update(Dot dot);

  ServiceResult<Dot> retrieve(String dotId);

  ServiceResult<List<Dot>> retrieve(String[] dotIds);

  ServiceResult<Dot> delete(Dot dot);
}
