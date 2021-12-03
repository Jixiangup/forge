package forge.enums;

/**
 * 请求及响应的日志输出方式
 * @auther bnyte
 * @date 2021-12-04 02:55
 * @email bnytezz@gmail.com
 */
public enum LogOutputType {
    /**
     * 使用json模式输出情趣及响应日志
     */
    JSON,

    /**
     * 调用toString输出情趣及响应日志
     */
    TO_STRING,

    /**
     * 便利对象所有属性输出日志，需要人为提供getter()
     */
    EACH,
    ;
}
