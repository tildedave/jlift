import javacard.framework.*;
//import java.util.Vector;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class ExcTest {
    
    // signal the the PIN validation is required
    // for a credit or a debit transaction
    final static short SW_PIN_VERIFICATION_REQUIRED = 0x6301;
  final static short SW_VERIFICATION_FAILED = 0x6300;

    String{Alice:} str;
    OwnerPIN{Alice:} pin;

    public void excTest{}(APDU{} apdu) : {Bob:} { // throws (ISOException{Bob:}) {
        if (str.length() == 0) {
	    ISOException.throwIt(SW_PIN_VERIFICATION_REQUIRED);
	}
    }

    public void excTest2{}(APDU{} apdu) : {Bob:}  {
	verify(apdu);
    }

    private void verify(APDU apdu) {
	byte[] buffer = apdu.getBuffer();
	byte byteRead = 
            (byte)(apdu.setIncomingAndReceive());

	if ( pin.check(buffer, ISO7816.OFFSET_CDATA, byteRead) == false )
	    ISOException.throwIt(SW_VERIFICATION_FAILED);
    }

    public void excTest3{}(APDU{} apdu) : {} {
	if (str.length() == 0) {
	    // do a jig
	}
	if (str.length() == 3) {
	    ISOException.throwIt(SW_PIN_VERIFICATION_REQUIRED);
	}
    }

    int{} publicInt;
    int{Alice:} secretInt;

    public void excTest4{}() : {} {
	try {
	    if (str.length() > 0) {
		if (str.length() > 0) {
		    throw new ISOException((short) 0);
		}
		secretInt = 2;
	    }
	    else {
		secretInt = 3;
	    }
	    
	    publicInt = 3;
	}
	catch (Exception e) { }
    }
    /*
    public void excTest5{}() throws NoSuchAlgorithmException {
	Vector v;
	BigInteger{Alice:} a, ai, ss, m;
	int n;

	if ( n == 0 ) {
	    do {
		ai = new BigInteger(m.bitLength()-1,SecureRandom.getInstance("SHA1PRNG"));
	    } while (ai.compareTo(m) >= 0);
	}
	
        v.add(new Integer(publicInt));
    }

    public void excTest6{}() throws NoSuchAlgorithmException {
	Vector v;
	BigInteger{Alice:} a, ai, ss, m;
	int n;

	if ( n == 0 ) {
	    if (ai.compareTo(m) >= 0) {
		ai = new BigInteger(m.bitLength()-1,SecureRandom.getInstance("SHA1PRNG"));
	    }
	}
	
        v.add(new Integer(publicInt));
    }

    public void excTest7{}() throws NoSuchAlgorithmException {
	Vector v;
	BigInteger{Alice:} a, ai, ss, m;
	int n;

	if ( n == 0 ) {
	    do {
		ai = new BigInteger(m.bitLength()-1,SecureRandom.getInstance("SHA1PRNG"));
	    } while (ai.compareTo(m) >= 0 || true);
	}
	
        v.add(new Integer(publicInt));
    }
    */
    public void excTest8{}() : {} {
	if (str.length() == 0) {
	    throwException();
	}
    }

    public void throwException() {
	if (str.length() == 0)
	    ISOException.throwIt(SW_PIN_VERIFICATION_REQUIRED);
    }

    
}
