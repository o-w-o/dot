package o.w.o.resource.symbols.field.util;

import o.w.o.resource.symbols.field.domain.ext.ResourceSpace;
import o.w.o.resource.symbols.field.domain.ext.ResourceSpacePayload;
import o.w.o.resource.symbols.field.domain.ext.ResourceSpacePayloadType;
import o.w.o.resource.system.user.domain.User;
import o.w.o.server.io.service.ServiceContext;
import o.w.o.server.io.service.ServiceException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;

import static o.w.o.server.config.properties.MyStoreProperties.StoreDir;

public class ResourceFieldSpaceUtil {

  /**
   * 获取 实际 存储地址 [根路径[可视等级/用户 ID]/资源 URN.资源后缀]
   *
   * @param o -
   * @return 地址
   */
  public static String getActualPath(ResourceSpace o) {
    return String.format("%s/%s.%s", generateRootDir(o), o.getUrn(), o.getSuffix());
  }

  /**
   * 获取 虚拟 存储地址 [根路径[可视等级/用户 ID]/虚拟地址/资源名.资源后缀]
   *
   * @param o -
   * @return -
   */
  public static String getVirtualPath(ResourceSpace o) {
    return String.format("%s/%s/%s.%s", generateRootDir(o), o.getDir(), o.getName(), o.getSuffix());
  }

  public static String getTemporalPath(String resourceName) {
    return String.format("%s/%s", String.format("%s/%s", StoreDir.TEMPORAL, ServiceContext.getUserIdFormSecurityContext()), resourceName);
  }

  public static String generateRootDir(ResourceSpace resourceSpace) {
    var dir = new AtomicReference<String>("");
    var optId = ServiceContext.fetchUserIdFormSecurityContext();

    optId.ifPresentOrElse(
        id -> {
          assert id.equals(resourceSpace.getOwner().getId());

          dir.set(
              String.format("%s/%s", resourceSpace.getVisibility().getName(), id)
          );
        },
        () -> {
          throw ServiceException.of("用户 ID 定位异常");
        }
    );

    return dir.get();
  }

  public static ResourceSpace generateResourceSpaceFormFile(File file, String virtualDir) {
    var resourceSpace = new ResourceSpace()
        .setStage(ResourceSpace.Stage.STORED)
        .setVisibility(ResourceSpace.Visibility.PRIVATE)
        .setSize(FileUtils.sizeOf(file))
        .setName(FilenameUtils.getBaseName(file.getName()))
        .setSuffix(FilenameUtils.getExtension(file.getName()))
        .setDir(virtualDir)
        .setFile(file);

    resourceSpace.setPayloadContent(
        generateResourceSpacePayload(resourceSpace)
    );
    resourceSpace.setOwner(new User().setId(ServiceContext.getUserIdFormSecurityContext()));

    return mountTemporalMetaData(resourceSpace);
  }

  public static ResourceSpace mountTemporalMetaData(ResourceSpace resourceSpace) {
    return resourceSpace
        .setUrn("")
        .setUrl("")
        .setStorageRepository("")
        ;
  }

  public static ResourceSpacePayload generateResourceSpacePayload(ResourceSpace resourceSpace) {
    return switch (judgePayloadTypeBySuffix(resourceSpace.getSuffix())) {
      case PICTURE -> new ResourceSpacePayload.Picture().setThumbnailUrl("");
      case AUDIO -> new ResourceSpacePayload.Audio();
      case VIDEO -> new ResourceSpacePayload.Video();
      case TEXT -> new ResourceSpacePayload.Text();
      case BINARY -> new ResourceSpacePayload.Binary();
      default -> throw new IllegalStateException("Unexpected value: " + judgePayloadTypeBySuffix(resourceSpace.getSuffix()));
    };
  }

  public static ResourceSpacePayloadType.TypeEnum judgePayloadTypeBySuffix(String suffix) {
    var type = new AtomicReference<ResourceSpacePayloadType.TypeEnum>();

    Arrays
        .stream(ResourceSpacePayloadType.TypeEnum.values())
        .filter(v ->
            v.getSuffixes().stream()
                .anyMatch(presetSuffixItem -> StringUtils.equalsIgnoreCase(presetSuffixItem, suffix))
        )
        .findFirst()
        .ifPresentOrElse(type::set, () -> {
          type.set(ResourceSpacePayloadType.TypeEnum.BINARY);
        });

    return type.get();
  }
}
