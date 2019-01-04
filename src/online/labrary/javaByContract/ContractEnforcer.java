package online.labrary.javaByContract;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The class that enforces contracts expressed in @Invariant, @Precondition and @Postcondition annotations.
 * Programmers should interact with this class via the static {@code enforce()} method, which wraps an object
 * in a Proxy that will automatically enforce the contract on method invocation.
 * @see Invariant
 * @see Precondition
 * @see Postcondition
 */
public class ContractEnforcer implements InvocationHandler {
	private Object target;
	@SuppressWarnings("rawtypes")
	private Class targetClass;
	private List<Invariant> invariants;
	
	@SuppressWarnings("rawtypes")
	private ContractEnforcer(Object o) {
		Class oClass = o.getClass();
		List<Invariant> allInvariants = getInvariantsForClass(oClass);
		target = o;
		targetClass = oClass;
		invariants = allInvariants;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static List<Invariant> getInvariantsForClass(Class oClass) {
		Invariant[] classInvariants = (Invariant[]) oClass.getAnnotationsByType(Invariant.class);
		ArrayList<Invariant> allInvariants = new ArrayList<Invariant>(Arrays.asList(classInvariants));
		for (AnnotatedType interfaceUse : oClass.getAnnotatedInterfaces()) {
			String interfaceName = interfaceUse.getType().getTypeName();
			try {
				allInvariants.addAll(getInvariantsForClass(Class.forName(interfaceName)));
			} catch (ClassNotFoundException e) {
				// pass - the type was found by the reflection API earlier
			}
		}
		Class superClass = oClass.getSuperclass();
		if (superClass != null) {
			allInvariants.addAll(getInvariantsForClass(superClass));
		}
		return allInvariants;
	}
	
	private static <T extends Annotation> List<T> getContractPartsForMethod(Method method, Class<T> annotation) {
		T[] annotations = method.getAnnotationsByType(annotation);
		return new ArrayList<T>(Arrays.asList(annotations));
	}
	
	private static List<Precondition> getPreconditionsForMethod(Method method) {
		return getContractPartsForMethod(method, Precondition.class);
	}
	
	private static List<Postcondition> getPostconditionsForMethod(Method method) {
		return getContractPartsForMethod(method, Postcondition.class);
	}
	
	@SuppressWarnings("rawtypes")
	private static Class[] getClassesForParameterList(Object[] args) {
		if (args == null) return null;
		Class[] results = new Class[args.length];
		for (int i = 0; i < args.length; i++) {
			results[i] = (args[i] != null) ? args[i].getClass() : Void.class;
		}
		return results;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static Boolean findAndInvokeMethod(String name, Class targetClass, Object target, Object[] arguments) throws Throwable {
		Method targetMethod = targetClass.getMethod(name, getClassesForParameterList(arguments));
		return (Boolean)targetMethod.invoke(target, arguments);
	}
	
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		Object[] arguments = (args != null) ? args : new Object[0];
		// check the preconditions
		for (Precondition precondition : getPreconditionsForMethod(method)) {
			String methodName = precondition.name();
			boolean expectedValue = precondition.value();
			Boolean actualValue = findAndInvokeMethod(methodName, targetClass, target, arguments);
			if (actualValue != expectedValue) {
				throw new ContractViolationException(target, methodName, actualValue);
			}
		}
		// run the method
		Object result = method.invoke(target, args);
		// check the postconditions
		for (Postcondition postcondition : getPostconditionsForMethod(method)) {
			String methodName = postcondition.name();
			boolean expectedValue = postcondition.value();
			Object[] argsWithReturnValue = new Object[arguments.length + 1];
			for (int i = 0; i < arguments.length; i++) {
				argsWithReturnValue[i] = arguments[i];
			}
			argsWithReturnValue[arguments.length] = result; 
			Boolean actualValue = findAndInvokeMethod(methodName, targetClass, target, argsWithReturnValue);
			if (actualValue != expectedValue) {
				throw new ContractViolationException(target, methodName, actualValue);
			}
		}
		// check the invariants
		for (Invariant invariant : invariants) {
			String methodName = invariant.name();
			boolean expectedValue = invariant.value();
			Boolean actualValue = findAndInvokeMethod(methodName, targetClass, target, null);
			if (actualValue != expectedValue) {
				throw new ContractViolationException(target, methodName, actualValue);
			}
		}
		return result;
	}

	/**
	 * Given an object and the interface it is fulfilling, return a proxy that enforces
	 * the object's contract for the methods on the stated interface.
	 * @param interfaceClass The interface that callers will want to use on the implementing object.
	 * @param implementation An object that implements the interface.
	 * @param <T> The type of the interface callers will want to use on the implementing object.
	 * @return A proxy that acts exactly like {@code implementation} and enforces its contract.
	 */
	@SuppressWarnings("unchecked")
	public static <T> T enforce(Class<T> interfaceClass, T implementation) {
		return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(),
				new Class[] { interfaceClass },
				new ContractEnforcer(implementation));
		}

}
