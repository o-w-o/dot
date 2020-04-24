package ink.o.w.o.server.def;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.HibernateException;
import org.hibernate.boot.registry.classloading.internal.ClassLoaderServiceImpl;
import org.hibernate.boot.registry.classloading.spi.ClassLoaderService;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.type.SerializationException;
import org.hibernate.usertype.ParameterizedType;
import org.hibernate.usertype.UserType;
import org.postgresql.util.PGobject;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Properties;


public class JsonbType implements UserType, ParameterizedType {

  public static final String CLASS = "CLASS";
  private static final ClassLoaderService classLoaderService = new ClassLoaderServiceImpl();
  private final ObjectMapper mapper = new ObjectMapper();
  private Class<?> jsonClassType;

  @Override
  public void nullSafeSet(PreparedStatement st, Object value, int index, SharedSessionContractImplementor session) throws HibernateException, SQLException {
    if (value == null) {
      st.setNull(index, Types.OTHER);
    } else {
      try {
        st.setObject(index, mapper.writeValueAsString(value), Types.OTHER);
      } catch (IOException e) {
        e.printStackTrace();
      }

    }
  }

  @Override
  public Object deepCopy(Object originalValue) throws HibernateException {
    if (originalValue != null) {
      try {
        return mapper.readValue(
            mapper.writeValueAsString(originalValue),
            originalValue.getClass()
        );
      } catch (IOException e) {
        throw new HibernateException("Failed to deep copy object", e);
      }
    }
    return null;
  }

  @Override
  public Object nullSafeGet(ResultSet rs, String[] names, SharedSessionContractImplementor session, Object owner) throws HibernateException, SQLException {
    PGobject o = (PGobject) rs.getObject(names[0]);
    if (o.getValue() != null) {
      try {
        return mapper.readValue(o.getValue(), jsonClassType);
      } catch (JsonProcessingException e) {
        e.printStackTrace();
      }
    }
    return new Object();
  }

  @Override
  public Serializable disassemble(Object value) throws HibernateException {
    Object copy = deepCopy(value);

    if (copy instanceof Serializable) {
      return (Serializable) copy;
    }

    throw new SerializationException(String.format("Cannot serialize '%s', %s is not Serializable.", value, value.getClass()), null);
  }

  @Override
  public Object assemble(Serializable cached, Object owner) throws HibernateException {
    return deepCopy(cached);
  }

  @Override
  public Object replace(Object original, Object target, Object owner) throws HibernateException {
    return deepCopy(original);
  }

  @Override
  public boolean isMutable() {
    return true;
  }

  @Override
  public int hashCode(Object x) throws HibernateException {
    if (x == null) {
      return 0;
    }

    return x.hashCode();
  }

  @Override
  public boolean equals(Object x, Object y) throws HibernateException {
    return ObjectUtils.nullSafeEquals(x, y);
  }

  @Override
  public Class<?> returnedClass() {
    return jsonClassType;
  }

  @Override
  public int[] sqlTypes() {
    return new int[]{Types.JAVA_OBJECT};
  }

  @Override
  public void setParameterValues(Properties properties) {
    final String clazz = (String) properties.get(CLASS);
    if (clazz != null) {
      jsonClassType = classLoaderService.classForName(clazz);
    }
  }
}
