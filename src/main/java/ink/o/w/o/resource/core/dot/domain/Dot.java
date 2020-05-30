package ink.o.w.o.resource.core.dot.domain;


import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import javax.validation.Valid;
import java.util.Date;
import java.util.Optional;

@NoArgsConstructor
@Data

@Entity
@Table(name = "t_dot")
public class Dot {
  @Id
  @GeneratedValue(generator = "dot-uuid")
  @GenericGenerator(name = "dot-uuid", strategy = "uuid")
  private String id;

  @ManyToOne
  private DotType type;

  @Valid
  @Transient
  @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
  private DotSpace space;
  private String spaceId;

  @Column(columnDefinition = "json")
  @Type(type = "ink.o.w.o.server.def.JsonbType")
  private Object spaceContent;

  @CreatedDate
  @Temporal(TemporalType.TIMESTAMP)
  private Date cTime;

  @CreatedDate
  @Temporal(TemporalType.TIMESTAMP)
  private Date uTime;

  @PrePersist
  public void recordCreateTime() {
    if (Optional.ofNullable(cTime).isEmpty()) {
      cTime = new Date();
      uTime = new Date();
    }
  }

  @PreUpdate
  public void recordUpdateTime() {
    uTime = new Date();
  }
}