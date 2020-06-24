package o.w.o.resource.symbol.ink.domain.ext;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@NoArgsConstructor
@Data

@Entity
@Table(name = "t_symbols__template_field")
public class TemplateSymbolsField implements Serializable {
  private static final long serialVersionUID = 4929154940346186425L;

  @Id
  protected String id;
}
