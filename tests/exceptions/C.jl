import java.lang.SecurityException;

public class C {

    public Object{Alice:} getSecretObject{*:}() throws (BadRuntimeException{}) { return null; }
    
    public Object{Alice:} stringOfSecretObject{*:}() {
	Object aliceObject = getSecretObject();
	return aliceObject.toString();
    } 

}

class BadRuntimeException extends RuntimeException {
}