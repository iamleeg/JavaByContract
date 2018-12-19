# Java by Contract

This is an implementation of Design by Contract in the Java language. Where previous implementations have relied on modifying the source code by extracting contract properties from Javadoc comments, this attempt uses runtime annotations and reflection to investigate the contracts.

## Usage

You can define invariants on classes or interfaces, but it probably only makes sense to define them on classes because the interface doesn't know what the implementation details of your class are so can't predict the class invariants.

You can define preconditions and postconditions on methods, and it makes sense to do that on the interface that defines the method. This makes the interface a true Abstract Data Type: it defines both the messages that can be sent to an object, and the expectations a caller can have when sending those messages.

    public interface Doer {
	    @Precondition(name = "inputMustBeOdd")
	    int doAThing(int input);
		default boolean inputMustBeOdd(Integer input) {
		  return (input % 2) == 1;
	    }
    }
	
    @Invariant(name = "getAPropertyThatMustBeTrue")
	@Invariant(name = "getAPropertyThatMustBeFalse", value = false)
	public class Foo implements Doer {
	    public int doAThing(int input) {
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

A method precondition takes the same arguments as the method being invoked, with the distinction that primitive types are promoted to their boxed types due to the way that the Java runtime resolves methods on reflection.

## Useful Design Rules for Contracts

1. Make the methods that test for contract enforcement `final`, so that a subclass cannot override them to violate the contract by e.g. always returning `true`.

2. Define preconditions and postconditions as default methods on the interface that declares the contract where possible. This puts all of the information about how the contract is satisfied in one place. It makes it easier for an implementation to adopt the contract.

3. Use [Command Query Separation](https://en.wikipedia.org/wiki/Command–query_separation) to design interfaces that have simpler contracts.

## License

MIT. See the License file.

## Outstanding

 - It's been a long time since I've Javad, so I need to work out how to actually make this a functioning package/module that can be imported and used in other projects.

 - There could be value in providing a flag to turn contract checking on or off, for when you value speed over correctness and your computer is slow.
 
 - You don't currently find out if the constructor fails to satisfy an invariant.


## Credits

Java by Contract is a product of [The Labrary.](https://labrary.online)
