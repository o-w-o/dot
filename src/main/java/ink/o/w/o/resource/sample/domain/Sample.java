package ink.o.w.o.resource.sample.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Sample
 *
 * @author symbols@dingtalk.com
 * @version 1.0
 * @date 2019/8/8 下午6:02
 */
@Entity
@Table(name = "t_sample")

@Data
@NoArgsConstructor
public class Sample {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private String payload;
}
