package online.labrary.javaByContract;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Repeatable(Invariants.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface Invariant {
	String name();
	boolean value() default true;
}
