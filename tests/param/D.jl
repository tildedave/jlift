public class D {

    void test() {
	//	Container c = new Container();
	new Container().setData(3);
        new Container().setData(4);
    }


}

class Container[label L]
{
    public int{L} data;

    public int{L} getData() { return data; }
    public void setData{L}(int{L} newData) {  this.data = newData; }
}
