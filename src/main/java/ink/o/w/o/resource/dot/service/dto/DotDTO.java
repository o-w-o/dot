package ink.o.w.o.resource.dot.service.dto;

import ink.o.w.o.resource.dot.domain.DotBasic;
import ink.o.w.o.resource.dot.domain.Dotable;

import java.util.Map;

public class DotDTO<T extends DotBasic> extends DotBasic implements Dotable<T> {
  @Override
  public Map<String, Object> dehydrateEx() {
    return null;
  }

  @Override
  public T hydrateEx(DotBasic inkBasic) {
    return null;
  }
}
