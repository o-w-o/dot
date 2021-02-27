package o.w.o.infrastructure.helper;

import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class ResourceLoaderHelper {

  @Resource
  ResourceLoader resourceLoader;
}
