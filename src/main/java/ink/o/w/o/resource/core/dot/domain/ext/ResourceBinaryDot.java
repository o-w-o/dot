package ink.o.w.o.resource.core.dot.domain.ext;

import com.fasterxml.jackson.annotation.JsonTypeName;
import ink.o.w.o.resource.core.dot.domain.DotType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 二进制文件单元
 *
 * @author symbols@dingtalk.com
 * @date 2020/02/12 16:48
 * @since 1.0.0
 */
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data

@JsonTypeName(DotType.TypeName.RESOURCE_BINARY)

@Entity
@Table(name = "t_dot__resource_binary")
public class ResourceBinaryDot extends Resource {
  @Override
  public DotType.DotTypeEnum getType() {
    return DotType.DotTypeEnum.RESOURCE_BINARY;
  }
}