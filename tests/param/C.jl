public class C {
    void api{}() {
	Container[{Alice:}] c = new Container();
	pass(c);
    }

    void pass(Container  d) {
	//       	int f = 4;
	d.setData(4);
    }
}


class Container[label L]
{
    public int{L} data;

    public int{L} getData() { return data; }
    public void setData{L}(int{L} newData) {  this.data = newData; }
}
