package online.labrary.javaByContract;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * A collection annotation, so that a method can have multiple Postconditions.
 * @see Postcondition
 */
@Retention(RUNTIME)
@Target(METHOD)
@Documented
public @interface Postconditions {
	/**
	 * @return The collection of @{code Postcondition} annotations for this method.
	 */
	Postcondition[] value();
}
