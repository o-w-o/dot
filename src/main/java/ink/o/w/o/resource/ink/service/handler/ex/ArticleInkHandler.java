package ink.o.w.o.resource.ink.service.handler.ex;

import com.fasterxml.jackson.core.JsonProcessingException;
import ink.o.w.o.resource.ink.constant.InkType;
import ink.o.w.o.resource.ink.domain.ex.AbstractInk;
import ink.o.w.o.resource.ink.domain.ex.ArticleInk;
import ink.o.w.o.resource.ink.service.handler.InkTypeSelector;
import ink.o.w.o.util.JSONHelper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@InkTypeSelector(value = InkType.ARTICLE)
public class ArticleInkHandler extends AbstractInkHandler {
  @Resource
  JSONHelper jsonHelper;

  @Override
  public String handle(AbstractInk ink) {
    ArticleInk articleInk = (ArticleInk) ink;
    try {
      return jsonHelper.toJSONString(articleInk);
    } catch (JsonProcessingException e) {
      return "{}";
    }
  }
}
