public class FixableInterproceduralException {

    int{Alice:} aliceInt;

    public void test{}() : {}  {
	verify(3);
    }

    private void verify(int k) {
	if (aliceInt != k)
	    throw new RuntimeException("die x 2");
    }


}