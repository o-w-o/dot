package ink.o.w.o.resource.core.symbols.service.handler.ext;

import com.fasterxml.jackson.core.JsonProcessingException;
import ink.o.w.o.resource.core.symbols.domain.SymbolsType;
import ink.o.w.o.resource.core.symbols.domain.Symbols;
import ink.o.w.o.resource.core.symbols.domain.SymbolsSpace;
import ink.o.w.o.resource.core.symbols.domain.ext.DocumentSymbols;
import ink.o.w.o.resource.core.symbols.repository.DocumentSymbolsRepository;
import ink.o.w.o.resource.core.symbols.repository.SymbolsRepository;
import ink.o.w.o.resource.core.symbols.service.handler.SymbolsTypeSelector;
import ink.o.w.o.server.io.service.ServiceResult;
import ink.o.w.o.server.io.service.ServiceResultFactory;
import ink.o.w.o.server.io.service.ServiceException;
import ink.o.w.o.util.JsonHelper;
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
    return ServiceResultFactory.success(symbols);
  }

  @Override
  public ServiceResult<SymbolsSpace> fetchSpace(String symbolsSpaceId, SymbolsType symbolsType) {
    return ServiceResultFactory.success(
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
      return ServiceResultFactory.success(spaceMountedSymbols);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
      return ServiceResultFactory.error(e.getMessage());
    }
  }
}
