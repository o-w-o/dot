package ink.o.w.o.api;

import ink.o.w.o.resource.ink.domain.ex.InkBasic;
import ink.o.w.o.resource.ink.service.InkService;
import ink.o.w.o.server.domain.ResponseEntityFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.EntityLinks;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@ExposesResourceFor(InkAPI.class)
@RequestMapping("inks")
public class InkAPI {
  private final InkService inkService;
  private final EntityLinks entityLinks;

  @Autowired
  public InkAPI(EntityLinks entityLinks, InkService inkService) {
    this.entityLinks = entityLinks;
    this.inkService = inkService;
  }

  @PostMapping
  public ResponseEntity<?> create(@RequestBody InkBasic ink) {
    var createdInk = inkService.create(ink).guard();
    return ResponseEntityFactory.ok(
        new EntityModel<>(
            createdInk,
            entityLinks.linkFor(InkAPI.class, createdInk.getId()).withSelfRel()
        )
    );
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> fetch(@PathVariable String id) {
    var ink = inkService.fetch(id).guard();

    return ResponseEntityFactory.ok(
        new EntityModel<>(
            ink,
            entityLinks.linkFor(InkAPI.class).slash(id).withSelfRel()
        )
    );
  }
}
