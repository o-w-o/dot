package ink.o.w.o.util;

import lombok.Getter;
import lombok.Setter;

import java.util.function.Supplier;

public class HelperException extends RuntimeException implements Supplier<HelperException> {
  @Getter
  @Setter
  private String helperName;

  public HelperException(String message) {
    super(message);
  }

  public HelperException(String message, Throwable throwable) {
    super(message, throwable);
  }

  public HelperException(String message, String helperName) {
    super(message);
    this.helperName = helperName;
  }

  @Override
  public HelperException get() {
    return this;
  }
}
