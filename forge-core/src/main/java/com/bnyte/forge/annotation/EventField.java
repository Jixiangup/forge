package com.bnyte.forge.annotation;

import java.lang.annotation.*;

/**
 * @author bnyte
 * @since 2022/5/27 16:49
 */
@Target(ElementType.FIELD)
@Documented
@Retention(value = RetentionPolicy.RUNTIME)
public @interface EventField {
}
