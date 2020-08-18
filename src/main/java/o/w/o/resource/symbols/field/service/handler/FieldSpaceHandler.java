package o.w.o.resource.symbols.field.service.handler;

import o.w.o.resource.symbols.field.domain.Field;
import o.w.o.resource.symbols.field.domain.FieldSpace;
import o.w.o.server.io.service.ServiceResult;

import java.util.Set;

/**
 * FieldSpaceHandler
 *
 * @author symbols@dingtalk.com
 * @date 2020/8/18
 */
public abstract class FieldSpaceHandler {

  /**
   * 持久化 Field，仅限 RESOURCE 类型
   * @param field -
   * @return -
   */
  abstract public ServiceResult<Field> persist(Field field);

  /**
   *
   * @param field -
   * @return -
   */
  abstract public ServiceResult<Field> process(Field field);

  abstract public ServiceResult<Set<Field>> process(Set<Field> field);

  abstract public ServiceResult<Field> fetch(String fieldId);

  abstract public ServiceResult<FieldSpace> fetchSpace(String fieldSpaceId);

  public abstract ServiceResult<Boolean> destory(FieldSpace space);
}
