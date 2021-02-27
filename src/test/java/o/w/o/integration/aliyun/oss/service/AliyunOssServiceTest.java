package o.w.o.integration.aliyun.oss.service;

import cn.hutool.core.io.FileUtil;
import o.w.o.domain.ResourceTest;
import o.w.o.integration.aliyun.oss.properties.MyOssProperties;
import o.w.o.integration.aliyun.oss.util.OssStsPolicy;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.Resource;

import static o.w.o.infrastructure.util.ServiceUtilTest.mockAuthorize;

class AliyunOssServiceTest extends ResourceTest {

  @Resource
  private MyOssProperties myOssProperties;
  @Resource
  private OssStsPolicy ossStsPolicy;

  @Resource
  private AliyunOssService aliyunOssService;

  @Value("${my.ip}")
  private String ip;

  @Value("${my.store.dir}")
  private String ossLocalStorageDir;

  @Test
  public void download() {
    var ossRemotePath = "public/images/logo.ink.svg";
    var ossLocalStoragePath = ossLocalStorageDir + "/" + ossRemotePath;

    mockAuthorize();

    aliyunOssService.fetchResourceOnOss(ossRemotePath);
    Assertions.assertTrue(FileUtil.exist(ossLocalStoragePath));
  }

}