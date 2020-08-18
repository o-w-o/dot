package o.w.o.resource.symbols.record.service.impl;

import lombok.extern.slf4j.Slf4j;
import o.w.o.resource.symbols.record.domain.Record;
import o.w.o.resource.symbols.record.repository.RecordRepository;
import o.w.o.resource.symbols.record.service.RecordService;
import o.w.o.resource.symbols.record.service.handler.RecordSpaceHandlerHolder;
import o.w.o.server.io.service.ServiceException;
import o.w.o.server.io.service.ServiceResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicReference;

/**
 * RecordServiceImpl
 *
 * @author record@dingtalk.com
 * @date 2020/02/13
 * @since 1.0.0
 */
@Slf4j
@Service
public class RecordServiceImpl implements RecordService {
  private final RecordSpaceHandlerHolder recordSpaceHandlerHolder;
  private final RecordRepository recordRepository;

  @Autowired
  RecordServiceImpl(RecordSpaceHandlerHolder recordSpaceHandlerHolder, RecordRepository recordRepository) {
    this.recordSpaceHandlerHolder = recordSpaceHandlerHolder;
    this.recordRepository = recordRepository;
  }

  @Override
  public ServiceResult<Record> fetch(String recordId) {
    logger.info("处理 RECORD 开始：{}", recordId);

    var record = recordRepository.findById(recordId).orElseThrow(() -> new ServiceException(String.format("Record id[ %s ] 不存在！", recordId)));
    var status = new AtomicReference<>(false);
    var result = new AtomicReference<Record>(null);

    recordSpaceHandlerHolder.select(record.getType().getType()).ifPresent(handler -> {
      result.set(handler.fetch(record.getId(), record.getType()).guard());
      status.set(true);
    });

    logger.info("处理 RECORD 结束：{}", status.get());
    return ServiceResult.success(result.get());
  }

  @Override
  public ServiceResult<Record> retrieve(String recordId) {
    return null;
  }


  @Override
  public ServiceResult<Record> create(Record record) {
    logger.info("处理 RECORD 开始： type -> [ {} ] [ {} ]", record.getType(), record);

    var result = new AtomicReference<ServiceResult<Record>>(null);

    recordSpaceHandlerHolder.select(record.getType().getType()).ifPresent(handler -> {
      result.set(handler.create(record));
    });

    var spaceMountedRecord = result.get().guard();

    recordRepository.save(spaceMountedRecord);

    logger.info("处理 RECORD 结束： success -> [ {} ]", result.get().getSuccess());
    return ServiceResult.success(spaceMountedRecord);
  }

  @Override
  public ServiceResult<Record> modify(Record record) {
    return null;
  }

  @Override
  public ServiceResult<Record> destory(Record record) {
    return null;
  }
}
