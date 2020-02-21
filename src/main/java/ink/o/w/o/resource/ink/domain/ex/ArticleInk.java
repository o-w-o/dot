package ink.o.w.o.resource.ink.domain.ex;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.HashMap;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "t_ink__article")

@NoArgsConstructor
public class ArticleInk extends InkBasic implements Inkable<ArticleInk> {
  public ArticleInk(InkBasic ink) {
    hydrate(ink);
  }

  @Override
  public Map<String, Object> dehydrateEx(ArticleInk ink) {
    return new HashMap<>();
  }

  @Override
  public ArticleInk hydrateEx(InkBasic ink) {
    return this;
  }
}
