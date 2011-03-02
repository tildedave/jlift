public class FixableInterproceduralException {

    int{Alice:} aliceInt;
    int{} publicInt;

    public void test{}() : {}  {
	if (aliceInt == 3) {
	    verify(3);
	}
    }

    private void verify(int k) {
	publicInt = k;
    }


}