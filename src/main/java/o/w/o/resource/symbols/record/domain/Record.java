package o.w.o.resource.symbols.record.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import o.w.o.resource.symbols.field.domain.Field;
import o.w.o.server.io.db.EntityWithSpace;
import o.w.o.server.io.json.annotation.JsonEntityProperty;

import javax.persistence.*;
import java.util.Set;

import static javax.persistence.CascadeType.PERSIST;


/**
 * Record
 *
 * @author symbols@dingtalk.com
 * @date 2020/02/12 12:36
 * @since 1.0.0
 */
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Data

@JsonEntityProperty

@Entity
@Table(name = "t_s_record")
public class Record extends EntityWithSpace<RecordType, RecordSpace> {
  /**
   * 标题
   *
   * @date 2020/02/12 12:36
   * @since 1.0.0
   */
  protected String title;

  @ManyToMany
  protected Set<Field> fields;

  /**
   * 参与者
   *
   * @date 2020/02/12 12:36
   * @since 1.0.0
   */
  @OneToOne(fetch = FetchType.EAGER, cascade = PERSIST)
  protected RecordParticipants participants;
}
