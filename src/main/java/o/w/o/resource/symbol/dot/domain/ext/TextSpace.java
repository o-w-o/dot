package o.w.o.resource.symbol.dot.domain.ext;

import com.fasterxml.jackson.annotation.JsonTypeName;
import o.w.o.server.io.json.annotation.JsonEntityProperty;
import o.w.o.server.io.json.annotation.JsonTypedSpacePayload;
import o.w.o.resource.symbol.dot.domain.Dot;
import o.w.o.resource.symbol.dot.domain.DotSpace;
import o.w.o.resource.symbol.dot.domain.DotType;
import o.w.o.server.io.json.annotation.JsonTypedSpace;
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

