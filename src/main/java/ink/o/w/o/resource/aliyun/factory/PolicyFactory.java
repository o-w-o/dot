package ink.o.w.o.resource.aliyun.factory;

import ink.o.w.o.resource.aliyun.domain.Policy;
import ink.o.w.o.resource.aliyun.domain.policy.*;
import ink.o.w.o.resource.authorization.domain.AuthorizedUser;
import lombok.Data;

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
                            Resource.publicResource()
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
                            Resource.publicResource(),
                            Resource.protectResource(),
                            Resource.privateResource(user)
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
                            .add(
                                Condition.Operator.StringEquals,
                                new Condition.OperatorKV()
                                    .add(Condition.Key.OSS_Delimiter, Set.of("/"))
                            )
                    )
                    .setResources(
                        Set.of(
                            Resource.publicResource(),
                            Resource.protectResource(),
                            Resource.privateResource(user),
                            Resource.temporaryResource(user)
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
