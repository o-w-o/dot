package ink.o.w.o.resource.user.domain;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.*;

import javax.persistence.*;
import java.util.Date;


/**
 * @author symbols@dingtalk.com
 */
@Entity
@Table(name = "t_user")

@Data
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String name;

    private String password;
    private String roles;

    private String nickName;
    private Integer sex = 0;

    @Temporal(TemporalType.DATE)
    private Date registerDate;
}
