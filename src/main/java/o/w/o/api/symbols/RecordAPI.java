package o.w.o.api.symbols;

import lombok.extern.slf4j.Slf4j;
import o.w.o.resource.symbols.record.domain.Record;
import o.w.o.resource.symbols.record.service.RecordService;
import o.w.o.server.io.api.APIContext;
import o.w.o.server.io.api.APIException;
import o.w.o.server.io.api.APIResult;
import o.w.o.server.io.api.APISchemata;
import o.w.o.server.io.api.annotation.APIResource;
import o.w.o.server.io.api.annotation.APIResourceCreate;
import o.w.o.server.io.api.annotation.APIResourceFetch;
import o.w.o.server.io.api.annotation.APIResourceSchema;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * RecordAPI
 *
 * @author symbols@dingtalk.com
 * @date 2020/08/15
 */
@Slf4j
@APIResource(path = "records")
public class RecordAPI {
  @Resource
  private RecordService recordService;

  @APIResourceSchema
  public APIResult<APISchemata> schema() {
    return APIResult.of(APIContext.fetchAPIContext(RecordAPI.class).orElseThrow(APIException::new));
  }

  @APIResourceCreate(name = "创建 Record")
  public APIResult<Record> create(@RequestBody @Valid Record record) {
    return APIResult.of(recordService.create(record).guard());
  }

  @APIResourceFetch(path = "/{id}", name = "根据 Id 获取 Record")
  public APIResult<Record> retrieve(@PathVariable("id") String recordId) {
    return APIResult.of(recordService.fetch(recordId).guard());
  }
}
