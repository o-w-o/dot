package o.w.o.resource.core.org.domain.ext;

import o.w.o.resource.core.org.domain.OrgSpace;
import o.w.o.server.io.jsonschema.JsonSchemaGenerator;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "t_org__set")
public class SetOrg extends OrgSpace {


  public static class Schema {
    @NotBlank(message = "Org:SET [name] 不能为空！")
    public String name;
    public String description;
    public static String generate() {
      return JsonSchemaGenerator.generateJsonSchema(Schema.class);
    }
  }
}
