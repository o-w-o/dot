package o.w.o.api;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import o.w.o.server.definition.ApiRateLimiter;
import o.w.o.server.definition.ApiResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * API 入口
 *
 * @author symbols@dingtalk.com
 * @date 2020/02/14 11:17
 * @since 1.0.0
 */
@Slf4j
@RestController
@RequestMapping
public class Api {
  @ApiRateLimiter(limits = 20)
  @ApiOperation("获取 API 说明")
  @GetMapping
  public ApiResult<String> index() {
    return ApiResult.of("烛火录接口服务。");
  }
}