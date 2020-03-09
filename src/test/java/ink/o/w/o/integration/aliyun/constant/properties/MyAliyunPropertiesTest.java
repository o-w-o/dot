package ink.o.w.o.integration.aliyun.constant.properties;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MyAliyunPropertiesTest {
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
    assertNotNull(myOssProperties.getEndpoint(), "endpoint was a required field !");

    assertTrue(ossEnable, "oss enabled !");
    assertEquals(myOssProperties.getEnable(), ossEnable);

    assertTrue(stsEnable, "sts enabled !");
    assertEquals(myStsProperties.getEnable(), stsEnable);
  }
}