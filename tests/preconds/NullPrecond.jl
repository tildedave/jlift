public class NullPrecond {

    Object myField;
    Object q;
    int{Alice:} aliceInt;

    public void test{}() { 
	if (aliceInt == 0) {
	    if (0 == 1) {
		myField = null;
	    }
	    else {
		myField = new Integer(3);
	    }
	    /*
	    myField = q;
	    */
	    myField.getClass();
	}
    }

    public void test2() {
    }

}