package online.labrary.jbcTests;

import online.labrary.javaByContract.Precondition;

public interface EvenToOdd {
	@Precondition(name = "inputIsEven")
	public int nextOdd(int even);
	default boolean inputIsEven(Integer input) {
		return (input % 2) == 0;
	}
}
