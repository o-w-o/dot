package ink.o.w.o.api;

import ink.o.w.o.server.domain.ResponseEntityFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.EntityLinks;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * API 入口
 *
 * @author symbols@dingtalk.com
 * @date 2020/02/14 11:17
 * @since 1.0.0
 */
@Slf4j
@ExposesResourceFor(API.class)
@RestController
@RequestMapping
public class API {
  private final EntityLinks entityLinks;

  @Autowired
  API(EntityLinks entityLinks) {
    this.entityLinks = entityLinks;
  }

  @GetMapping
  public ResponseEntity<?> index() {
    return ResponseEntityFactory.ok(
        new EntityModel<>(
            new Object(),
            entityLinks.linkFor(API.class).withSelfRel(),
            entityLinks.linkFor(DocAPI.class).withRel("doc"),
            entityLinks.linkFor(AuthorizationAPI.class).withRel("authorization"),
            entityLinks.linkFor(UserAPI.class).withRel("user")
        )
    );
  }
}
