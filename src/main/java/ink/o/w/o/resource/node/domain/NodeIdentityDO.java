package ink.o.w.o.resource.node.domain;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author dot
 */
@Entity
@Table(name = "t_node_identity")

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class NodeIdentityDO implements Serializable {
    @Id
    private Long urn;

    @Column(nullable = false, length = 720)
    private String url;

    @Column(nullable = false)
    private Long location;

    @Column(nullable = false, length = 81)
    private String coordinate;

    @Column(nullable = false, length = 81)
    private String node;

    @Column(nullable = false)
    private String config;

    private Boolean status = true;

    public NodeIdentityDO(Long urn, String url, String node, String config) {
        this.urn = urn;
        this.url = url;
        this.node = node;
        this.config = config;
    }
}
