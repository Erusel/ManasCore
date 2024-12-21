/*
 * Copyright (c) 2024. ManasMods
 * GNU General Public License 3
 */

package io.github.manasmods.manascore.command.api.parameter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
public @interface FloatNumber {
    /**
     * Argument Name in the Command
     */
    String value() default "";

    /**
     * Minimum value of the number
     */
    float min() default Float.MIN_VALUE;

    /**
     * Maximum value of the number
     */
    float max() default Float.MAX_VALUE;
}