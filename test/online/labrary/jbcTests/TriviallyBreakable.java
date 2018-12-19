package online.labrary.jbcTests;

import online.labrary.javaByContract.Invariant;

@Invariant(name = "getInvariant")
public interface TriviallyBreakable {
	void breakInvariant();
	boolean getInvariant();
}
