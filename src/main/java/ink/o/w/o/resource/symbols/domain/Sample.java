package ink.o.w.o.resource.symbols.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Sample
 * @author symbols@dingtalk.com
 * @version  1.0
 * @date 2019/8/8 下午6:02
 */
@Data
@Entity
@Table(name = "t_sample")
public class Sample {
    @Id
    String id;
    String name;
    String message;
    String type;
}
