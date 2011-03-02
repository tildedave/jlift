public class E {
    int{Alice:} aliceInt;
    int{Bob:} bobInt;

    public void api{}() {
	Container c = new Container();
	c.setData(aliceInt);
	bobInt = c.getData();
    }
}

class Container[label L]
{
    public int{L} data;

    public int getData() { return data; }
    public void setData(int newData) {  data = newData; }
}

