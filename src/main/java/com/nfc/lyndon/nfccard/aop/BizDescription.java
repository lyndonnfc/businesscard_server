package com.nfc.lyndon.nfccard.aop;

import java.lang.annotation.*;

@Target({ElementType.METHOD})//作用于参数或方法上
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface BizDescription {

    String value() default "";
}