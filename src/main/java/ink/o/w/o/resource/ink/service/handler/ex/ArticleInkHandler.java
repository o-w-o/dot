package ink.o.w.o.resource.ink.service.handler.ex;

import com.fasterxml.jackson.core.JsonProcessingException;
import ink.o.w.o.resource.ink.constant.InkType;
import ink.o.w.o.resource.ink.domain.ex.ArticleInk;
import ink.o.w.o.resource.ink.domain.ex.InkBasic;
import ink.o.w.o.resource.ink.repository.ArticleInkRepoitory;
import ink.o.w.o.resource.ink.service.handler.InkTypeSelector;
import ink.o.w.o.server.domain.ServiceResult;
import ink.o.w.o.server.domain.ServiceResultFactory;
import ink.o.w.o.server.exception.ServiceException;
import ink.o.w.o.util.JSONHelper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@InkTypeSelector(value = InkType.ARTICLE)
public class ArticleInkHandler extends AbstractInkHandler {
  @Resource
  private JSONHelper jsonHelper;

  @Resource
  private ArticleInkRepoitory articleInkRepoitory;

  @Override
  public String handle(InkBasic ink) {
    ArticleInk articleInk = (ArticleInk) ink;
    try {
      return jsonHelper.toJSONString(articleInk);
    } catch (JsonProcessingException e) {
      return "{}";
    }
  }

  @Override
  public ServiceResult<InkBasic> fetch(String inkId, InkType inkType) {
    return ServiceResultFactory.success(
        articleInkRepoitory.findById(inkId).orElseThrow(
            () -> new ServiceException(String.format("未找到 Ink -> id[ %s ], type[ %s ]", inkId, inkType))
        )
            .dehydrate()
    );
  }

  @Override
  public ServiceResult<InkBasic> create(InkBasic ink) {
    return ServiceResultFactory.success(articleInkRepoitory.saveAndFlush(new ArticleInk(ink)));
  }
}
