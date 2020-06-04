package ink.o.w.o.resource.core.dot.domain.ext;

import com.fasterxml.jackson.annotation.JsonTypeName;
import ink.o.w.o.resource.core.dot.domain.Dot;
import ink.o.w.o.resource.core.dot.domain.DotSpace;
import ink.o.w.o.resource.core.dot.domain.DotType;
import ink.o.w.o.server.io.json.annotation.JsonEntityProperty;
import ink.o.w.o.server.io.json.annotation.JsonTypedSpace;
import ink.o.w.o.server.io.json.annotation.JsonTypedSpacePayload;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.Valid;
import java.util.Set;

/**
 * 文本单元
 *
 * @author symbols@dingtalk.com
 * @date 2020/02/12 17:03
 * @since 1.0.0
 */
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data

@JsonTypeName(DotType.TypeName.TEXT)
@JsonEntityProperty
@JsonTypedSpace

@Entity
@Table(name = "t_dot__text")
public class TextSpace extends DotSpace {
  @Type(type = "jsonb")
  @Column(columnDefinition = "jsonb")
  protected Object payloadContent;

  @Valid
  @Transient
  @JsonTypedSpacePayload
  private TextSpacePayload payload;

  @ManyToOne
  private TextSpacePayloadType payloadType;

  @ManyToMany
  private Set<Dot> dependentDots;
  @ManyToMany
  private Set<Dot> referenceDots;

  @Override
  public DotType.TypeEnum getType() {
    return DotType.TypeEnum.TEXT;
  }

}
