package online.labrary.javaByContract;

public class InvariantViolationException extends Error {

	
	public InvariantViolationException(Object target, String methodName, Boolean actualValue) {
		super(String.format("Invariant %s had unexpected value %b on object %s", methodName, actualValue, target));
	}

	private static final long serialVersionUID = 1L;

}
