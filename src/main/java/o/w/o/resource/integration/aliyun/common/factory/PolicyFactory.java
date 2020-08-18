package o.w.o.resource.integration.aliyun.common.factory;

import lombok.Data;
import o.w.o.resource.integration.aliyun.common.domain.Policy;
import o.w.o.resource.integration.aliyun.common.domain.policy.*;
import o.w.o.resource.system.authorization.domain.AuthorizedUser;

import java.util.Set;

@Data
public class PolicyFactory {
  public static Policy forAnonymousUserReadOnly(AuthorizedUser user) {
    return new Policy()
        .setStatements(
            Set.of(
                new Statement()
                    .setEffect(Effect.Allow)
                    .setActions(Set.of(Action.OSS_GetObject))
                    .setCondition(
                        new Condition()
                            .add(
                                Condition.Operator.IpAddress,
                                new Condition.OperatorKV()
                                    .add(Condition.Key.ACS_SourceIp, Set.of(user.getIp())))
                    )
                    .setResources(
                        Set.of(
                            Resource.MyOssResource.publicResource()
                        )
                    )
            )
        );
  }

  public static Policy forNormalUserReadOnly(AuthorizedUser user) {
    return new Policy()
        .setStatements(
            Set.of(
                new Statement()
                    .setEffect(Effect.Allow)
                    .setActions(Set.of(Action.OSS_GetObject))
                    .setCondition(
                        new Condition()
                            .add(
                                Condition.Operator.IpAddress,
                                new Condition.OperatorKV()
                                    .add(Condition.Key.ACS_SourceIp, Set.of(user.getIp())))
                    )
                    .setResources(
                        Set.of(
                            Resource.MyOssResource.publicResource(),
                            Resource.MyOssResource.protectResource(),
                            Resource.MyOssResource.privateResource(user)
                        )
                    )
            )
        );
  }

  public static Policy forNormalUserReadAndWrite(AuthorizedUser user) {
    return new Policy()
        .setStatements(
            Set.of(
                new Statement()
                    .setEffect(Effect.Allow)
                    .setActions(Set.of(Action.OSS_GetObject, Action.OSS_PutObject))
                    .setCondition(
                        new Condition()
                            .add(
                                Condition.Operator.IpAddress,
                                new Condition.OperatorKV()
                                    .add(Condition.Key.ACS_SourceIp, Set.of(user.getIp())))
                    )
                    .setResources(
                        Set.of(
                            Resource.MyOssResource.publicResource(),
                            Resource.MyOssResource.protectResource(),
                            Resource.MyOssResource.privateResource(user),
                            Resource.MyOssResource.temporaryResource(user)
                        )
                    )
            )
        );
  }

  public static Policy of(Preset preset, AuthorizedUser user) {
    switch (preset) {
      case User_ReadOnly:
        return forNormalUserReadOnly(user);
      case User_ReadAndWrite:
        return forNormalUserReadAndWrite(user);
      default:
        return forAnonymousUserReadOnly(user);
    }
  }

  public enum Preset {
    Anonymous,
    User_ReadOnly,
    User_ReadAndWrite,
    ;
  }
}
