package ink.o.w.o.resource.sample.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "t_space")

@Data
@NoArgsConstructor
public class Space {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private String name;

  @OneToOne
  private Dot dot;
}
