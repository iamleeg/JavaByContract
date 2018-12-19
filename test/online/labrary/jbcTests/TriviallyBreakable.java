package online.labrary.jbcTests;

import online.labrary.javaByContract.Invariant;
import online.labrary.javaByContract.Postcondition;

@Invariant(name = "getInvariant")
public interface TriviallyBreakable {
	@Postcondition(name = "unimportant")
	void breakInvariant();
	boolean getInvariant();
	default boolean unimportant(Void v) {
		return true;
	}
}
