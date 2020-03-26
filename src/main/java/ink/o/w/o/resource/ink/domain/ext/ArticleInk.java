package ink.o.w.o.resource.ink.domain.ext;

import com.fasterxml.jackson.annotation.JsonTypeName;
import ink.o.w.o.resource.ink.constant.InkType;
import ink.o.w.o.resource.ink.domain.InkSpace;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data

@JsonTypeName(InkType.TypeName.ARTICLE)

@Entity
@Table(name = "t_ink__article")
public class ArticleInk extends InkSpace implements Serializable {
  private static final long serialVersionUID = 2582992954173051962L;
  private String summary;
  private String content;

  @Override
  public InkType getType() {
    return InkType.ARTICLE;
  }
}
