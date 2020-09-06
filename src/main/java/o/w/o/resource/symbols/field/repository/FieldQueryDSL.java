package o.w.o.resource.symbols.field.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import o.w.o.resource.symbols.field.domain.Field;
import o.w.o.resource.symbols.field.domain.QField;
import o.w.o.resource.symbols.field.domain.ext.QResourceSpace;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * FieldQueryDSL
 *
 * @author symbols@dingtalk.com
 * @date 2020/8/29
 */
@Component
public class FieldQueryDSL {
  @Resource
  private JPAQueryFactory jpaQueryFactory;

  public List<Field> retrieveResourceFieldsByUserIdAndDir(Integer userId, String dirPath) {
    var space = QResourceSpace.resourceSpace;
    var field = QField.field;
    var ids = jpaQueryFactory
        .selectFrom(space)
        .select(space.id)
        .where(space.owner.id.eq(userId).and(space.dir.eq(dirPath)))
        .fetch();

    return jpaQueryFactory.selectFrom(field)
        .where(field.id.in(ids))
        .fetch();
  }
}
