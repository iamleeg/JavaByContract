package online.labrary.jbcTests;

import online.labrary.javaByContract.Postcondition;
import online.labrary.javaByContract.Precondition;

public interface EvenToOdd {
	@Precondition(name = "inputIsEven")
	@Postcondition(name = "outputIsOdd")
	public int nextOdd(int even);
	default boolean inputIsEven(Integer input) {
		return (input % 2) == 0;
	}
	default boolean outputIsOdd(Integer input, Integer output) {
		return (output % 2) == 1;
	}
}
