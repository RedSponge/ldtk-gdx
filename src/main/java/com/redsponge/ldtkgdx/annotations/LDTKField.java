package com.redsponge.ldtkgdx.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Defines a class-field as a field to be filled in from the LDTK map
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface LDTKField {

    /*
    * Field name
    */
    String value() default "";

}
