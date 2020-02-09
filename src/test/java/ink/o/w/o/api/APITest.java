package ink.o.w.o.api;

import ink.o.w.o.api.config.RestDocumentTestConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.OverrideAutoConfiguration;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

/**
 * @author symbols@dingtalk.com
 * @date 2019/8/8 上午8:59
 */

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(uriHost = "api.o-w-o.ink", uriScheme = "https", uriPort = 443)
@Import(RestDocumentTestConfiguration.class)
public class APITest {
  @Autowired
  public MockMvc mockMvc;

  @Autowired
  public RestDocumentationResultHandler restDocumentationResultHandler;
}
