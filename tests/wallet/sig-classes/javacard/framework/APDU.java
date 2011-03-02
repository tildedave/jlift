package javacard.framework;

import javacard.framework.APDU;
import javacard.framework.APDUException;
import java.lang.SecurityException;
import java.lang.Object;

final public class APDU extends Object {
    static int __JIF_SIG_OF_JAVA_CLASS$20030619 = 0;
    
    public APDU javacard$framework$APDU$() {
        this.jif$init();
        {  }
        return this;
    }
    
    final public static byte STATE_INITIAL = 0;
    final public static byte STATE_PARTIAL_INCOMING = 0;
    final public static byte STATE_FULL_INCOMING = 0;
    final public static byte STATE_OUTGOING = 0;
    final public static byte STATE_OUTGOING_LENGTH_KNOWN = 0;
    final public static byte STATE_PARTIAL_OUTGOING = 0;
    final public static byte STATE_FULL_OUTGOING = 0;
    final public static byte STATE_ERROR_NO_T0_GETRESPONSE = 0;
    final public static byte STATE_ERROR_T1_IFD_ABORT = 0;
    final public static byte STATE_ERROR_IO = 0;
    final public static byte STATE_ERROR_NO_T0_REISSUE = 0;
    final public static byte PROTOCOL_MEDIA_MASK = 0;
    final public static byte PROTOCOL_TYPE_MASK = 0;
    final public static byte PROTOCOL_T0 = 0;
    final public static byte PROTOCOL_T1 = 0;
    final public static byte PROTOCOL_MEDIA_DEFAULT = 0;
    final public static byte PROTOCOL_MEDIA_CONTACTLESS_TYPE_A = 0;
    final public static byte PROTOCOL_MEDIA_CONTACTLESS_TYPE_B = 0;
    final public static byte PROTOCOL_MEDIA_USB = 0;
    
    native public static byte getProtocol();
    
    native byte getLogicalChannel();
    
    native boolean getNoChainingFlag();
    
    native public byte[] getBuffer();
    
    native public static short getInBlockSize();
    
    native public static short getOutBlockSize();
    
    native public byte getNAD();
    
    native public short setOutgoing() throws APDUException;
    
    native public short setOutgoingNoChaining() throws APDUException;
    
    native public void setOutgoingLength(final short v0) throws APDUException;
    
    native public short receiveBytes(final short v1) throws APDUException;
    
    native public short setIncomingAndReceive() throws APDUException;
    
    native public void sendBytes(final short v2, final short v3)
          throws APDUException;
    
    native public void sendBytesLong(final byte[] v4, final short v5,
                                     final short v6)
          throws APDUException,
        SecurityException;
    
    native public void setOutgoingAndSend(final short v7, final short v8)
          throws APDUException;
    
    native public byte getCurrentState();
    
    native public static APDU getCurrentAPDU() throws SecurityException;
    
    native public static byte[] getCurrentAPDUBuffer() throws SecurityException;
    
    native public static byte getCLAChannel();
    
    native void resetAPDU();
    
    native void complete(final short v9) throws APDUException;
    
    native void undoIncomingAndReceive();
    
    native public static void waitExtension() throws APDUException;
    
    final public static String jlc$CompilerVersion$jif = "3.1.1";
    final public static long jlc$SourceLastModified$jif = 1196711306000L;
    final public static String jlc$ClassType$jif =
      ("H4sIAAAAAAAAAM1dC3wU1bmf7AMIDxEIEB4hgYQGBBJCAiQktmxeEFiSkAcg" +
       "KutmM0kWNjv7mA0B\nH6XFKlattShC2+ujtdWqxVZb9Ie9rbfaKqW2Nb310f" +
       "60Qltvb6nW+qtX64Vf7/m+MzNnZs5kZ3ah\n9ub32zOzZ8/jO///933nO2fP" +
       "bB55S/AmE0LBznBfmbwnJibLNoT72oOJpNjbLkX2dJGsQGjkhrvv\n+1H1H5" +
       "9zCW6/kBtMyQNSIizvkYWL/TuDQ8HylByOlPvDSbnWL1wcjiblYFQOB2Wxtz" +
       "khDcrCfH+M\nNNUfkeRycVgujwUTwcFy7Ky8vSESTCZJtTGYm4wL1wk5wwmh" +
       "SK2hCEUlwsJUpDXFZ3f/9MYNc9zC\n5O3C5HC0Uw7K4VCDFJVJF9uFiYPiYI" +
       "+YSPp6e8Xe7cKUqCj2doqJcDAS3ksKStHtwtRkuD8alFMJ\nMdkhJqXIEBSc" +
       "mkzFxAT2qWb6hYkhiYwpkQrJUoJKSOTtC4uRXvWdty8S7E/Kwgw2Ujq+Zsgn" +
       "wxsf\nJoIl+oIhUa3i2RWO9spCobmGNsaSjaQAqTp2UCR4a115okGSIUylyE" +
       "eC0f7yTjkRjvaTol4pRXqR\nhdmjNkoKjYsFQ7uC/WJAFvLN5drpR6RULgIB" +
       "VWRhurkYtkRYmm1iScdP25iJ5z7b/n6RC2XuFUMR\nkH8sqTTPVKlD7BMTYj" +
       "Qk0oofpMruaLksNdclCKTwdFNhWsa38Ilu/x9/UEjLzLEo09azUwzJgdDZ\n" +
       "VXMLRny/z3WDGONiUjIM5BtGjsrbrnxSOxwj1jBDaxE+LFM/fLrjx5fte0g8" +
       "4xLGtQhjQlIkNRht\nEXLFaG+Dcj+W3PvDUZHmtvX1JUW5RfBEMGuMhO8JHH" +
       "3hiAhwjCH3saA8gPfDMUEQJpHXTPK6WKB/\neJWFcb72xu4yYqOyUEzUdlky" +
       "ESoH/kPBRG95HzEccbeU2FWulhqGBifvzskhY5lrtqQIUcL1UqRX\nTARCD/" +
       "zuJ9c0bbzpgEvTLEUUWZiptl+mtV8G7Qs5OdjuTCNGAHovuIA/P1Z78eeWJY" +
       "8RX7FdyA0P\nDqbkYE9EJFYUjESk3WJvQEalmqJTYNUJTOwh+kdUORAhDVFX" +
       "EBOGEsICs54xe2xBbxMSR677xy/e\nDux+HFQCKMyD1qlohJBdVLaJizuv3H" +
       "DVgQVuKLTbQ6CFkSww+D6LtgOhPf8xfc3xp//+lEvwbice\nLtko9gVTEbm9" +
       "oV5KRYnbyNOyOkTiUaL+YI8Y8QsTqOMIEuNXzXdsLIR1CMB+0q9iUBEoX461" +
       "CAwT\nEqwRqFZIlLzEHoJA6O3Pbnr8pZOvLWJGJwslnC/ga4ItmzFuT0ghsZ" +
       "d4R9b8XR+uf+egt+a7LsFD\nHAQZm0xGBv5mnrkPg03Xqv4RxkJqTuiTEoPB" +
       "CHykojJeHkhIu1kOKvAkvAcLGEdeF5HXVMUq8Aof\nToVkGlV3IN00BvS+H+" +
       "wfs/zl7034kUvvqCfrJq5OUaZmP4XpTFdCFEn+a4fbD9751o2Xo8IoGiOT\n" +
       "uSrVEwmHhlG+mTlEQadZuKCy/Lw7Di3+8suqRk5jrfsSieAeUMjhT40UHHku" +
       "+G/EPRE3kQzvFdEL\nCNiToHYAaRnel+s+hPclxBSZEEyPfckk4YW4rLqZB0" +
       "7/vOBnXVQIcxNEqjmsEiohmWnDCdS8QOjl\n09eeeGzTD+93CS5CEeG7j4QE" +
       "4RCZ9+dyutugfQoKDLNdv1q4gCvcwj4G1ZtplkHpv3H/wl3jPhx8\nAPVtQq" +
       "+YDCXCMRiV4jvHJ8ODsUiYzMS9aCZkVpalDQRlmFaxz0QwmoyQQISaVhd+2D" +
       "QcS8AENxQk\n4QDxTIhKKdU4SBYOx+BSR8ApRsHURhg0rKFAaOW+P/7t8V8d" +
       "K6VzUaGxBld6/tGCd0oe2bEQ+QCt\nmGcee4cYJK6ZghMI3bYtXLx50ax1SI" +
       "FX2o3mVqgDNEam/lA4FiRTmHoH0VQCW4Fx+IhU+RzJSvOL\n/GOe/OY/3vk4" +
       "2oYOTpggzaSpfOmAgrTR6EE1ecq6pJgmUiC0/tQPf/uZu/JP6mEyVdCVrpie" +
       "n1/0\nrjge5dJILTaRqlXQEasXj0j2MSMfepn0tLzyqdmvL1l62/NUOjPtVj" +
       "W++vD79+8t/Xo/M6wmpVO4\ntFgxu5XEzIxZ6fa/VN6xv/keHbNIFxnvbixI" +
       "yYO0nqHdRhpeaAVevSTL0qAOwhXPL5/pe3DTt12K\nq2jWMFlsHJ6ppn6QFR" +
       "O/8v0/PPSNe9U2NuDg2nUD7cB0dYzazBZ8dynFY20M62yjdWL68sZ3rbTg\n" +
       "lRTFmIqg4Z3iCiEYMAc0zRCIq5PUYM/V7z1z9/giRstc9HV5EAkZ5nlDtUDI" +
       "9eAbNy7On/wqoWO7\ncNFAMNkSJTEfLBfEBOEnop+LzRGsqam93++++4MX5N" +
       "dRedk0CbWLh3n5twR1M3j1S0NTxnzrnkGX\nMJYEGhg+kLXUlmAkBdPRdrKO" +
       "SDYomX5hkuFz45qABsC1Whgw1zxF67o1T9AsECT3UBrucy3m5Ary\nmqLMyV" +
       "NMc3KOgCqxCyt8DNPFbAZN4nJtWBaKAoENLc2BzpZ1gbbmwAbfFl+gwe/r7C" +
       "xZsXx55fJV\nFTVJw/yADlXspcuAFydUHiha2ZeHJpSLFJF1oqzMBeOghvqe" +
       "Sj9RUSdVX5MW+ppyqq970xbktYRY\n2CBRqSFlnXP7vPvffPx3HXku3WKwmF" +
       "uP6evQBaE2kIQwP10PWPrZJfMfua7j9R7q16YaQ/amaGpw\n5T2viIvXTgxZ" +
       "LADcZBLHwCqHQdWn0T+dvJaS1zSF/mlm+nO0gAr0sQynfDEx9fS997//qRur" +
       "XRDw\neIdAb4lgF7NyrSlYt9/wyJ0FE+5442Y0Y7XlG3hVcstEc8PRIJrmEk" +
       "gkolOTOrt8XU2BltaWrhaf\nH8vHKWd0FJ9UWPq0LHh69shi+lGuBPehjDKP" +
       "HyXcHFCzD1qICPe3GuSbQeVr93WAfETOhrZNLa3r\nDIJC8vn0gq1QroJ6TS" +
       "PYFx0JNo0K1tztz16qZeQ1Q5Fqho1U9zmS6iIqVVt317q2LARaoyypBfWa\n" +
       "RqAHHQk0xyhQwN/Uuq5rfWBja9vW1kylA+3KV6TLt5Hu0Wy0K1vYQLtmKYLN" +
       "shHsWMbala1UdeQ1\nW5Fqto1U/+5IqgIqVVNHR1tHoLUt0LU8sK6pq6Ops7" +
       "2ttbMpU/lWk9ccRb45NvI960i+fL18XRWB\nlubGgK++raMrG8Ocq4g210a0" +
       "k5kYJhWtpS1TgarJq0ARqMBGoBFHAs3iuexoauns7M6YR9D+eYps\n82xke9" +
       "WZ9rd3tHW1NbT5A5uaGlt8gU2+zo2ZSgXxVqEiVaGNVG84kmqqJlXXZe1NWQ" +
       "l1CXkVKUIV\n2Qj1X46EmsCEWp6NNPMVaebbSPN2ptJUZOPdFyjSLLCR5j1n" +
       "3t2kRo1Nzb5uf8bOYC15FSuCFdsI\ndtaRYPNNgjW0tXb5Grr8TZ2dVLd82c" +
       "hYoshYkl7GHPeFkbE+G4tcqMi40EbG8RlaJJWxuzO9UMN8\nqy7WqrpuhpRu" +
       "Ic5le38Fo319g2uHG7f9deINwWevVBf/+2UhV5ZiyyLikBhRmh3iG9mE31ap" +
       "S8vJ\nWwtPNa964Frzenwm952jsV4gdPLVye80VZ1888JtucPbQuvd9cK0gw" +
       "iEpgzN2eweCD+PX4AqK2ru\nmztjpVrjOno87bXLsJpeoOkSRMofU16CetXp" +
       "EhJuoT9kKR0NwpJPp0OUm1F2jdN+CFpaSDxevyi3\nJyRZCkkRve5tQd3Ttp" +
       "whXWndwWpoqQgX2TlLMUddZNMPIKXdN9EMvG8ZJaeVVarEjHZdkQ4+B2WF\n" +
       "25y5GsC5ioGWKgCXOgMYBplTfJ6QVsvCFAKpX+oPh4KRhoFgNKpYUNwg37Q0" +
       "8llt1XcNhJPKVvmK\ns0///JNPNbyJWyGuEC7ci/3CuL5UJNKqreohXQPJFO" +
       "JoSrCRIqmvSB4Qi5IxMRQORorUXfGiBTJp\nfQGI36jtOWp7hg1kCJLM7W7/" +
       "qe3tp/fGxOdVl/Fx3GWZPPqXiZkpVA1VqI2cQtUgzJuY+tRwClXD\nKRStpF" +
       "OfGk6hamwUapHyEtSrWaGgyprzU56tVHlaJaI34Wg42t+s7tQZlWdGGllQ9A" +
       "ZKPNwiIldh\nbrMdRdiJbsdkbI8kRcRg1DFr2yhrAxxr21CWnYyjbRxr2zjW" +
       "aCWJcbSNY21bGtamKr51sYLUYmdu\nwIVuIK6bUbOlUyazKKGzPtXXJyZMNI" +
       "Jwc9MIRzHVis+2KY6wf5KDfR/K8WmG1z4OQUNOK6t0o6NK\nCWE281S4W43f" +
       "d9Jtz09cfajuudvfPIj77GOV3Wzc126VovjG4lyJrv5j7pK/up6aUYLfCXp6" +
       "gkk6\nnZoP5PDnbQzHaJDnCQbgZ9sBb+Qp3zlPM2yKx2IcRweVYKmdvjFDfZ" +
       "Dj5yDy80UnlSD5fCwWg4Mc\nDk04RXXpPk7OFHb7VWawKc6EU5wJ00oPMslS" +
       "nKypNCYMcF6ivAT1am/CbmbCkJxvmPRNWbiI2HFL\ntD4ihXZ1hvdSRVMjJb" +
       "PX9CYHpITsGPCjFPDvcYAfxc6/z+A9ygF+lAOcVnqGwXuUA/yoDeBLlJeg\n" +
       "Xj9ywJ+XhckE8LaUbI04lHnCMcAnKMD/yQF8Ajt7icF5ggP4BAcwrfQbBucJ" +
       "DuATaQCeLNCdk6UK\nwEs/6knpDbKMgBjD12jydCDZrDSS4UDMgcUZzLUNLO" +
       "Bj57HfKUrYOxxhp7Djdxk9pzjCTnGE0Urv\nM3pOcYSdsiEMoohlCizLPgrC" +
       "cpQDa0aGxgp06pqkiIJXEjICwk3DIRHPy9CTg0ttTg4aiuvcWJ4s\nFI3CoV" +
       "YF8TxLlo5JNNF+iQSqFro0Ow1oVrrkmuRYlzIw/nOoS66pnC6dg45deUxzzn" +
       "G6dI7TJVpp\nFtOcc5wunbPRpeXkVabAUvaR6xKI79WcqGscjmeeLEzXccmW" +
       "H6OwOpr4lqxe8k9g1VVIWS03s0o+\ngLRC45BkmFg15rSySqs0DtUiHXzOaK" +
       "zCdkO5Akv5BWLV2gmAL1qSpjdjSE43D3yJfmXv4KbLZj92\n7w+nv4UhtSsU" +
       "hhN73JnRXnG0ja1ULCYm9BtsrqEw3O3Uthtc9cOycAU0OSglYgPhUJG29UCP" +
       "ghYF\nE/2pQTEqFw0th1waqhfp9M8vRvvlgaJFPdCP2FsU7JGGxKKePUVXX7" +
       "Km6NrFMUWhKAvQZZ22AHdd\n6swE6hFxVwO9hxQV1LUum1b5Yz7d0V1RaXeU" +
       "rmJWvPTCT3/ZOHxU3SiZGBtOyzd0uBllqiW+ncMF\nPxaE9IZicSJNkUnRA3" +
       "fxX9ZeOf3WZlWoNXTEMT24rq2mTJnQTb+kWW1Qx7I06oiwXsnZ6Q4c4FXM\n" +
       "KndwdrqDs1NaSWRWuYOzUy3HfATML4WCEbbJm3dt/eoHXhO/TQ8Kpz3xZar5" +
       "40P9Vfc8+qhXRW6A\ndkuPQLkivJ0jR7cOI3jo5TjVg0RRP0jXQyIhbtgkVs" +
       "rWt9dCQr8y0h+FGZLCvY69bZ3FshUyGVd1\nHHt1HHtoT659jKs6jr06vZeV" +
       "BffQ8tILCgMkjhdmMEK43GwxdEhvzWb4X3A8fItJZokSPgjqVWdn\n8Qs6yU" +
       "DsX5GmN6bCdxPF3urM3Vfo3H1CDInhIbF+jywmR/X00P491PfgyUDTUx7rg8" +
       "mBTlF+JfLy\n9jtfu2QePfemO9evfH688TN3HnryiSr6IMhEMpCLP7GWDmuY" +
       "efvDzNsfcaZjdxtQgPRe1Jf7Ypm3\nGkef72xWuEsWJurhizuZEOKqOz9s5e" +
       "MPG318hcnHgzZUpdEGHPYTnJk8ieIeZ0bxJGcmT3JmQiv9\ngBnFk5yZ6HNc" +
       "O2mO4n4tjtno3G9F3Oh+FdYgeZ4yB+nXIHkOh4RNxs/H79wFyaMm6wJbnpfO" +
       "uiy+\n/9lAIgBl7j5985fEP3lvqFQf1RiMSVFR+Qq0lORE0fDCe5UnJej8rn" +
       "WeZ9M5PqlgOhWvda7/Aujk\nr2772VsfHN/PvgDC744082uQIhExBAvGZEl3" +
       "dFDqDffht03EJPcVfOHF2760r5ua7FL7Oix/Vr2w\n7+SO9+fhxJ0Tgscw2d" +
       "M9rFgtnZ1cXzEYvAuV3LxCeUVRfTTV9F+OWFk7pA+p6qXoDN3AcL6eOWw1\n" +
       "wx7Wz7CHOds5zNkOehvXm8xSDnO2c9g8w1aU/hM0HW5foE7nZ5D8groI3NBx" +
       "vUiZ+aUVPxodnIH+\nDcG2qIPK5/qaeSyY+07GLMDl7xZMQPq/WbDhFhyzYT" +
       "HhL1emYUG96uw0nuWEf357BW4v3StoiRK/\nQ9YkvmhvB52LLHzc7DTiW1mi" +
       "e4ozS8yIVfcYZNU9w8yqG78jd+drHJIME6vGnFZWqUDjUC3SwedY\nsQr763" +
       "C+aYUCy4oLxKqLFjORsFCZvEfrjVqaVvwSm+KysNlZpLfCsLCP9o4a5lms5t" +
       "3FWszkLrEf\nKxRbzCIx9xLkZylSviyLVqGhzXHn4Zh7gSzkamOMZxKLEZn4" +
       "WEyfCZPKClMsZscRDnwtp+v4cJm7\ngSmpj1NbQ04rq7TeaSXXToPilaeRko" +
       "Zr7k1pw7UVeoZxWHFD/KZyDUkn5RvSMkg2q+Xdm7RKOsMA\nb1Tt3DBW2RR3" +
       "bBiVmRsGjKES4Kg0aYKdUFjzck4TrkDQdjAKr+BIvYLTBFop5LSSThMA6Vp7" +
       "TRhI\nqwmVTBO6UZLLTZqwFXO3QQIPDLsvY5oQUcu7B+LnEd+44XiUuzqeyQ" +
       "6CZssGAop1YZ27mJtyirkp\nB12X+2oGdTEHfrF+yoFQaEWppQFQBDncYjrM" +
       "oHZl6b8WNLjcZAEcpDdnA97nHYNnMV/DCatKRX0r\nL9B87aZfm5vc0mKlh9" +
       "F6M7mlCpvistDtzC1VWbklvxTtdzpnH2Kz613244ViX9ZpIYb57nuQ9nuz\n" +
       "aDWuzdnx0Sdufn6Hdu+UhUmGEcczmsEPWc3gh4wzeJXJb9uxhjA8zmn/d1De" +
       "Y0xtv8Mp8nc4v00r\nPeW0ks5vL1ZmmNGkVPz202n9dpWeb21YObcYeqlI04" +
       "uFfZTbFWdIL7MpjiP4CYf0SRT1BQbRSQ60\nkxzStNKIk0qQ0ENZnAlA8jI1" +
       "A0jvg+RZBE4DO+cWuH1umEGLJ1LcPzE2q0MNgoRPOPcql9oUd+xV\nVmbnVW" +
       "AwXwftWWmi004wrHmKo/M0wvN7xsNpjpnTHJ200n87raQzHEC73t5wLJ5H0h" +
       "nOSsbub1CS\nU3FjwPMa5r4OCeyMuH/L9OVdtbz77bhF6NtIXhuda0OLTXHH" +
       "2rDqPLVhlUkb7ATDmmc5bcATHO5/\nMBq5ExzGnFatksfjtJJOGwDtVltt8O" +
       "Sm1YZVTBv+B8U/a9KGDzD379ASzpYfatrgmaSW9+TGR4/k\nzEtqQyRnUiA4" +
       "hQQbyLnKkPAqC9M7xVAKfinQdBKpVH8SSfkNO6uSxkNIc9ivBHCl6cQNycPx" +
       "jCLL\nQ1bh+CF9OH6IiygPcRElRiWeBYz6Q5wyHNJHlBBQV2FAnYUTpx6As/" +
       "uYzuah/ZWllkZPNYbTk5hO\nR6D2qtILpSTQRJ6W65l5PlRBA6ss6IL86mwo" +
       "u9QxZUgRfwSwStH4KpMRQ3rhNu3gaaCyNL2ZHPRS\nm+KysN2Zg15tfRrHF+" +
       "3tJO7a4UrA06DF7J5GR1rkaWEK6tmIRPmR+01ZtBrPcPfOUy8LU/nBxjNZ\n" +
       "BBDh+EWAPhNc+GrT7GXHGiJwlVn7PUEUmu3DkAyTIhtzWlmlAaeVdLMXqGJF" +
       "GimV2Wsw7ey1Wk81\nDitumL1U0iFJUuIhRdHjannPYNwiloHZdI1zU6m2Ke" +
       "7YVKrPw1RgMJ2AS7VJJeykw5pXcypxDaJ3\nHePyGo7daziVoJX2O62kUwmA" +
       "/FJ7lTiQViWqmUrgAyKeq00qsRtzMfkcJHuYStyilvcciJ/H1pQH\nfmjNAz" +
       "uGzmclzagNBDToAgjyxjQbGXNa46oz83yJQd3Agd9gDiBWl1paAkWQwy2mww" +
       "xqV5f+a0GD\nyzcsgIP04WzA+5Zj8CymcthFWKmo78oLNJU7ffzC8136aEtD" +
       "KpEgrgR+ydrqm9NZaUTEEZm+OfU8\nzbzLBXsOw3OMMvcjjrljOJTnGE/HOO" +
       "aOcczRSj9lPB3jmDuWhrkZAtuUEtSrPXMX4Mkks1lwEa7n\nRfqAmEKq9iRt" +
       "XCOJPk4/2THyIxT51z");
    final public static String jlc$ClassType$jif$1 =
      ("nkR7C/NxjOIxzyIxzytNIfGM4jHPIjNshDFLBaQX71/yPk\n/yQLeUbkLZ+2" +
       "hSHMtRkCHTZOL++xhRm5h1t8n9mTlJ4zlMQPORLPoOhnGWVnOBLPcCRiJW8O" +
       "o+wM\nR+IZGxIXKVGGoF4/EhJ13s87RhYmAV1+n+GXEbL5yQnvWITXyz3ZQz" +
       "6AlD3ZQzJM8BpzWlkl9mSP\nWqSDz7GCFzZB4AfnahR4a6zghSrn9wsB3nmy" +
       "kJsQScypeHPn87CXPjPjLeXwwhOC3sUMHe6ZGS/3\nzIxSqYyhwz0z4033zI" +
       "z6KwZrFLzWZIaX9anlmYqVj9YoFMQ4yVtNwtBOZ7F+jS7Wh7OWEVEWnT+b\n" +
       "4q3KaChUNr2ckK5B4mqzaDXufCnsrZSFceoA45ksgIk0/AJYnwkhfw1VRANX" +
       "hWm4wiG3crrahqJu\nZprZxulqG6ertNIWppltnK7qc/BMMcmhSxnv5bxX1C" +
       "1lauKGlYvKGCQByhqkdZDswCFhk/HzCMK9\nlZDgQyPOjb/KYuUCmQy2Kg7I" +
       "Kg5IVDtvhMFWxQFZpTd6WHvUlP5rhw2X3RZDh3RPNsO/zvHwLXwe\nnP2rVV" +
       "S+9p80R+yXhRnEO0n8Gc3MwLuegncLB9712M3nGFTXc+Bdz4FHKx1kUF3PgX" +
       "d9GvDU+KVO\nAa/uI4tf7DXzMIlqdgfDctOwLEaT9H8WZIDzEYrz/RzOR7D1" +
       "rzNUj3A4H+FwppUeZqge4XA+oseZ\neDEPBBNwej+f+09M9L8HhRaMXLXomd" +
       "iUE/SXWNT/8DNO+dEp/U+I6+7HxBJiXxjBQpyESRjneR+T\nhan8+pQENto9" +
       "iOf9Ni1NFszj1NLw/hgFKE/W/5I0/QV0hT/8pbq0/3TgyOW3x6LLfeFR/1PG" +
       "TOPP\n1aXov64KhP4c27HuNx2/fUj53wEaXOKwXIb/1Er9TTmtxvFnFt7061" +
       "+/Wkx/+j8UCe7dC/2N9Qtj\naaiB3cO/wZo/amtqW+E3Xur77P7fT1afuMB/" +
       "fTBFp/o49HmjtwP3WybVvrLx+PEHzb/SJ+jQ0w0f\n66hPP1Ytem/sBy+8u9" +
       "Yat/8Dp4UWpV9sAAA=");
    
    public APDU() { super(); }
    
    public void jif$invokeDefConstructor() { this.javacard$framework$APDU$(); }
    
    private void jif$init() {  }
    
    final public static String jlc$CompilerVersion$jl = "2.3.0";
    final public static long jlc$SourceLastModified$jl = 1196711306000L;
    final public static String jlc$ClassType$jl =
      ("H4sIAAAAAAAAAMW8a8z0WJ7YVW/3TM90Te/OdM9l5z49Mz3JTLzTtstV5arp" +
       "AeJy2WW7XL677HK0\netfl+/1eviy7YhVpJyQKAe0GEgmyRAoKgpWArEg+cF" +
       "lEFgIkILSgbD6Q5ZKAiCCB/YCyQoHgep7n\nvb/d7zs7w8wj+fLYx+f8zjn/" +
       "2yn571/7+5MPV+Xk7TyLey/O6nfrPneqdwWzrBwbj82qUsYL963v\nfP0ftX" +
       "/je8wXXp183Jh8PEjl2qwDC8/S2ulqY/JG4iRnp6ww23ZsY/Jm6ji27JSBGQ" +
       "fDWDBLjclb\nVeClZt2UTiU5VRZfrgXfqprcKW/afHCRnbxhZWlVl41VZ2VV" +
       "Tz7BhubFBJs6iEE2qOr32MlrbuDE\ndlVMfmFyj5182I1Nbyz4GfZBL8CbGk" +
       "Hyen0sPg1GzNI1LefBIx+KgtSuJ195+omHPX5nPxYYH/1I\n4tR+9rCpD6Xm" +
       "eGHy1i1SbKYeKNdlkHpj0Q9nzdhKPfn8+1Y6FvpoblqR6Tn368lnny4n3N4a" +
       "S71+\nMyzXR+rJp58udlNTV04+/9ScPTZb/Gtv/D9/XPiHb79yw2w7Vnzl//" +
       "D40JefekhyXKd0Usu5ffD3\nmnd/hT41X3xlMhkLf/qpwrdlsG/8FZX9X3/j" +
       "K7dlvvCcMvw5dKz6vvWPll/80m9hf/f1V68YH82z\nKriKwhM9v5lV4e7Oe1" +
       "0+yuJnHtZ4vfnug5v/kfSfnv6Zf8P5316ZfJSevGZlcZOk9OR1J7Xxu/OP\n" +
       "jOdskDq3V3nXrZyannwovrn0Wnbz/zgcbhA71+H40Hiem7V/c97lk8nkJ8bt" +
       "p8btE5Pbv5tjPfko\nJmzVd8PArSdfH4X421Vpgdf5t8zSBt1ylIg2KyPwQa" +
       "nuWuFPtPfujX354tN6FY9CSGWx7ZT3rb/4\nd/6Lf5rY/7N/7JWHknWHUk9+" +
       "6kH97z6s/91r/ZN7927q/aknx+g66PZVN/73v/TeJ/65b1d/+ZXJ\nq8bk9S" +
       "BJmto8x86oU2YcZ61j369vhOrNxwT4Rm5GoXvjPMrfKMr347GiG3kfB+VSTr" +
       "72tJw90k56\nPDNH4fmtX/jH//U/uN/++lUkrlP4qWvtt2jjhES3bG98S/4Z" +
       "5mf/2NdevRZqPzQO7bUn77y49vvW\nP/jjh1//m3/9b3/zkTzXk3eeUbNnn7" +
       "yqydP4QplZjj2aoUfV/0v/N/V//vKH1//uK1eZeH20PrU5\nysyoyl9+uo0n" +
       "1OW9B6bnOlivsJOPuVmZmPH11gN7Ma39MmsfXbmRjY/dnP/kP779+3+v21XQ" +
       "rv/c\nCtxbeJbko5SWb++ckcOsHTu/larr2D7Vnxsj93t/9DXot//9j/0nrz" +
       "xuDz/+mOGUnfpWu958NDVK\n6Tjj9b/9Z4Rf/tN//3t/5GZe7iamnryWN+c4" +
       "sLob1k/fG+Xgk8/R9Hc/+6lf+Re/9S//9oOJ/+Sj\n2rGyNPvrvHe/+Ftf+r" +
       "N/zfxXRiswamMVDM6Nsk1uWpo8aOC6B27Of/qxm9f/374rcpXFp/WJvPqB\n" +
       "BxOZnH/u//qrf2769i3M9ZnP31Tz2epZu/fEg/et4T9U/9zv/Zf179yM3yMJ" +
       "uNbxle7ZZo/mY8K5\n+puXN1/7t381eWXyEWPyiRvfZab10Yyb6+gao/ep8L" +
       "uL7OQnnrj/pCe5NZvvPZTwLz4tfY81+7Ts\nPTIf4/m19PX8I4+L2zgQHx03" +
       "eNzevLNvN8frzU9cd2929yb59WR188DXb/Z/4JFAVDcuv6snb9+/\nz9DkfZ" +
       "ne3efJ+wx2xO7jLCbL78wgCIGW8Po54y2UQTKa8cudn/kXvvwX/pdf/zvSp1" +
       "55zBl//Rl/\n+Pgztw75pkPTvBtb+OoHtXBT+jeBr/7aL0i/c751VG89aTKJ" +
       "tEkWv/q3nG/94Tes5xjgV8eQ4Vbj\nrvv5wyH89LhdBfSTd0P4yaeH8N5DHb" +
       "vO6bv0GHl4TvnW//Sv/oV/+IvfW71y1YEPX65zPyJ94lE5\nrrnGT7/0a3/6" +
       "Sx/7lf/hT9xI8IOaiWen49V6nP0gNW/c+jevu++O8/ITsoIpxH2aoxUaY2/K" +
       "/5P5\nrfJg9eRD57523q9Li3H71F2XPvVsl64nuweXxefwXM/ZJ2A+cwsjYN" +
       "IVZoTC+QPN7a63ufejmN0d\nJw+OH0ChvRTFJ28pSJV9SYRvj9tn7hA+8wKE" +
       "n3kphJ+8ReBVZce/qPXv3AUgkwfHD2jdeqnWv/Bk\n6/dZgtsp1P09x2vcB6" +
       "JcJeKzdyiffQFK8PuRiJcakKtEfO6O4nMvoMi+b4l4KYTvjtvn7xA+/wKE\n" +
       "5qUQvnSLQEgSL93n+PsKdH9HKBIhCzwnEx8Ig47bF+5gvvACmJ97KZjPPg6j" +
       "wPdpcnsf2/CS8kI1\n+eIdxxdfwPGL34+a3HLQ/Ae2vhq3L921/qUXtP69l2" +
       "r9c89OiUTQsqx+8HRcxfPLdyBffgHIn3w5\n8RQkXuFxnr1/ILY0dv+AyfsP" +
       "RLj686/cIXzlBQi//FIIbz1EUE4C8WKCPzRub98RvP0Cgj/7UgQf\ne0QAvb" +
       "Dpr941/dUXNP2r32/T8Ac2fbWPX7tr+msvaPpfezn7+NTUbwkSU9kP1sM/PG" +
       "5fv6P4+gso\n/s2XovjqUxQ4zykYrrCELN/KA/ZCoHfugN55AdC/88MB2rxQ" +
       "P75xB/SNFwD9le9TP26BVPkZguvu\nq2P1Y5iGvAu/eyNJ/8FLVf5TYWy982" +
       "D5d3TKKsjSd8LAvXnsrfrxkPH2h6fntDpGoD/5qBibpd57\nf+Lv/vN/4099" +
       "/b8fw0/mQfh5LU2Nkf1n/vyftG7G4zevu9+oJ5+/IshZU1oOa1b1IbMDN3Ds" +
       "hxSP\nx5Rx9lyC+u0JNa9o7MHfAbZxpFXhc+Ki2IHedgRNbOa+dxJ3mw2DY4" +
       "EY1UnnEGQgR5jl7Zs6OUCR\nxoUXNMGgzZRa6uQ+Nuta9vEZ0IS1rRXd0Tiv" +
       "aWddnl1zX2gz7ozAkLuExLi+nJepBhS1vpwfXRRM\ngMPApUa1iAzE9Kfr5o" +
       "zMBn4wVmDIgyC4RpCUn7nCebNYyCCWEBC7GdKNm5zblAnOLJcp6CzjmZwL\n" +
       "z2ykz2xmV4m7MPLLy2UKwBRMLdYDri3BaFMfepGT6WC4RCeNBZVmK66oPt70" +
       "fCxctvI51Vi4tcLg\nHJ9Ikd/MtK4eIn9rlbhLAYcpx6lu5Ei9dfSasFWV1i" +
       "J7dN+vLjIhK7jB1MrulMzbsY5o0baJL+9n\nPk6ocVsxar7VSQNxwfXhxFkj" +
       "z3TBI/iCwiG1RffzI74lmSNDrhsyOAnBiQZOspAvnZXuoNZeLfBw\nZmcFY2" +
       "0xGYmqvVxTdVtLUbNdmTF5YKYHO/KZQh9nTbaSaCA7TJkV2nnpnyI/OLV5JJ" +
       "aMmjF7mVdq\nfN5I+kWz634lowS4b712poYFYFRe2naVO2UyOgYy2ldNJJhJ" +
       "Gr+ucjw7JzuuMuXQFLFCyWcqLs1N\nGF5k0U6m1T7uCQeHXLRXwkgKL6aSxf" +
       "Nc0gtp6ttJyEXZPjxVpIg2OKgxGHlhuYVd5tJSUlmP3hw1\nyW/K6FRH0ilJ" +
       "4EC2bGG2CJjFgJ7BdCnhM70h1YM0jahx7asownEfb9HNQTmFYgRlmZsfysZk" +
       "o4qz\nrRjfK61Ncr5WmciKjhuwW4MqEbIDS2TQ2hzUdmA8e2tMj/qBldIeHB" +
       "Btu+O5QA3pw2JT78KqPJdp\nJ/NdG8LhZmt2BMCSB9Bly7BbuLUDeat5x+5q" +
       "0Q/FmI44ya+nhM6S5g7YWiGgIaMsqQaV1BcdZlRt\nIbkb4rBrDj6/YigK8Q" +
       "PMb0OcQioTW3mCuz2ohMjklK4I+6W234BTh9FI64zOumbenwq+V4lBteOo\n" +
       "J+SMAiNhI+3WC2ogLOvsqDNSa2K/2G0C9zJUkX9axGVdiLRqsbBaSPpUwSR8" +
       "Cw4Ns7ag83nIPeIy\nGmdV1Ywgr2MgqY+rHSpWC94OGwXndhiVXRoaAwXYQF" +
       "eOozGnlaSjpN1jkDDdFufoRKwJhjypISFH\niMZcRMKMk4JRTpzKKHzrA5Gt" +
       "8hLmb4+kTmz4HVHReLWYd86s25y7gywtUbTrQWe6bTqAIkV8f+i3\ncBFnPr" +
       "Oxj6zJBENzQajt0ROPzZ5VzuISJyO5bYHtkq093hfcVedaSJeDIBATl/YcWM" +
       "6UraNs7R0V\n4pyss+NiL53Sc8/xPR8pgWqywIlenVZipRTjGpNoOH3Q7Yql" +
       "JRg/bACoWAfqhsj0+RLGsVaZEkUr\nFaRBx+xAUBmDLXlEOKflKjf4C6sxO6" +
       "at4hQHPXNO18lANgrk+ZVhz6X1eaVLQOMCjlcDJspW5HZK\nYaKRQN1uc1kk" +
       "aRkJHoToxWUudRFTKE2gEJmwwak+ykLGoSKfbuCI31+w6IJZ7I6wHXJB6ceD" +
       "Wqyw\nzJ0q2THaxUeE5iBoc5EBCmjdChxiFELmkXq5FLhbMsWCV00jEeZ6rp" +
       "A2ThIpYM7d83aNLuBspmcr\nOtsFvDJt5PJItAnlxzsgUWJCNG2j2eM9yLbo" +
       "NkCAVdUUUrQIS7eSAwdz1EPoK3NIpaioAXYgIUgE\nEonDhbQJdopzgZJRJ8" +
       "yUUicGKsAXrLjbyAZB9IJrRdH8pM1LtjA2EApe+E2qH0Xa6jiiCJOKCDI/\n" +
       "0dItbssrkQ/jKbR2HGc3KFtgjeDyAiecHRrORWIu4cYasPumbjxXzPZblrMv" +
       "cDH38IvVhvMdbNR5\nHFH5KD9gGyOh4HerKSYca7EATltcxw9ScJyHF4pZ8d" +
       "tKDU7DQV6buBEdczPeilKckGY3QGKbZL0o\n0rJ59G1gthUteYURXCdG07m4" +
       "7RTa3+9tdS5vTnwHbGRpheVSRamS70HlnsRMXgMEEK1Xx5W9Xx4H\n1dx1m9" +
       "hegvUOt6HMmZ0JERDxeprsLMeZWydmvzQ8/0Aj51Ots90CMjtYpkl9liHJsf" +
       "C2HGEmYIzN\nyUBSEiqjoKOIa3OyA0YjDUgDI194dgrueAfkTyByieaz7MQr" +
       "uecNppy7fIJ5pSItCQJ2rGKZM16i\nBjgE1qLggo3RgIywTIj+aOPHMg+cFA" +
       "ahaRl6zsJmaFYxpSLWRx2v9BAKZwf54Dp5CtXs0Bd2cmHh\neDkDOZUPhfoY" +
       "agBw2aJrYFWDegYelm2SH/n5tCa9rlnoxEB3xtI1wSO37C5g5FBnjia79HIO" +
       "Oro/\n8VBaULbk546JHZ2LrLBQgY32RBfYBZg7yFap4dl+WsxC7UDGFcUs8z" +
       "xdiqTEg6sLBAqCzDlrdcY1\nW2+WqlvU2kD2aTCHjbGy+5Q8zaDDctaHtsYu" +
       "V5WBlGfFm8aSQiZ04c0NJMAD4nTALGiQcLpYCOrp\nAB7RAR7WoBdIVW2tqe" +
       "OCwCJjkcbbWt3Sawc7cz3Kp2BbZBqPTi2OxA5cJDMK4bIbd7R9RJrlADtI\n" +
       "u6owkzUCjH/zg77gbMmxIwg/a6yPpIu6leADEsIyxCldOpRptNOnB+ZE7HwP" +
       "iFhdjRMs2tiCZJd7\nXt6eskRf+FFDDvNFgW0qHNZ8r4WHjlyiGayssYrAFU" +
       "TdlI3VI8kCK/tpUrCH/sgdLGTPe3jR4X29\n2nICecYMbp4ytbYx91hGcKw1" +
       "2wy5AXvnc9JiZ5eMAa91wBLFO6PSu8XCZY5TUjkyLdhj21ivcqvB\nyN5Wqc" +
       "A3Z4ZDMbnMkgwJZ8Vw2Di8518KRw7XkirbR31x0IA874tijULdOapP0mk6wz" +
       "dSIZNxqgvo\nRbi4rtAhlB41iLZvE7IRls5F3JjELKLFVoAhQRNKg3EPbmOi" +
       "Y7VoAS9PpgiaibnL2+lCmV0C+qIr\nXmew2NYyNizV4gUl2loKKYB9YUwZHm" +
       "QSTOIESHnIw0qoEvvU3RLd2pXXaxQEC6kpdWnbTh3axs7z\n2CNRri/l+FSv" +
       "wbZnIJIAFvP57ASdUD3eIf7RqmrCWgkSzZQJEXlRFo+rj7Je6epsE9emVvqm" +
       "O6VW\nF2qv2RDJrFVNKCCW8pfryg+AeNdsBMcPt3uB6nWLIvFh9IU0LjjIrp" +
       "HPonMs14iGuYTC+6XBsSZS\nTrc0g68uKeQVsFhmzFKRhRPZQQyXQXu4HuoB" +
       "XhaGI8Ligiz8DFbXQyIvA7o4HmsERosZi0lm1vf5\nxtXMqVRgFDVGUV3viy" +
       "0Hqk2xKF1tE5rriwZHfYwByP64VNh6QTWQ1o8tXE4iWyf7E8tVPlVph7MZ\n" +
       "m7Bqjw1OtbHFemwRBBajUl1b5CgJrqmMVWuIVVfVfpkXSz6v0Fldnw+5CsZa" +
       "tLYs6TxfcBd6vYQC\nwGXh0Ydqm2ncnHfeHgBK81DuVrw1WvkZDJlQtIhOjK" +
       "/VuQwrnjl6OwiTisNml21ICquQYFw7ZWaT\nVuhheZLWW8A55dV0NesiZ60Z" +
       "69JGakqDubOywBaNuYvrlafOgCGbGbKnd8eCCI7mjjvuVvMoKQom\n1nXDgT" +
       "lNjGFcj457lT5OZ2IIj7ECkVQ4oeVLjhMOR9M5n2WBm+U1oxq8YJN9KLMsLi" +
       "1COEXqWSRl\nMDecF427OvMnHF6QcoOTdkVP7dqUTgLHeaEG1fp5sSvLlWLM" +
       "xkjIXs0s/5RVYxAfoztKcMjoTAC7\neW0Vh2AOEefdSopNhtG6ZuDX6oFbTY" +
       "se1Yoyb/xcamhrETvIiswQeO3NGmqHumvboblWcy7HjQsC\nM9U/Loyg79kg" +
       "2vhS4iPBbp0QCGzm8urCTLW2FjhzvZyT5eCkbbJezMLqOPdsgT32zgo6GvBq" +
       "tWLq\nxhLOYnOx19Ye8tqAyL2Bmzmtg28BQZ9jxX5WNtNkqMP8WIZ8mq3Qs8" +
       "5WTO5kfCRLh8xV4SXu1HpO\n+EyTKetoL0FcmzNCDvFqApOz0OYAnMxNeds5" +
       "lz5ApkwvGXgUSRdnUYs2otT56Ca01G61PCtET58p\nMXtQbdvgDgK9121uTR" +
       "wuu5XBmM7xaJZESBl2JM6BcYVHTmeLfrGr8EWQ7bS9h+u8dzht8lhxmaV4\n" +
       "rC44ie1zHcgEf76X/Iz0uH1+UrnWQ1ARWsVxDesW5zPC/nhJ9tMKGlesDCdw" +
       "557R+l1D4KgVgAje\nsAuV4fZF6WS+LgoMyTOZXOISXSQXeTMXgwXdHSSRl7" +
       "JS1d1dsFGYfOqKOxpZhjWv5ajvwRHOsAdU\nQ8HCQeocdHcdbx9jkOyQnWYI" +
       "QqEHRpQfMfhY0DNLrFVvYJoTUEstHuvb6V6S65zk3V209M2kVc2c\n6olaLj" +
       "Vz5/V5OaiLZX2kRnkyaWSuefNZDVRoLRqxZu92cSyuMWMvcG1Ax7Y/PZcHW7" +
       "ID5lzZ0Zle\nRZIlefCWLGe9X2JdV85gXEtPfXiWxvMh1ExcY6yzzJ1m8HlX" +
       "m8m+XMDViVVPid1MW/rY0PvlWT3z\nsdqbw36/G4Ra15neOHA001rrAy0N1D" +
       "GogNn+tGzdxX7rSuB+pSU7eJjzosrx+2UNZUtuP8U1YraC\n5bk2R1x7zdQD" +
       "tLZcM2wa++IKdIiiO/6IaIMFz5bH7BLu5nHPlJdFiOZ7sMmXpnpiXVzPl14U" +
       "Laep\nkQkLYZuexYEymW2Ki2fPWFebMyDmyBALh1m/72a0JbXO8bRP7EtIbl" +
       "1jGy76bb+C1DSWtwsXb8fR\nPFjTbQTNJV3PhQjlSeqMHjWz3VtLQl20DARl" +
       "wqGK5XTf5xx+OljkMrhgCjyGffg5OeMR010SQeK0\nc0DPKJKd1tt54OsG7F" +
       "9sWToeQuYs6Gv8ROW0Uu0EPCSF7cU6YW5nLcEBwNa9RwIxxlvHtNi1eqNi\n" +
       "YJ9tnQ0bM4431cN13zKA7lsRsWJN9UxD85hK8YOFpfvDFuxFcoWFNRpY5MLz" +
       "hPkGs9rSFc+bxmK2\nDaaYIu6wHG5HS3U/bYkKXEZpvj3vD94m3MKzsLfxrV" +
       "FuZXmubg+epRoDq3IsvwkXKOXu1XKMojs/\nR7eh3UXrfAhxJd0RkFpepgck" +
       "562TbFcBccgyP8aDgRW8ErdycWtCY1jt5dDoUqKAZnQipPE2Qra0\nRAn9Gl" +
       "c8qK4Vb24GzcXSmHk1JWkEz44ncqV0bJwtGI4jgUCUC7LMIL6Fs2NZRkkjne" +
       "KBbsGUz+Tl\nMk/xpe3NF7ajdsOQ7pY2jSrMMc+nZM1b1qJt0rMg5c7oGcwz" +
       "pl5MadntaXStWcWussLEcVhi0C0r\n0agl3/J5OY+LzllFaw8+HmlNhpAKyo" +
       "Cpx0r4fLGJEtKDF6Xcy0UFjTFmQOkDb+6HcWT5xbggXGw0\n9jjzEoyFCzkP" +
       "7VAm5sgW6Rk30S/z/Z");
    final public static String jlc$ClassType$jl$1 =
      ("k1wvVsirZAepmrfi1Xqu9zKMKkkp7uoHD00ix+StabuioL\nokV4RK39Ma61" +
       "+N7CVIiXvfjkCb7K7GZrFRc8/0ROCXCWiwpRkC1U+3BrFr6brw+ri3S5xJ7J" +
       "kIKN\nCzOyUlFmt+v2e0dQj7qO7pMlV5IHVfP32EU0jNkMo2x7uqxaYumT3b" +
       "Hl134nHmFWbRL4vDgNqA0d\nYcnUHbVMGO0iKGMECxo81amIHBi7Tc33gw01" +
       "mzguAg1IrQM5FTqTWNNGYMkMF6mphCFup68Nh3cR\nbTRaGacrxEGZzctili" +
       "hMKipnINI0WOxQGlpmQatXqbEU59ac1A/T/eg77LLYVjBnypqkNSd+UHuE\n" +
       "cjHlwM41nDifmQSxa9DJKN3nQUVc7fMB6QyzXMwhYX10rRVQ+GPcssSmdtQI" +
       "IhsFFwYKJdTiaSTM\nFkp49CBJEqBQF3vaP9dedKxKrV7buFiuesgHyJ3B4p" +
       "fTcn02ELLy1X53tOEpRdXwuGXKcrEO99Uy\nLKHlxXBGB+6Kq5pmce8Qhb6d" +
       "HIj9HNm1nU34l0E7z3UEW7l0zRhZJAteWBw1O52uxGrZq9tKX+wS\nbnnG7U" +
       "4gkaY8IEc3QygY39KpT59JIOzbRAJ1CBLma1hwvfEZRe3Q0lslGyUmAcZl++" +
       "k6rN1mJjqC\n6yT82rm41HK1UjiESss+kMslffCJQ46zArrTtklzctzLduAq" +
       "1zKNUdFr0RMkB2Zc9JQnyHQXOS46\n67SGqDSmKf1gaAyC3xf8sK4WzkHYIf" +
       "DKt1LKGww1t4kxGjopKROtAod2DOjcMTsrURAmEYPLeupt\nUjJa9MaS4ix+" +
       "lvDzdF6HdZohuzWJ8ONI2yfPMeb9Bkh1mop4fy5xXQVS6Lole/OQLFvmnI7r" +
       "5ibo\ngSm4jjHa28RouFivFi4hIPGlIEUXloKDfCQlQCygmJcIyN3izkJNGS" +
       "dclanmGOvDQihij/e1i22i\nINaE0bScoejsWLkUBwoXb4uj8zm0mjUi7RPd" +
       "BrvsDCJGbHJbbF3XryGEYATr6PIUOUOXUDmGqyjJ\nonzQgcdy1k5RQObXgO" +
       "t3DKR4ldXO7MO6JL06NWBLH4jTBWuOCNvhjnqUXEU5rkmA18gIzTEgXxZ6\n" +
       "tuUQzGKXDq+18LTtUG1AcYq3q8XARbzjX/Rk1cnblJ9Za2/VwqYK+geI0hZu" +
       "RqCpsdiuvDKjEW7T\nXdTAAEz1KHSZndIUMN1au1lBC/sFWJiO3J8ACN2xh2" +
       "2MdsZBFApQO2990u+Hk0L5LOwkJ3W3XjYt\nlDYa4SILdKPNL4hcZ4G0B6fi" +
       "+hRdEv+yWcxYlhosdKsEGpwEzjwb5FVPj7p3Mjh13a76YoeuQhuw\nS4qhD8" +
       "TysktJQyvUbajiiNw69mlaiSW6RHDAPKl4P8/Ybl+wZMhLFYkbF8Itw2NhHi" +
       "+tnAD7vuJi\nIx+jgYrFhXU2NxdxUi1qhNSqIWJLwp3uO+k84HS37tjI6GP5" +
       "EvTssdj5g06lc8NHPXmIzIQ/GN2e\nJYx0rhG9ixvCHo8PlVFZ0jZt6xy4zC" +
       "0mQaeenOThuC4RodFkoYpjyrquZEIQm44UZUaJRnDeourR\nyYxOo8gtuDf6" +
       "rRqlC+kiHyJvVgC6naDSnq8O2PQMKzWy3WgzSULOm+CYSNUu3BPyfO9B5mlv" +
       "q2Q4\nWNFcKaOtaIfYvgQ29OXEKhsyyxesLCv+AVsec1SRfHpatMlR2fBL1W" +
       "BZxShaUucRUaSFLVtdTotM\nVK0G3QBsStjAfK4HajMUTi8ucaOqdliWwydj" +
       "hiZM3QdllE838EwHZtnOVgjKLUmgz0jRHFdRuUiK\nm21qncLykFFGvDyvNI" +
       "4NLQMjrHxrp9gaTakTlx6O4BY3672kQ+WUXW9WqGm4yeJiQck+9tfk8oAm\n" +
       "MZ5fgqre1gMOH7VzFF86pbaHPKVm+2EJI7P5/BiMviKnT+cFmjbiOm2GqbnV" +
       "msFOO0i2GzddUnYD\nqL6a7AjLGpKKEDQL8fx5OziMzpzXTeeH2GpIUtCGt8" +
       "4J6urL6pCvUmt+gUYywdGgqFpcWNhIEm4X\nHYVhBs3StZ1rznkGn/izdXKK" +
       "eAy0GtI1lDOp2TSLHvIwEOb4yYVkSYE34lmSxSU0jbpBrrk9HUGF\nCBGckW" +
       "IxtgppGIPCiwdIxoaYb3nA0hoKbhJixzIwawkDMSugA+dlksALVhGvM9UGuu" +
       "P0mJzTo4a1\neuvWmpYEEDRvSyrZZ+ABai8c2lVcHuapQjEzCEpXaGmOwe+4" +
       "jEHqRcsyCwryU16uzs0ZxuWpipBh\nKxzMRTnncac/NBTIrvmjXZ8HFD375J" +
       "Krad1vbBPiWUWnqFm5LcbVox+r0RnFlG2SWIuzhSkUvwCn\nh02xILphVFqz" +
       "jio3rBmG37Xb+hjX9YqXk1GQRjm8AJuVCprr/W47bBF0jclDfnLNuvSS0XWv" +
       "AWUn\nzcFoOhB1IfZSqgbY3BfFc8+m+0jACnx9QLsjtneDVLQ3+/xIrWTZjT" +
       "h+2chdxNHWOrhsKDxeWiLX\nW0IX4iw7FeGzP65A1zy1pIPeEyyJiKVAczan" +
       "YSku2p7iGCrebMTjjMTSk6rk3RKrwOY0NKYPGloc\nR4bAnc5KtPD5qbTczo" +
       "rRc+eyIkrMUlsu4oUQSxtsWVVLml0tkzZr1mJnqJ7ktnIr19R8n4+LQ6T0\n" +
       "uQbxTBPvOXZmUIIkT2/t1MwY48balUqKBJCa4VFzrpYYtNySKLPYSXAZmRox" +
       "LpESWhISCYBgo96U\ngiW4vNqvWBVMN13IGdPNZusDhIYJPmcBu1JBdzq1C5" +
       "g2PqaooBJq0iDbCOsV2q8HLU5ibFxO2+xB\nL3YMuQuS40yrWtTpYS1awdNt" +
       "UDcJC6tQQDJqQ/uSTYVmmgqncQ0Zjxpu0/CgLpPZ3kFPx30ptLDk\nHZgZJq" +
       "NHhnH6qF3kutXn3dZkkCnlCP0uQ21zCI4Zo8/45SxS2wQDSQaNlUA8BlqRqK" +
       "WH9XungKCi\nkurh4EYWQzUnebQ88cWyAgak4Fykpm4SctjQ7IbEigfT4zZb" +
       "JcO8sO9C8bzTOXO+zIbVGFWLNoJ5\n9trbBhsthnMTizSx7WdJ23bFceECJr" +
       "TbTvvBqjxagObagAELI1zhfWLVPqntkwxdZSzHqKjrDvgK\nRpYmUW/3OeGV" +
       "ngT0Y7iZrDnDVBWCMBKAd9XVdDgP7Eav+DE2PxwuVhiWccgz19UQSPG8Oa44" +
       "BkkO\n17GwQcJk22h+GRlY3ajXdbygH/T5IYoqOG4JE02nkltgCbEv1oFU6z" +
       "jPCNEcnClUT3fKAh6urwL8\n9bs3Ct68eanhYWpNGLjXG//x814c+NB300hC" +
       "cZDrQp7uhv661Rzt7g7zkBrX11zlb0l2Y4o6CoO9\nEE3HxSPrqOeINYCZL1" +
       "OHWopwSjF4FgA6X0oWPlyv0nWixMx8iMfggJdYIeP9dKcdU37DChYuBYy2\n" +
       "4jOgPUwlgDZ2Ha3vj7tdRs5BwmdgpFqhkn9KsCMMpMBOJ87sohfTi0Yrej8g" +
       "hjgssXh7PTKH1SYf\nBTeK14ksTLVC3g7oDicHYzPH3NT0pbm5HDxfLtN1W2" +
       "2QNOkjphLlUQodShhNIa4LoRCyjt62Po8j\nlXqgnKWxk0FkOtCdXJx6vx2g" +
       "YQ4hyEAPy31rlCQ7aAZt6mszjZNTcQzXp4w8iPP+WHNtveENdIy2\nZVTzUV" +
       "4jjoSWKsB5Oi+QvYn7KlfOrGi/2MyTYjC3euPiaVUaMmk5GTZfRKPcRRCIMa" +
       "e0jXy3hY8S\nxum+lszbYIdYZWn6BdNMjbw0uiYudkunHGdq9LRxne7RwgI3" +
       "h4OV+HKXXhbnTodxODab/S4+XfY7\nXNz0fhHZxrZqcdKPQGrAD/v1NDa2nt" +
       "gLfHgG96dGxROxn+09lN57C/q4rS/+gdQ6kTRyxV5q1EWN\nhUXptzMdkYyt" +
       "j0gCV/HgcKFaVm+daSrMyuNRBtb5ChoVQvellOBHl7OzkEPLNo6XWuxmjcKz" +
       "A5Lu\nSEW3gI09zElq38GRSiKCp9vr3WWrAvsjP8VMsKHntYtu9eFAx4XtuV" +
       "ur5luWMJU1tRonYTuGxOUA\nSgMvoVu4HDg1RLA5BZBtnquOUXjOXMeArHKM" +
       "qbMlhM2Ga50QcAFofVizK8qXzIPO6lliByoByrrG\nKeBqy/GATeSgCtBLAK" +
       "W6DRPoXbgR856QpTB1JECZ+ma6AnKR4OpsHfDumgtgjqyGMdZjW9D3yD0B\n" +
       "auCChkDxCPljnfm5gEBE8XpeQ7oly7oOKYGwsFN4QZjCpp5dlvO9L8ytGUo1" +
       "XDcDXTVcIKPO6/y+\n1ekzCBPlWlGSuruQKVmj0BIGQC3qQCBcqbiD2jg449" +
       "ZiHU5NM9ggFg+2RgaaSV1oAIHoTYmiAuoq\nsOk2hw3ojjFhDmbzNZWEdSsg" +
       "Wn8+Xkq2AwSCGs6gzAChCLWQO10ufUGAT7WTgNWmB1bN+mTW4wjl\nc1XLj+" +
       "sKw7B/4mpG/qs7o/PJZ4zOO/Ajs9M9+zbWK4/exnqQcXLdA49lkkyuiQdfer" +
       "+8u5ukg+/p\nv/vGL5m/+TOv3GWvbOrJ63WWfzt2Lk78KJHl6UoON2mGD7I7" +
       "Pq595X8kl3/x55/OZPn82PxXPvDJ\n+9ably+Ir/rBf/bK5NWHmSTP5Dk++d" +
       "B7T+aPTEunbspUeSKL5MtPZJFcj2/dvVX31lNv1d2M73X3\njQ9M7Xlh3s/v" +
       "1JPPPkiDe+dhGtw71zS4dx65jv/uIdf1df0/cLdNHhyf5npuLsVrqXlNHHns" +
       "Rbwf\njPx/ricf85xaKLM6s7KbJA3uKdjX715H/IN3sH/w5WBvGv17PyDe/z" +
       "F65BGPzbzAMmN8tB+p876Q\n37zbJg+Oz5vp3/3BgH7vFojLRpYgDVKPfJDZ" +
       "9Ni7hh85Z1nsmOlTjG/dzfS37hi/9XIDeaPsf+9x\nZf99wt97ZVTwEX7TuK" +
       "5TVs+m5d7kwd1mPf2lV9/53Vf+vc+8c5Nn+KGzWd3q19P5zM+mKz+RhXzT\n" +
       "pdcfDsC181emJ9J36skUGx2xU9+mrX7jg9JWH5a7qZjL8/w5ivWH7rbJg+NL" +
       "KNajIf4hKNW9T9ST\nnxzHmU43cWZFcjA4T0vIhys/K+vnwAN32+TB8UcO/8" +
       "V68vERnm/qh/TXy597Cvbjk9tX6H/6Dvan\nf9TC/I3RGF41Eds+zx58/E7c" +
       "vn2H9+0fBd69u1Th6/9v1ZO33yczmugsJ7/mid/0493RAlc34+1l\nty9JP3" +
       "esoXF7964z7/4IO3PlWd6AruvJpx8DfWQC3w/56jTAO2Twh4T8GNXnXpJ7DG" +
       "vefIybdVKv\n9p95PfySBfZz+gDcDf3kwfHH0weunrxROpYzuv5NX9+Yk/eV" +
       "EvgOF/6xSIl2KyV0amXJONpYaku3\n3M9Dvlq8a+rF7A559kNCfuVhsXuf+3" +
       "6G+WdHB1k5qf1wjPfPAb6GFsgdMPJDAn71oSW/98aLqB/r\n3PKBqfnCY7kW" +
       "jtWUQd0/aWWCeuR60DP2NhXimd49MJrzu97Nf8zTUdeTtx7T3FGW5LEL70d+" +
       "DacW\nd+SLH4XsP+6Nfv7Wa+JNWTppff3kzE3A9LRb+swd5/KOc/lynD8EF/" +
       "/Y4BY3xL90G6TcEV/90qPG\nnwS+GhL0Dhj98QH/qXryqSeBbwPZG6V5Hz1d" +
       "3WGvfmTYjwvFnxmV7krMYi9YuVwTqNd3qOvnoV53\nP9jK5d6fH21b6YzqdD" +
       "fVz+jQ63fj9p07kO98fyC/D6/2r9eTj45u4hrUO+9HdHUP790Rvff/09D8\n" +
       "W/XkM01qZ893Wu/nA757R/XdH4dK3A7gXx4FrDWDmuhqJ73m3z2P9yOTW6t+" +
       "74735lhPgme/3PKd\nt4vGrIKiyWrnm7cfVXn7Ghe9ff1FKkgvWeRsHfexj9" +
       "d881tv/1ztB9W77/fjxze/9d7Pfyv/Yf3O\ncu83rr+zvA/L87p+/f3nzWe7" +
       "/k+9qOtlcBkvPt73oL729e0/8jPy2w879Owc3xvX/ncP/4Cm46+N\nqvGg5Z" +
       "uedWOMeh3S66drPvvMJ9BuP9Rlfe23fvabfzV/8z+/XbU/+JjWa+zko24Tx4" +
       "9/d+Wx89fy\n0nGDmy69dvsVlpv+3ftvR+/77ApmtCIPz29CpP/mtvRvX4Hv" +
       "Sl///1v5g9jksTzQ28/GdP8ftOKn\ng8pNAAA=");
}
