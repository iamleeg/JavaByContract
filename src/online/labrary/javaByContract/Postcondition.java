package online.labrary.javaByContract;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Express a method postcondition. A postcondition is a predicate that should be
 * true after a method has run, i.e. it expresses the expected outcome and state
 * resulting from correct execution of the method.
 */
@Retention(RUNTIME)
@Target(METHOD)
@Repeatable(Postconditions.class)
@Documented
public @interface Postcondition {
	/**
	 * The name of this postcondition. At runtime, Java by Contract uses this name as a
	 * method on the target object. The signature of the postcondition method must contain
	 * the same argument types as the method under test, with an extra argument at the end
	 * with the same type as the method's return value.
	 * 
	 * As an example, if an interface contains a method {@code Foo doSomething(String x)},
	 * any postcondition should have the signature {@code boolean postcondition(String x, Foo result)}.
	 * @return The name of a method that evaluates this postcondition.
	 */
	String name();
	/**
	 * The expected outcome of this postcondition's method, default is {@code true}.
	 * @return The value expected for this postcondition.
	 */
	boolean value() default true;
}
