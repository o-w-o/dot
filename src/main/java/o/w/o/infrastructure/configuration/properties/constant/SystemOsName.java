package o.w.o.infrastructure.configuration.properties.constant;

import lombok.Getter;

/**
 * 运行时的环境
 * @author symbols@dingtalk.com
 * @version  1.0
 * @date 2020/1/20 11:06
 */
public enum SystemOsName {

    /**
     * 开发环境
     */
    Linux("Linux"),
    /**
     * 正式环境
     */
    Window("Window");

    @Getter
    private String osName;

    SystemOsName(String osName) {
        this.osName = osName;
    }
}
