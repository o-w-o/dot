package o.w.o.resource.symbols.record.service;

import o.w.o.resource.symbols.record.domain.Record;
import o.w.o.server.io.service.ServiceResult;

public interface RecordService {
  ServiceResult<Record> create(Record record);

  ServiceResult<Record> modify(Record record);

  ServiceResult<Record> destory(Record record);

  ServiceResult<Record> fetch(String recordId);

  ServiceResult<Record> retrieve(String recordId);
}
