package o.w.o.domain.core.authentication.service;

import lombok.extern.slf4j.Slf4j;
import o.w.o.domain.ResourceTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
class AuthenticationServiceTest extends ResourceTest {
  @Autowired
  private AuthenticationService authenticationService;

  @Test
  void getIpLocationByIpV4() {
    var ipOpt = authenticationService.getIpLocationByIpV4("123.9.10.122");
    Assertions.assertTrue(ipOpt.isSuccess());

    var ip = ipOpt.guard();
    Assertions.assertEquals(ip.getCountryName(), "China");
    Assertions.assertEquals(ip.getRegionName(), "Henan");
    Assertions.assertEquals(ip.getCityName(), "Puyang");
    logger.info("ip -> [{}]", ip);

    Assertions.assertEquals(authenticationService.getIpLocationByIpV4("123.6.10.122").guard().getCityName(), "Zhengzhou");
  }
}