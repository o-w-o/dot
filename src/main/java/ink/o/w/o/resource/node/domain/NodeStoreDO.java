/*
 * Copyright (C) 2017 河南大学
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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
