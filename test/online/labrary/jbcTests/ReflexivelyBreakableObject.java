package online.labrary.jbcTests;

public class ReflexivelyBreakableObject implements ReflexivelyBreakable {
	private boolean invariant = true;
	
	@Override
	public void breakInvariant() {
		invariant = false;
	}

	@Override
	public boolean getInvariant() {
		return invariant;
	}

}
