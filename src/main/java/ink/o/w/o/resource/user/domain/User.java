package ink.o.w.o.resource.user.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.Optional;


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

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    private String roles;

    private String nickName;
    private Integer sex = 0;

    @Temporal(TemporalType.DATE)
    private Date cTime;

    @Temporal(TemporalType.DATE)
    private Date uTime;

    @PrePersist
    public void recordUpdateTime(){
        if(Optional.ofNullable(cTime).isEmpty()) {
            cTime = new Date();
        }
        uTime = new Date();
    }
}
