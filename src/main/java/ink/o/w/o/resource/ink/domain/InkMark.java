package ink.o.w.o.resource.ink.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Data
@Entity
@Table(name = "t_ink_mark")
public class InkMark {
  @Id
  private Long id;
}
