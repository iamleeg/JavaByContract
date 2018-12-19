package online.labrary.javaByContract;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import online.labrary.jbcTests.TriviallyBreakable;
import online.labrary.jbcTests.TriviallyBreakableObject;

public class ContractEnforcer implements InvocationHandler {
	private Object target;
	private Class targetClass;
	private List<Invariant> invariants;
	
	public ContractEnforcer(Object o) {
		Class oClass = o.getClass();
		ArrayList<Invariant> allInvariants = getInvariantsForClass(oClass);
		target = o;
		targetClass = oClass;
		invariants = allInvariants;
	}

	private ArrayList<Invariant> getInvariantsForClass(Class oClass) {
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
	
	@Override
	@SuppressWarnings("unchecked")
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		// check the preconditions
		// run the method
		Object result = method.invoke(target, args);
		// check the postconditions
		// check the invariants
		for (Invariant invariant : invariants) {
			String methodName = invariant.name();
			boolean expectedValue = invariant.value();
			Method targetMethod = targetClass.getMethod(methodName, null);
			Boolean actualValue = (Boolean) targetMethod.invoke(target, null);
			if (actualValue != expectedValue) {
				throw new InvariantViolationException(target, methodName, actualValue);
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
