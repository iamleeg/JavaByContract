package online.labrary.javaByContract;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(METHOD)
@Repeatable(Preconditions.class)
public @interface Precondition {
	String name();
	boolean value() default true;
}
