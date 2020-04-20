package ink.o.w.o.resource.core.symbols.service.impl;

import ink.o.w.o.resource.core.symbols.domain.Symbols;
import ink.o.w.o.resource.core.symbols.repository.SymbolsRepository;
import ink.o.w.o.resource.core.symbols.service.SymbolsService;
import ink.o.w.o.resource.core.symbols.service.handler.SymbolsHandlerHolder;
import ink.o.w.o.server.io.service.ServiceResult;
import ink.o.w.o.server.io.service.ServiceResultFactory;
import ink.o.w.o.server.io.service.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicReference;

/**
 * InkServiceImpl
 *
 * @author symbols@dingtalk.com
 * @date 2020/02/13 12:46
 * @since 1.0.0
 */
@Slf4j
@Service
public class SymbolsServiceImpl implements SymbolsService {
  private final SymbolsHandlerHolder symbolsHandlerHolder;
  private final SymbolsRepository symbolsRepository;

  @Autowired
  SymbolsServiceImpl(SymbolsHandlerHolder symbolsHandlerHolder, SymbolsRepository symbolsRepository) {
    this.symbolsHandlerHolder = symbolsHandlerHolder;
    this.symbolsRepository = symbolsRepository;
  }

  @Override
  public String test(Symbols symbols) {
    logger.info("处理 SYMBOLS 开始：{}", symbols.getType());
    var status = new AtomicReference<>(false);
    var result = new AtomicReference<>("");

    symbolsHandlerHolder.select(symbols.getType()).ifPresent(handler -> {
      result.set(handler.handle(symbols));
      status.set(true);
    });

    logger.info("处理 SYMBOLS 结束：{}", status.get());
    return result.get();
  }

  @Override
  public ServiceResult<Symbols> fetch(String symbolsId) {
    logger.info("处理 SYMBOLS 开始：{}", symbolsId);

    var symbols = symbolsRepository.findById(symbolsId).orElseThrow(() -> new ServiceException(String.format("Symbols id[ %s ] 不存在！", symbolsId)));
    var status = new AtomicReference<>(false);
    var result = new AtomicReference<Symbols>(null);

    symbolsHandlerHolder.select(symbols.getType()).ifPresent(handler -> {
      result.set(handler.fetch(symbols.getId(), symbols.getType()).guard());
      status.set(true);
    });

    logger.info("处理 SYMBOLS 结束：{}", status.get());
    return ServiceResultFactory.success(result.get());
  }


  @Override
  public ServiceResult<Symbols> create(Symbols symbols) {
    logger.info("处理 SYMBOLS 开始： type -> [ {} ] [ {} ]", symbols.getType(), symbols);

    var result = new AtomicReference<ServiceResult<Symbols>>(null);

    symbolsHandlerHolder.select(symbols.getType()).ifPresent(handler -> {
      result.set(handler.create(symbols));
    });

    var spaceMountedInk = result.get().guard();

    symbolsRepository.save(spaceMountedInk);

    logger.info("处理 SYMBOLS 结束： success -> [ {} ]", result.get().getSuccess());
    return ServiceResultFactory.success(spaceMountedInk);
  }
}
