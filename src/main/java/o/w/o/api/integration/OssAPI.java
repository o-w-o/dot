package o.w.o.api.integration;

import o.w.o.resource.integration.aliyun.core.service.AliyunOssService;
import o.w.o.server.io.api.APIContext;
import o.w.o.server.io.api.APIException;
import o.w.o.server.io.api.APIResult;
import o.w.o.server.io.api.APISchemata;
import o.w.o.server.io.api.annotation.APIResource;
import o.w.o.server.io.api.annotation.APIResourceSchema;
import o.w.o.server.io.service.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;

@Slf4j
@APIResource(path = "oss")
public class OssAPI {
  @Resource
  private AliyunOssService aliyunOssService;

  @APIResourceSchema
  public APIResult<APISchemata> fetchSchema() {
    return APIResult.of(APIContext.fetchAPIContext(OssAPI.class).orElseThrow(APIException::new));
  }


  @PostMapping
  public APIResult<?> upload(@NotNull MultipartFile file) {
    String filename = Optional.ofNullable(file.getOriginalFilename())
        .orElseThrow(() -> ServiceException.of("文件名不能为空！"));

    if ("".equals(filename.trim())) {
      throw ServiceException.of("文件名不能为空！");
    }

    File temporalFile = new File(filename);

    try (FileOutputStream os = new FileOutputStream(temporalFile)) {
      os.write(file.getBytes());
      file.transferTo(temporalFile);
    } catch (IOException e) {
      logger.error("文件读取异常！ [{}]", e.getMessage());
      throw ServiceException.of(e.getMessage());
    }

    var createdDot = aliyunOssService.uploadTemporalResourceToOss(temporalFile).guard();
    return APIResult.of(createdDot);
  }
}
