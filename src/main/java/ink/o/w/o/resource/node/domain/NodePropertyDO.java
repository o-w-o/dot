package ink.o.w.o.resource.node.domain;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 *
 * @author dot
 */
@Entity
@Table(name = "t_node_property")

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class NodePropertyDO implements Serializable {

    @Id
    private Long uuid;

    @Column(nullable = false)
    @JoinColumn(foreignKey = @ForeignKey(name = "urn"), table = "page_identity")
    private Long refUuid;

    @Column(nullable = false, length = 81)
    private String title;

    @Column(nullable = false, length = 36)
    private String author = "system";

    @Column(nullable = false, length = 120)
    private String summary = "";

    @Column(nullable = false, length = 5000)
    private String content = "";

    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date modifyDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date start;

    @Temporal(TemporalType.TIMESTAMP)
    private Date end;

    @Column(length = 81)
    private String category;
    @Column(length = 81)
    private String tag;

    private Integer pageViews;

    private Boolean status = true;

}
