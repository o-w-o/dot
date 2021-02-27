package o.w.o.integration.aliyun.oss.util;

import com.google.common.collect.Sets;
import lombok.Data;
import o.w.o.domain.core.authorization.domain.AuthorizedUser;
import o.w.o.domain.core.storage.domian.FileStorage;
import o.w.o.integration.aliyun.oss.domain.sts.OssRamPolicyResourceGenerator;
import o.w.o.integration.aliyun.oss.domain.sts.policy.OssAction;
import o.w.o.integration.aliyun.sts.domain.Policy;
import o.w.o.integration.aliyun.sts.domain.policy.Condition;
import o.w.o.integration.aliyun.sts.domain.policy.Effect;
import o.w.o.integration.aliyun.sts.domain.policy.Statement;
import o.w.o.util.IpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

@Data
@Component
public class OssStsPolicy {
  private final OssRamPolicyResourceGenerator ossRamPolicyResourceGenerator;
  private FileStorage.Stage stage;
  private AuthorizedUser user;
  private Policy policy;

  @Autowired
  private OssStsPolicy(OssRamPolicyResourceGenerator ossRamPolicyResourceGenerator) {
    this.ossRamPolicyResourceGenerator = ossRamPolicyResourceGenerator;
  }

  private OssStsPolicy setUser(AuthorizedUser u) {
    for (var statement : this.policy.getStatements()) {
      if (!IpUtils.isLocalIpAddress(u.getIp())) {
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

  private OssStsPolicy setStage(FileStorage.Stage type) {
    switch (type) {
      case STAGING: {
        for (var statement : this.policy.getStatements()) {
          statement.getResources().add(ossRamPolicyResourceGenerator.temporaryResource(this.user));
        }
        break;
      }
      case PERSISTING: {
        for (var statement : this.policy.getStatements()) {
          statement.getResources().add(ossRamPolicyResourceGenerator.temporaryResource(this.user));
          statement.getResources().add(ossRamPolicyResourceGenerator.privateResource(this.user));
        }
        break;
      }
    }
    return this;
  }

  public OssStsPolicy generatePolicy(AuthorizedUser u, FileStorage.Stage type) {
    return new OssStsPolicy(ossRamPolicyResourceGenerator)
        .setPolicy(
            new Policy().setStatements(
                Set.of(
                    new Statement()
                        .setEffect(Effect.Allow)
                        .setActions(Set.of(OssAction.OSS_GetObject))
                        .setCondition(new Condition())
                        .setResources(
                            Sets.newHashSet(
                                ossRamPolicyResourceGenerator.publicResource(),
                                ossRamPolicyResourceGenerator.protectResource()
                            )
                        )
                )
            )
        )
        .setStage(type)
        .setUser(u);
  }
}
