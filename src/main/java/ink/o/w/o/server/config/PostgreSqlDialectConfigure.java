package ink.o.w.o.server.config;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.dialect.PostgreSQL10Dialect;

import java.sql.Types;

@Slf4j
public class PostgreSqlDialectConfigure extends PostgreSQL10Dialect {
  public PostgreSqlDialectConfigure() {
    super();
    registerColumnType(Types.JAVA_OBJECT, "jsonb");
    logger.info("PostgreSqlDialectConfigure init ……");
  }

}
