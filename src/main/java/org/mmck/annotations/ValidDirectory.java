package org.mmck.annotations;

// literally my fist java bare hand made annotation, this is so cool

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidDirectory {
    String message() default "Specified directory is not a valid directory";
}
