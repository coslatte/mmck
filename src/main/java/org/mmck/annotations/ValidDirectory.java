package org.mmck.annotations;

// literally my fist java bare hand made annotation, this is so cool

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// @target annotation: tells java compiler that we are going to refer a function parameter
// @retention: tells java compiler that this will save information and variables on runtime memory

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidDirectory {
    String message() default "Specified directory is not a valid directory";
}
