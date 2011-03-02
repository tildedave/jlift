package jif.lang;

final public class Capability {
    
    private Capability jif$lang$Capability$(final Closure closure) {
        this.jif$init();
        { this.closure = closure; }
        return this;
    }
    
    private Closure closure;
    
    public Closure getClosure() { return this.closure; }
    
    public Object invoke() {
        if (this.closure == null) return null;
        return this.closure.invoke();
    }
    
    final public static String jlc$CompilerVersion$jif = "3.1.1";
    final public static long jlc$SourceLastModified$jif = 1202225505000L;
    final public static String jlc$ClassType$jif =
      ("H4sIAAAAAAAAAK0aeXgU1f3t5j4wCQSSAEmWHECKJCB4YKwaAhFwKTEBlPjJ" +
       "Mpl9mwzMzgwzs7lE\nRPkEtFVqRay1goqK4i2fR6FVFM9StdDK1Q9RtH5eIE" +
       "Ut6Kef/b335t7dpKX+MW8n773f7/3u400e\nPYrSNBWVLhYitXqvgrXaWUKk" +
       "mVM1HG6Wxd65MBXi96zacO+r533yuh+lBFEWF9M7ZVXQe3WUH1zM\ndXF1MV" +
       "0Q64KCptcHUb4gaTon6QKn43CTKkd1NCqoAKoOUdbrcI9ep3AqF62jh9U1N4" +
       "qcpgFYOp3V\nlqLlyNejooAJYRDFKKKbGUnnV37f/ZfVs0akoLw2lCdIrTqn" +
       "C3yjLOlwRBvKjeJoO1a1hnAYh9tQ\ngYRxuBWrAicKfbBRltrQYE3okDg9pm" +
       "KtBWuy2EU2DtZiClbpmeZkEOXyMvCkxnhdVhmFQG9EwGLY\n/CstInIdmo6G" +
       "2Zwy/prIPLCXLQBhaoTjsQmSukSQwjoq90JYPFZdChsANCOKQd7WUakSBxNo" +
       "MJO8\nyEkdda26KkgdsDVNjsEpOhqeFClsylQ4fgnXgUM6Kvbua2ZLsCuLCo" +
       "KA6GiodxvFBFoa7tGSQz9z\n0nN/uKn5ZMBPaQ5jXiT0ZwBQmQeoBUewiiUe" +
       "M8BTsdp1MxfERvoRgs1DPZvZnobq5+YFP3mxnO0Z\nkWDPnPbFmNdD/PfnjC" +
       "zd0/BRVgohI1ORNYEo38U5Nd5mY6W+RwFvGGZhJIu15uKOltcWrNiCP/ej\n" +
       "zJkonZfFWFSaibKwFG403jPgPShImM3OiUQ0rM9EqSKdSpfp3yCOiCBiIo50" +
       "eFc4vZO+9ygIoUHw\nlBq/yPzV0RmNnMK1CyI4XS14qo5Kauvgl6nfvdZDkO" +
       "V1+3zAx0ivF4lggDNkMYzVEL/5wz8vm37p\njWv8llUZZOhoCIkGBHetjRv5" +
       "fBRnkVs2RNhh4vpfPF2ff8t47VmIEW0oS4hGYzrXLmLwHk4U5W4c\nDunUmA" +
       "ochms6f2472B2YcEgERCwEKKhLRRVe+7L9cCaNMjzes/zHvx4LdW8lpkBUV0" +
       "iwM9JAEUsY\nbbk1rVfNWrSmIoVs6k4FkRJOKlwxLwHuEN/70tDzt+34drsf" +
       "pbVBZNOm4QgXE/XmxqlyTIJwUWhN\ntWCIJFKQa8diEOWwgMGB05tum6HwFE" +
       "ZHRUGiOeZIItlfR6FADDmqjYSAlYNxVw0sghB/7KbZW/fu\nOjTWdjYdVcXF" +
       "gHhI4sNeGTerMo/DEBVt9Hd8N+P4bWlTnvGjVAgMwJsOnJE4U+Y9w+XL9WZc" +
       "JLwA\nZE5EVqOcSJZMqWTrnarcbc9Q4x1E3/NBS7nwnAXPYMMb6C9ZHEyGIc" +
       "zUidI9PNCoe2pl+oR9f8x5\n1e8M0HmOhNWKdebuBbbNzFUxhvlDv22+7faj" +
       "q6+kBmNYjA46VIUuSGs9lMAiH1jokASxp7a4cN36\nmt/vM01yiI2+QVW5Xm" +
       "KRPdftKb3zde5uiEsQHzShD1P391nGWWQbJ7UHHGahb3fOpDWBsyOFfuQH\n" +
       "XVD7gdxInaYcIhyBMP/OovLJtaRZBE9NP9KEU4fbp1LUQHAHPT7E37hg+NP3" +
       "vDz0KDUCPy/oqDTe\nwsKW1dQ7YwooOqZAOqEeYJDq7wIUI70o5nMu4yzy0m" +
       "MQM21l9ZLM76KbKTE5YazxqqCQEG0epwlR\nRRQgR5vHpevyLFADSbjU/VRO" +
       "0kTQJXO+uXRxeo+iktTXxUGhALGLSm6MxyYrB5CijuYRnqKyqnQK\nfICSHZ" +
       "AjAWb8AU7tiEWxpAd4UdbAy8iSo7wIjG0nMsLhANcud+FAe2/gar1T0K6pId" +
       "ingIqqqUhM\n8iFAS5Kse5gI8Z/NObajT8FvsAxZ7oaJ2z3q8dLjVY8urDYt" +
       "tsgbRGdwWie4xX5xX9vth35WxrA6\n3MZY3zbthtvXP//cZBZniffmX3QxEx" +
       "OVYhX4kME3nDLCq9y5wKih4LO+3/HOtdsbP6Zm7uepPVeC\nfUdiovgLK1WR" +
       "8VwyFIDYqyxR6504oCmYh2ovYOoyUEHEWKFQPV6g0MiRONGRvOnKDEGZ50Q7" +
       "GhZe\nM/XczYfwUyyuiM5g7S1tPJCvre+YvPGJJ9KY+LzJ2WH6If68vV0F6U" +
       "9ujPpRBiQeaiBQU8/nxBgJ\nT21QT2qNxmQQDXKtu2tDVgjVO2qwczxh2emm" +
       "qYQUO3YMcsWOif1YvQ9RkQYpwGg61lhxMy0iSJxI\nMU6lSxPJMIlOTItrPl" +
       "pj7ZruqCQLL1g6ZZ4w5zImtRpLalDn19KmwQD1wq2v+bxr3ITr76YmlNrO\n" +
       "aZSxDOBeIzt1FEjellBc9ZTAbEsGGfAUG1kJmb86ymlkFs0Ks5HQVYzXVN5R" +
       "ntnLFOE8irAQmifb\n+CyfKIwXRYjffujEbcfm9y2jFpdHqadu0sr4GO0oKG" +
       "ygqlb3vnpXV5VQeCG++v38L/997ZFxtFw2\n5eTMmbM5JS5nEueH+bSMgy/t" +
       "HLZodwryN6FsUebCTRwtNlAWZHmsdULd2aMY8SC3O5NEB3j8gKzE\n5rqZEO" +
       "Vwmf2H86OfbriTpTtH6VHsZtrZ4BidVZljhwsra6yoLmZZyi2Bp8qrXLJ4OR" +
       "mu6EHMwHG8\ngaeYBq5DExtrFwUeXjTajkJU8jVr8XGhWRWi0FF0GS3PrWX3" +
       "f7z1w5ZCv6MvrIxrzZwwDhZyFZK4\nR/V3At39yrhRjy5vea+dudFgdxU/XY" +
       "pFz964H9dcnMsn6AeyoPCReEHhaABZ4I6Q8bK9ueSBT8X0\nR3JoK5BPIhWN" +
       "whKzRjo1U3JP5Qhas3kGZWspGTRWC1XZh1mEsGMtGDDdEyu6dh9c+HdK/iDF" +
       "SRS0\nS8lsod7Kjl70NuaJQ4uLAydwNsVs1RGVnjrCAnDUEp4gWg5PoP8CzJ" +
       "OpKa3OTH3dAx0nf3hpyzcs\nUxOwdgt9mVGZuHpHB3rDhJcnMGHy3pzMfsli" +
       "VKG8qDRvkiHmOXw0POMH9p9VSQ4XyLCYDEvIoUHz\nUPoLFmjn2QV0ihYENy" +
       "MfctsiqyOo1IxCYtOKU9/et5o/ksAsCIpuVkNYbIyCp6Lf6o6dEaCYsI7V\n" +
       "QJCWcCT+BBJUEwTs1xT9Mq++xhndzQD6WpdMX2S4ngwrXSIjP7+y9HRLT4/b" +
       "g5JniKasH+v/lnNo\ntmHnUC5VTSBXWomyCyQBgj1Eq1Ka1cymiIy19L2O8E" +
       "PpNao/n8/RONkUNWgaRHQo3i8oWnPkndK3\n59rG7USRqGJs5gTVUPS+I9e8" +
       "+fTsl++nmSIbMkWkWQZb7iVNhrfhbrRWSddNruY6zM2lcZtn2sv1\ndsVZzQ" +
       "TwIFBV6fVZgyan35694pOvt7777Bi/IYufkxxa5mWnBXNhrLKzQvzaK4TKy8" +
       "aWXEI5SpO7\nad4rd9BnRSs7+JCbVJViIadsgehWHCczA/3YYPrzj/14/EKq" +
       "bkfD5M6tTAZO9gcx9sn4mPsWxY6e\nc2XFEUBnfPDy4RvuKN5lMt/DMADwaL" +
       "fonGBO6e2/bvh7485ca3QzXnkngtj0yMn7+8Y82GGb0uPG\noeTnyUTCv1wV" +
       "dFv48q1fTlq3smmjQ/hUoiDfbrqRyZeMj9gCedZq0DwCmSrruuxMV2e9MaGo" +
       "4aHZ\nT8XLpMbNngfSyeTE3Pte+OeWh+8xcTxFmXvOwegf6LhJYcb6J/rXZi" +
       "aPh1mkeJHBKM797r+eYRtf\nYVJUTAm6/jKcn9zZeduaJnJPbobcaPvV3+zc" +
       "kB2w1TLSuvFwN10usBDvf+j91TXFeQdAHW3ojE4O\nCggob8htPlYH6sI8qP" +
       "pemLfh1Fv6e9Tu7ZKSQFd6ikIS/WvhGWZE6GGJu5534iO034rQ43ocTS+Z\n" +
       "mOhCX90PerqxkQwFlLDpZNj7X3SwZK2FDPMp3AE7b15Jx0XxVTiZZvR1WfQN" +
       "h2fEQPQtozWFndvX\nWuClRk2SDPy0culhI5fSzNjDSPgdGQ5SgVD8e3v6U8" +
       "j1prHamWqknWJKk33SoJXt6iv+lbuKe+Uq\n0+OWQm2sy8p4EXdh0fYBL5LZ" +
       "9AuOaYJ5l5d/0HTO5mu8TuCPa4XdcCF+14G849Mn7/r4p7uOpj6T\n+Oa5vF" +
       "8mQnxB14jLUjqFN+hHQePSOe5rlhvIeynITp3runOosOwnB56xiPXcyPx12A" +
       "9VeIILB6eS\nk5QjA9YqJ3SU3YH1xoR+m2MYdzLCEvrtD/+z3/rQafrtEHgK" +
       "B6Ivud8ONXw1Gfjp+K0vPYnf0q9K\ntjbuTayqTWT4imYx3yBXFmMLZD7PTv" +
       "NfuZJU3MwzNlChnTK/cqVA5wwZWO780hISUcAoI0Yi89dr\nnGT4+v8yRF8J" +
       "NGOC1CUv8RphpnFoMgISlcuzZEEyyuUjv7wLf5a2apJZLkcVWcJGfBgDMxK9" +
       "rob0\nykKEp006Y4DDqaIq42tjiwBnGbPr3bVvHz21baWrNq6275caZVHEPG" +
       "kQtKp5UlQOQ5lKrnRbsb6i\n9De71961Yh4rDM8cGMaeL5mKVuxaeLKMlgE+" +
       "njY61jWXva3eCNEXua6zqdvt9fgN8ZOSfoRyWn5z\npstvmFjJO70/JOxSGH" +
       "bLixJ7kv1RyfF5pyGmdzZaaSHES9ujvSmvldSybsAqXFm+8NxjkD5/Sn+m\n" +
       "x6jocqWzBFcrd155qyJNaBDc/Z5JrjumnQvPeQOdyWKaEV1sxZCGvvKnVsyF" +
       "TsXQCAF4su09xIiL\n4/6Lhf3nBV+xZ9HYnUrBm/T7lXUzn2l84HBeyDve0x" +
       "UVRwSqi0ymEVqZ+6bpKNMkUkep5IfQ5mtk\ny5foKAWWyesM6pcTHAWQb8oA" +
       "cr8QnotQ8ksKp9zdny1J/RJj/98T4r9QFl7yj5bDW4wOLuk9tA2x\nbWf1jQ" +
       "cPHqhkDRiooq+PnJIBtQv7lGaYpvPu04vNxCW8vzdy08qP8swIQ1vYAkcBQa" +
       "u2suR4yPv8\nQfX7L9227SFv2YYczY+Dfdc14+Sx32SceuvExW5L99uV6XLm" +
       "3/8BUYCer44lAAA=");
    
    public Capability(final Principal jif$P, final Label jif$L) {
        super();
        this.jif$jif_lang_Capability_P = jif$P;
        this.jif$jif_lang_Capability_L = jif$L;
    }
    
    private void jif$init() {  }
    
    public static boolean jif$Instanceof(final Principal jif$P,
                                         final Label jif$L, final Object o) {
        if (o instanceof Capability) {
            Capability c = (Capability) o;
            boolean ok = true;
            ok = ok &&
                   PrincipalUtil.equivalentTo(c.jif$jif_lang_Capability_P,
                                              jif$P);
            ok =
              ok &&
                LabelUtil.singleton().equivalentTo(c.jif$jif_lang_Capability_L,
                                                   jif$L);
            return ok;
        }
        return false;
    }
    
    public static Capability jif$cast$jif_lang_Capability(final Principal jif$P,
                                                          final Label jif$L,
                                                          final Object o) {
        if (o == null) return null;
        if (Capability.jif$Instanceof(jif$P, jif$L, o)) return (Capability) o;
        throw new ClassCastException();
    }
    
    final private Principal jif$jif_lang_Capability_P;
    final private Label jif$jif_lang_Capability_L;
    final public static String jlc$CompilerVersion$jl = "2.3.0";
    final public static long jlc$SourceLastModified$jl = 1202225505000L;
    final public static String jlc$ClassType$jl =
      ("H4sIAAAAAAAAAL16a6zr2HWe7p2ZOzOasT0zfk3GY8+xPTHuQM6VKD4k5jZo" +
       "RIqUKJLiSyRFusY1\nnyIpvsQ35dpIUCB2HaAPxG5SoE2AokCAwgXSGn38KN" +
       "oCSd8pUPhH0j9JWyQogrYJ2h9FjSJtSumc\nc899jado0QogtcW99tprr/Wt" +
       "tTb30nd/v/dCnvUu0iRsd2FS3Cva1Mnv8UaWOzYeGnm+6R48sH78\n839Y//" +
       "o3Vp96rvcRvfcRP5YKo/AtPIkLpyn03quRE5lOls9s27H13uux49iSk/lG6B" +
       "87wiTWe2/k\n/i42ijJzctHJk7A6Eb6Rl6mTnee8fsj0XrWSOC+y0iqSLC96" +
       "rzGBURnDsvDDIePnxX2md8f1ndDO\nD72v924xvRfc0Nh1hJ9grlcxPHMckq" +
       "fnHXnf78TMXMNyroc8v/dju+i98+SIhyt+l+4IuqEvRk7h\nJQ+nej42uge9" +
       "Ny5FCo14N5SKzI93HekLSdnNUvTeel+mHdFLqWHtjZ3zoOi9+SQdf9nVUb18" +
       "Vstp\nSNH7+JNkZ05N1nvrCZs9Yi3uzqv/41v8f7u4fZbZdqzwJP8L3aDPPD" +
       "FIdFwnc2LLuRz4g/Letymt\nfPt2r9cRf/wJ4kua2Y/+HZn5vX/wziXNp55B" +
       "w5mBYxUPrD9E3v7092e/+/JzJzFeSpPcP0HhsZWf\nrcpf9dxv0g6Ln3jI8d" +
       "R577rzH4r/WPupv+b8x9u9l6jeHSsJyyimei87sY1ftV/s2owfO5dPOdfN\n" +
       "nYLqPR+eH91Jzr87dbh+6JzU8XzXTo3CO7ebtNfrfai7Pn313bv+Lnofxo3U" +
       "MP3QL9p7ge8WvR+5\nN+y+L83/eF9zYvah+tatbh1vP+lTYQfAZRLaTvbA+u" +
       "Xf+ed/kqD/9DdvP0TVlRhF76Mdn3sn3vdu\nePdu3Trz/OTjujkp2z75xH/6" +
       "m/df+zM/lv/t273n9N7LfhSVhWGGTudLRhgmtWM/KM5gev0R4J7x\n0oHtVb" +
       "PDXQfhB2HH6IzzThlV1vvck/i68UqqaxkdaL7/9T/6V3/woP7eCQon033sxP" +
       "1StM4Q+0vZ\nXn1P+vLqK9/83HMnovr5TqWnlbz7wdwfWH/wLfZ7v/Evfuvu" +
       "DY6L3rtPudfTI0/u8aT4fJZYjt2F\nnxv2P//fl//5515A/9btExZe7qJOYX" +
       "RY6Vz4M0/O8Zib3L8OOSdl3WZ6r7hJFhnhqes6TvQLL0vq\nmydnXLxybn/4" +
       "jy4///N0nQB2+nEJtDfwJEo7dGYXC6eTwygcO71E1Em3T6znHNx+8KfujH7z" +
       "773y\nj24/Ggc/8kjAlJzi0qtevzHNJnOc7vlv/QL/c9/5/W986WyXK8MUvT" +
       "tpaYa+1Zxl/fitDgcffYaH\n33vzY9/+C+/9pd+8NvxHb7jPssxoT3Zvfvr7" +
       "n/6L/8T4y533d16Y+0fn7GS3zzPdPvN/o1vzQ7Tz\nXRy1/NQIr7s+/LCLMU" +
       "wnvBbpdB+c2188qe3MrndW08UVyQm9T3ofecoY16aPzK/+11/9xf7Fpfin\n" +
       "MW+d2dzJn46Qjw18YB3/vvyLP/iXxW+fNX6DmROPd5qnp1WMR+A8/Y3q9Tu/" +
       "8kvR7d6Leu+1c5Yz\n4kIxwvJkD73LUzl+9ZDpfeix/sdzzmWAvf/QJ95+Eq" +
       "+PTPskWm+CTdc+UZ/aLz4K0E4Rb3TXve76\nxFUkPH+fOl873V5vbvXSU+OP" +
       "nQd8/nz/wkMIvZhmfmWcNga9F60wyU8p/8qkr93EtsuOS3Sf7tPT\n7bPNrY" +
       "7BC+A94B5w+j1/eoLnun7Xj41zSrvbATY/b0W6yT4ZhNa71y6kdLuRLmu820" +
       "14M/kJpOfZ\nL5P2MybvAPDhGzIm6TL7z/7un/v1P/v5f9MZfNV7oToZo7Pz" +
       "I7zW5Wnr8zPf/c6nX/n2v/3ZM6Q6\nNXzyP6Dm7524sqcb2W0LTtJJSZlZDm" +
       "PkBZvYfreLsc8CPo26zhmiLu1VV3n5z3/mr/777/2O+LHb\nj2xePv/U/uHR" +
       "MZcbmLNZ+2nTzfDZHzbDmfrXBp/97tfF3zYvE/sbj6caIi4j+Jf+tfPeT75q" +
       "PSNp\nPR8mz9Rn8dGfWkI5Nbv+0CPD2e5kwAULOIBqXJCkGRGpSwKnVzgGaX" +
       "RtzDyypjcEFvLBik1DNKqP\n7HFXH/NJSPRFfk8UpHIQjRbYJQca8seuiJAK" +
       "yaJSYc9pQEDFYgk7FCEXCO24QxME4WJip/R2wKrT\neDIB3ckQLKHRyOnzWi" +
       "atMVVhPM0vtodql20NVab3kcICywWiNjYCs9jWR5GjYXGDDAbBtvQGIUYS\n" +
       "KomyE13ZCg6oykvgyPbtLQmwykhAIBpPC2spm9DO2CQxItAxBOIbem8Da/yQ" +
       "zOiEk20zsyjUScHZ\ngUTGgYfKwBo6WOmO1LgUVPrhgQnbtRMkyWyliXOSpm" +
       "lzIcexAk7coe4q+cjYJ0w9DnGbyjaHGAfb\ngOHCWsQqOnBWCQcYgBfPNA8t" +
       "hP5OE5sSU7OZb9GuiYlhkQkaASb7+Q5NI7rA1LIdE/J4Xug+Hppp\n5Oh7gt" +
       "u2bIXvm9izFS8ihQVK1AtG6w/xvKBiyo4SZDHW63aYZhTP4FtGwDwj2QS5EE" +
       "nlXhLqIDmg\no5XGk7qNe/lsjXO0YE9U2dhLWLNO2BU37bfE3raNMULvd3Y+" +
       "V9ZBTqScd3BIPMzbUIDcXdyBhUXo\nyiyNsc7keMngCT1DFn5iNxWJkYkxKR" +
       "aScESP/VG8HpZ0DOssxhsCHGz9zUivMSESnASTndr3ZYed\nYpvcTgG2cQmx" +
       "SsRdDsx5vdLjxDzAA43kwaiJuZHe36tzPuZNMlrb8yHvxgF+tEdDTFR2iUcB" +
       "nLjW\niAScyvDahiMNUQdiTVaLhe6tqBKAS5oLC0vN1j7OD52gf1ztuzcgl1" +
       "RJyfBNuiYX0AgM+B0veVTE\ngpwvC6OMRYmDRNtS6qZgi3M+jvlUAo8c0oIq" +
       "nRug7Y5K5GXdD7HDsOWPykiM1jFLA3hLQVsJgA4J\nTBtltQuR2K1sfb7nFG" +
       "AAHmfNyK9IKNzt5us9qx0VghnC9XFaReAk7QdqNQ1DWdc5L8Gp/QJbOkUg\n" +
       "VrCLifFeycfi2vOgKnV5cuy60wKaKBFFqKNEgryFdsy2TMTUyz0kwypk9edl" +
       "LEezxPKiHHHH0GSM\njTWL2BEiXhM07TGiqDiDCYNauLFmg2MXsY7LqoEYW5" +
       "2G6R6TlZFfs+hqRsi51jfNPZAp7lrNpIhK\nlBzZQ+ReXHF0WDfdnCWxHk5G" +
       "xfxYDEDIznI3NRVMX3MRJOy2xHAbNYew3VCrWla9db9QcWpF0oIx\ntX2CU+" +
       "sdu8BGcFFGyLImCX0pygufc3SKKsJTNJnN59Eh0KRZqK0bpR7lOw8bTmhTV4" +
       "du2eeCjWoH\nmq1YW3K39CgoXzuLjZOxyXBOYEqBLu3pesBDphpu4moRKLMd" +
       "W69yLl4RkDzQ0HqwZVBVt1ZMPevr\nBEwoB86SVjp7UDdZbGSWaaEC1AAEOF" +
       "xj0iSu49mW11jWRHBUXxLtgrKOsm9TKL/GE2echoNERN1p\nA/cBZ8qOlhHn" +
       "rVXadswSM831kZQbrwrn1Xg8mLhGcEAGeyxaa8EMQhRKLqVFjobszF8CQbKf" +
       "SU06\nMSBrDSL9qBhW7AhofWo8EBQV3ka0yNtuQZvg0Xbg0idyYXmUvDnAgy" +
       "gQL1RVMFcTeblcGJ4Julw6\nV7G85Ui3FJb9rTKf7OyGig3NorwWY/khl1TM" +
       "qjBEHSPQNY/ShAy5ZgowsrCcLfnNhAAn3m6w5sFq\nfhzRcxnn22FymG+cfp" +
       "3TKmALE61OEu6gz7gD4Wir+WFSUzI1GSVwzJZQe2xHESuwYyBEdYadILTZ\n" +
       "JRVYM47N4YALxGLkpHUo9kVR2IlwtUydqmU6y0tmIA8VkRvtN8Jqyw3Krdg4" +
       "ycDfF5ttIy+8GbNg\npAhhm1bLMBj3g1IT4wPdHmKB7rsULi31WTBTzelMnh" +
       "Q+oTbpzIa6eD9rp76UdCIGi1HM5ow3W1ul\nPSBnzni4MzJgM6KRlLK7XQl1" +
       "OBxkj+1PfXImwXTuwDtqqg/XnGkEmylZ5bNGnyTGanwED1ObCRRt\nWmZzrS" +
       "jynSku7Fr1HajZws0WrQoPhNUmmyF9kNU18GgWXlFt+eHQ0jE99l032oJCQz" +
       "SconlbaM8S\nYDxn8APawjXtexDDDcDhkivs3OQRPZqUTsorDt53ZdtPqh1D" +
       "A6o4VeqWpObaoW1EBB5BCR5p3jip\nNC5pZWJJqDQp8AqyG7G+yK0XYsXF9J" +
       "xY6PBImCw83emzw6111NxabtvSZ+HdRAIKfoVXmTPQKn6x\nWqG0NmCOq7G6" +
       "RCgXmVbKiERktAz4VMR8wTF8TJCnw7jBdqv+ZgwIm/Fh3USCMN/SRTku5sou" +
       "ULjx\nobaOjTd2ouigCTIHaqyVq5zvAlrVbRn2gpbWix0ChF7DtiNfWjVUn5" +
       "WRUGr4FUuF3IYqMj5Mg4me\nlR18GAbYIboO1fCRigoxZYxwHgMoHdXCHKgP" +
       "42pgNhlIbbIJokXhOuP6hi41cw5zqGMBkETghCFm\nofpmZhL5YuHO5AWwX0" +
       "XmFGmjcE/mIzqHl4KiHbfKGhbjImZIjpj62UyIfNfsD/DWpal6K2qB65YB\n" +
       "YVpuhLbjgEzt5cGwy1GeQQtulNCLdncITbCK060CK4KDLwaDCpwyQxSd6GRT" +
       "kPW4DzsbNjJZeCXB\nMVE6y4Epsbt2WCwZPbHrrF0jZC47Jr4R62IdzlR9Ge" +
       "W2zNhjg9WmEg5y8JrAwGC6o49EH84OOsEp\ndeSOl1OEc83M36SbYokoy1wv" +
       "Mh3CfFvRw8Cr88xA2oSJdL31x1g6PXTZZAVxYlQixJKtl4tpf2c4\nGnaA50" +
       "RTZGQtO9tgYwT7lTwNlGy6XXiqqPojkhwtJJDP1G23szsCNazjFcJWGdVkR7" +
       "2u557ekjoN\n9cWhAs4KEG0zTwxauHKMgV3aZBeV5OFWS8kjikBL1LY654yR" +
       "mdMmPC46xNIzZpgOUYgwNWm0e0/D\nNtPtqC+Cq4SGMZ7y1HXt0UcxSAAZXP" +
       "Gw4Bk5uF/oap4uNqKKA1vdA6LOxmyxmM9toi0R20OtwdKm\nh/6clERp1Jem" +
       "YZ7YEzoIJ6N1aawEqGRmrV2vcyUHxIUSBxk+i+cykUI7dQfzCT/FjysupQ2Q" +
       "2yxB\nu0vxsQt6aXu08r7ccLW9K9BIWejlMl1iYDb3Vt2qFssWTI+sm4e+Qi" +
       "+mh7lY7f0uWWzrEc+xSzRF\nFUmAti2Da3OiOu01jn2ZTxgyCuKx0OVmTyPW" +
       "LetaRe4fWi4BqAbXCo6W0aFTwMlwn89ynEpLZb/W\nTKrFU4RxkQSxCQPXcm" +
       "qi9IUVrlr4ZoBhK5moNznQbFD2MBg0XmwfhTmipja7HUtQvKuzrUQF/GTQ\n" +
       "okuR2UBDcwevYp828uWgGobxOukX0TKWcVjZSNhWTaIWbwVsFwDEAaANwGoj" +
       "n2oBlBlzU4JP10Yy\njKfDLvzs5bW6M8SlkID+NFFjfh/bNNiH0WwxkSXxsF" +
       "jl8G6jyLw06Hbn04G7ycPBII4kymT36l4V\ndHPiK2svcz0zE1XJttdZqSFH" +
       "dhxaehGP8wro27miH4VNTZGYgriKvmuNGIQRFEq5qph0H8wfUlrp\n7pvGgC" +
       "M1xrYdP4Y+CtXRoKEN3U4NdUtQsJRyk75hQ5mNYrEo5ZPBCjeVleJThp3vHE" +
       "9d4tZxNgQ1\njc+OwRCAhgR9MPfuHGaRYjPTOXRnNWgeQxGfq42GOH1rvCUJ" +
       "GYtdft5CYlXSazhki+Gcnc1rg3GT\neIFxSJeOzMqbQFPYMkDhuI338a4IRi" +
       "0bTwZTbTpeLMxRyS/6ibOJW3mDV0Ct2GQFTQ+AuwkPYB5F\ngTD05MSfd85/" +
       "EFvdYQN0hzu2fhjNazySsqwsG1YZOyhrLBLIWOr9zqeYlDXX8c6VWmdDgHzh" +
       "DDhH\n9QbTxto0ZLStuNrZuQjjrTTLRFbDbKv7Bt+51t5ZLTGanaCTbOBoYu" +
       "H2NWUlb2YRMgxoK0aPumN3\nPhWveDMxLYOvFHBwtPbYuAY2jFRWZL0GRmWO" +
       "zSt3EKDAZks6gj1bqmASEBDXx3e7xJx6BR9qSySn\npgc+dSLG9QCyQhx0U0" +
       "50WhJIG/H3lDRUK2+h6gSyWKxKGAYhnxuBUCZAYGJtpIHe73Zm+BYd8U52\n" +
       "aPZCTYbMPNXXwWQLSrVOj8k5z3ZvhMTEBOBVM9o2ysbX9UoEVFdYe2OXEHih" +
       "SI4qGdvVtq+WeCju\nx9Kcb0dWGXoU7ErHdTCX+J29EGBpp8ynDlghXbQE0m" +
       "xpROphH6sreg+CUrLaFohfDUKzpJ2BfOiP\nBpwqROO8naYQsbW9kathDMzo" +
       "6KzkDG0Fh7A9HC03HByV002J8wa0tS2cHADQYgqhHTaGGZROSi4N\nkE1/wB" +
       "lwRU4jocvJABQwcnAg1wNvKTi8xDlwFnoV5bQxtUnBrIy5JOfqGIORxue3Vs" +
       "s3wHiGKg5F\nMRjiSP09LAQzVDusQEkGtSXQJOIgGrOafWRwosqK+XBwBIeZ" +
       "YLhZQSx3NIZQVWlPufW2BhZukA7o\nUcVrsTs6oHS/CMxyY7SHjbqB/EhrwB" +
       "bbkhHL6PMJv6nWbqmvZJCtj+sQ9+INPbQOygyua13lJtOR\nR0myNUuokeZP" +
       "UTvrx4sNNKimrUhOV+sFhwO4XE3F1EtjMmF3lBRa5VEniG5bIwfVpGZc2Bgb" +
       "6Upn\n9paiWquFi+3mbEOjWWKO+u2eJtLRCNGg+fxgUAdoXAR+jcULk89AU6" +
       "mWxzFgGt0r5QEZTzrX1JUl\nwS/2QkGnsBwKnACHdBaLZm4ifh+dOxMARQDN" +
       "g7I5O7UWrOEwcEQrZdM1aDbcEYM40BAgULeVATr4\nmtiWBnI82FWBurv1VO" +
       "ckNxFbNYaP/TQvqogMVtxkNFxxjbRYmozfrCb7ChBbl4TTdhHZR90wpCUh\n" +
       "dduySoqq0JZaM58dOuUOpusm2M/hoRQPon6DBIl1UI8YaWU2AMOHdrU2sLk1" +
       "1OFVFpRRLFS8I3B2\nq9IGoQB5o+OLqqJjJW/JTb4V8BkJ5eUIbSif6UPDLh" +
       "64YIwpg0ZHl1osZ1g03EzCrXyMx+EUWmJO\nO6+cUSMrYi0d/FEbdS9b+gAx" +
       "U3rN7uW9k8KUK8BRse+3sJvQM1VtnZxFUmuznHXvM9NkZmVTIU4K\nYbrP6R" +
       "Q92nELrIbiKhii0w2UoQIZTtoANucqVHNMuUeL2bjpm6oLBILFLw/efi76SX" +
       "IY8PGKwgqh\nmoQLQ0srvhxB1brB1Wl1tNtRF1emeU0JQKXN9hYmGOU4L0Wo" +
       "5atpX3C33UvzeDyRyGqsad1meTB3\nbQuABlIDjiXLKSuiUWEGRUsF6d7Iwe" +
       "EUkzXcySAonM1mP3E6itOuDiFfPx+RPixyBr576qCfPri7\nPN/9E08fv94+" +
       "tZen2082Re9HOg4nLg9OJ6EPbupVD/gTxb334/uV/2O+zIlidObb/DAmd69L" +
       "ATfV\ngreuKgV51vv0+5VOz+eg39j+l1d/xvi1L9++KisoRe/lIkl/LHQqJ7" +
       "ypMDzJhD1Xiq+P3T+ivvPv\nSOSXv/asEsM7P3TkA+v16lPCc57/T2/3nnt4" +
       "xP9UqfrxQfcfP9jvZ05RZvHmseP9zzw83n+1u8ZX\nx/y96+9Hjvdv7Pb42f" +
       "6leZ5Zh7l1Q4F9cKXmUPQ+djLxybzv3pj33ZuJk4fCvtJdd7vrzSth33yW\n" +
       "sKfbj/7QaT9QpmPR6++c4pHCBPaEJC9112e7660rSd76fyTJTxe9O35cJftH" +
       "6iYPyw2XtZ9nSPb6\nSf9Xkt26LCj+8acLij9+cSiNbgdfJoVz96pQc1Elvn" +
       "1xsocf+8Xd9y6+evGlL0sXX3svvVlS/n+3\npG8VvZeuJzgTiOnlILnoPX+a" +
       "/okFffiqFP/4gm793Q9Y0GUt6OKyhnlhJknoGPF5ZdeOkrh3v3Qu\nH108XX" +
       "j8qhGZXztT81+8eILoXIK8IWCuCS7NcdmRfJnsdOe7F3eTC//hfBc3+D5p9u" +
       "bXhXXxExd3\nH+lN7j8UOdl3fUVWOvfPre72hS88Q2L5VJpxDmVnxtCJi01y" +
       "17r3viH5i+eVvfdMjuflnbnlfrwL\nnSKJ7773v8uZOXNm3rt/GXU63ve/dt" +
       "V0jTB37j9E0vuE7LunG/VsFD13WfV7mE/Owf+bH4y471wW\nkB8x/JO4e/FK" +
       "2Y9C76oY/+Y15K6L86");
    final public static String jlc$ClassType$jl$1 =
      ("fOz/3/X8RfKXpvnxZhGXnxLNVfJsIucN08OpXj33zq71yX\nfzqyPvf9r9z9" +
       "1fT1f3b+x8PDPwbdYXovuWUYPloZfqR9J80c1z/Lc+eyTnypiL9+6dNnAJ0q" +
       "gN3X\neRHfvez+laL33NUG42+kl+v9XyMxARVuJgAA");
}
