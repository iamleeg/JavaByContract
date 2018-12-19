package online.labrary.jbcTests;

public class TriviallyBreakableObject implements TriviallyBreakable {
	boolean invariant = true;
	
	public void breakInvariant() {
		invariant = false;
	}
	
	public final boolean getInvariant() {
		return invariant;
	}
}
