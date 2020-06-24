package o.w.o.resource.integration.aliyun.core.domain.oss;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Sets;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import o.w.o.resource.integration.aliyun.core.domain.Policy;
import o.w.o.resource.integration.aliyun.core.domain.policy.*;
import o.w.o.resource.system.authorization.domain.AuthorizedUser;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.util.Set;

@Getter
@Setter
@Builder
public class TemporalOssResource {

  @JsonIgnore
  private File file;
  private AuthorizedUser authorizedUploader;
  private Boolean status;

  @Data
  public static class OssStsPolicy {
    @Value("${my.aliyun.oss.temporaryDir}")
    private String temporaryDir;

    private AuthorizedUser user;
    private Policy policy;

    private OssStsPolicy() {
    }

    public static OssStsPolicy getInstance(AuthorizedUser user) {
      return new OssStsPolicy()
          .setPolicy(PolicySingletonHolder.INSTANCE.clone())
          .setUser(user);
    }

    private OssStsPolicy setUser(AuthorizedUser u) {
      for (var statement : this.policy.getStatements()) {
        statement.getResources().add(Resource.temporaryResource(u));
        if (!"0:0:0:0:0:0:0:1".equals(u.getIp())) {
          statement.getCondition().add(
              Condition.Operator.IpAddress,
              new Condition.OperatorKV()
                  .add(Condition.Key.ACS_SourceIp, Sets.newHashSet(u.getIp()))
          );
        }
      }

      this.user = u;
      return this;
    }

    private static class PolicySingletonHolder {
      private static final Policy INSTANCE = new Policy()
          .setStatements(
              Set.of(
                  new Statement()
                      .setEffect(Effect.Allow)
                      .setActions(Set.of(Action.OSS_GetObject, Action.OSS_PutObject))
                      .setCondition(new Condition())
                      .setResources(
                          Sets.newHashSet(
                              Resource.publicResource(),
                              Resource.protectResource()
                          )
                      )
              )
          );
    }
  }
}
