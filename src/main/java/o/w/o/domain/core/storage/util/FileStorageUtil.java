package o.w.o.domain.core.storage.util;

import o.w.o.domain.core.storage.domian.FileStorage;
import o.w.o.infrastructure.definition.ServiceException;
import o.w.o.infrastructure.util.ServiceUtil;

import java.util.concurrent.atomic.AtomicReference;

import static o.w.o.domain.core.storage.properties.MyStoreProperties.StoreDir;

public class FileStorageUtil {

  /**
   * 获取 实际 存储地址 [根路径 [可视等级 / 用户 ID]/ 资源 URN. 资源后缀]
   *
   * @param o -
   * @return 地址
   */
  public static String getActualPath(FileStorage o) {
    return String.format("%s/%s.%s", generateRootDir(o), o.getUrn(), o.getSuffix());
  }

  /**
   * 获取 虚拟 存储地址 [根路径 [可视等级 / 用户 ID]/ 虚拟地址 / 资源名。资源后缀]
   *
   * @param o -
   * @return -
   */
  public static String getVirtualPath(FileStorage o) {
    return String.format("%s/%s/%s.%s", generateRootDir(o), o.getDir(), o.getName(), o.getSuffix());
  }

  public static String getTemporalPath(String resourceName) {
    return String.format("%s/%s", String.format("%s/%s", StoreDir.TEMPORAL, ServiceUtil.getPrincipalUserId()), resourceName);
  }

  public static String generateRootDir(FileStorage resourceSpace) {
    var dir = new AtomicReference<>("");
    var optId = ServiceUtil.fetchPrincipalUserId();

    optId.ifPresentOrElse(
        id -> {
          assert id.equals(resourceSpace.getOwner().getId());

          dir.set(
              String.format("%s/%s", resourceSpace.getVisibility().getName(), id)
          );
        },
        () -> {
          throw ServiceException.of(" 用户 ID 定位异常 ");
        }
    );

    return dir.get();
  }

  public static FileStorage mountTemporalMetaData(FileStorage resourceSpace) {
    return resourceSpace
        .setUrn("")
        .setUrl("")
        .setStorageRepository("")
        ;
  }
}
