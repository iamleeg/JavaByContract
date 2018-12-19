package online.labrary.jbcTests;

import online.labrary.javaByContract.ContractEnforcer;
import online.labrary.javaByContract.ContractViolationException;

public class ContractTester {
	
	public static void main(String[] args) {
		TriviallyBreakable t = ContractEnforcer.enforce(TriviallyBreakable.class, new TriviallyBreakableObject());
		try {
			t.breakInvariant();
			System.out.println("Failed: Survived breaking TriviallyBreakable invariant");
		}
		catch (ContractViolationException e) {
			System.out.println(String.format("Pass: %s", e.getMessage()));
		}
		
		ReflexivelyBreakable r = ContractEnforcer.enforce(ReflexivelyBreakable.class, new ReflexivelyBreakableObject());
		try {
			r.breakInvariant();
			System.out.println("Failed: survived breaking ReflexivelyBreakable invariant");
		}
		catch (ContractViolationException e) {
			System.out.println(String.format("Pass: %s", e.getMessage()));
		}
		
		EvenToOdd e = ContractEnforcer.enforce(EvenToOdd.class, new OddNumberFinder());
		try {
			e.nextOdd(1);
			System.out.println("Failed: Survived breaking EvenToOdd#nextOdd precondition");
		}
		catch (ContractViolationException err) {
			System.out.println(String.format("Pass: %s", err.getMessage()));
		}
	}

}
