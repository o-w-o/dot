package ink.o.w.o.api;

import ink.o.w.o.resource.core.dot.domain.Dot;
import ink.o.w.o.resource.core.dot.service.DotService;
import ink.o.w.o.server.io.api.ResponseEntityFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.EntityLinks;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@ExposesResourceFor(DotAPI.class)
@RequestMapping("dots")
public class DotAPI {
  private final DotService dotService;
  private final EntityLinks entityLinks;

  @Autowired
  public DotAPI(EntityLinks entityLinks, DotService dotService) {
    this.entityLinks = entityLinks;
    this.dotService = dotService;
  }

  @PostMapping
  public ResponseEntity<?> create(@RequestBody @Valid Dot dot) {
    var createdDot = dotService.create(dot).guard();
    return ResponseEntityFactory.ok(
        new EntityModel<>(
            createdDot,
            entityLinks.linkFor(DotAPI.class).slash(createdDot.getId()).withSelfRel()
        )
    );
  }

  @GetMapping("/{id}")
  public EntityModel<Dot> retrieve(@PathVariable String id) {
    var dot = dotService.retrieve(id).guard();
    var hal = new EntityModel<>(
        dot,
        entityLinks.linkFor(DotAPI.class).slash(dot.getId()).withSelfRel()
    );

    return hal;
  }
}
