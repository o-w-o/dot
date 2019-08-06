package ink.o.w.o.resource.node.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author LongY
 */
@Entity
@Table(name = "t_node_store")

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NodeStoreDO implements Serializable {

    @Id
    private Long uuid;

    @Column(nullable = false)
    private Long refUuid;

    @Column(nullable = false, length = 49)
    private String name;

    @Column(nullable = false)
    private Long size;

    @Column(nullable = false, length = 36)
    private String ext;

    @Column(nullable = true, length = 108)
    private String dsc;

    @Column(nullable = true, length = 64)
    private String dir;

    @Column(nullable = false)
    private Boolean share = false;

    @Temporal(javax.persistence.TemporalType.DATE)
    @Column(nullable = false)
    private Date uploadDate;

    @Temporal(javax.persistence.TemporalType.DATE)
    @Column(nullable = false)
    private Date modifyDate;

}
