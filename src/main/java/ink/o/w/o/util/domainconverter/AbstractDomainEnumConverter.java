package ink.o.w.o.util.domainconverter;

import lombok.extern.slf4j.Slf4j;

import javax.persistence.AttributeConverter;
import javax.persistence.Convert;

/**
 * @author symbols@dingtalk.com
 */
@Slf4j
@Convert
public abstract class AbstractDomainEnumConverter<S extends AbstractDomainEnum<S, T>, T> implements AttributeConverter<S, T> {

  @Override
  public T convertToDatabaseColumn(S attribute) {
    logger.info("S -> {}", attribute);
    return attribute.getValue();
  }

  @Override
  public S convertToEntityAttribute(T dbData) {
    return S.of(dbData);
  }
}
