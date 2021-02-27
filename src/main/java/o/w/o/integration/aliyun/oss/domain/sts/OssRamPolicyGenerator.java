package o.w.o.integration.aliyun.oss.domain.sts;

import lombok.Data;
import o.w.o.domain.core.authorization.domain.AuthorizedUser;
import o.w.o.integration.aliyun.oss.domain.sts.policy.OssAction;
import o.w.o.integration.aliyun.sts.domain.Policy;
import o.w.o.integration.aliyun.sts.domain.policy.Condition;
import o.w.o.integration.aliyun.sts.domain.policy.Effect;
import o.w.o.integration.aliyun.sts.domain.policy.Statement;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Set;

@Data
@Component
public class OssRamPolicyGenerator {
  @Resource
  private OssRamPolicyResourceGenerator ossRamPolicyResourceGenerator;

  public Policy forAnonymousUserReadOnly(AuthorizedUser user) {
    return new Policy()
        .setStatements(
            Set.of(
                new Statement()
                    .setEffect(Effect.Allow)
                    .setActions(Set.of(OssAction.OSS_GetObject))
                    .setCondition(
                        new Condition()
                            .add(
                                Condition.Operator.IpAddress,
                                new Condition.OperatorKV()
                                    .add(Condition.Key.ACS_SourceIp, Set.of(user.getIp())))
                    )
                    .setResources(
                        Set.of(
                            ossRamPolicyResourceGenerator.publicResource()
                        )
                    )
            )
        );
  }

  public Policy forNormalUserReadOnly(AuthorizedUser user) {
    return new Policy()
        .setStatements(
            Set.of(
                new Statement()
                    .setEffect(Effect.Allow)
                    .setActions(Set.of(OssAction.OSS_GetObject))
                    .setCondition(
                        new Condition()
                            .add(
                                Condition.Operator.IpAddress,
                                new Condition.OperatorKV()
                                    .add(Condition.Key.ACS_SourceIp, Set.of(user.getIp())))
                    )
                    .setResources(
                        Set.of(
                            ossRamPolicyResourceGenerator.publicResource(),
                            ossRamPolicyResourceGenerator.protectResource(),
                            ossRamPolicyResourceGenerator.privateResource(user)
                        )
                    )
            )
        );
  }

  public Policy forNormalUserReadAndWrite(AuthorizedUser user) {
    return new Policy()
        .setStatements(
            Set.of(
                new Statement()
                    .setEffect(Effect.Allow)
                    .setActions(Set.of(OssAction.OSS_GetObject, OssAction.OSS_PutObject))
                    .setCondition(
                        new Condition()
                            .add(
                                Condition.Operator.IpAddress,
                                new Condition.OperatorKV()
                                    .add(Condition.Key.ACS_SourceIp, Set.of(user.getIp())))
                    )
                    .setResources(
                        Set.of(
                            ossRamPolicyResourceGenerator.publicResource(),
                            ossRamPolicyResourceGenerator.protectResource(),
                            ossRamPolicyResourceGenerator.privateResource(user),
                            ossRamPolicyResourceGenerator.temporaryResource(user)
                        )
                    )
            )
        );
  }

  public Policy of(Preset preset, AuthorizedUser user) {
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
