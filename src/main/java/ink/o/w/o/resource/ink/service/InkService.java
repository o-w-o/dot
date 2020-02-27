package ink.o.w.o.resource.ink.service;

import ink.o.w.o.resource.ink.domain.InkBasic;
import ink.o.w.o.server.domain.ServiceResult;

public interface InkService {
  String test(InkBasic ink);

  ServiceResult<InkBasic> create(InkBasic ink);

  ServiceResult<InkBasic> fetch(String inkId);
}
