package ink.o.w.o.resource.ink.service.handler.ext;

import com.fasterxml.jackson.core.JsonProcessingException;
import ink.o.w.o.resource.ink.constant.InkType;
import ink.o.w.o.resource.ink.domain.Ink;
import ink.o.w.o.resource.ink.domain.InkSpace;
import ink.o.w.o.resource.ink.domain.ext.ArticleInk;
import ink.o.w.o.resource.ink.repository.ArticleInkRepoitory;
import ink.o.w.o.resource.ink.repository.InkRepository;
import ink.o.w.o.resource.ink.service.handler.InkTypeSelector;
import ink.o.w.o.server.domain.ServiceResult;
import ink.o.w.o.server.domain.ServiceResultFactory;
import ink.o.w.o.server.exception.ServiceException;
import ink.o.w.o.util.JSONHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
@InkTypeSelector(value = InkType.ARTICLE)
public class ArticleInkHandler extends AbstractInkHandler {
  @Resource
  private JSONHelper jsonHelper;

  @Resource
  private ArticleInkRepoitory articleInkRepoitory;

  @Resource
  private InkRepository inkRepository;

  @Override
  public String handle(Ink ink) {
    ArticleInk articleInk = (ArticleInk) ink.getSpace();
    try {
      return jsonHelper.toJSONString(articleInk);
    } catch (JsonProcessingException e) {
      return "{}";
    }
  }

  @Override
  public ServiceResult<Ink> fetch(String inkId, InkType inkType) {
    var ink = inkRepository
        .findById(inkId)
        .orElseThrow(
            () -> new ServiceException(String.format("未找到 Ink -> id[ %s ], type[ %s ]", inkId, inkType))
        );
    ink.setSpace(fetchSpace(ink.getSpaceId(), inkType).guard());
    return ServiceResultFactory.success(ink);
  }

  @Override
  public ServiceResult<InkSpace> fetchSpace(String inkId, InkType inkType) {
    return ServiceResultFactory.success(
        articleInkRepoitory.findById(inkId).orElseThrow(
            () -> new ServiceException(String.format("未找到 Ink -> id[ %s ], type[ %s ]", inkId, inkType))
        )
    );
  }

  @Override
  public ServiceResult<Ink> create(Ink ink) {
    var createdInkSpace = articleInkRepoitory.save((ArticleInk) ink.getSpace());
    try {
      var spaceMountedInk = ink
          .setSpace(createdInkSpace)
          .setSpaceId(createdInkSpace.getId())
          .setSpaceContent(jsonHelper.toJSONString(createdInkSpace));
      logger.info("spaceMountedInk -> [{}]", spaceMountedInk);
      return ServiceResultFactory.success(spaceMountedInk);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
      return ServiceResultFactory.error(e.getMessage());
    }
  }
}
