package ink.o.w.o.api;

import ink.o.w.o.server.io.api.annotation.APIResource;
import ink.o.w.o.server.io.api.annotation.APIResourceFetch;
import ink.o.w.o.server.io.api.annotation.APIResourceSchema;
import ink.o.w.o.server.io.api.APISchemata;
import ink.o.w.o.server.io.api.APIException;
import ink.o.w.o.server.io.api.APIResult;
import ink.o.w.o.util.ContextHelper;
import lombok.extern.slf4j.Slf4j;

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
    return APIResult.of(ContextHelper.fetchAPIContext());
  }

  @APIResourceSchema
  public APIResult<APISchemata> schema() {
    return APIResult.of(ContextHelper.fetchAPIContext(API.class).orElseThrow(APIException::new));
  }
}
