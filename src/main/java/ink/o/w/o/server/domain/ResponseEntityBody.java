package ink.o.w.o.server.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

/**
 * API 结果
 *
 * @author symbols@dingtalk.com
 * @version 1.0
 * @date 2019/8/4 下午6:56
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseEntityBody<T> extends RepresentationModel<ResponseEntityBody<T>> {
    private T payload;
    private String message = "";
}
