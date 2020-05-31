package ink.o.w.o.resource.core.dot.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import ink.o.w.o.server.io.json.annotation.JsonTypedSpace;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import javax.validation.Valid;
import java.util.Date;
import java.util.Optional;

@NoArgsConstructor
@Data

@TypeDefs({
    @TypeDef(name = "json", typeClass = JsonStringType.class),
    @TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
})
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
  @JsonTypedSpace
  private DotSpace space;
  private String spaceId;

  @Type(type = "jsonb")
  @Column(columnDefinition = "jsonb")
  private Object spaceContent;

  @CreatedDate
  @Temporal(TemporalType.TIMESTAMP)
  private Date cTime;

  @CreatedDate
  @Temporal(TemporalType.TIMESTAMP)
  private Date uTime;

  @JsonIgnore
  public DotSpace getSpace() {
    return this.space;
  }

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
