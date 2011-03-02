public class IllegalArrayCall2 {

    int{Alice:}[]{Alice:} aliceArray;
    int{}[]{} bobArray;

    public void test{}() {
	for(int i = 0; i < aliceArray.length; ++i) {
	    bobArray[i] = aliceArray[i];
	}
    }


}