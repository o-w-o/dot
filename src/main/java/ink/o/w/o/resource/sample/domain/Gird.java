package ink.o.w.o.resource.sample.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "t_gird")

@Data
@NoArgsConstructor
public class Gird {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private String name;

  @ManyToMany
  private Set<Space> spaces;
}
