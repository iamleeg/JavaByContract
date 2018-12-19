package online.labrary.jbcTests;

import online.labrary.javaByContract.ContractEnforcer;
import online.labrary.javaByContract.InvariantViolationException;

public class ContractTester {
	
	public static void main(String[] args) {
		TriviallyBreakable t = ContractEnforcer.enforce(TriviallyBreakable.class, new TriviallyBreakableObject());
		try {
			t.breakInvariant();
			System.out.println("Failed: Survived breaking TriviallyBreakable invariant");
		}
		catch (InvariantViolationException e) {
			System.out.println(String.format("Pass: %s", e.getMessage()));
		}
		
		ReflexivelyBreakable r = ContractEnforcer.enforce(ReflexivelyBreakable.class, new ReflexivelyBreakableObject());
		try {
			r.breakInvariant();
			System.out.println("Failed: survived breaking ReflexivelyBreakable invariant");
		}
		catch (InvariantViolationException e) {
			System.out.println(String.format("Pass: %s", e.getMessage()));
		}
	}

}
