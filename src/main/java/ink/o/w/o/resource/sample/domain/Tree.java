package ink.o.w.o.resource.sample.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "t_tree")

@Data
@NoArgsConstructor
public class Tree {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private String name;

  @OneToOne
  private Space parent;

  @OneToMany
  private Set<Space> children;
}
