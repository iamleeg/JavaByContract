package online.labrary.javaByContract;

import static java.lang.annotation.ElementType.TYPE;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Target;

/**
 * Express a class or interface invariant. An invariant is a property that should be
 * true at all times that an object is quiescent; i.e. between method invocations.
 *
 * Currently Java by Contract only tests invariants after instance methods have run;
 * complete coverage would be ensured by inspecting the invariant after the constructor
 * has returned, too.
 */
@Target(TYPE)
@Repeatable(Invariants.class)
@Retention(RUNTIME)
@Documented
public @interface Invariant {
	/**
	 * The name of this invariant. At runtime, Java by Contract uses this name as a
	 * method on the target object. The signature of the method must be
	 * {@code boolean invariantMethod();}
	 * @return The name of a method that evaluates this invariant.
	 */
	String name();
	/**
	 * The expected outcome of this invariant's method, default is {@code true}.
	 * @return The value expected for this invariant.
	 */
	boolean value() default true;
}
