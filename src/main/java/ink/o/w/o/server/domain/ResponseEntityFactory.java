package ink.o.w.o.server.domain;

import org.springframework.data.domain.Page;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * 响应结果生成工厂类
 *
 * @author symbols@dingtalk.com
 * @version 1.0
 * @date 2019/8/4 上午10:16
 */
public class ResponseEntityFactory {
  public static final Object DEFAULT_EMPTY_DATA = new Object();

  public static <T extends EntityModel<T>> ResponseEntity<?> ok(T data) {
    return ResponseEntity.ok().body(new EntityModel<>(data));
  }

  public static <T extends RepresentationModel<T>> ResponseEntity<?> ok(T data) {
    return ResponseEntity.ok().body(
        new EntityModel<>(
            data
        )
    );
  }

  public static <T extends RepresentationModel<T>> ResponseEntity<?> ok(Page<T> data) {
    return ResponseEntity.ok().body(
        new PagedModel<>(
            data.getContent(),
            new PagedModel.PageMetadata(
                data.getSize(), data.getNumber(), data.getTotalElements(), data.getTotalPages()
            ))
    );
  }

  public static <T extends CollectionModel<T>> ResponseEntity<?> ok(T data) {
    return ResponseEntity.ok().body(new CollectionModel<>(data));
  }

  public static <T extends PagedModel<T>> ResponseEntity<?> ok(T data) {
    return ResponseEntity.ok().body(new PagedModel<>(data.getContent(), data.getMetadata(), data.getLinks()));
  }

  public static ResponseEntity<ResponseEntityBody<Object>> success() {
    return ResponseEntityFactory.success(DEFAULT_EMPTY_DATA, "OK");
  }

  public static <T> ResponseEntity<ResponseEntityBody<T>> success(T data) {
    return ResponseEntityFactory.success(data, "OK");
  }

  public static <T> ResponseEntity<ResponseEntityBody<T>> success(T data, String message) {
    return ResponseEntity.ok(
        new ResponseEntityBody<T>()
            .setMessage(message)
            .setPayload(data)
    );
  }

  public static <T> ResponseEntity<?> error(String message) {
    return ResponseEntityFactory.error(message, HttpStatus.BAD_REQUEST);
  }

  public static <T> ResponseEntity<?> error(String message, Integer status) {
    return ResponseEntity
        .badRequest()
        .body(
            new EntityModel<ResponseEntityExceptionBody<T>>(ResponseEntityExceptionBody.error(message, status))
        );
  }

  public static <T> ResponseEntity<?> error(String message, HttpStatus status) {
    return ResponseEntity
        .badRequest()
        .body(
            new EntityModel<ResponseEntityExceptionBody<T>>(ResponseEntityExceptionBody.error(message, status))
        );
  }

  public static <T> ResponseEntity<?> unauthorized() {
    return ResponseEntity
        .status(HttpStatus.UNAUTHORIZED)
        .body(
            new EntityModel<ResponseEntityExceptionBody<T>>(
                ResponseEntityExceptionBody.unauthorized()
            )
        );
  }

  public static <T> ResponseEntity<?> forbidden() {
    return ResponseEntity
        .status(HttpStatus.FORBIDDEN)
        .body(
            new EntityModel<ResponseEntityExceptionBody<T>>(
                ResponseEntityExceptionBody.forbidden()
            )
        );
  }

  public static ResponseEntity<?> generateFrom(ServiceResult<?> result, HttpStatus status) {
    switch (status) {
      case OK: {
        return success(result.getPayload());
      }
      case FORBIDDEN: {
        return forbidden();
      }
      case UNAUTHORIZED: {
        return unauthorized();
      }
      default: {
        return ResponseEntity
            .status(status)
            .body(
                new EntityModel<ResponseEntityExceptionBody<?>>(
                    ResponseEntityExceptionBody.error(result.getMessage(), status)
                )
            );
      }
    }
  }

  public static ResponseEntity<?> generateFrom(ServiceResult<?> result) {
    try {
      return ResponseEntityFactory.generateFrom(
          result,
          HttpStatus.valueOf(result.getCode())
      );
    } catch (IllegalArgumentException e) {
      return ResponseEntity
          .status(HttpStatus.BAD_REQUEST)
          .body(
              new EntityModel<ResponseEntityExceptionBody<?>>(
                  ResponseEntityExceptionBody.error(result.getMessage(), result.getCode())
              )
          );
    }
  }


  public static ResponseEntity<?> generateFrom(ResponseEntityExceptionBody<?> body, HttpStatus status) {
    return ResponseEntity
        .status(status)
        .body(
            new EntityModel<ResponseEntityExceptionBody<?>>(
                body
            )
        );
  }

  public static ResponseEntity<?> generateFrom(ResponseEntityExceptionBody<?> body) {
    try {
      return ResponseEntityFactory.generateFrom(
          body,
          HttpStatus.valueOf(body.getCode())
      );
    } catch (IllegalArgumentException e) {
      return ResponseEntity
          .status(body.getCode())
          .body(
              body
          );
    }
  }
}
