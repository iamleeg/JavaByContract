package online.labrary.javaByContract;

import static java.lang.annotation.ElementType.TYPE;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;

@Target(TYPE)
@Repeatable(Invariants.class)
@Retention(RUNTIME)
public @interface Invariant {
	String name();
	boolean value() default true;
}
