package ink.o.w.o.resource.sample.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "t_dot_pointx")

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true, value = {"hibernateLazyInitializer", "handler", "fieldHandler"})
public class DotPointX {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private String name;

  // 当在一对一，多对多，一对一的情况下使用 Jackson 的 ObjectMapper 类将集合对象或者是 Java 对象序列化成 JSON 数据的时候，会抛出无限递归调用的异常……
  // 这是因为一对一，多对一，或者是多对多里面互相引用了对方的引用。
  // 只需要加入一个注解即可解决， @JsonIgnore
  // 参考： https://blog.csdn.net/u013803262/article/details/53978557
  @JsonIgnore
  // 可选属性 optional=false,表示 author 不能为空。删除文章，不影响用户
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  // 设置在 article 表中的关联字段 (外键)
  @JoinColumn(name = "dot_id")
  private Dot dot;
}
