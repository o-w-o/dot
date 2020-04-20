package ink.o.w.o.server.io.system;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApplicationException extends Exception {
  private String scope;
}
