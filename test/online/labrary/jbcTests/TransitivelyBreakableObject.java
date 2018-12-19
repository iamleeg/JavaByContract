package online.labrary.jbcTests;

public class TransitivelyBreakableObject implements TransitivelyBreakable {
	private boolean invariant = true;
	
	@Override
	public void breakInvariant() {
		invariant = false;
	}

	@Override
	public final boolean getInvariant() {
		return invariant;
	}

}
