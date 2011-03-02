public class FixableReturnValue {

    int{Alice:} aliceInt;
    int{} bobInt;

    public void test{}() {
	bobInt = getAliceInt();
    }

    int getAliceInt() {
	return aliceInt;
    }

}