public class A {

    protected Integer{Bob:} myInteger;
    protected int{Alice:} leak;

    public void foo{}() : {Bob:} { 
	possiblyThrowException();
	/*
	  int{Bob:} test = myInteger.intValue();
	    int{Bob:} test2 = myInteger.intValue();
	    int testTaint = test;
            int i = testTaint;
	    //          } catch (NullPointerException e) { }
	    */
	leak = 4;
    }

    public int someException(Integer i) throws (SomeException{Bob:}) { return 0; }
    public void possiblyThrowException () throws (SomeException{Bob:}) { }
}

class SomeException extends RuntimeException { }
