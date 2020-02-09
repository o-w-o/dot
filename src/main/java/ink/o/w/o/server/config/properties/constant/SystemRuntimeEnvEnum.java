package ink.o.w.o.server.config.properties.constant;

import lombok.Getter;

/**
 * 运行时的环境
 * @author symbols@dingtalk.com
 * @version  1.0
 * @date 2020/1/20 11:06
 */
public enum SystemRuntimeEnvEnum {

    /**
     * 开发环境
     */
    DEVELOPMENT("development"),
    /**
     * 正式环境
     */
    PRODUCTION("production");

    @Getter
    private String env;

    SystemRuntimeEnvEnum(String env) {
        this.env = env;
    }
}
