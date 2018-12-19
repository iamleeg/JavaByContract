package online.labrary.jbcTests;

import java.lang.reflect.Proxy;

import online.labrary.javaByContract.ContractEnforcer;
import online.labrary.javaByContract.InvariantViolationException;

public class ContractTester {
	
	public static void main(String[] args) {
		TriviallyBreakable p =
				(TriviallyBreakable) Proxy.newProxyInstance(TriviallyBreakable.class.getClassLoader(),
						new Class[] { TriviallyBreakable.class },
						new ContractEnforcer(new TriviallyBreakableObject()));
		try {
			p.breakInvariant();
			System.out.println("Failed: Survived breaking TriviallyBreakable invariant");
		}
		catch (InvariantViolationException e) {
			System.out.println(String.format("Pass: %s", e.getMessage()));
		}
		
		ReflexivelyBreakable r =
				(ReflexivelyBreakable) Proxy.newProxyInstance(ReflexivelyBreakable.class.getClassLoader(),
						new Class[] { ReflexivelyBreakable.class },
						new ContractEnforcer(new ReflexivelyBreakableObject()));
		try {
			r.breakInvariant();
			System.out.println("Failed: survived breaking ReflexivelyBreakable invariant");
		}
		catch (InvariantViolationException e) {
			System.out.println(String.format("Pass: %s", e.getMessage()));
		}
	}

}
