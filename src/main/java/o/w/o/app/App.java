package o.w.o.app;

import lombok.extern.slf4j.Slf4j;
import o.w.o.domain.core.storage.service.FileStorageService;
import o.w.o.infrastructure.definition.ApiRateLimiter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

/**
 * API 入口
 *
 * @author symbols@dingtalk.com
 * @date 2021/02/23
 * @since 1.0.0
 */
@Slf4j
@Controller
public class App {

  @Resource
  private FileStorageService fileStorageService;

  @ApiRateLimiter(limits = 20)
  @RequestMapping(path = "/")
  public String index() {
    fileStorageService
        .loadTemplateResource("public/index.html", "index.html")
        .guard();

    return "index";
  }

  @RequestMapping(path = "/test")
  public String demo() {
    return "test";
  }
}