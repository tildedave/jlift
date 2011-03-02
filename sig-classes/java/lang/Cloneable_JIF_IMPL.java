package java.lang;

abstract public class Cloneable_JIF_IMPL {
    
    public static boolean jif$Instanceof(final Object o) {
        return o instanceof Cloneable;
    }
    
    public static Cloneable jif$cast$java_lang_Cloneable(final Object o) {
        if (o == null) return null;
        if (Cloneable_JIF_IMPL.jif$Instanceof(o)) return (Cloneable) o;
        throw new ClassCastException();
    }
    
    public Cloneable_JIF_IMPL() { super(); }
    
    final public static String jlc$CompilerVersion$jl = "2.3.0";
    final public static long jlc$SourceLastModified$jl = 1202225504000L;
    final public static String jlc$ClassType$jl =
      ("H4sIAAAAAAAAAIVWX2wURRifu7bXP5yUloIVWrpASXoxtL6IkSaGpqFycISz" +
       "xSglzTK3O9suzO0s\ns7PtAYIaE0EeTAz4L0F9MSExPNmoLyaagP99MH0QXz" +
       "AxGGOiEH0wEoPiNzO3d9e9Qje5vdmZ75vv\nN9/3+75vLt1ATQFHhs/osWnK" +
       "xKA45pNgMI95QOxRioNgP0yY1vbNt+e+Pb17fQNqn0TtrjchsHCt\nUeYJUh" +
       "KTKF0kxQLhwYhtE3sSdXiE2BOEu5i6x0GQeZOoM3CnPSxCToJxEjA6KwU7g9" +
       "AnXNmMJnMo\nbTEvEDy0BOOBQKtyh/EsHgqFS4dybiCGcyjluITawVF0CiVy" +
       "qMmheBoE1+aiUwypHYfG5DyIt7kA\nkzvYIpFK4xHXswXqi2tUTty/BwRAtb" +
       "lIxAyrmGr0MEygTg2JYm96aEJw15sG0SYWghWB1t11UxBq\n8bF1BE8TU6Du" +
       "uFxeL4FUq3KLVBFoTVxM7VTiaF0sZjXR2pdK/3s2/7eRVJhtYlGJvwmUNsSU" +
       "xolD\nOPEsohVvhYPnswfCniRCILwmJqxlRrZ89GTu10/6tMz6JWT2FQ4TS5" +
       "jW7W09vQsjP7c2SBgtPgtc\nSYVFJ1dRzZdXhks+cHFtZUe5OBgtfjr++YHn" +
       "3iO/JVFLFqUsRsOil0WtxLNHy+NmGOdcj+jZfY4T\nEJFFjVRNpZj6Bnc4Li" +
       "XSHY0w9rGYUeOSf0c//8kfgkd+yH+I9igr+qDEjceJRzgWxPZLUuu+uUQC\n" +
       "APfEk4cC03YxahNuWhevf/3Mzj0vnUlW6FO2J1CPJNGgJBHEjnkEFygxd2fH" +
       "zOzefA4lEmrz+xd7\nQ7rXllnw+/vDq17eGnyYRA2TqNUtFkMh9SF7MKVsjt" +
       "imUPTpqKGqYgjQK10ApgFpTQobKWaXfDTL\n0aY4o6p5mIURBposnLrz3U1z" +
       "bl4GXwarS51BQQPXH9HY0pmJqd2HzmxqkEJzjeBEeZL+5Xc3rZtn\n985//8" +
       "21gSpzBeqvS6h6TZkQcfh5zixiQ8Gpbv/6P7v+ONf06AdJGf1WqDMCAzsgaT" +
       "fEbSxKjOGo\nyEhnJXNohcN4EVO5FFWGNjHD2Vx1RhFkhXy1a65IZ8UAqvp0" +
       "64XUQ1c/XvFZsraUtdfUvAkidGJ0\nVH29nxMC89feyJ979cbpg8rRZU8LlP" +
       "LDAnWtkgLSlYDArl4iSQe7u86/lrlwNYrk6uruI5zjYzKQ\npecXet/8Ar8F" +
       "CQyJFLjHCRAFHmUJRQbke0CNMzWL8ruvLCLJFc+SMVnCo8gUCyf+uvx2m6HB" +
       "SJ0H\nam0Y6r1ZnzApUAsuAAGwpci7JTJSxVFR5qj3blVSVfjTT/+ZfhFfmd" +
       "K1rHNxru30wuLD7/xAMjvS\n1hLp2yqYv5WSWUKrh4xb26u6R3TK9qf6fhrb" +
       "dvFk/JRJwNl3T03T6phd/0TDjPsl5HslMera12Kl\n4VrEwFBOoPt68uxypl" +
       "lFqFfBWAkg5K9XEgjpJ6GL37P1xW+7cTTEgXs0ZIIMBOoiYGjKGQXGKMGe\n" +
       "cdh1+iMYzBk46LgepobuCydwsXDSYFNjGeOEhmQMMMOtSBuVYpgZPpkpV9ol" +
       "aKBiD2zXCJYmY6Jc\nfeR3B1wlqhVXY1mewjsEWrn4NEF9F81ztwhNarbcRV" +
       "/Z8O4v89fHu5I1V43Ndd2+VkdfN5TBNl/S\nduO9LCjpKw9uvHRq/MdCsgz0" +
       "EYGay+5XB3lMR7bcxrqjiEZtTS6uWsa58rXzno6Vn7uWd+KEbHbg\nRAsHol" +
       "/GwJQxMCuBjuKzeomOWJINuK49yorVXXdp1Vcra9PCoYHLfsdXqspXrj8puI" +
       "M4IaU1KVGb\nHimfE8dVcFO6eGvvHIJMr8AScJ2AP4XX1OsWzMl1ObZ97ZD/" +
       "ATaSLo5WCwAA");
}
