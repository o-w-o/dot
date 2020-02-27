package ink.o.w.o.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import ink.o.w.o.resource.ink.domain.InkBasic;
import ink.o.w.o.resource.ink.service.InkService;
import ink.o.w.o.server.domain.ResponseEntityFactory;
import ink.o.w.o.util.JSONHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.EntityLinks;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;

@Slf4j
@RestController
@ExposesResourceFor(InkAPI.class)
@RequestMapping("inks")
@Transactional
public class InkAPI {
  private final InkService inkService;
  private final EntityLinks entityLinks;
  private final JSONHelper jsonHelper;

  @Autowired
  public InkAPI(EntityLinks entityLinks, InkService inkService, JSONHelper jsonHelper) {
    this.entityLinks = entityLinks;
    this.inkService = inkService;
    this.jsonHelper = jsonHelper;
  }

  @PostMapping
  public ResponseEntity<?> create(@RequestBody InkBasic ink) {
    var createdInk = inkService.create(ink).guard();
    return ResponseEntityFactory.ok(
        new EntityModel<>(
            createdInk,
            entityLinks.linkFor(InkAPI.class).slash(createdInk.getId()).withSelfRel()
        )
    );
  }

  @GetMapping("/{id}")
  public EntityModel<InkBasic> fetch(@PathVariable String id) throws JsonProcessingException {
    var ink = inkService.fetch(id).guard();
    var hal = new EntityModel<>(
        ink,
        entityLinks.linkFor(InkAPI.class).slash(ink.getId()).withSelfRel()
    );;

    return hal;
  }
}
