package o.w.o.resource.integration.aliyun.core.constant.properties;

import o.w.o.resource.ResourceTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import static org.junit.jupiter.api.Assertions.*;

class MyAliyunPropertiesTest extends ResourceTest {
  @Autowired
  private MyAliyunProperties.MyOssProperties myOssProperties;

  @Autowired
  private MyAliyunProperties.MyStsProperties myStsProperties;

  @Value("${my.aliyun.oss.enable}")
  private Boolean ossEnable;

  @Value("${my.aliyun.sts.enable}")
  private Boolean stsEnable;

  @Test
  public void testProperties() {
    assertNotNull(this.myOssProperties.getEndpoint(), "endpoint was a required field !");

    assertTrue(this.ossEnable, "oss enabled !");
    assertEquals(this.myOssProperties.getEnable(), this.ossEnable);

    assertTrue(this.stsEnable, "sts enabled !");
    assertEquals(this.myStsProperties.getEnable(), this.stsEnable);
  }
}