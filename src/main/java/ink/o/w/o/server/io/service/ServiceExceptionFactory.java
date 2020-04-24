package ink.o.w.o.server.io.service;

public class ServiceExceptionFactory {
  public static ServiceException of(String message) {
    return new ServiceException(message);
  }
}
