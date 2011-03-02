/* goal: test how we get methods from the summary constraint info */

public class F {

    public void api{}() {
	Container[{Alice:}] aliceContainer = new Container();
	Container[{Bob:}] bobContainer = new Container();

	aliceContainer.getData();
	bobContainer.getData();
    }
}

class Container[label L] {

    Container() { }

    int{L} getData() { return 0; }
}