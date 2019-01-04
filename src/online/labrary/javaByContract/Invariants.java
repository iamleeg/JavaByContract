package online.labrary.javaByContract;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A collection annotation, so that a class or interface can have multiple Invariants.
 * @see Invariant
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Invariants {
	/**
	 * @return The collection of {@code Invariant} annotations for this type.
	 */
	Invariant[] value();
}
