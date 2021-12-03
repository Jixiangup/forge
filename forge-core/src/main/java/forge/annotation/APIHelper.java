package forge.annotation;

import com.bnyte.forge.enums.LogOutputType;

import java.lang.annotation.*;

/**
 * API助手注解
 * @auther bnyte
 * @date 2021-12-04 02:46
 * @email bnytezz@gmail.com
 */
@Target(ElementType.METHOD)
@Documented
@Retention(value = RetentionPolicy.RUNTIME)
public @interface APIHelper {

    /**
     * 请求的API接口地址，默认获取类上的任意MVC的请求 + 方法中的任意MVC请求得出，如果指出值则使用指出的值
     * @return API请求地址
     */
    String value() default "";

    /**
     * 被执行的方法名，默认为当前方法名，如果不为空则使用声明的值
     * @return 方法名
     */
    String method() default "";

    /**
     * 是否打开请求日志输出
     * @return true则会自动打输出请求日志，false则不会输出请求日志
     */
    boolean enableRequest() default true;

    /**
     * 是否打开响应日志输出
     * @return true则会自动输出响应日志，false则不会输出响应日志
     */
    boolean enableResponse() default true;

    /**
     * 日志输出方式，为增加可读性及可移植性，默认使用JSON字符串输出，可选值包括：JSONString、toString[需要人为手写toString()，否则输出为对象地址]、each[该方式需要人为提供gather()]
     * @return 日志输出方式
     */
    LogOutputType output() default LogOutputType.JSON;

}
