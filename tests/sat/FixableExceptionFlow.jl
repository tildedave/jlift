public class FixableExceptionFlow {

    int{Alice:} aliceInt;
    int{} bobInt;

    public void test{}() : {} {
	if (aliceInt == 3) {
	    throw new RuntimeException("die");
	}
    }

}