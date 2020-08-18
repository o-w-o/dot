package o.w.o.resource.integration.aliyun.oss.util;

import com.google.common.collect.Sets;
import lombok.Data;
import o.w.o.resource.integration.aliyun.common.domain.Policy;
import o.w.o.resource.integration.aliyun.common.domain.policy.*;
import o.w.o.resource.symbols.field.domain.ext.ResourceSpace;
import o.w.o.resource.system.authorization.domain.AuthorizedUser;

import java.util.Set;

public class OssStageStsPolicyGenerator {
  @Data
  public static class OssStsPolicy {
    private ResourceSpace.Stage stage;
    private AuthorizedUser user;
    private Policy policy;

    private OssStsPolicy() {
    }

    public static OssStsPolicy getInstance(AuthorizedUser user, ResourceSpace.Stage stage) {
      return new OssStsPolicy()
          .setPolicy(PolicySingletonHolder.INSTANCE.clone())
          .setUser(user)
          .setStage(stage);
    }

    private OssStsPolicy setUser(AuthorizedUser u) {
      for (var statement : this.policy.getStatements()) {
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

    private OssStsPolicy setStage(ResourceSpace.Stage type) {
      switch (type) {
        case STAGING -> {
          for (var statement : this.policy.getStatements()) {
            statement.getResources().add(Resource.MyOssResource.temporaryResource(this.user));
          }
        }
        case PERSISTING -> {
          for (var statement : this.policy.getStatements()) {
            statement.getResources().add(Resource.MyOssResource.temporaryResource(this.user));
            statement.getResources().add(Resource.MyOssResource.privateResource(this.user));
          }
        }
      }
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
                              Resource.MyOssResource.publicResource(),
                              Resource.MyOssResource.protectResource()
                          )
                      )
              )
          );
    }
  }
}
