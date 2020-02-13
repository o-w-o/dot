package ink.o.w.o.resource.sample.domain;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "t_dot")


@Setter
@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true, value = {"hibernateLazyInitializer", "handler", "fieldHandler"})
public class Dot {
  @Id
  private Long id;

  private String name;

  @OneToOne(cascade = CascadeType.ALL)
  // 通过外键的方式
  @JoinColumn(name = "payload_id", referencedColumnName = "id")
  // 通过关联表保存一对一的关系
  // @JoinTable(name = "t_dot_dotpayload",
  //    joinColumns = @JoinColumn(name="dot_id"),
  //    inverseJoinColumns = @JoinColumn(name = "payload_id"))
  private DotPayload payload;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "dot", fetch = FetchType.LAZY)
  // 级联保存、更新、删除、刷新;延迟加载。当删除用户，会级联删除该用户的所有文章
  // 拥有 mappedBy 注解的实体类为关系被维护端
  // mappedBy="author"中的 author 是 Article 中的 author 属性
  private Set<DotPointX> xPonints = new HashSet<>();

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(name = "t_dot_pointidot",
      joinColumns = @JoinColumn(name = "dot_id"),
      inverseJoinColumns = @JoinColumn(name = "dot_pointi_id")
  )
  /*
    1、关系维护端，负责多对多关系的绑定和解除
    2、@JoinTable 注解的 name 属性指定关联表的名字，joinColumns 指定外键的名字，关联到关系维护端 (User)
    3、inverseJoinColumns 指定外键的名字，要关联的关系被维护端 (Authority)
    4、其实可以不使用 @JoinTable 注解，默认生成的关联表名称为主表表名+下划线+从表表名，即表名为user_authority
    关联到主表的外键名：主表名 + 下划线 + 主表中的主键列名,即 user_id
    关联到从表的外键名：主表中用于关联的属性名 + 下划线 + 从表的主键列名,即 authority_id
    主表就是关系维护端对应的表，从表就是关系被维护端对应的表
   */
  private Set<DotPointI> iPoints = new HashSet<>();
}
