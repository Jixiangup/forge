package com.bnyte.forge.springboot.annotation;

import com.bnyte.forge.springboot.scanner.EnableForgeScanner;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @auther bnyte
 * @date 2021-12-04 21:22
 * @email bnytezz@gmail.com
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import({EnableForgeScanner.class})
public @interface EnableForge {
    String[] value() default {};

//    String configuration() default "";

    String[] basePackages() default {};

    Class<?>[] basePackageClasses() default {};

    String[] defaultPackages() default {
        "com.bnyte.forge.aop.actuator" // 带有
    };
}
