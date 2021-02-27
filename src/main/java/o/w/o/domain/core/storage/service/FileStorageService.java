package o.w.o.domain.core.storage.service;

import o.w.o.domain.core.storage.domian.FileStorage;
import o.w.o.infrastructure.definition.ServiceResult;

public interface FileStorageService {
  ServiceResult<FileStorage> stage(FileStorage resourceSpace);

  ServiceResult<FileStorage> persist(FileStorage resourceSpace);

  ServiceResult<Boolean> move(FileStorage resourceSpace);

  ServiceResult<Boolean> changeVisible(FileStorage resourceSpace);

  ServiceResult<Boolean> copy(FileStorage resourceSpace);

  ServiceResult<Boolean> remove(FileStorage resourceSpace);

  ServiceResult<Boolean> loadTemplateResource(String path, String templatePath);
}
