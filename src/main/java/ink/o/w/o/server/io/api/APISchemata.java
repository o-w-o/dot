package ink.o.w.o.server.io.api;

import ink.o.w.o.server.io.api.annotation.*;
import ink.o.w.o.server.io.jsonschema.JsonSchemaGenerator;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.lang.annotation.Annotation;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

/**
 * APISchema
 *
 * @author symbols@dingtalk.com
 * @date 2020/5/29
 */
@Slf4j
@Data
@Builder
public class APISchemata {
  private Class<?> clazz;
  private String namespace;

  @Singular("schema")
  private Set<APISchema> schemata;


  public static APISchemata of(Class<?> clazz) {
    var apiSchemataBuilder = builder().clazz(clazz);

    AtomicReference<String> namespace = new AtomicReference<>("/");
    Optional.of(clazz.getAnnotation(APIResource.class).path()).ifPresent(v -> {
      namespace.set(v.length == 0 ? "/" : v[0]);
      apiSchemataBuilder.namespace(namespace.get());

      logger.debug("API.NS -> [{}]", namespace.get());
    });


    Stream.of(clazz.getMethods()).filter(
        method -> method.isAnnotationPresent(APIResourceDo.class)
            || method.isAnnotationPresent(APIResourceCreate.class)
            || method.isAnnotationPresent(APIResourceModify.class)
            || method.isAnnotationPresent(APIResourceFetch.class)
            || method.isAnnotationPresent(APIResourceDestroy.class)
    ).forEach(method -> {
      var builder = APISchema.builder().namespace(namespace.get()).clazz(apiSchemataBuilder.clazz).clazzMethod(method.getName());

      Optional.ofNullable(method.getAnnotation(APIResourceDo.class)).ifPresent(api -> {
        logger.debug("API.URI -> [{} {}：{}]", api.method(), api.path(), api.name());

        builder.api(api.path()[0]);
        builder.description(api.name());
        builder.method(api.method()[0]);
      });
      Optional.ofNullable(method.getAnnotation(APIResourceFetch.class)).ifPresent(api -> {
        logger.debug("API.URI -> [Fetch {}：{}]", api.path(), api.name());

        builder.api(api.path()[0]);
        builder.description(api.name());
        builder.method(RequestMethod.GET);
      });
      Optional.ofNullable(method.getAnnotation(APIResourceCreate.class)).ifPresent(api -> {
        logger.debug("API.URI -> [Create {}：{}]", api.path(), api.name());

        builder.api(api.path()[0]);
        builder.description(api.name());
        builder.method(RequestMethod.POST);
      });
      Optional.ofNullable(method.getAnnotation(APIResourceModify.class)).ifPresent(api -> {
        logger.debug("API.URI -> [Modify {}：{}]", api.path(), api.name());

        builder.api(api.path()[0]);
        builder.description(api.name());
        builder.method(RequestMethod.PATCH);
      });
      Optional.ofNullable(method.getAnnotation(APIResourceDestroy.class)).ifPresent(api -> {
        logger.debug("API.URI -> [Destroy {}：{}]", api.path(), api.name());

        builder.api(api.path()[0]);
        builder.description(api.name());
        builder.method(RequestMethod.DELETE);
      });

      for (var parameter : method.getParameters()) {
        logger.debug("parameter, name -> [{}]", parameter.getName());

        for (Annotation annotation : parameter.getAnnotations()) {
          logger.debug("annotation -> [{}]", annotation);

          if (annotation.annotationType() == RequestBody.class) {
            var schema = JsonSchemaGenerator.generateSchema(parameter.getType());
            logger.debug("API.RequestBody[{}], schema -> [{}]", parameter.getType(), schema);

            builder.body(schema);
          }

          if (annotation.annotationType() == PathVariable.class) {
            Optional.of((PathVariable) annotation).ifPresent(ann -> {
              var val = ann.value().length() == 0 ? ann.name() : ann.value();

              builder.pathVariables(
                  new APISchema.APIProperty()
                      .setName(val)
                      .setType(parameter.getType().getSimpleName())
                      .setRequired(ann.required())
              );

              logger.debug("API.PathVariable[{}], name -> [{}]", parameter.getType(), val);
            });
          }

          if (annotation.annotationType() == RequestParam.class) {
            Optional.of((RequestParam) annotation).ifPresent(ann -> {
              var val = ann.value().length() == 0 ? ann.name() : ann.value();

              builder.parameters(
                  new APISchema.APIProperty()
                      .setName(val)
                      .setType(parameter.getType().getSimpleName())
                      .setRequired(ann.required())
              );

              logger.debug("API.RequestParam[{}], name -> [{}]", parameter.getType(), val);
            });
          }

          if (annotation.annotationType() == RequestPart.class) {
            Optional.of((RequestPart) annotation).ifPresent(ann -> {
              var val = ann.value().length() == 0 ? ann.name() : ann.value();

              builder.parameters(
                  new APISchema.APIProperty()
                      .setName(val)
                      .setType(parameter.getType().getSimpleName())
                      .setRequired(ann.required())
              );

              logger.debug("API.RequestPart[{}], name -> [{}]", parameter.getType(), val);
            });
          }

          if (annotation.annotationType() == RequestHeader.class) {
            Optional.of((RequestHeader) annotation).ifPresent(ann -> {
              var val = ann.value().length() == 0 ? ann.name() : ann.value();

              builder.headers(
                  new APISchema.APIProperty()
                      .setName(val)
                      .setType(parameter.getType().getSimpleName())
                      .setRequired(ann.required())
              );

              logger.debug("API.RequestHeader[{}], name -> [{}]", parameter.getType(), val);
            });
          }

          if (annotation.annotationType() == CookieValue.class) {
            Optional.of((CookieValue) annotation).ifPresent(ann -> {
              var val = ann.value().length() == 0 ? ann.name() : ann.value();

              builder.cookies(
                  new APISchema.APIProperty()
                      .setName(val)
                      .setType(parameter.getType().getSimpleName())
                      .setRequired(ann.required())
              );

              logger.debug("API.CookieValue[{}], name -> [{}]", parameter.getType(), val);
            });
          }
        }
      }

      apiSchemataBuilder.schema(builder.build());
    });

    return apiSchemataBuilder.build();
  }
}
