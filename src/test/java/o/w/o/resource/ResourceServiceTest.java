package o.w.o.resource;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;

@Slf4j
@AutoConfigureCache
@AutoConfigureDataJpa
public class ResourceServiceTest extends ResourceTest {
}
