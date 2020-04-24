package ink.o.w.o.api;

import ink.o.w.o.resource.integration.aliyun.service.AliyunOssService;
import ink.o.w.o.server.io.api.ResponseEntityFactory;
import ink.o.w.o.server.io.service.ServiceExceptionFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.EntityLinks;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;

@Slf4j
@RestController
@ExposesResourceFor(OssAPI.class)
@RequestMapping("oss")
public class OssAPI {
  private final AliyunOssService aliyunOssService;
  private final EntityLinks entityLinks;

  @Autowired
  public OssAPI(EntityLinks entityLinks, AliyunOssService aliyunOssService) {
    this.entityLinks = entityLinks;
    this.aliyunOssService = aliyunOssService;
  }


  @PostMapping
  public ResponseEntity<?> upload(@NotNull MultipartFile file) {
    String filename = Optional.ofNullable(file.getOriginalFilename())
        .orElseThrow(() -> ServiceExceptionFactory.of("文件名不能为空！"));

    if ("".equals(filename.trim())) {
      throw ServiceExceptionFactory.of("文件名不能为空！");
    }

    File temporalFile = new File(filename);

    try (FileOutputStream os = new FileOutputStream(temporalFile)) {
      os.write(file.getBytes());
      file.transferTo(temporalFile);
    } catch (IOException e) {
      logger.error("文件读取异常！ [{}]", e.getMessage());
      throw ServiceExceptionFactory.of(e.getMessage());
    }

    var createdDot = aliyunOssService.uploadTemporalResourceToOss(temporalFile).guard();
    return ResponseEntityFactory.ok(
        new EntityModel<>(
            createdDot,
            entityLinks.linkFor(OssAPI.class).withSelfRel()
        )
    );
  }
}
