package com.example.util.aop.authPerm.annotation;

import java.lang.annotation.*;


/**
 *
 * @author Administrator
 *
 */
@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthPermAnnotation {

}
