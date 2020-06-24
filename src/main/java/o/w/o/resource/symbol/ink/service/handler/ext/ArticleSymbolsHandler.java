package o.w.o.resource.symbol.ink.service.handler.ext;

import com.fasterxml.jackson.core.JsonProcessingException;
import o.w.o.resource.symbol.ink.repository.DocumentSymbolsRepository;
import o.w.o.resource.symbol.ink.repository.SymbolsRepository;
import o.w.o.server.io.service.ServiceException;
import o.w.o.server.io.service.ServiceResult;
import o.w.o.resource.symbol.ink.domain.Symbols;
import o.w.o.resource.symbol.ink.domain.SymbolsSpace;
import o.w.o.resource.symbol.ink.domain.SymbolsType;
import o.w.o.resource.symbol.ink.domain.ext.DocumentSymbols;
import o.w.o.resource.symbol.ink.service.handler.SymbolsTypeSelector;
import o.w.o.server.io.json.JsonHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
@SymbolsTypeSelector(value = SymbolsType.SymbolsTypeEnum.DOCUMENT)
public class ArticleSymbolsHandler extends AbstractSymbolsHandler {
  @Resource
  private JsonHelper jsonHelper;

  @Resource
  private DocumentSymbolsRepository documentSymbolsRepository;

  @Resource
  private SymbolsRepository symbolsRepository;

  @Override
  public String handle(Symbols symbols) {
    DocumentSymbols documentSymbols = (DocumentSymbols) symbols.getSpace();
    try {
      return jsonHelper.toJsonString(documentSymbols);
    } catch (JsonProcessingException e) {
      return "{}";
    }
  }

  @Override
  public ServiceResult<Symbols> fetch(String symbolsId, SymbolsType symbolsType) {
    var symbols = symbolsRepository
        .findById(symbolsId)
        .orElseThrow(
            () -> new ServiceException(String.format("未找到 Symbols -> id[ %s ], type[ %s ]", symbolsId, symbolsType))
        );
    symbols.setSpace(fetchSpace(symbols.getSpaceId(), symbolsType).guard());
    return ServiceResult.success(symbols);
  }

  @Override
  public ServiceResult<SymbolsSpace> fetchSpace(String symbolsSpaceId, SymbolsType symbolsType) {
    return ServiceResult.success(
        documentSymbolsRepository.findById(symbolsSpaceId).orElseThrow(
            () -> new ServiceException(String.format("未找到 Symbols -> id[ %s ], type[ %s ]", symbolsSpaceId, symbolsType))
        )
    );
  }

  @Override
  public ServiceResult<Symbols> create(Symbols symbols) {
    var createdSymbolsSpace = documentSymbolsRepository.save((DocumentSymbols) symbols.getSpace());
    try {
      var spaceMountedSymbols = symbols
          .setSpace(createdSymbolsSpace)
          .setSpaceId(createdSymbolsSpace.getId())
          .setSpaceContent(jsonHelper.toJsonString(createdSymbolsSpace));
      logger.info("spaceMountedSymbols -> [{}]", spaceMountedSymbols);
      return ServiceResult.success((Symbols) spaceMountedSymbols);
    } catch (JsonProcessingException e) {
      logger.error("JsonProcessingException", e);
      return ServiceResult.error(e.getMessage());
    }
  }
}
