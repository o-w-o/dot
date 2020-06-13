package o.w.o.util.domainconverter;

public abstract class AbstractDomainEnum<S, T> {
  public final T value;

  public AbstractDomainEnum() {
    super();
    this.value = null;
  }

  public AbstractDomainEnum(T value) {
    super();
    this.value = value;
  }

  public static <T, S extends AbstractDomainEnum<S, T>> S of(T t, S s) {
    return s.locateValue(t);
  }

  public abstract S locateValue(T t);

  public T getValue() {
    return this.value;
  }
}
