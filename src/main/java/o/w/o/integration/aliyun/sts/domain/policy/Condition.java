package o.w.o.integration.aliyun.sts.domain.policy;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.HashMap;
import java.util.Set;

public class Condition extends HashMap<Condition.Operator, Condition.OperatorKV> {

  public Condition add(Operator operator, OperatorKV operatorKV) {
    super.put(operator, operatorKV);
    return this;
  }

  public enum Operator {
    StringEquals("StringEquals"),
    StringNotEquals("StringNotEquals"),
    StringEqualsIgnoreCase("StringEqualsIgnoreCase"),
    StringNotEqualsIgnoreCase("StringNotEqualsIgnoreCase"),
    StringLike("StringLike"),
    StringNotLike("StringNotLike"),
    NumericEquals("NumericEquals"),
    NumericNotEquals("NumericNotEquals"),
    NumericLessThan("NumericLessThan"),
    NumericLessThanEquals("NumericLessThanEquals"),
    NumericGreaterThan("NumericGreaterThan"),
    NumericGreaterThanEquals("NumericGreaterThanEquals"),
    DateEquals("DateEquals"),
    DateNotEquals("DateNotEquals"),
    DateLessThan("DateLessThan"),
    DateLessThanEquals("DateLessThanEquals"),
    DateGreaterThan("DateGreaterThan"),
    DateGreaterThanEquals("DateGreaterThanEquals"),
    IpAddress("IpAddress"),
    NotIpAddress("NotIpAddress");

    private String value;

    Operator(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }
  }

  public enum Key {
    ACS_SourceIp("acs:SourceIp"),
    ACS_UserAgent("acs:UserAgent"),
    ACS_CurrentTime("acs:CurrentTime"),
    ACS_SecureTransport("acs:SecureTransport"),
    OSS_Prefix("oss:Prefix"),
    OSS_Delimiter("oss:Delimiter");

    private String value;

    Key(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }
  }

  public static class OperatorKV extends HashMap<Key, Set<String>> {

    public OperatorKV add(Key key, Set<String> value) {
      super.put(key, value);
      return this;
    }
  }
}
