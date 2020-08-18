package o.w.o.resource.symbols.record.service.handler;

import o.w.o.resource.symbols.record.domain.Record;
import o.w.o.resource.symbols.record.domain.RecordSpace;
import o.w.o.resource.symbols.record.domain.RecordType;
import o.w.o.server.io.service.ServiceResult;

public abstract class RecordSpaceHandler {
  abstract public ServiceResult<Record> create(Record record);

  abstract public ServiceResult<Record> fetch(String recordId, RecordType recordType);

  abstract public ServiceResult<RecordSpace> fetchSpace(String recordSpaceId, RecordType recordType);
}
