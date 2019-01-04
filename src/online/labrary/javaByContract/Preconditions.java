package online.labrary.javaByContract;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * A collection annotation, so that a class or interface can have multiple Preconditions.
 * @see Precondition
 */
@Retention(RUNTIME)
@Target(METHOD)
@Documented
public @interface Preconditions {
	/**
	 * @return The collection of @{code Precondition} annotations for this method.
	 */
	Precondition[] value();
}
