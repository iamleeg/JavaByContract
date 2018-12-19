package online.labrary.javaByContract;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ContractEnforcer implements InvocationHandler {
	private Object target;
	@SuppressWarnings("rawtypes")
	private Class targetClass;
	private List<Invariant> invariants;
	
	@SuppressWarnings("rawtypes")
	public ContractEnforcer(Object o) {
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
		Class[] results = new Class[args.length];
		for (int i = 0; i < args.length; i++) {
			results[i] = (args[i] != null) ? args[i].getClass() : Void.class;
		}
		return results;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		Object[] arguments = (args != null) ? args : new Object[0];
		// check the preconditions
		for (Precondition precondition : getPreconditionsForMethod(method)) {
			String methodName = precondition.name();
			boolean expectedValue = precondition.value();
			Method targetMethod = targetClass.getMethod(methodName, getClassesForParameterList(arguments));
			Boolean actualValue = (Boolean) targetMethod.invoke(target, arguments);
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
			Class<?> returnType = method.getReturnType();
			Object[] argsWithReturnValue = new Object[arguments.length + 1];
			for (int i = 0; i < arguments.length; i++) {
				argsWithReturnValue[i] = arguments[i];
			}
			argsWithReturnValue[arguments.length] = result; 
			Method targetMethod = targetClass.getMethod(methodName, getClassesForParameterList(argsWithReturnValue));
			Boolean actualValue = (Boolean) targetMethod.invoke(target, argsWithReturnValue);
			if (actualValue != expectedValue) {
				throw new ContractViolationException(target, methodName, actualValue);
			}
		}
		// check the invariants
		for (Invariant invariant : invariants) {
			String methodName = invariant.name();
			boolean expectedValue = invariant.value();
			Method targetMethod = targetClass.getMethod(methodName, null);
			Boolean actualValue = (Boolean) targetMethod.invoke(target, null);
			if (actualValue != expectedValue) {
				throw new ContractViolationException(target, methodName, actualValue);
			}
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public static <T> T enforce(Class<T> interfaceClass, T implementation) {
		return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(),
				new Class[] { interfaceClass },
				new ContractEnforcer(implementation));
		}

}
