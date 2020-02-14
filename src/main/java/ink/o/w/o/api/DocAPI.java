package ink.o.w.o.api;


import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.mediatype.alps.Alps;
import org.springframework.hateoas.mediatype.alps.Format;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.MediaTypes.ALPS_JSON_VALUE;
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
@ExposesResourceFor(DocAPI.class)
@RequestMapping("docs")
public class DocAPI {
  @GetMapping(produces = ALPS_JSON_VALUE)
  public Alps profile() {
    return Alps.alps()
        .version("v1.0")
        .doc(doc()
            .href("https://api.o-w-o.ink/doc/index.html")
            .value("API 文档 (generate by Spring REST Doc.)")
            .format(Format.HTML)
            .build()
        )
        .build();
  }
}
