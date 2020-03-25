package ink.o.w.o.resource.dot.domain;


import com.fasterxml.jackson.annotation.JsonTypeInfo;
import ink.o.w.o.resource.dot.constant.DotType;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.util.Date;
import java.util.Optional;

@Data
@Entity
@Table(name = "t_dot")
public class Dot {
  @Id
  @GeneratedValue(generator = "dot-uuid")
  @GenericGenerator(name = "dot-uuid", strategy = "uuid")
  private String id;

  @Enumerated(value = EnumType.STRING)
  private DotType type;

  @Enumerated(value = EnumType.STRING)
  protected DotType spaceType;

  @Transient
  @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
  private DotSpace space;
  private String spaceId;
  @Lob
  private String spaceContent;

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
