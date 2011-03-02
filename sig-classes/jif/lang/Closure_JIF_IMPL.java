package jif.lang;

abstract public class Closure_JIF_IMPL {
    
    public static boolean jif$Instanceof(final Principal jif$P,
                                         final Label jif$L, final Object o) {
        if (o instanceof Closure) {
            Closure c = (Closure) o;
            boolean ok = true;
            ok = ok &&
                   PrincipalUtil.equivalentTo(c.jif$getjif_lang_Closure_P(),
                                              jif$P);
            ok =
              ok &&
                LabelUtil.singleton().equivalentTo(
                  c.jif$getjif_lang_Closure_L(), jif$L);
            return ok;
        }
        return false;
    }
    
    public static Closure jif$cast$jif_lang_Closure(final Principal jif$P,
                                                    final Label jif$L,
                                                    final Object o) {
        if (o == null) return null;
        if (Closure_JIF_IMPL.jif$Instanceof(jif$P, jif$L, o))
            return (Closure) o;
        throw new ClassCastException();
    }
    
    public Closure_JIF_IMPL() { super(); }
    
    final public static String jlc$CompilerVersion$jl = "2.3.0";
    final public static long jlc$SourceLastModified$jl = 1202225505000L;
    final public static String jlc$ClassType$jl =
      ("H4sIAAAAAAAAAI1XW2xURRie3W23F6q9ULBcSg9QyG6E1hcxstHQNFRaDmFt" +
       "IUgJWWbPzmkPnT1z\nes6c7QKWaEwEeTAx4C3x8mJCYniyUV9MNAHvPpg+iC" +
       "+YGIwxUYg+GIlB8Z+ZPbvbswv1JDtnzsx/\nm////n/+vXQDNXou0hxGT0xR" +
       "xgf4CYd4A2nseiQ3TLHnHYCFjLFz8+25b86MrYuh9knUbtkTHHPL\nGGY2J0" +
       "U+idryJJ8lrjeUy5HcJOq0CclNENfC1DoJhMyeRF2eNWVj7rvEGyceowVB2O" +
       "X5DnGlzmBR\nR20Gsz3u+gZnrsdRh34cF/Cgzy06qFseT+koblqE5rxZdBpF" +
       "dNRoUjwFhKv14BSDUuLgiFgH8lYL\nzHRNbJCApWHGsnMc9YU5yifu3wsEwN" +
       "qUJ3yalVU12BgWUJcyiWJ7anCCu5Y9BaSNzActHK29q1Ag\nanawMYOnSIaj" +
       "njBdWm0BVYt0i2DhaFWYTEoqumhtKGZV0dofb/vnXPovLSptzhGDCvsbgWlD" +
       "iGmc\nmMQltkEU4y1/4MLoYX99FCEgXhUiVjRDWz48qP/ycZ+iWVeHZn/2OD" +
       "F4xri9Y33v4tBPLTFhRrPD\nPEtAYcnJZVTTpZ1U0QEsri5LFJsDweYn458d" +
       "fuZd8msUNY+iuMGon7dHUQuxc8OleRPMdcsmanW/\naXqEj6IGKpfiTH6DO0" +
       "yLEuGOBpg7mE/LedG5o55/xQ/BIz7EG6I9zPIOMLnaE8QmLuYk5xQF131z\n" +
       "kQgYvD6cPBSQtofRHHEzxsXrXz29e+8LZ6Nl+JT0cbTmuGUOCAxB6JgHiZEZ" +
       "Gx3JjO5L6ygSkZIf\nWOoK4ducSIHf3kt1vLjd+yCKYpOoxcrnfY6zlEDqYE" +
       "rZHMlluMROZxVOJTwAW21ZgBkgNkNBkIR1\n0UEFF20Kw6mShKMww4CRxdN3" +
       "vr2ZmVsQkReR6hbSlWng9xllW1ty4ujYsbObYoJorgE8KE7Sv7z0\njHHz3L" +
       "6F776+lqjAlqP+mmyq5RTZEDY/7TKD5MCpFfGv/r3n9/ONj74fFaFvgSLDMU" +
       "ADMnZDWMeS\nrEgFFUY4K6qjFSZz85iKraAstPJpl81VViQ6VoihXQFFOCtk" +
       "oCxOt56LP3T1oxWfRqvrWHtVwZsg\nXGVFZ8XXB1xCYP3aa+nzL984c0Q6uu" +
       "RpjuKOn6WWUZSGdEcgsCvrZOhAT/eFV5JvXA0iubIifch1\n8QkRyOKzi72v" +
       "f47fhOyFLPKskwSAAo/UhAIFYkzIebJqU3z3lUgEuMIpMiLqdxCZfPbUn5ff" +
       "atWU\nMYJnTbUOTY6b1QmjHDXjLAAAGxK8WwIlFTvKzC7qvVuJlOX9zFN/tD" +
       "2PrxxVhaxraa7ttv38w29/\nT5K72ow6udvCmbOdkgKhlUOGte2TV0dwyvZD" +
       "fT+O7Lg4Hz5lFOzsuydnxugsrHsyNm19AfleToya\nu2spU6raYkCoS+Dqtc" +
       "XZxUqTjFCvNON+MEL8egWAkHrkm0cWaivfTm3Wx5416zNOEp7sAjQFOS3L\n" +
       "GCXY1qCs9QdmMDNxxLRsTLVysUvDfWlYDqancD47L6nT27QQkY6zpIpADwjU" +
       "1aI22NGRpHbKMrUE\n06yyPq1UTGErmGqG9piWCNZZqmwpm4ENKCYkJWcwbN" +
       "1ax9CDAhBk1rcKmBKbH2AJY0BYNUU4vDKC\nOBOU8HQiuU0eKVlXpjyXlOdB" +
       "DaaEMzuR/L+y9ZJsPZlS4QTpqfnS1MTUI6n5ZOlqqpM6Ml+gQqio\n1U/gmC" +
       "SNye9OuPpqnRFs3b/0TMFyh6wkcl3FavlCsUsJq8KMV9uogP489AGFUqPy0o" +
       "Z3fl64Pt4d\nrermNtc0VNU8qqOTClsdURw23kuDpL7y4MZLp8d/yEZLhj7C" +
       "UVMJOvIgj6v8KXUKPUHeBJ2D2OxY\nJhxi2H3PUIjPPWIYE4O+vDsPqc6i38" +
       "Ae7w9jqBKmUO9RhLVwGyJuhp6afwaqfzU2LR5LXHY6v5S3\nabnHjEOjZ/qU" +
       "VpWe6jIUd1xiWtLMuLoklX8ges2BRRxaNnhJSw21DWsx2BbTaUe54T8ic9G+" +
       "uQwA\nAA==");
}
