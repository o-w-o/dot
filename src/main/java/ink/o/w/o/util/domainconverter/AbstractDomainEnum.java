package ink.o.w.o.util.domainconverter;

public abstract class AbstractDomainEnum<S, T> {
  protected final T value;

  protected AbstractDomainEnum(T value) {
    this.value = value;
  }

  public static <T, S> S of(T t) {
    return null;
  }

  public T getValue() {
    return this.value;
  }
}
