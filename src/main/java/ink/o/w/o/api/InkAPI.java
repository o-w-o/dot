package ink.o.w.o.api;

import ink.o.w.o.resource.ink.constant.InkType;
import ink.o.w.o.resource.ink.domain.ex.ArticleInk;
import ink.o.w.o.resource.ink.service.InkService;
import ink.o.w.o.server.domain.ResponseEntityBody;
import ink.o.w.o.server.domain.ResponseEntityFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.EntityLinks;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@ExposesResourceFor(UserAPI.class)
@RequestMapping("inks")
public class InkAPI {
  private final InkService inkService;
  private final EntityLinks entityLinks;

  @Autowired
  public InkAPI(EntityLinks entityLinks, InkService inkService) {
    this.entityLinks = entityLinks;
    this.inkService = inkService;
  }

  @GetMapping
  public ResponseEntity<ResponseEntityBody<String>> test() {
    return ResponseEntityFactory.success(
        inkService.test(new ArticleInk().setId(12L).setType(InkType.ARTICLE))
    );
  }
}
