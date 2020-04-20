package ink.o.w.o.api;

import ink.o.w.o.resource.core.symbols.domain.Symbols;
import ink.o.w.o.resource.core.symbols.service.SymbolsService;
import ink.o.w.o.server.io.api.ResponseEntityFactory;
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
@ExposesResourceFor(SymbolsAPI.class)
@RequestMapping("symbols")
@Transactional
public class SymbolsAPI {
  private final SymbolsService symbolsService;
  private final EntityLinks entityLinks;

  @Autowired
  public SymbolsAPI(EntityLinks entityLinks, SymbolsService symbolsService) {
    this.entityLinks = entityLinks;
    this.symbolsService = symbolsService;
  }

  @PostMapping
  public ResponseEntity<?> create(@RequestBody Symbols symbols) {
    var createdInk = symbolsService.create(symbols).guard();
    return ResponseEntityFactory.ok(
        new EntityModel<>(
            createdInk,
            entityLinks.linkFor(SymbolsAPI.class).slash(createdInk.getId()).withSelfRel()
        )
    );
  }

  @GetMapping("/{id}")
  public EntityModel<Symbols> fetch(@PathVariable String id) {
    var ink = symbolsService.fetch(id).guard();
    var hal = new EntityModel<>(
        ink,
        entityLinks.linkFor(SymbolsAPI.class).slash(ink.getId()).withSelfRel()
    );

    return hal;
  }
}
