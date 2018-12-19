# Java by Contract

This is an implementation of Design by Contract in the Java language. Where previous implementations have relied on modifying the source code by extracting contract properties from Javadoc comments, this attempt uses runtime annotations and reflection to investigate the contracts.

## Usage

You can define invariants on classes or interfaces, but it probably only makes sense to define them on classes because the interface doesn't know what the implementation details of your class are so can't predict the class invariants.


    public interface Doer {
	    void doAThing();
    }
	
    @Invariant(name = "getAPropertyThatMustBeTrue")
	@Invariant(name = "getAPropertyThatMustBeFalse", value = false)
	public class Foo implements Doer {
	    public void doAThing() {
		  // ...
	    }
		
		public final boolean getAPropertyThatMustBeTrue() {
		  return // invariant test
	    }
		
		public final boolean getAPropertyThatMustBeFalse() {
		  return // invariant test
		}
	}

Now, when you want to give someone an object that honours a contract, rather than returning that object directly you create a `Proxy` that enforces the contract.

    Doer doesIt = ContractEnforcer.enforce(Doer.class, new Foo());

They see nothing out of the ordinary, except that your contract is now enforced.

The names of invariant properties are discovered from the annotations, and are treated as the names of methods with no arguments that return `boolean` (or `Boolean`). When a message is sent to the object that expresses the contract, the invariant methods are subsequently invoked to check that the invariant properties still hold.

A useful design rule is to make the methods that test for contract enforcement `final`, so that a subclass cannot override them to violate the contract by e.g. always returning `true`.

## License

MIT. See the License file.

## Outstanding

 - It's been a long time since I've Javad, so I need to work out how to actually make this a functioning package/module that can be imported and used in other projects.

 - There could be value in providing a flag to turn contract checking on or off, for when you value speed over correctness and your computer is slow.
 
