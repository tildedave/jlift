public class A
{
    int{Alice:} aliceInt;
    int{Bob:} bobInt;

    Container aliceContainer = new Container();
    Container bobContainer = new Container();

    public void foo{}() {
	//	Container[{Alice:}] aliceContainer = new Container();

	int harry;
	/*
	aliceContainer.setData(aliceInt);
	aliceContainer.setData(bobInt);
	harry = aliceContainer.getData();
	*/

	aliceContainer.data = aliceInt;
	aliceContainer.data = bobInt;
    }
}

class Container[label L]
{
    public int{L} data;

    public int{L} getData() { return data; }
    public void setData{L}(int{L} newData) {  data = newData; }
}

