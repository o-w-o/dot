package ink.o.w.o.resource.dot.domain;

import java.util.Map;

public interface Dotable<T extends DotBasic> {

  Map<String, Object> dehydrateEx();

  default DotBasic dehydrate() {
    var ink = (T) this;

    return new DotBasic();
  }

  default DotBasic dehydrate(T ink) {
    return new DotBasic();
  }

  T hydrateEx(DotBasic inkBasic);

  default T hydrate(DotBasic dotBasic) {
    var that = (T) this;

    that
        .setId(dotBasic.getId());

    return hydrateEx(dotBasic);
  }


}
