package org.checkerframework.checker.signedness.qual;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.checkerframework.framework.qual.SubtypeOf;

/**
 * The value represents a pattern of bits, not an arithmetic quantity. Examples include the return
 * value of {@link Double#doubleToLongBits} and values used as bitsets.
 *
 * <p>This annotation permits bitwise operations ({@code &}, {@code |}, {@code ^}, {@code ~}) and
 * shifts ({@code <<}, {@code >>}, {@code >>>}), but forbids arithmetic operations such as addition,
 * subtraction, multiplication, division, remainder, and comparisons.
 *
 * @checker_framework.manual #signedness-checker Signedness Checker
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE_USE, ElementType.TYPE_PARAMETER})
@SubtypeOf({UnknownSignedness.class})
public @interface BitPattern {}
