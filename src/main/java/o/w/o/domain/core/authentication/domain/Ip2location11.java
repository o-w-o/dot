package o.w.o.domain.core.authentication.domain;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Data
@Accessors(chain = true)
@Table(name = "t_ip2location_11")
@IdClass(Ip2location11.class)
public class Ip2location11 implements Serializable {
  private static final long serialVersionUID = -4552165082638537519L;

  @javax.persistence.Id
  @Column(name = "ip_from", nullable = false)
  private Long ipFrom;

  @javax.persistence.Id
  @Column(name = "ip_to", nullable = false)
  private Long ipTo;

  @Column(name = "country_code", nullable = false, length = 4)
  private String countryCode;

  @Column(name = "country_name", nullable = false)
  private String countryName;

  @Column(name = "region_name", nullable = false)
  private String regionName;

  @Column(name = "city_name", nullable = false)
  private String cityName;

  @Column(name = "latitude", nullable = false)
  private Float latitude;

  @Column(name = "longitude", nullable = false)
  private Float longitude;

  @Column(name = "zip_code", nullable = false)
  private String zipCode;

  @Column(name = "time_zone", nullable = false)
  private String timeZone;
}
