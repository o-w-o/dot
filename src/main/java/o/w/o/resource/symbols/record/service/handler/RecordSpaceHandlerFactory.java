package o.w.o.resource.symbols.record.service.handler;

import lombok.extern.slf4j.Slf4j;
import o.w.o.resource.symbols.record.domain.RecordSpace;
import o.w.o.resource.symbols.record.repository.RecordRepository;
import o.w.o.resource.symbols.record.repository.RecordSpaceRepository;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
public class RecordSpaceHandlerFactory<T extends RecordSpace> {
  @Resource
  private RecordRepository recordRepository;

  public RecordSpaceDefaultHandler<T> getDefaultHandler(RecordSpaceRepository<T> recordSpaceRepository) {
    return new RecordSpaceDefaultHandler<T>()
        .setRecordRepository(recordRepository)
        .setRecordSpaceRepository(recordSpaceRepository);
  }
}
