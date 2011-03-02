import java.lang.Comparable;

public class H {
    MyComparable mc;
    Comparable c;

    public void foo() {
	//      	mc = c;
	c = mc;

	mc.compareTo(new Integer(4));
    }
}

interface MyComparable extends Comparable {
}

/*
class MyComparableImpl implements MyComparable {
    void blah(byte b) { }
    void blah(Serializable f) { }
    }*/