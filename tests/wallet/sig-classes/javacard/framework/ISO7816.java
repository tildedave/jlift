package javacard.framework;

public interface ISO7816 {
    int __JIF_SIG_OF_JAVA_CLASS$20030619 = 0;
    short SW_NO_ERROR = 0;
    short SW_BYTES_REMAINING_00 = 0;
    short SW_WRONG_LENGTH = 0;
    short SW_SECURITY_STATUS_NOT_SATISFIED = 0;
    short SW_FILE_INVALID = 0;
    short SW_DATA_INVALID = 0;
    short SW_CONDITIONS_NOT_SATISFIED = 0;
    short SW_COMMAND_NOT_ALLOWED = 0;
    short SW_APPLET_SELECT_FAILED = 0;
    short SW_WRONG_DATA = 0;
    short SW_FUNC_NOT_SUPPORTED = 0;
    short SW_FILE_NOT_FOUND = 0;
    short SW_RECORD_NOT_FOUND = 0;
    short SW_INCORRECT_P1P2 = 0;
    short SW_WRONG_P1P2 = 0;
    short SW_CORRECT_LENGTH_00 = 0;
    short SW_INS_NOT_SUPPORTED = 0;
    short SW_CLA_NOT_SUPPORTED = 0;
    short SW_UNKNOWN = 0;
    short SW_FILE_FULL = 0;
    short SW_LOGICAL_CHANNEL_NOT_SUPPORTED = 0;
    short SW_SECURE_MESSAGING_NOT_SUPPORTED = 0;
    short SW_WARNING_STATE_UNCHANGED = 0;
    byte OFFSET_CLA = 0;
    byte OFFSET_INS = 0;
    byte OFFSET_P1 = 0;
    byte OFFSET_P2 = 0;
    byte OFFSET_LC = 0;
    byte OFFSET_CDATA = 0;
    byte CLA_ISO7816 = 0;
    byte INS_SELECT = 0;
    byte INS_EXTERNAL_AUTHENTICATE = 0;
    String jlc$CompilerVersion$jif = "3.1.1";
    long jlc$SourceLastModified$jif = 1196711306000L;
    String jlc$ClassType$jif =
      ("H4sIAAAAAAAAAKWaC2wcxRmAx4/4HfyIEzuJn3FM7EDubEJCiivEcr5zNlnf" +
       "HXfnODGCZb23Z5bs\n3W139+xLCggUKeEhtYoaKpBaQtsI0jRULVS0ohWteJ" +
       "RSFYlGKgGqUsqjQiq0lBYRISr6z+zs3d7a\n8e45lmaf//zzzf//88/cjs9+" +
       "hFbpGuq+XU75jEOqpPv2yKmooOlSMppVDiXgES+eO/rI917c9cFL\nlaiKQ/" +
       "VCzrgtq8nGIQO1cLcL84I/Z8iKn5N1Y4xDLXJGN4SMIQuGlAxp2bSB+jkVVM" +
       "0pWcMv5Q2/\nKmhC2k8a80cDiqDrUK2GPNW/hu5CFXkN9Vk1KJRJRIRNpGsH" +
       "vlj4w7E9G6tQ8wxqljNxQzBkMZDN\nGNDEDGpKS+lZSdOZZFJKzqDWjCQl45" +
       "ImC4p8GASzmRnUpstzGcHIaZIek/SsMo8F2/ScKmmkTesh\nh5rELPRJy4lG" +
       "VjMJgTclS0rSuluVUoQ53UDrij01+xfCz6F7DTKAaSlBlKwq1QflTNJAvc4a" +
       "hT5u\n3gsCULU2LYG9C01VZwR4gNpMyytCZs4fNzQ5Mweiq7I5aMVAGy6qFI" +
       "TqVEE8KMxJvIE6nXJR8xVI\n1RND4CoGWusUI5rASxscXrL5J1LT9L/7o5/1" +
       "VRLmpCQqmL8WKvU4KsWklKRJGVEyK17I+U6wB3Jd\nlQiB8FqHsCnDDP58iv" +
       "vg172mzMYlZCKzt0uiwYtf7OzqPse8V1+FMerUrC5j55f0nARvlL4Zy6sw\n" +
       "GtYVNOKXPuvlb2K/PXD3GekflaiORTViVsmlMyyqlzLJAL2uhWtOzkjm00gq" +
       "pUsGi6oV8qgmS+7B\nHClZkbA5auBaFYzbyHVeRQjVQlkPpQmZf+RsoEY2Hr" +
       "lm1+hOHwxTA10OkbtN10Q/DgFR0JL+FIwd\naSGrHfTbBPNYbfNCRQX0qMs5" +
       "nhQIxd1ZJSlpvPj4u7+/I7j3vnsrC/FFgSCSrCZ8hSZ8tAlUUUFU\nd5QaC1" +
       "s/iXPBh0+OtXxjm/40JI0ZVC+n0zlDmFUkGE6ComQXpCRvkOhqtUWylQ2aZi" +
       "EQIaZ5BRSZ\nOUFF8xra5Ay44sBkSdoRpXN3ffnqP/mFp3BsYF+2Y+0mGnjm" +
       "oMnWNBy/ec+t926qwkIL1djIeTLA\nOnArTmOF8FC39Kdnv/7p84809Jn6cZ" +
       "0uoqAfW7kki5ZU48XK028fG+5sfqMSVc6gy24TdDYDUYUT\nkqTB0FWEWUkx" +
       "UAcHOugwI4/8HD6OLTFwHPoPPzv1yIVXjLeIF+shZRkChB1kgx7n8C0ZcViz" +
       "s8P7\nBK2od9dr8601PzmZrkS1M6iFpEJI7/sEJSfFJUi1DbIeoA85tLrkfW" +
       "maMsfkGE0HBupyctmaHbNy\nKjZwtT0q4RpL4+t6EuGriUwLuKAOyiiU1XTw" +
       "kDN+2YYPa/IkXFtJPGAgHws5eU7S2t559NRn9xzb\nBYZj0ap5DA42aSnKhX" +
       "N4Ljl69sHuxhNvP0Acb2kexhHm9AvmvnCkZuT8rxpfrLRPD8226RJsZyab\n" +
       "1mKAJjQJ2/QvD0W/9eBHx24i0WmGZ5UBSuSMAGarUXOziizChU7mvLyB+nh+" +
       "Dxvi4+wEHwnxe5h9\nDB/gmHh881UjI9tHdo5+BQ/TYmySiJKSZi79Y+P2e/" +
       "t2pNohLCFsSMjBZEsG3QCkTFzDujft3aTm\nQd3GojpSB2ZoWSOKefH8O3e+" +
       "/OTkc6eIygaIiBQsJWTxEHa5M7oDhbfg8kY8S85Zwt2LhNniaxy2\nHU4G2v" +
       "74kcGDdZ+nH6/EkdOYlHRRk1WcwGkANehyWlVkmMGTtJs1RnYPeAFPx6RNTc" +
       "joCixgzMGX\nIC+DeVXDE+O8AMsISGQk7PxmCKp5FZ+vA8MMEChLQdEsRSW8" +
       "uOPuD/771J+e3mLOX72lNRZJ9/+4\n++PNZ28ZJIGHI6bH2e+YJEAiNw3Di9" +
       "/cLw/cOLR+gph/VXaBJIFemzFVWC6IsirAtGdd4RWYRrTg\nfgSAqnORg6n6" +
       "Ia7mF098+fF1JLJtpsSTqtNhlq+okfApBB0YLOousPhuyBpGNl0g4sWrfjfS" +
       "wZye\n/KndSo46NunRtZ2dfZ9IDQSr4M8Bhz8LFWw+tegAbLjUFQ4ku1NGm7" +
       "7/7PtnfvioCed0+lI1fvCj\nz04d3vLYXHHimFDNSWfvUj6dhhV20afZ4//a" +
       "fuJI6KTNp8RR0NUFImi6DR/HTTvf6GhiUYPOFYEm\np2E+mqfLsOM9p/7+1L" +
       "ux9krbWnVg0XLRXsdcr9pSRP9yLRDpF67oP3tX7K1Z04htpQuJYCaX3nHy\n" +
       "dWn4+iZxiZVJFeQKssCpMEcePm4qTAVroVwB5TI6FVy2eCrAF1dYj28mtz5y" +
       "HKUpF19vx4er8WEH\n5NnG+DQfjvDBWCwSI6LXmLY2W99HrXsAcrUOv5Bc8H" +
       "ZAaaZ4zS54c57w1gLeDQcSwTgfC04ybJgN\nT/AjIyWg+JBcnssPpYVytbhw" +
       "ZTxxNQPXdCwCNFwwPJHYXS7R9VBaKVGrC1HOE1EfEMWDgakYmzjA\nxxNMYi" +
       "oOjk3wcSbBxkNscHwlRmujiG0uiHd4NlqI5YI8G97HcOyKiNZQojUuREc8E4" +
       "0zCWalRGNQ\n2ilRuwvR/Z6INgJRIBIeZxNsJHyJHtxJz8g6L0N33BPdOkI3" +
       "OcmExwkaw3GR6fLBroGyjoKtcwF7\nyBNYB4Ax0SgXBHMFuWAgwYcYiLSyyb" +
       "ZB6aBkHS5kJz2RrS5kChxo5fLgjNpJeTpdeB7znFFDU+GA\nGVpT0Wgklijf" +
       "Tvg3yXrKtd6F6wlPXK1WcsBcochUuGym7VA2UKYNLkw/88S0BphiwUAkNr5y" +
       "Kmyp\njZRqowvVLz1big0DVQzHeXQ0etVKoryLMnW5MD1XZpSvhOdqKN2Up9" +
       "uF52VPPO0kUZkWMmfoFSwb\nMFYPxepxwXrVMxZrpfWVjj2M1Uuxel2wznu3" +
       "FsdcGtZWKH0Uq88F6y1PWA2ANRXeG45Mh8uFuRJK\nP4Xpd4F53xNMk5WfQl" +
       "Mct5Ll3iaKs8kF50PPyz0uMsEGGI4P7GbC4SB3ae5joAxQxAEXxP94Quy3\n" +
       "VqRBfjIYjzMTePF+SYzXQtlMGTe7MH7uiXEDzltMjPyuwIvmIAQcNudE+XDD" +
       "UAYp3ODycPTWNf4j\noVAcVjQwNEtgyI3tp1n17CFDcoe7nMJd7gLXWBYcpD" +
       "OnpSrqlocZgrKFwmxxgWnzBFNPYaKjK2EZ\noixDLizry2NZNA16YRmmLMMu" +
       "LH1lsXCBcllwQ1spy1YXli3eEqgVzUutg91wtlIkZJ2XwfF5wmnE\nEx7d9y" +
       "mXBjvnSkpzpQvNDm+jCa8KzF8u5cLsQuaCDlnnZWC+6glmPYYJ7k8EY2GYXZ" +
       "ipxO5gOAEz\nTSK4LFt+CeUGqhNmdUMTRAOisbBjTJqzbU+tWWK/09fZfuLb" +
       "w98579yVQvhDXPfFtmnJR7hj+//d\ndFR44WZcFVecgbaNrLpNkeYlpdiqU8" +
       "kk2ZW29muap3v/Ftr5+J2L2zdQLQ0c/Om6c9G+vrkXLW46\nd+vQ82rry+Sb" +
       "fWG/uI5Ddamcoth3f2zXNaompWRCXWf7xlyRMFDb4o1L6FfhGiNWxE3pabC7" +
       "JY3v\n95sdaDfse0Dm5pXNC93Lfo9++KbjamaEka1dyDXFTR5G04RDeBMyf8" +
       "+57odfEr5bhfedqnX5sGTu\nKxUd11GynziZM/9bghc/VG+Z+HPsr2fox+eC" +
       "TaW84SP/R2H5qFDjmecH73vzzTcGzO/HoiIcPox7\nUsuh2lRWSwumo/F/Xv" +
       "RfVJulS377tdT9R95rtgJmNT60mrvNRfv0XFwPvt63euz1vc88c3qJgC2o\n" +
       "sHWf1LGC7eqhT2svvPLJ9YW6FbfY7PZ/bSTmCtIiAAA=");
    String jlc$CompilerVersion$jl = "2.3.0";
    long jlc$SourceLastModified$jl = 1196711306000L;
    String jlc$ClassType$jl =
      ("H4sIAAAAAAAAAJV6a8zjVnqeZmyPbXl27Zm1vV5fx5fdtc318CJSotYIsBRv" +
       "IkWRFC+6MAi+ULxI\nFK/iRaLUZNOiaDbporkgmzY/0qRFAwQtFmjRoO2PFm" +
       "2CXIoWCRDsj6Q/2rRNgiJALkCAFFkUaVNS\nn77xeGyP5A94ySPyPed5+J7n" +
       "vOeQ3/n2nzYey9LGnSQOdosgzu/mu8TJ7spmmjk2GZhZplUXLqyv\nvvlX29" +
       "/8Bv/SI42njcbTXqTmZu5ZZBzlTpkbjZuhE86dNCNs27GNxq3IcWzVST0z8P" +
       "aVYxwZjduZ\nt4jMvEidTHGyONjUjrezInHSA+bVRaFx04qjLE8LK4/TLG88" +
       "I6zMjQkWuReAgpfl7wuNG67nBHa2\nbny9cU1oPOYG5qJyfF64egrw0CLI1N" +
       "cr96ZX0Uxd03Kuqjzqe5GdN157sMa9J35rUDlUVR8PnXwZ\n34N6NDKrC43b" +
       "l5QCM1qAap560aJyfSwuKpS88eInNlo5PZGYlm8unIu88cKDfvLlrcrryUNY" +
       "6ip5\n47kH3Q4tlWnjxQf67L7ekm7c/L9/V/7LO9cPnG3HCmr+j1WVXn2gku" +
       "K4TupElnNZ8bvF3W9xs+Ll\n641G5fzcA86XPsQX/40u/NF/eO3S56WP8ZHm" +
       "K8fKL6y/ar/8yneIP3zykZrGE0mcebUUPvTkh16V\nj3feL5NKi8/fa7G+ef" +
       "fq5i8rvzH7m//M+ePrjSe4xg0rDoow4hpPOpFNHsuPV2XBi5zLq5LrZk7O\n" +
       "NR4NDpduxIffVThcL3DqcDxalRMzXx7KZdJoNB6v7AuV3Wxc/h3OeeMpTpU6" +
       "ONy+u/LcvPGlSsfv\nZakF1hKwzNQG3bQSxTZOffA+x7Ju9jPba9eqJ3r5wd" +
       "EVVFLsx4HtpBfWL/7Bf/4BevCjP3L9nr6O\nhColXUHcvQdx9wjRuHbt0PTn" +
       "PxysOvp2PUj+5F++/8yPvZf96+uNR4zGk14YFrk5D5xqcJlBEG8d\n+yI/qO" +
       "vWfUo+CKhS3815JcRK0xdB1dBB+FV0NmnjjQcF98Ew5aqSWanoO1//69/+s4" +
       "vtL9XaqPvy\n2br1S2pVz/iX3G6+o34f//0/8sYjtdP20TrI5WGAPVejPBgs" +
       "ph7qV+2H87/xv3/155p3Ltuv67x4\naOCt7KPS/lDFC2v/7/Wf++5v5b93iP" +
       "OTVYrJzUoY1Xh99cEB9qExUY+0BymNzfSDdvHf2dy68S9+\nPrzeeNxoPHNI" +
       "XWaUj82gcFSnSo1NLyOPF4XGZz50/8OJ5HLUvH8csHnj5Qd53Qf7/lXWq0Nw" +
       "/X7d\nVOXauy4/ftDgUwefz1ZBeqIyuLLPHOV9ONc3n6kPt8qDoG4deqwmdJ" +
       "ersubCSW///j/6hb/8W9/A\nq8Bxjcc2NfEqJs984CcWde7/4W//9CtPfet/" +
       "fPPQNVctf7HWwIP9UvP+7t++Af3uv3vq16/fn8Cf\nvi/TV7G7TAe3PpCQlj" +
       "p1TP/bz8g/9dN/+o3vPejnUkCP5FUjXmRWYbuRFPPAs6pCdpijyrxx5+KC\n" +
       "55gLlWMvJOaCJ8bEBSkQqvoWAkEtqA13P0Y9cuqFVd7ZHBPjT776C//rl/5A" +
       "efb6fbPHmx9J4PfX\nuZxBDl3QTMoK4fWHIRy8fw14/dtfV35vfplZb394aN" +
       "NREWI//1+cd7520/qYXPFINccdUs6hu+/c\n6/TnKgMq++yx0z/70U6vC1++" +
       "uvzVw893D8f3jsGty2B9gOoDXEX0KXVyIUoXtKJIysG1lVwO4XbV\nEdkyTj" +
       "+RC1bZ00cuT5/gQp7F5bmKS2+m0eqFQg8JTuRE9gI63P3aJ5EAK3vmSOKZEy" +
       "S4s0g8XZGY\nKFIFLdAiq/UfCv+1ym4d4W+dgJfOgr9Twas0qSucNrtQNULT" +
       "1ap/tAuV0DiV4WjqZDhuH/ncPsFH\nPzscDCfQF5w4JgTuNPznjvCfOwH/vW" +
       "fDU4RGnAX/fmXPHuGfPQFvngX/UgVPSiLFaZwkfpqOaB/P\njavzQ6gszqLy" +
       "/IHKcEiI1IEHIQjS5ASLTmXPH1k8f4JFeBaLz1csCFkW6CoQtECT2gVDVOp4" +
       "OI33\nKvv8kcbnT9DIzqLxmXuDtBbHQ8HrNPXCEfyFE+C7s9MUo4vkpRx0WZ" +
       "YU7UQE6sn6C0cSXzhB4ofO\nInHralzWJBhJFx9OoFXZi0cCL54g8HfOIvC5" +
       "ioBCk5JCnUmhjsFLRwovnaDwzbNjwIkVBaUWogzL\nyEkZvnwk8PIJAj/5KW" +
       "V4Ehyt7JUj+CsnwP/BWeDPHhLC5bNfTlWnJsuaw6tHDq+e4PAPz+bAXSXG\n" +
       "s0ZCzeG1I4fXTnD4J+fHQSA+BYd3K7tz5HDnBId/ehaHZsVBFweiNBEfivyV" +
       "yl4/Ir9+Avmfn4V8\n8yoPMLogPBT7a5W9ccR+4wT2vzp7rSJILEcSwgXZJ0" +
       "SRFj5FLxCVvXnk8+YJPv/2LD6vX62d6Ish\nraoEWy8gzyf01creOhJ66wSh" +
       "XzmL0It1fiCUw0K2XsvRlUjqQLEnmLxT2RePTL54gsl/PE+gEsOo\n1aRdDZ" +
       "QHV/mPzne58zAmXzoy+dIJJr/9qZhUaaO+9FufhPx2ZV8+In/5BPLvnIX85B" +
       "FZhk8Cv30E\nfvsE8H/9dMDISeB3jsDvnAD+/U8FLJAPBa7fKt89Ar97AviP" +
       "zstKV3K7Wp59Iva7R/zG1fkh2H92\nFvZT9WRw/Lj2UOg6xl85Qn/lBPRfnK" +
       "ftei68XBc/FBlvXK5GGlfnhyD/n7OQv1Aj01ONVsQqGRO6\n1qdFrUrMGv3x" +
       "RP768u//1VYj1T/qc964TcZh4gVOeod1Iic1c8dOKlJ547HWXfhuPXSuNc57" +
       "VVgF\n1ltXjY2dNPPi6K2V5x6q3c7v/+50+eX9PpI1yrXLz1if/cBNiKPF+9" +
       "/8w5/4zR9/879fb1zjr75h\n1VWqwXPt+X/896w6iteePC871wTVuEgtRzCz" +
       "fBjbnus59j2O9+fIIP5YfvnT7T6accTV32BiksjW\nKpWQKFEQ7bs7jt4XfN" +
       "Rihh2aKuY0T3rehrZSbC8GkdufupFEsaQxR1oGNs+arQnc6gKlQC4W+sCh\n" +
       "l/ksNgdJzxzlGQetFF0Yq1C8nOQOr6jMLGAHC3ZdvcevRt4aCnozhVPT9h7Y" +
       "Zy27YzZxB8WC0HRB\ncL/fV0ccB61V1gWWzDxhizUw1TvYipbWibxBdoMwjq" +
       "VAHSbJmPKmC1oFHAlMuS7csboWIzQD0xuM\nwDG5Txf5Go9YbiTBlmAJOMqB" +
       "Bu4NIkyEZ2hJCVOeFVF1yI0KNiDKBJmrPWDlj8dDKJlYww02QsDm\nIFySiI" +
       "+bOGRR3ppK9QHCyuIEXBYYyUx9sTv24QGv8AqniAmb9TfBCt126C7bzkf4tl" +
       "wHK7Ec7Xv+\n0KDEJitSNMUmAxUIDNWWDIPhB/s1NVmRowDFuJBEE5wl1H2o" +
       "RHqS5XvW6nOmwTk+qGbDGGcQULUG\nYehrTrZsLrGUFmSF36EBO14N+YVmBS" +
       "qUqkVQjPV4hLV0KvVWEAyULrlvMx1KGxCmGvSCwXYNj9xA\nE3liY6HmUsOK" +
       "ZmuRkbqgI8KM7ZIQPDa2CroQk+Ful5NGhrrifoQNt4637nCYORwT7K69Zjht" +
       "PaZp\nfDAs+Q1Ht6Mpr+DlmGvCikihZMxOikznsJRQy8yapgLGw4LWFcBOjI" +
       "HwWBHChBytQwGfDDY6Osgm\nfjBQeHZM7wZwhwP09rbX4u1mMPJ6OLPuZ2RE" +
       "UTKGe5vNAu4g4X44CwxysM+hwtvOcrcj7ddy5uBj\nGGCGTqqnJLbCMBeKCG" +
       "UTCKixa2fbJpvPxqI0mMrFJpgjA1CeUi3W8d1KMtWia+yNszConnkd7AWe\n" +
       "EJCi0q+091chBC3WiZ6xXjse4+tFqAKdXrNPBoY/JzCz6JnMbD/f9ceOMlqN" +
       "R4EvJ6PQcAEli+kZ\n0k+DEAaWUn9bagQMSf48F2FoBFILRoF4AWC4rd00VT" +
       "btu9q8J4uzuSuSymgKrYcZFi1HXGnDBq8G\nyUZeoUUHALnOqrWJspQEGWah" +
       "kmpKGvhSNfCcCHRdZ/SmjY1MEs5Abyr3lhAg5dNyFYIoxHOQMSaB\nCNoHVM" +
       "joPku3q6w3miaksvGl5TrXJ7tghJiGR48q4WE9RTWaks4weJmaGLFD0aznq3" +
       "asJFOx5brR\nWgZDJmn1shkDIDMACtloFcbDaCL2dqBjAXkXaa+3PifA9opz" +
       "NIFoShGRaqHE4AzZ6g7saAN2EhzF\noS4ftS0DKuI14U46vu9hho7i0wGE4C" +
       "4LYJm7Hqpjq73YIahmUzNKn+la019rPYiZ0BPdL3XC6kvF\nYsuhIN6n2pKg" +
       "Mdu+HMvyfgPvuPF0JTgcA2RLQ/HJZQ/2F2UnwknIsMbePsPlTrM3QGK7a6d4" +
       "G4Ll\nfWikvgoP2x6T0DzXATOaV12G1z2YmnNOTOBYLqS+OwnNYeZWcYT6vS" +
       "LZWXlo9ufirjlYGdtcg9ao\nkggokLfpgk09fSsUA0LaeZ4OT7WtT4lV0Gay" +
       "CXL+lMl5mSUW/W2GOXpHRXIM64pK1C3382ZvZqwg\nM8EoAkVMNRospyIqrI" +
       "J2N+sX5VLcyBBhzRHfKZf9sEtswk1E+kah4WmZuKmgyBSOIG2Fz4ydOW/C\n" +
       "4YDgVyMeAUcbCpqAXafXLxHUgyYha2B6Ty3UIcGaqT6V9p63J5d6J9wIux4Q" +
       "LQGq3HgosKX726h0\nsGW3SfXARdhf4jisr7hu3rdjDOiNQG0W7lSPH6iG1+" +
       "nOABlxwBxJxHgpkEymDlc9g87Y1oj0NWZP\nFSs3b5la2BzZLTZeJWoPMeKJ" +
       "Kg0ct49LLQcAJc232g63W5ic1xtl/EQr6b6JDTsxtZy7C9yf4SqO\nVQFHhK" +
       "UYeWa5Q5p9HWWRTQf1LZayDSDqt0Hat6VZ0FcUH55YW9YnRGe5mwRjPxyyWs" +
       "5nsBLNZIdg\n5LKtiJq1ikmvXOyH7LTJkQDljKcUDUg4Mcw4lR35HbkMyUGC" +
       "MQawIfDhkCaXwsimM0Wd4IUkFxTY\n6cA84LRCQZp2kEBUDL4LAmgTAGTRQH" +
       "CD59g+1573EQeY8TThidsW5XVBS9iCliQowCJZguOp2eM5\nxgn3zHhCtrnM" +
       "S4mctW1UmcyUVFs03YVKrCNJWbSjNmd2Qgbq4oDLwLzA50mmautcXAZrIPZd" +
       "Iun1\noJkgxUUmL4tdL2HFAFqg2SR1V666NXZOExxEthqjbbGIlNxAbNfoct" +
       "RITdEh3lvucYGZbIHcWRBz\nYaUzI9QvYJENOx2wI+eRu/Fi0rEhIOBgd0Ou" +
       "mnsItVsbYTbt972QoUR14m70VdteLscIZLoGCoKm\nW/Bce6CJCa4okKgzXO" +
       "i1KQXsa9rGbbNLT5/JEjOjw2aC0EC4doVMGnYTsAPAQlF2/XQ+icYLaCoo\n" +
       "08y20m6iKnJ3JyhUwrQ7sdzKp50BbEDoMk9GBT8jzWkr6u+a9iRCAc8oPWPM" +
       "M85okGVkMqQtRw7R\ntUaM03i87WEa12bGO8QhU4KB7dkKwcN8j+36Az1h4O" +
       "6mRJ0Un9D9JiZGLECN3NWG6k3Ircq1hWko\nzJBJt9tPpxlhpbq0wXoW3jcI" +
       "0zTQvYqhFtbZwgvBoUaWutUWLgmhuzm/EJsaSKy43WQB8D0ryjI5\nmY13+X" +
       "y5NbSyFEfy1Ca3pMhstzLfn03VjtId98zBmE1JuC/RMO3Swwm1MP1ZuF8Rza" +
       "rKcObF0spv\nbS113it9MbP620AJi5XHVpNm30bFYM1hMw5nu52+Je0GnmiM" +
       "4pVeKLSF0yi0JydmoLDiqIl4gVJs\nNwpotDlxUS12epxjlGLG93d8Py/5Pb" +
       "5oC5DYavVkW/K5lmSFdLn1zW2LVdqu0EtjdreKB7m1ZTrN\nXU+c9VCihNTF" +
       "tNcfMsWEWGxn1swT1N1ubvTaZs/vq5jP7xElrc7ksowxV1isxIR3tMR2ep19" +
       "xDCU\nDrBEkx0nGjRZ+IG3crXNorWbpoPcbjPtbSBPB9VIz3pWbC31gt2FnT" +
       "GB6IOZKPjbaoWpQr1k6W11\nwhdTrFpU7pLmylDW8XYzFvbw3FtA1Hi07zvT" +
       "EeTk40Gc7oRqZsOGFDw1EnWzlOz1QET0pFswGDKW\nZQnydcOfDEM3ZtTtvD" +
       "lkZnLpJOV2mo6HpZEm7mS8WiFDWC9KJ0C2+6hoMZVw2dyYDPqQvsZxpr2D\n" +
       "5QmBEKIydVgUGy7xgikUftCU0WliowzWqnrVxybuKBhaOSYELc8cZLjPYJAs" +
       "7/ZQMcxoluVQucPO\nSVTttUwHW8VEOSoJelrKXXRU7FrN8VrpuStuttoPNU" +
       "dmAHW2n47yoEqDTmfY7xTelKb2QT5cybti\ny21CjhhZYmHTwJSidGKWtCRq" +
       "Z8naRHTRVTMeE9Z4NdqKdL7Yca08gSO4QzKUFTudaMjbnYUwnfRm\nVkJJUa" +
       "Km6yC2jEU6YLZhWS4m0b7HiPLMHKwGCG00Y69alnv7dIeIhkjA7ZkShsJwWA" +
       "h9OMimZM8K\ngu0IzpIdPJsGXsuh14vxXi/yRRfe5gHnGzwy3boMtg0jukmX" +
       "Hbu7C6e91SZS/WSfjDYiFQIr0sQH\newYvMEvIRoVJUkEUGRMSaC+CUJ7N+I" +
       "6PV6s2djY1vKSUsIVE0Pumpg5QgckB0tHZ9VIMbX2oKYTk\nZfPeSuPX+7kO" +
       "5JPJAIS6ruEWwnzbxbFOdzcebZbUVHaZPIx4pbNcASBoN21GQdGJYTFjx1rM" +
       "TTEV\nEtF3ZkFie2sd933EmCDdsDCT1gZk8hQDeERA9vZIGEEWKIVtq8sCm9" +
       "SfChO03bSBzQhH0nQHqHN3\nwoIFh1G2GVpxvmy5yH4qmtWL7UQhpxO3UwY7" +
       "ZchQczSGhTbjLllPnJR0KzHnZIeftTbNLpbZJuVm\nDkSFWLXKI7stwY27AD" +
       "CYr8zQpH0TtnfUlNEUjIVQhXJ6w52LA1gntl06xeZL17HX0RTLWK1oDkrd\n" +
       "hmF2F+09Ywh254ulBY5ZwAD6cpYiblwCDqmZExxVZVBB+tiaoVahY/U7rc1+" +
       "r1nsXgL7scdZclx2\nmnpWLJFAmajbIoxBBpxuFHfMF4Ha6nRyW+sqXSzNF3" +
       "woam0oTDei4m6gKbOVN3m75RR7eGPhuNVa\n8wsSiZtZf+LCAkt2nPVSQ7LN" +
       "Rhb47mzSZuburCMb4FzVQjLnvOo9+XvqF+jb5/2T5/AZ4d5uvurl\nvK77+O" +
       "FlvPyYFvLGE+Y8y1PTyvPGk/f2Cx7avG9z0uc+Zrfb3Ree/dbff+dnf/fBPU" +
       "mNetPHK5+0\nSe+w4eMb0z+/+cPmr31fXbWu2Kmw8zh5L3A2TvAB6oONDA97" +
       "Eq/2Aj09ee1/Mu1f/MGP4ueNx49f\ntOqNNC98ZI/n5U5E643vfP/bv5rc+k" +
       "/XD7uHrnYL3hAaT7hFENy/s+i+8o0kdVzvwPrG5T6jpD5d\neztv3P7otrXq" +
       "ue6Va4rXvnzpDVRxv/Kuf38l+ZjvPJcbo8r/DygVxvirKgAA");
}
