package o.w.o.resource.symbols.record.domain.ext;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import o.w.o.server.io.json.annotation.JsonTypeTargetPayloadType;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;
import java.util.List;

/**
 * 主观记录
 *
 * @author symbols@dingtalk.com
 * @date 2020/08/13
 * @since 1.0.0
 */
@Data
@JsonTypeTargetPayloadType
public abstract class ObjectSpacePayload {
  private ObjectSpacePayloadType payloadType;
  private List<String> contentFieldsSequence;

  /**
   * 日记
   *
   * @author symbols@dingtalk.com
   * @date 2020/08/13
   * @since 1.0.0
   */
  @EqualsAndHashCode(callSuper = true)
  @NoArgsConstructor
  @Data

  @JsonTypeName(ObjectSpacePayloadType.TypeName.DIARY)
  public static class Diary extends ObjectSpacePayload {
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @Override
    public ObjectSpacePayloadType getPayloadType() {
      return new ObjectSpacePayloadType(ObjectSpacePayloadType.TypeEnum.DIARY);
    }
  }

  /**
   * 宋词
   *
   * @author symbols@dingtalk.com
   * @date 2020/08/15
   * @since 1.0.0
   */
  @EqualsAndHashCode(callSuper = true)
  @NoArgsConstructor
  @Data

  @JsonTypeName(ObjectSpacePayloadType.TypeName.SONG_CI)
  public static class SongCi extends ObjectSpacePayload {
    private String tuneTitle;

    @Override
    public ObjectSpacePayloadType getPayloadType() {
      return new ObjectSpacePayloadType(ObjectSpacePayloadType.TypeEnum.SONG_CI);
    }
  }
}
