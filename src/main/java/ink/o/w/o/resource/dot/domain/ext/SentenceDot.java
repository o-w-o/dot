package ink.o.w.o.resource.dot.domain.ext;

import com.fasterxml.jackson.annotation.JsonTypeName;
import ink.o.w.o.resource.dot.constant.DotType;
import ink.o.w.o.resource.dot.domain.DotSpace;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 句子单元
 *
 * @author symbols@dingtalk.com
 * @date 2020/02/12 17:03
 * @since 1.0.0
 */
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data

@JsonTypeName(DotType.TypeName.SENTENCE)

@Entity
@Table(name = "t_dot__sentence")
public class SentenceDot extends DotSpace {
}

