package o.w.o.api.symbols;

import lombok.extern.slf4j.Slf4j;
import o.w.o.resource.symbols.field.domain.Field;
import o.w.o.resource.symbols.field.service.FieldService;
import o.w.o.resource.symbols.field.util.ResourceFieldSpaceUtil;
import o.w.o.server.io.api.APIContext;
import o.w.o.server.io.api.APIException;
import o.w.o.server.io.api.APIResult;
import o.w.o.server.io.api.APISchemata;
import o.w.o.server.io.api.annotation.APIResource;
import o.w.o.server.io.api.annotation.APIResourceCreate;
import o.w.o.server.io.api.annotation.APIResourceFetch;
import o.w.o.server.io.api.annotation.APIResourceSchema;
import o.w.o.server.io.service.ServiceException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;

/**
 * FieldAPI
 *
 * @author symbols@dingtalk.com
 * @date 2020/04/23
 */
@Slf4j
@APIResource(path = "fields")
public class FieldAPI {
  @Resource
  private FieldService fieldService;

  @APIResourceSchema
  public APIResult<APISchemata> schema() {
    return APIResult.of(APIContext.fetchAPIContext(FieldAPI.class).orElseThrow(APIException::new));
  }

  @APIResourceCreate(name = "创建 Field")
  public APIResult<Field> create(@RequestBody @Valid Field field) {
    return APIResult.of(fieldService.save(field).guard());
  }

  @APIResourceFetch(name = "根据 Id 获取 Field", path = "/{id}")
  public APIResult<Field> retrieve(@PathVariable("id") String fieldId) {
    return APIResult.of(fieldService.fetch(fieldId).guard());
  }

  @APIResourceCreate(name = "上传资源 Field", path = "/resources")
  public APIResult<?> upload(@NotNull MultipartFile file, @RequestParam String virtualDir) {
    String filename = Optional.ofNullable(file.getOriginalFilename())
        .orElseThrow(() -> ServiceException.of("文件名不能为空！"));

    if ("".equals(filename.trim())) {
      throw ServiceException.of("文件名不能为空！");
    }

    File storingFile = new File(filename);

    try (FileOutputStream os = new FileOutputStream(storingFile)) {
      os.write(file.getBytes());
      file.transferTo(storingFile);
    } catch (IOException e) {
      logger.error("文件读取异常！ [{}]", e.getMessage());
      throw ServiceException.of(e.getMessage());
    }

    return APIResult.from(
        fieldService
            .persist(ResourceFieldSpaceUtil.generateResourceSpaceFormFile(storingFile, virtualDir))
    );
  }

}
