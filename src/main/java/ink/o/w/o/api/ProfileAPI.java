package ink.o.w.o.api;


import ink.o.w.o.resource.user.domain.User;
import ink.o.w.o.server.constant.HttpConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.mediatype.alps.*;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.stream.Collectors;

import static org.springframework.hateoas.MediaTypes.ALPS_JSON_VALUE;
import static org.springframework.hateoas.mediatype.PropertyUtils.getExposedProperties;
import static org.springframework.hateoas.mediatype.alps.Alps.doc;

/**
 * 概要的 API
 *
 * @author symbols@dingtalk.com
 * @date 2020/02/10 12:02
 * @since 1.0.0
 */

@Slf4j
@RestController
@ExposesResourceFor(ProfileAPI.class)
@RequestMapping("profile")
public class ProfileAPI {
  @GetMapping(produces = ALPS_JSON_VALUE)
  Alps profile() {
    return Alps.alps()
        .doc(doc()
            .href("https:example.org/samples/full/doc.html")
            .value("value goes here")
            .format(Format.ASCIIDOC)
            .build())
        .descriptor(getExposedProperties(User.class).stream()
            .map(property -> Descriptor.builder()
                .name(property.getName())
                .type(Type.SEMANTIC)
                .build())
            .collect(Collectors.toList()))
        .build();
  }
}
