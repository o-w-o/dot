package o.w.o.api;

import lombok.extern.slf4j.Slf4j;
import o.w.o.server.io.api.APIContext;
import o.w.o.server.io.api.APIException;
import o.w.o.server.io.api.APIResult;
import o.w.o.server.io.api.APISchemata;
import o.w.o.server.io.api.annotation.APIResource;
import o.w.o.server.io.api.annotation.APIResourceFetch;
import o.w.o.server.io.api.annotation.APIResourceSchema;

import java.util.Map;

/**
 * API 入口
 *
 * @author symbols@dingtalk.com
 * @date 2020/02/14 11:17
 * @since 1.0.0
 */
@Slf4j
@APIResource
public class API {

  @APIResourceFetch(name = "获取 API 文档索引！")
  public APIResult<Map<String, String>> index() {
    return APIResult.of(APIContext.fetchAPIContext());
  }

  @APIResourceSchema
  public APIResult<APISchemata> schema() {
    return APIResult.of(APIContext.fetchAPIContext(API.class).orElseThrow(APIException::new));
  }
}
