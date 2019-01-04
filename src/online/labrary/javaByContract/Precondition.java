package online.labrary.javaByContract;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Express a method precondition. A precondition is a predicate that should be
 * true before a method has run, i.e. it expresses the requirements for correct
 * execution of the method.
 */
@Retention(RUNTIME)
@Target(METHOD)
@Repeatable(Preconditions.class)
@Documented
public @interface Precondition {
	/**
	 * The name of this precondition. At runtime, Java by Contract uses this name as a
	 * method on the target object. The signature of the precondition method must contain
	 * the same argument types as the method under test.
	 * 
	 * As an example, if an interface contains a method {@code Foo doSomething(String x)},
	 * any precondition should have the signature {@code boolean precondition(String x)}.
	 * @return The name of a method that evaluates this precondition.
	 */
	String name();
	/**
	 * The expected outcome of this precondition's method, default is {@code true}.
	 * @return The value expected for this precondition.
	 */
	boolean value() default true;
}
