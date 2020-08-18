package o.w.o.resource.symbols.record.service.handler;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import o.w.o.resource.symbols.field.domain.Field;
import o.w.o.resource.symbols.field.service.FieldService;
import o.w.o.resource.symbols.record.domain.Record;
import o.w.o.resource.symbols.record.domain.RecordSpace;
import o.w.o.resource.symbols.record.domain.RecordType;
import o.w.o.resource.symbols.record.repository.RecordRepository;
import o.w.o.resource.symbols.record.repository.RecordSpaceRepository;
import o.w.o.server.io.service.ServiceException;
import o.w.o.server.io.service.ServiceResult;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@Slf4j
@Component
public class RecordSpaceDefaultHandler<T extends RecordSpace> extends RecordSpaceHandler {
  @Resource
  private FieldService fieldService;

  @Resource
  private RecordRepository recordRepository;
  private RecordSpaceRepository<T> recordSpaceRepository;

  @Override
  public ServiceResult<Record> fetch(String recordId, RecordType recordType) {
    return ServiceResult.success(
        recordRepository.findById(recordId).orElseThrow(
            () -> new ServiceException(String.format("未找到 Record -> id[ %s ], type[ %s ]", recordId, recordType))
        )
    );
  }

  @Override
  public ServiceResult<RecordSpace> fetchSpace(String recordSpaceId, RecordType recordType) {
    return ServiceResult.success(
        recordSpaceRepository.findById(recordSpaceId).orElseThrow(
            () -> new ServiceException(String.format("未找到 Record -> id[ %s ], type[ %s ]", recordSpaceId, recordType))
        )
    );
  }

  public ServiceResult<Set<Field>> preprocessFields(Set<Field> fields) {
    return fieldService.preprocessFields(fields);
  }

  public ServiceResult<Set<Field>> postprocessFields(Set<Field> fields) {
    return fieldService.postprocessFields(fields);
  }

  @SuppressWarnings({"unchecked", ""})
  @Override
  public ServiceResult<Record> create(Record record) {
    record.setFields(this.preprocessFields(record.getFields()).guard());

    T space = (T) record.getSpace();

    T createdRecordSpace = recordSpaceRepository.save(space);
    var spaceMountedRecord = (Record) record
        .setSpace(createdRecordSpace)
        .setSpaceId(createdRecordSpace.getId());

    spaceMountedRecord.setSpaceContent(createdRecordSpace);
    spaceMountedRecord.setFields(this.postprocessFields(spaceMountedRecord.getFields()).guard());
    logger.info("spaceMountedRecord -> [{}]", spaceMountedRecord);

    return ServiceResult.success((Record) spaceMountedRecord);
  }

}
