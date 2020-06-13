package o.w.o.resource.core.way.domain.ext;

import com.fasterxml.jackson.annotation.JsonTypeName;
import o.w.o.resource.core.symbols.domain.SymbolsType;
import o.w.o.resource.core.way.domain.WaySpace;
import o.w.o.resource.core.way.domain.WayType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data

@JsonTypeName(SymbolsType.TypeName.DOCUMENT)

@Entity
@Table(name = "t_way__sitemap")
public class SitemapWay extends WaySpace implements Serializable {
  private static final long serialVersionUID = 2582992954173051962L;

  private String summary;
  private String content;

  @Override
  public WayType.TypeEnum getType() {
    return WayType.TypeEnum.NET;
  }
}
