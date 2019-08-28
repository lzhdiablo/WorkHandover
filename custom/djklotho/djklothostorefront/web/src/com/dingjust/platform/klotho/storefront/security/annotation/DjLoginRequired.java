package com.dingjust.platform.klotho.storefront.security.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DjLoginRequired {
}
