/*
 * Copyright (C) 2017 Pivotal Software, Inc.
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
