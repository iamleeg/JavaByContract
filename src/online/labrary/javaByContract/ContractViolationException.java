package online.labrary.javaByContract;

/**
 * A runtime error that signals that an object's contract was violated.
 */
public class ContractViolationException extends Error {

	
	public ContractViolationException(Object target, String methodName, Boolean actualValue) {
		super(String.format("Invariant %s had unexpected value %b on object %s", methodName, actualValue, target));
	}

	private static final long serialVersionUID = 1L;

}
