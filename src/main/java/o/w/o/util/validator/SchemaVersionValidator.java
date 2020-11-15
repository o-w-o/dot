package o.w.o.util.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraints.NotEmpty;

public class SchemaVersionValidator implements ConstraintValidator<SchemaVersion, String> {
  private SchemaVersion schemaVersion;

  @Override
  public void initialize(SchemaVersion constraintAnnotation) {
    this.schemaVersion = constraintAnnotation;
  }

  @Override
  public boolean isValid(@NotEmpty String value, ConstraintValidatorContext context) {
    return value.matches(this.schemaVersion.regexp());
  }
}
