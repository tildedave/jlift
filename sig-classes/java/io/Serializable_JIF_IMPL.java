package java.io;

abstract public class Serializable_JIF_IMPL {
    
    public static boolean jif$Instanceof(final Object o) {
        return o instanceof Serializable;
    }
    
    public static Serializable jif$cast$java_io_Serializable(final Object o) {
        if (o == null) return null;
        if (Serializable_JIF_IMPL.jif$Instanceof(o)) return (Serializable) o;
        throw new ClassCastException();
    }
    
    public Serializable_JIF_IMPL() { super(); }
    
    final public static String jlc$CompilerVersion$jl = "2.3.0";
    final public static long jlc$SourceLastModified$jl = 1202225504000L;
    final public static String jlc$ClassType$jl =
      ("H4sIAAAAAAAAAIVWXWxURRSevW23P6yWloIVKL1ASboxbH0RI00MTUNlYQlr" +
       "i1FKyGX23tl2YPbO\n5d657QJC/EkEeTAx4F/iz4sJieFJor6YaAL++2D6IL" +
       "5gYjDGRCH6YCQGxTMze3e3dxd6k707d+ac\nOd+c851z5sJ11Bb4yPQ4OzrD" +
       "uMiIox4JMnnsB8QZZzgI9sKEZW/deGv+21M717Sg7mnUTd0pgQW1\nx7krSF" +
       "lMo1SJlArED8YchzjTqMclxJkiPsWMHgNB7k6j3oDOuFiEPgkmScDZnBTsDU" +
       "KP+MpmNJlD\nKZu7gfBDW3A/EGh57hCewyOhoGwkRwMxmkPJIiXMCY6gkyiR" +
       "Q21FhmdAcFUuOsWI2nFkQs6DeBcF\nmH4R2yRSaT1MXUegwbhG9cRDu0AAVN" +
       "tLRMzyqqlWF8ME6tWQGHZnRqaET90ZEG3jIVgRaPUdNwWh\nDg/bh/EMsQTq" +
       "j8vl9RJIdSq3SBWBVsbF1E5lH62OxawuWnuSqX/P5P82DYXZITaT+NtAaV1M" +
       "aZIU\niU9cm2jFm2HmXHZfuNZACIRXxoS1zNimj57I/frJoJZZ00RmT+EQsY" +
       "Vl39qydmBh7OfOFgmjw+MB\nlVRYdHIV1XxlZbTsARdXVXeUi5lo8dPJz/c9" +
       "8x75zUAdWZS0OQtLbhZ1EtcZr4zbYZyjLtGze4rF\ngIgsamVqKsnVN7ijSB" +
       "mR7miFsYfFrBqXvdv6+U/+EDzyQ/5DtMd5yQMl33yMuMTHgjheWWrdM59I\n" +
       "AOC18eRhwLQdnDnEt+zz175+evuuF08bVfpU7Ak0IEmUoTxTzZQCI9bO7ISV" +
       "3Z3PoURC7X7fYndI\n/zoyDX5/f3T5S5uDDw3UMo06aakUCqkP6YMZ4/PEsY" +
       "TiT08dVxVFgF+pAlANWGsx2EhRu+yhOR9t\niFOqlohZGGHgycLJ29/dsOYv" +
       "yujLaPWpQyho4PvDGlsqPXVg58HTG1qk0HwreFGeZGjp3S37xpnd\nF7//5u" +
       "pwjboCDTVkVKOmzIg4/LzPbeJAxalt/9o/O/442/bIB4YMfycUGoGBHpC16+" +
       "I2FmXGaFRl\npLOMHFpW5H4JM7kUlYYuMevz+dqMYsgy+erWZJHOigFUBerm" +
       "88kHr3y87DOjvpZ11xW9KSJ0ZvTU\nfL3XJwTmr76eP/vK9VP7laMrnhYo6Y" +
       "UFRu2yAtKXgMCuaJKlmf6+c6+m37wSRXJFbfcx38dHZSDL\nzy4MvPEFfgsy" +
       "GDIpoMcIEAUeZQlFBuR7WI3TdYvye7AiIskVT5MJWcOjyJQKx/+69HaXqcFI" +
       "nfvr\nbZjqvVGf0BCoAxeAANhW5N0UGanhqCr7aOBOZVKV+FNP/Zl6AV8+oI" +
       "tZ7+Jc2+6GpYfe+YGkt6Xs\nJvnbKbi3mZE5wmqHjFvbrdpHdMruJwd/mthy" +
       "/kT8lAbgHLyrpmX3zK15vGWWfgn5Xk2Mhv61WGm0\nHjEw1CfQfl15djnTri" +
       "I0oGDcCyDkb0ASCOknoavfc43Vb6t5JMQBPRJyQYYDdRMwNeXMAueMYNc8\n" +
       "RItDEQxeHN5fpC5mpm4Mx3GpcMLkBybS5nENyRzmJq1Km/X1MD16Il2ptk2Y" +
       "oMIPhNcgmvMxUSlA\n8rsHrhOK5rIcZjScpVm8TaB7Fx8oaOykeZ+WoFHNVT" +
       "rpy+ve/eXitck+o+66sbGh49fr6CuHMtjl\nSeauv5sFJX35gfUXTk7+WDAq" +
       "QB8WqL0SAXWQR3VwK62sPwpq1Nrk4vIlnCtf2+/qWPm5Y2knTsmG\nB060cS" +
       "CGZAwsyq36SEcB6mvWFstwEWraJmXl6m+4veo7lr1h4eDwJa/nK1Xtq/egJF" +
       "xGiiFjdalR\nnyZJzydFqjAndRHXLjoIzq1AE8igXOG19JotUKtck2PH0x75" +
       "H55Usc1bCwAA");
}
