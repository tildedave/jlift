public class B {

    int{Alice:} aliceInt;
    int{Bob:} bobInt;

    void api1{*!:}() {
	Container[{Alice:}] aliceContainer = new Container();
	Container[{Bob:}] bobContainer = new Container();

	aliceContainer.setData(aliceInt);
      	launder(aliceContainer, bobContainer);
	//	launder(aliceContainer, aliceContainer);
	bobInt = bobContainer.getData();
    }

    void launder(Container fromContainer, Container toContainer) {
     	toContainer.setData(fromContainer.getData());
    }

}


class Container[label L]
{
    public int{L} data;

    public int{L} getData() { return data; }
    public void setData{L}(int{L} newData) {  this.data = newData; }
}
