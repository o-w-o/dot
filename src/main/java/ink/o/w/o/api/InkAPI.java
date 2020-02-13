package ink.o.w.o.api;

import ink.o.w.o.resource.ink.constant.InkType;
import ink.o.w.o.resource.ink.domain.ex.ArticleInk;
import ink.o.w.o.resource.ink.service.InkService;
import ink.o.w.o.server.constant.HttpConstant;
import ink.o.w.o.server.domain.HttpResponseData;
import ink.o.w.o.server.domain.HttpResponseDataFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@ExposesResourceFor(UserAPI.class)
@RequestMapping(HttpConstant.API_ENTRY + "/inks")
public class InkAPI {
  private final InkService inkService;


  @Autowired
  InkAPI(InkService inkService) {
    this.inkService = inkService;
  }

  @GetMapping
  HttpResponseData test() {
    return HttpResponseDataFactory.success(
        inkService.test(new ArticleInk().setId(12L).setType(InkType.ARTICLE))
    );
  }
}
