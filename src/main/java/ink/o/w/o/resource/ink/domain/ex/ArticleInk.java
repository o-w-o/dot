package ink.o.w.o.resource.ink.domain.ex;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Table;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "t_ink__article")
public class ArticleInk extends AbstractInk {

}
