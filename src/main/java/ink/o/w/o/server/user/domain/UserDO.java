package ink.o.w.o.server.user.domain;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author symbols@dingtalk.com
 */
@Entity
@Table(name = "t_user")

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class UserDO implements Serializable {

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

    public Boolean isExist() {
        if (name != null) {
            return true;
        } else {
            return false;
        }
    }

    public static List<String> getRoles(UserDO u) {
        List<String> list = new ArrayList<>();
        if (u.roles == null) {
            list.add("USER");
        } else if (u.roles.indexOf(":") > 0) {
            list.addAll(Arrays.asList(u.roles.split(":")));
        } else {
            list.add(u.roles);
        }

        return list;
    }
}
