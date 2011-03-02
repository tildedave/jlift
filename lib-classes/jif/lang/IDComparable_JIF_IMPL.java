package jif.lang;

abstract public class IDComparable_JIF_IMPL {
    
    public static boolean jif$Instanceof(final Label jif$L, final Object o) {
        if (o instanceof IDComparable) {
            IDComparable c = (IDComparable) o;
            return LabelUtil.singleton().relabelsTo(
                     c.jif$getjif_lang_IDComparable_L(), jif$L);
        }
        return false;
    }
    
    public static IDComparable jif$cast$jif_lang_IDComparable(final Label jif$L,
                                                              final Object o) {
        if (o == null) return null;
        if (IDComparable_JIF_IMPL.jif$Instanceof(jif$L, o))
            return (IDComparable) o;
        throw new ClassCastException();
    }
    
    public IDComparable_JIF_IMPL() { super(); }
    
    final public static String jlc$CompilerVersion$jl = "2.3.0";
    final public static long jlc$SourceLastModified$jl = 1202152645000L;
    final public static String jlc$ClassType$jl =
      ("H4sIAAAAAAAAAIVWW2wUVRg+3bbbC6u9ULACbQcoyW6ErS9iZKOhqVS2LGFt" +
       "IUoJWc7OnNkOnJ0Z\nzpxpF7BEYyLIg4kBb4mXFxMSw5NEfTHRBLz7YPogvm" +
       "BiMMZEIfpgJAbF/5yzs5fZhU4yM+fy387/\nf///nwvXUbvHkOY69FiBOjzJ" +
       "j7nES2Yx84gxQbHn7YWFnL5t462Fb09NrW1FPbOox7JnOOaWPuHY\nnJT4LI" +
       "oVSTFPmDduGMSYRX02IcYMYRam1nEgdOxZ1O9ZBRtznxFvmngOnReE/Z7vEi" +
       "Z1BosZFNMd\n2+PM17nDPI56M4fxPB7zuUXHMpbHUxkUNS1CDe8oOolaMqjd" +
       "pLgAhKszwSnGpMSxSbEO5N0WmMlM\nrJOApe2IZRscjYQ5Kice3QUEwNpRJH" +
       "zOqahqszEsoH5lEsV2YWyGM8suAGm744MWjtbcUSgQdbpY\nP4ILJMfRYJgu" +
       "q7aAqku6RbBwtCpMJiWVGFoTillNtPZEY/+eyf6tRaTNBtGpsL8dmIZDTNPE" +
       "JIzY\nOlGMN/3kufR+f10EISBeFSJWNOObPtqX+fWTEUWztgnNnvxhovOcfm" +
       "vruqGl8Z+7WoUZna7jWQIK\ndSeXUc2Wd1IlF7C4uiJRbCaDzU+nP9//7Hvk" +
       "twjqTKOo7lC/aKdRF7GNifK4A8YZyyZqdY9peoSn\nURuVS1FHzsEdpkWJcE" +
       "cbjF3M5+S45N5Wz3/iRfCIifhDtCecogtMTHuC2IRhTgy3JLjuWWhpAYPX\n" +
       "hZOHAtJ2OtQgLKefv/b1Mzt2vXg6UoFPWR9Hw4ctMykwlEw/LlRghvOU5KbS" +
       "k7n07mwGtbRI8ffV\n+0M42BB58Pv7qd6XtngfRlDrLOqyikWfC37IH0yps0" +
       "CMHJcA6qsBq8QIACyWB6wBbHMUBElsl1w0\nz9CGMKaqmZiGEQagLJ28/d2N" +
       "3MJFEX4RrgEhXZkGzj+ibIslZg5OHTq9oVUQLbSBG8VJRpeXntNv\nnNl98f" +
       "tvrsar2OVotCGlGjlFSoTNzzJHJwaUnKr41/7Z+cfZ9kc+iIj4d0Gl4RjwAW" +
       "k7HNZRlxqp\noMwIZ0UyaIXpsCKmYiuoDd18jjkL1RUJkRXi06PQIpwVMlBW" +
       "qJvPRx+88vGKzyK1xaynpurNEK5S\no6/q672MEFi/+nr27CvXTx2Qji57mq" +
       "Oo6+eppZekIQMtENiVTdI0OThw7tXEm1eCSK6sSh9nDB8T\ngSw9tzT0xhf4" +
       "LUhhSCXPOk4AKPBITShQIL5xOU7UbIr5SJlEgCucJ5OiiAeRKeZP/HXp7W5N" +
       "GSN4\n7q/VocnvRnXCCEedOA8AwLoE76ZASdWOCjNDQ3eqk7LGn3r6z9gL+P" +
       "JBVc3663Nth+0XH3rnB5LY\nHtObJHAXd9wtlMwTWj1kWNtu2T+CU/Y8NfLT" +
       "5Nbzi+FTRsDOkbty5vS++bVPts5ZX0K+VxKjoYHV\nM6VqLQaEMgL91xZnFy" +
       "sdMkJD0ox7wQjxDgkAIfXIP2/Z3Fj+tmlHfexZR32Hk7gnrwKagpyWdxxK\n" +
       "sK1BbRsNzHDM+AHTsjHVKhUvg/OEnsDF/KKkzGzWFIFqHWrDOTiZ0E5YphZ3" +
       "NKsiSqstlrBfN9d0\n7VEtXkfhpDR17JDyfSLAHtRASrhjxxNJRqhY9/Y6cT" +
       "0pjCoQDr+cYMnVVehMPLFZWp1ILZZlm5h6\nJLWYKDeGJpiVQIXUVO5qnjmR" +
       "MhjEvI+je+stDpZ7ZZ7KdeWu5dNwuxJWExGv8S6QZVYRWu18+S7w\n8vC7v1" +
       "y8Nj0QqbkwbWy4s9TyqEuTVNjtitRbfzcNkvryA+svnJz+MR8pG/owRx1lCM" +
       "mDPKbQWW7G\ngwEqg+YsNnuX8bn47Lirv8V0p/hMLe/Jfapvj+rY46NN4RGE" +
       "aVXT9l6CjabtXlTgwYZruLos6huW\nDsUvuX1fya5VudBF4VZl+pTWpHhtuk" +
       "ddRkxLWh1VzUh5Kg/lM7CNw/0IftJmrLbhytAK22Jousor\n/wPw49zYJgwA" + "AA==");
}
