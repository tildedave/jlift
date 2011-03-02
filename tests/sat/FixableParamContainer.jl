public class FixableParamContainer {

    public void test{}() {
	Container[{Alice:}] aliceContainer = new Container();
	Container[{}] bobContainer = new Container();

	aliceContainer.setInt(bobContainer.getInt());
    }
}

class Container[label L] {
    int{L} myInt;

    public int getInt() {
	return myInt;
    }

    public void setInt(int k) {
	myInt = k;
    }
}