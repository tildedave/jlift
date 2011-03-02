package signature;

abstract public class SignatureInterface_JIF_IMPL {
    
    public static boolean jif$Instanceof(final jif.lang.Label jif$L,
                                         final Object o) {
        if (o instanceof SignatureInterface) {
            SignatureInterface c = (SignatureInterface) o;
            return jif.lang.LabelUtil.singleton().equivalentTo(
                     c.jif$getsignature_SignatureInterface_L(), jif$L);
        }
        return false;
    }
    
    public static SignatureInterface jif$cast$signature_SignatureInterface(
      final jif.lang.Label jif$L, final Object o) {
        if (o == null) return null;
        if (SignatureInterface_JIF_IMPL.jif$Instanceof(jif$L, o))
            return (SignatureInterface) o;
        throw new ClassCastException();
    }
    
    public SignatureInterface_JIF_IMPL() { super(); }
    
    final public static String jlc$CompilerVersion$jl = "2.3.0";
    final public static long jlc$SourceLastModified$jl = 1204380219000L;
    final public static String jlc$ClassType$jl =
      ("H4sIAAAAAAAAAIVWXWwUVRS+u223P1T6Q8FKWzpASXYjbH0RIxsNTUNhyxLW" +
       "FqKUkOXu7J3twN25\n05k72wUs0ZgI8mBiwB8Sf15MSAxPEvXFRBPw3wfTB/" +
       "EFE4MxJgrRByMxKJ57787udnZpJ9nZO+ee\nc8/fd849l2+hFtdBms3oiTxl" +
       "PM5P2MSNp7Hjktw4xa57AAgZfcfmu/PfnpkcaEJdM6jLtKY55qY+\nzixOSn" +
       "wGdRZIIUscdyyXI7kZ1GMRkpsmjompeRIYmTWDel0zb2HuOcSdIi6jRcHY63" +
       "o2caROn5hC\nnTqzXO54OmeOy1F36hgu4lGPm3Q0Zbo8kUIRwyQ0586h0yiU" +
       "Qi0GxXlgXJfyvRiVJ45OCDqwd5hg\npmNgnfgizcdNK8fRcFCi4vHIXmAA0d" +
       "YC4bOsoqrZwkBAvcokiq386DR3TCsPrC3MAy0crb/vocDU\nZmP9OM6TDEf9" +
       "Qb602gKudhkWIcLR2iCbPKnkoPWBnNVka3+k899z6b+1sLQ5R3Qq7G8BoQ0B" +
       "oSli\nEIdYOlGCd7z4heQhbzCMEDCvDTArnrEtHx1M/frJsOIZaMCzP3uM6D" +
       "yj390+OLQ49nN7kzCjzWau\nKaCwxHOZ1XR5J1GyAYvrKieKzbi/+enU54ee" +
       "e4/8FkZtSRTRGfUKVhK1Eys3Xl63wjplWkRR9xuG\nS3gSNVNJijD5DeEwTE" +
       "pEOJphbWM+K9cl+556/hM/BI/4EP+Q7XFWsEHI0XYTiziYk5xdElIPzIdC\n" +
       "YPBgsHgoIG0PozniZPRLN79+dtfel86GK/Ap6+NoS6Ui4tP+KukjNTOZnMgk" +
       "96VTKBSSWh5cGhYR\n55woh9/fT3S/vM39MIyaZlC7WSh4HGcpgTLClLJ5ks" +
       "twiaOeGsxKqADOOrMAOUBvhsJBEuIlGxUd\ntCkIrWpBJmGFAS+Lp+99dzsz" +
       "f0WgQGStT5yuTIMcHFe2dcamj0wePbupSTDNN0M0hScjK5+e0W+f\n23fl+2" +
       "9uRKsQ5mikrrLqJUVlBM1PO0wnORld//jX/9nzx/mWxz8ICxi0Q8PhGGAC1b" +
       "shqGNJhST8\nbiOCFU6hVQZzCpiKLb9FdPBZh81XKRIpq8SrS4FGBCtgoGxU" +
       "d16IPHL941WfhWt7WldN85smXFVI\nTzXWBxxCgH7jjfT5V2+dOSwDXY40Rx" +
       "Hby1JTL0lD+kKQ2DUNqjXe33fhtdib1/1MrqmePuY4+IRI\nZOn5xaGLX+C3" +
       "oJKholzzJAGgwCM1IV+BeEflOlazKb6HyywCXMFymRC93M9MIXvqr6tvd2jK" +
       "GCHz\nUK0OTb43Kw/DHLXhLAAA6xK8W3wlVTsqwg4aul+7lK3+zDN/dr6Irx" +
       "1RTa13aa3tsrzCo+/8QGI7\nO/UGddzOmb2NkiKhVSeD2vbJa8T3suvp4Z8m" +
       "tl9aCHoZBjuHl5XM6D3FgaeaZs0vod4rhVF3jy0V\nStRaDAh1CLQaS/guKK" +
       "0yQ0PSjNVghPgNCQAh9ch/Htpd3wV3aHMeds05j3ESdeVEoCnIaVnGKMGW\n" +
       "dsw0RnwzmBE9bJgWpoIaF40onsJZQk/hQnZBcqa2aopB3SBqgx2ZiGmnTEOL" +
       "Ms2sHKXVt0zgakDV\ndO0JLdqAmyU0FYiAOQdFyl3oipRwZkVjcTLnmUVMic" +
       "UPsKgeF4bmCa8070yD5p2KxrZKh2KJhbIS\nA1OXJBZi5aujAZwlhqFqVSQb" +
       "F1W4jBPx3cPR6qWm++RuWcKSriK5coXuVIfVJMutnxbSjlmAy7hY\nnhZe2f" +
       "DuL1duTvWFa0aqzXVTTa2MGqukwg5bVOXG5TRI7msPb7x8eurHbLhs6GMctZ" +
       "bRJR15UgG3\nfF33+4D1r2+x2b1CzMVr17LxFp97xGty5UgehJtdRFLHLh9Z" +
       "DiV+tgaXmwNKHA0sMx2Iht1fN7yr\nEVPftHg0etXu+UpecpUxMAKzmOFRWt" +
       "MRartDxHaIYUpPIuruUtHLQqOrGCoIuq0C8j9N6fgQQwwA\nAA==");
}
