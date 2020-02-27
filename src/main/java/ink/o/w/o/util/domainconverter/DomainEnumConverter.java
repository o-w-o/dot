package ink.o.w.o.util.domainconverter;

import javax.persistence.AttributeConverter;
import javax.persistence.Convert;

/**
 * -
 *
 * @author symbols@dingtalk.com
 */
@Convert
public interface DomainEnumConverter<S extends AbstractDomainEnum<S, T>, T> extends AttributeConverter<S, T> {
  S getDefaultEnum();

  @Override
  default T convertToDatabaseColumn(S attribute) {
    return attribute.getValue();
  }

  @Override
  default S convertToEntityAttribute(T dbData) {
    return S.of(dbData, getDefaultEnum());
  }
}
