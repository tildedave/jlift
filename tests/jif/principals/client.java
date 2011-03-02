package jif.principals;

public class client extends jif.lang.ExternalPrincipal {
    
    public client jif$principals$client$() {
        this.jif$init();
        { this.jif$lang$ExternalPrincipal$("client"); }
        return this;
    }
    
    private static client P;
    
    public static jif.lang.Principal getInstance() {
        if (client.P == null) {
            client.P = new client().jif$principals$client$();
        }
        return client.P;
    }
    
    final public static String jlc$CompilerVersion$jif = "3.1.1";
    final public static long jlc$SourceLastModified$jif = 1202236209000L;
    final public static String jlc$ClassType$jif =
      ("H4sIAAAAAAAAAK0Ya2wcxXl8ts8+28Gx42DH7/hBbIXchUIoxbSK49jEzoUc" +
       "tjHBKBzrvTl7473d\n7e7c+ew2CBSVhPwooqSlSG3SFoukgVYtKZSmFaWklN" +
       "CUPiw1DyqepVWkAk1RESmiot/M7PvO9p+e\ntHOzM9/3zff+vtkn30PFho6a" +
       "9kjJMJnVsBEekpIxQTdwIqbKs6OwFBcX7j/83RdvuPhSABVGUUhI\nkylVl8" +
       "gsQSuje4SMEEkTSY5EJYP0RNFKSTGIoBBJIDgxoKspgtZGNSA1KaskgrMkog" +
       "m6kIqwwyKx\nPlkwDEALslXji+geVJDVUauFYTLFOWLAnKUb2z+Z+d3+oYZC" +
       "VDmOKiVlhAhEEvtUhcAR46gihVMT\nWDd6EwmcGEdVCsaJEaxLgizNAaCqjK" +
       "NqQ5pUBJLWsTGMDVXOUMBqI61hnZ1pLUZRhaiCTHpaJKrO\nOQR+kxKWE9Zb" +
       "cVIWJg2CrnQk5fIN0HUQr0wCxvSkIGILpWhaUhIEtfgxbBk7tgMAoJakMOjb" +
       "PqpI\nEWABVXPNy4IyGRkhuqRMAmixmoZTCKpflCgAlWqCOC1M4jhBdX64GN" +
       "8CqBBTBEUhaLUfjFECK9X7\nrOSyz85gxX8Pxj5qDTCeE1iUKf8lgNTsQxrG" +
       "SaxjRcQc8XI6fGjwjnRjACEAXu0D5jC9nT+9LXrx\nly0cpiEPzM6JPVgkcf" +
       "GT6xubFnrfCRVSNko11ZCo8T2SM+eNmTs9WQ2i4UqbIt0MW5vPD//mjnuP\n" +
       "438EUOkgCoqqnE4pgyiElUSfOS+BeVRSMF/dmUwamAyiIpktBVX2DupISjKm" +
       "6gjCXBPIFJtnNYRQ\nCTyr4ClE/Mf+CSoTZQkrJAxRStAaGCMamFyUNEE2Is" +
       "5elhKqnCkoABka/REkg/NtU+UE1uPi0b/+\n9sv92x84ELA9ymQBjE0zgUM9" +
       "zKmjggJGtdarGarqBA38d5/qWfnVDcYzkCHGUUhKpdJEmJAxxI4g\ny+oMTs" +
       "QJc6Uql9taoV8xAV4HDhyXgRBPABrK6KjN711OFA6yHCPihXs+/eP78ZkT1B" +
       "Go4Woodc4a\nmGGa81bRPbJ76O4DbYUUaKYIFEolafNkvDy04+Lsr1bfePL5" +
       "//w8gIrHIa8ZW3FSSMsk1rdFTSuQ\nLGrspWEMeUSJChNYjqJyni4ECHkraE" +
       "s0keEQVBul1uNhJFP4CMMCNZTrDhGK1gKu3bG8CuLi+wd3\nnDh75rUuJ9QI" +
       "6sjJALmYNIL9Oo7pqogTkBMd8o98vO3Sw8WfezqAiiAtgGwEJKNZptl/hieS" +
       "e6ys\nSGUBzPKkqqcEmW5ZWikjU7o646ww913B5ivBSuVmLBSZscD+6WY1HV" +
       "ZxZ6dG98nAcu7lfcGN535R\n/mLAnZ4rXeVqBBMe7FWOz4zqGMP6a9+MPfz1" +
       "9/bfyRzG9BgCFSo9IUtilvFXWwAOuipP4gnX1Rz6\nRve3zlkeucqh3qvrwi" +
       "x1yOx9C02PviR8G5ISJAdDmsMs9hE7CVkH0DHM5hHXJn3vgFB0mHD8uNcw\n" +
       "wC6QqG6qPfD2H5p+P8qZ8JMArhocJOaEUF8lnXleXDz39t6Xn9rxwnwABcBE" +
       "YO8kNAKSCNW+Mcd3\n++xd6sC0xk1awE05wIPONnW9Wj8P5vlb93VOl36cOs" +
       "r8rTyBDVGXNCqVmTHLDCmlyRLU3wQLE6jF\nRB0CLdNiys7UBcWQof3goTXK" +
       "Nvuzmk7LWkaAJgAyE9PKOu5xdOjMavTvJlBOO2PMIuKoxiEUFzfd\ne/HfJ/" +
       "78zDpegVq8GDnQa3/YdKnjybs6mT2oVzT7ZR/GAiRmrpy4+OAuqf3WrjU3Mx" +
       "MUqzMs3Fpc\nCrXzcyRmzWgPpTMqVI5e4Koux8gm+a5o8NkffHrpCyw2XOqk" +
       "ZdFvNMteLkXRcas3g9r8hEdVzWYp\nLm5764U3vvJI3Rm3mnwILuhrVtfVtX" +
       "6AyxhftlHbfUa1EVyGdbMHnF3ltYebJ7dZzt9X//r6qx88\nzbnzmz0fxmNP" +
       "fDQ/t+7xSSew+s1D6d9gPsveDp2yY1n1oX9ee2jfwBGXZZm5QN4ZBsiNR8ct" +
       "jrZ3\nAuHOfMrbohKiplwq/MzpjbW9x3b8OGCmigFbJ91e8XyYbiGvqfjec3" +
       "87/v3vWDSGmHAxl6DDbPys\nxmNmjL19nutjs8ZwdnEczQ3vfbuFA+7mWtQs" +
       "DXrezFRImwF/OzNA22+rSKUmvvThqcNlrY5ZGu06\n3+ip8x60uBg49ub+7r" +
       "rKC2COcXTFlGAMKtDp0UsC1sE+srsW+/tWH6m55247fPkV8jpzXqdMUuz2\n" +
       "bC7/Y4Krgt9wNlMV/NGRVACVQKPB2ge4QY0JcpqWo3G4PRh95mIUrfDse28C" +
       "vO3tsduARn+Jdh3r\nL9BOGwhzCk3nIV9NpvW4DZ5SsyaX+mpyAWIuMc0Qrm" +
       "JjN6+gAQJdkC5BRAPPQYNd17LQV8YMTylg\nuRMneJ//p/JrD7RuStawaAkx" +
       "a8BFkJhpv5RiWO+c0QoPoy1LMMo92AZfsww4kyrtcXQ6ZNipWSdA\nMh6Hz1" +
       "m5xT6wHp7mJQ6k414eDR6chuVkGvQhNS1zEEdivRQb2F6and9lhp/TiTQ6LU" +
       "TTYnc/dm/d\nv+tfFfcLv97NU2u199bQr6RTm46cx92bK8Q8N5AQUbUNMs5g" +
       "2Ql//2k72J3YcuXK21veGrj+6N58\n8e/9suHFi4tnLlRe6r/uzN//fy2+6Z" +
       "z5uvmWJYWIi1WZhlsLp6TT7DOLGcE53we8SD3euC3jp456\norfN9oVqeNbB" +
       "EzJ9IZS3o84NXTpfTwdt6eZ02c71awSVT8KV2OSeweg2eyUuH0fWP0Er7CrF" +
       "7791\n4QjtCFjC82xlnWpUA3mRGp4COaUcFbg43JSffScnUE1tWEJbLCc8lp" +
       "MT5hkbjzsZYD4nJ8znzwld\n8Fy9jHme8IU3xeleAidvTli/zEFOThiDFB3k" +
       "nwBo31qX81mOf0oS2xbu7jqlVb3Mmnb7c08pJOlk\nWpbdlcU1D2o6TkpMrF" +
       "JeZ3hP8DRBV3i/QhBU5rww7n7CQZ8lqBBA6fRnmmX5etvy/VloqRRBtj3A\n" +
       "1VE0LdmNPnrnQ5qysVda9ApV680raf4lMy6+q91181+G3zhuNpW2wnCWhNk3" +
       "Tiv4bYyTpzofePXV\nC+28JxRlYW6OnlcCOYXfme2vomsXpWbRkt48mzy475" +
       "1Kq3djPXGVK7CZ6M2L06HzsRU957efPHnM\nn06RS3su8RmOFdLXdX1YcvmV" +
       "Dzbn19v/AOCnqqBuFgAA");
    
    public client() { super(); }
    
    public void jif$invokeDefConstructor() { this.jif$principals$client$(); }
    
    private void jif$init() {  }
    
    final public static String jlc$CompilerVersion$jl = "2.3.0";
    final public static long jlc$SourceLastModified$jl = 1202236209000L;
    final public static String jlc$ClassType$jl =
      ("H4sIAAAAAAAAAK1ZWczk2FV2/73MdE0nM93ZhsksnZlOmFGRrnK5ymWnQcHl" +
       "cu12ueyya4mijst7\ned9dDhmBkMgmAigJiwTJC1IklAdEBHlBgJSwB4TmIe" +
       "GBBFAihIBE8ICIUCBcV/1b/91JHsIv2f+1\n77nnnnvOd8/x/eqz34SuRiF0" +
       "2/fsnW578d1456vRXVYKI1UhbSmK5uDFffldL30n+9IHR2+9DD25\nhp40XT" +
       "6WYlMmPTdW83gN3XBUZ6OGEaEoqrKGbrqqqvBqaEq2WQBBz11DtyJTd6U4Cd" +
       "WIUyPPTkvB\nW1Hiq+F+zpOXE+iG7LlRHCZy7IVRDD012UqpVEti065NzCi+" +
       "N4GuaaZqK1EAvQpdmkBXNVvSgeCb\nJyerqO011nrleyBeMYGZoSbJ6smQK5" +
       "bpKjH0wsURpyu+MwYCYOhjjhob3ulUV1wJvIBuHUyyJVev\n8XFoujoQveol" +
       "YJYYeuZ7KgVCj/uSbEm6ej+Gnr4oxx66gNT1vVvKITH0potie015CD1zIWbn" +
       "ojW9\nduN/PsL+1+2jvc2KKtul/VfBoOcvDOJUTQ1VV1YPA7+d3P3EcJU8ew" +
       "RBQPhNF4QPMsTbPy9M/vkP\nXzjIvPURMtPNVpXj+/J30Gefe434xvXLpRmP" +
       "+15kllB4YOX7qLLHPfdyH2Dxzacay867J51/xP3J\n6qd/S/3XI+jxIXRN9u" +
       "zEcYfQddVVyOP2Y6A9MV318HaqaZEaD6Er9v7VNW//DNyhmbZauuMKaPtS\n" +
       "bOzbuQ9B0GPgegO4LkOHv/3/GKrItqm68d2tqcXQj4B7zQchl01fsqPaWV9e" +
       "KnpddukSWMOzF/eT\nDcA38GxFDe/Ln/n6X/wUNf7wh45OEXVsAgg20HP3TP" +
       "vdg3bo0qW91rc86JnS1Uq5I/7td+499bF3\nRr93BF1eQ9dNx0liaWOrYCdJ" +
       "tu1lqnI/3kPp5jnY7tECoHZjA1AHAHzfBor2KAeuSEPoxYvoOtuT\nQ9CSAG" +
       "Ree/W7f/Ot+9nnSiCUgXtjqf1gGgiDdbDtxiv8e0fv+9CLl0uh7ApwaLmSOz" +
       "9Y+335Wx+h\nP/flv/zqy2cojqE7D22uh0eWm+Oi+WzoyaoCks+Z+l/578G/" +
       "f/wq/rtHJRKug5wTSwApYAM/f3GO\nBzbJvZOEUzrraAI9oXmhI9ll10mWqM" +
       "RG6GVnb/bIeGLffv13D3//W14lvMqHA8xukZ7jA2yGt/sq\nsEOKVcU/YKr0" +
       "7YX17FPbt3/2Wv0rv//EHx+dz4JPnkuXvBof9tTNs9DMQ1UF77/6q+zHP/nN" +
       "D75n\nH5fjwMTQNT/Z2Kac72190yWAgzc8Yn/fffqNn/jlV379KyeBf8OZdi" +
       "IMpV0Z9/xnXnvu1/5U+g2w\n98EejMxC3W8xaD8TdDJBea/u2z92rrN8vn0s" +
       "UmLx4m7qldn/JJDO5v3/+YVPVW4fjCnHPLNXcyV6\nONs9MPC+XPyB8Klv/1" +
       "X8tb3/zhBQ6nghf3haUToHTuzL6c1rv/1p5wh6bA09ta9YkhuLkp2U3l2D\n" +
       "mhORxy8n0Ose6H+wfhyS5b1ThD97EX3npr2IvbPkAdqldNl+7DzcjjPai+B6" +
       "/Dir7f+XnU+Vt5v5\nJcgvG9h+wEv7+zsOgDiKocdAMkoBFAE0on3Jz0E2Yv" +
       "eoPECzvDfL29vySwBAV5G78F24fH73w/ou\ng37NdKV9NXq5vP0EUPeWrS3f" +
       "OcG+CD4iQLK/A/LgfuQtUP/36Cp9dfdQax8xMYj168/EJh4oyB/9\nxi9+6R" +
       "de+nsQ2xF0NS39DkJ6TheTlF8sP/fZTz73xCf+4aN79IAt8JZ/+bvP7ytCv7" +
       "wRoJqX1vFe\nEsrqRIpi2lNM8PGh7A18GGBsaDqgWqXH5fSXnv/Nf/rc17k3" +
       "Hp375njpobJ/fszhu2MfwYqfgxne\n9v1m2Et/sfq2z77KfW1zqMe3HqwRlJ" +
       "s4rU//rfrKT96QH1FvrtjeI/0Z3/jrQTMaEid/4/pKamRy\nvrSxOMKqDayf" +
       "NxvdQbtmdDbDpWwIQy/f8OMmvqUKT5Dj7aAz7hRhOrfbzTaiVNpqW66qWYPs" +
       "8KLB\nZp4X8DbfR/iel1i6uIrHa97pSQvLZkiBNTO4uwtUX1xYhAVzFO+YwF" +
       "l0m02nuF9VK/FgWtB4DSk0\nrZrGlovU2Z7hoRJubUf0yBWjDqVa/jCfzZVs" +
       "GGzGETrNeh152d7WMIRT0oQ1eKIj9gXBGFasnS3a\nmB7N+GSrNnmaoqw5v2" +
       "pRoxVb55jemJ/3h16nMxp29dwbR74er/sMT8BjKbL0kLY83UGtYZBlu3Fe\n" +
       "YW0r61LEJLKXW7Oqq+KoKwpS0lvNJ73pYkK4Y2HTKKrrqoJUvdWaFLvOdGyK" +
       "tkEYFJXGgiETXBPV\nTUOk55VFV0GpzJH7lCKSkTSipHyimwVDSGSwHTPBrO" +
       "ULndFkEc37dFdcTgW6EwxR2HDXsD8R1pwh\nmeR84EujcTitTGdc1crHCwpf" +
       "b2RxuBjpDTJ0Z1ZLGzL1gTAL6xmZp7w17A8n/sKQgGuoWNPzTcdq\nxzpNeC" +
       "PJoqfbDRv4wwq1AO5bssyuOdz1OctIAo4mHDN3vPWcHPuaTeqEvQjIKQnTza" +
       "3RUZZos89g\nZNDpzDuUYEgTQdv2KHKML9uVcMrvCqyl50uMzbetoDsnVQo2" +
       "hbGG2Tozj2Z52OelfMvJMxlbDlGt\nv822LrclSIpYJvVua0vi7ryKT5ZhXr" +
       "F3ddk3Kbag0REr6pas9edonmx6bqiRnMgzlgfDa9LD5g2e\nrqqMM4/wxaxh" +
       "NoVgM4zr9KKVst1azdm6CVtpIhglTC2d9ILApZq+vjMpW+C1KV/tqCLH072a" +
       "4giZ\n5BA7c+hbMfBJo5v4Q347dlB9Sy477LA3il1mxc0q5HwXNkmbqtFale" +
       "S9+XpmhSMi5/iYkPokLTm9\nncBQVGMYtVTbGAcuaXlEx5JqHGXpNSYWqgIF" +
       "J6xZ39SqFTxK8UaONakJge+cWWdL1QtdoPFi7Gx1\nfdSPFSnsS2HQIRBMad" +
       "WCheaKXXlVWOSgRP3YV0aUA2bPh029sqt6NHDzSEaFwMZjyROxnBCbg66x\n" +
       "W+pchyU64Rgdb5k2DE97BLobkKxpLKNqTWXb2Nqaw52+11x4niI3pYrT70sY" +
       "vOHYzaiNTpvdzUoQ\npPFq2KM8W9IYK+3u6s3qJArhjaayk1atlmH6MBwMJW" +
       "y9QHo8T0cWHCq9OZ/MKpN+GPkLilYFfkQa\nzGobjHau2aTIgRVKVFS3VGom" +
       "dhIbThhj1GObJE2TzHoedo3+GNbHCbLpY2QyMauZglTcrtFuhUIy\nwmdDNN" +
       "qOiImzYoYTfOq1/cDT51aHVjAPx9IpwmfYQlOLgDBNItb7GTUaM2s/tmuGCY" +
       "o7uosqfF/o\nFOzYCgKZchat9cK0tkLbQ5toluEw0mmZaYdPRGfS4deR3K2i" +
       "ixU8ENf5GlX6OOYSctK2NbnPtJAW\nV6k5IDOsm+FSFcEOodV2bzEtmkXQp1" +
       "mQ1Ko0qxUwXttoO67o4x2d7PEePhRAdk97y0VGTBPTZ5u6\nu4S5Aq1wqYoU" +
       "tqUmcmAgQRpmZg7vpjlO1htsanJYvzXkR4TSdSI6wg2qTYrWoN4d0C1jsBy5" +
       "SKr3\nOnVn0MUSRs4qGYnOgj4asFq3MWj5VRYL1tiIJkLDk0dTiRJ6mO0ubX" +
       "XZLRA8RpcuHuaZmxEduUOt\nppjFEGOeZBZVWgYBqFZzBY3Iem7BUkbVW9mQ" +
       "nTLuAl9sMbaV2hscEac2gsrTKMTA2bfTFftLhgxE\nZz6YdEnH3nETAclmEw" +
       "rd9ipcp05OFMbcRW2hIy9mnobo3nw5ZtKkcKz2ZGFJxEgndhTOjqdJK1em\n" +
       "W1pK5+nI7kynDZYeJmMvN8ECCk2pqLWBNqq1saLrq7oYYP2uO5OQ2koZu9vd" +
       "JkKdecMTi3l7zdX6\nhMk5ob9rVqdEEmjIItwKCJ3TQlDQEdLz5QoZLgt5xz" +
       "nFQmf9OR2Iw5baHHtJjulOVWp5GGNp2iD0\nyV3WWE4Da630UoaO1Ym15GQp" +
       "QnQ8xAyQ8ovFyq9MnFYbbQpImo6GCOcgjpsNJ4kRirEmxkuU2OWG\nFtuDjS" +
       "ZIMCXu+sqGHrg9xI5XKtiAyrqI8WWtFqJBjmGVYuMWTlHXcocBAZ3V4mo753" +
       "k1q0ZwF1Vw\nnY1Stes3divYhb0lrqqNrZZ1+sv12mnNmOa6E5kMh4HiHq6R" +
       "CpEMphodbjxjbPp2FtTowt9uFniO\nbLBpn3RMvbcZ4bCKLXerjdlPAw185i" +
       "JYu5Yi6aLL950Bkdtot7thwXYCFjnbCYy7SbyNsHWmuJRc\nzJBU2yQ9otp2" +
       "FvEs6i5DtehxQyfr1aagCjYa1ZAfC06azo1AZuxw1Vr3kzpaQaqmj+VoPeno" +
       "TTzZ\nVNthOCYb5FReznt2n6DHcdevCxEJR1Q1CqNFtRYvBqjTajWabY21RG" +
       "zrF8tAoYWkaFQ2ZLu2ao5D\nPcYzxqbFGtPnRIIUZ2O209DV5mxVH8l+sPCQ" +
       "fpPlDLzlDIVQ60ZqAyNBJokjJSE4tzpSDCSsTPDO\ntJ8ZBWkh7lDEGT9dNL" +
       "vrFh/tpGXV8XfEYpjQcn0WOqMtg1Z1YjZSLLLV4QiWna0YBW5PZiK2ouc7\n" +
       "W68kHO2qo1rNr43CbptL+FjKJMLKJazTs0aGn7Y4czGtAnUe6+3gCU17eINY" +
       "a1GWNa1dGMvLGbpm\nTH/BK0SFNtBBATutfI5mA7/tjGao1YSbapdDZSMRrX" +
       "46MxgeLihKMVNutmn0UMlVcNvwa3NzPkMn\nwny+cmVQD2tKJa3GWk+TJyne" +
       "ZoRGAuP4Zhai3NxponKONzZjbIYxLWImbASGjlp6Y2bXag2r3Rgh\nKzlZjh" +
       "twqvbDbRVlWlgFU2t4lAx6ZjN2JzanomEtKZhaTds1qui6aCWpsxitFkMTXq" +
       "wNO5r46Hze\n7k4nfZlqIZmK8V1ETUhk6zdbaMULNQb3xEBlNh2qGOHDNY94" +
       "WJNZzn1tnikxqq7rDbkN8hMvzrGx\n2JiC7aHqDXUnJIO6tmDrVNgmw167jc" +
       "OV5mwbk70tiTVy2134AoE4XN9vklNJkHsbvYmMeE3jZRfU\n7XhBjTYTUsZs" +
       "n563DGuzLaL1qtt0R1jhFpmtVLDeDt8OHBUbCkiu2iphKuDj2awVVG86N8xu" +
       "r4NY\nbtbxEDhYYrAz1eoe38g3SXsb2OIGoR1305brmD+a2lZl4W8UWxbgbs" +
       "ND55ZPh72RukYMFutyHREx\nazOjI7TCLGu7bXK2xmxQdQhU41zHWbbUON3V" +
       "RNsLOK4TbxGiAotUfaKAlOGC78VWsItERcIJX0Sx\neihpTZveodUAh2EMrv" +
       "aRwkgYf9HIXHJhMBYyN9PmZrWb+4UHqv1SrJDg6DDKOXu1QKeuLZPT1VJz\n" +
       "1EbqOdHSX2w4K25iTrFrtlvwOECULbvsi+M2uUbpxqQOIBpxAt5qqxo7cOmK" +
       "W0c5HokSjHOmPWWy\nVJbwSk4dsQsSHYynNWJKukHQSXo6QZQHG/74SHdzf+" +
       "A8ZXrBSa7soPbHoP3J6+3H1MMZO/HMCW0R\nQs99L9p1fxj74PI/bvyc9MX3" +
       "Hh3TGGwMXY89/522mqr2GaNxUQm9Z5lPjvlPLl74xx76mQ88itJ4\n4fuOvC" +
       "/fTN86u2yYf3YEXT6lFB6iuR8cdO9BIqESqnESuvMH6ITnT+mEJ44phSvHdM" +
       "KVC3TCKTfw\n9u/L8fxAAkiKoTeD0Nw5Y0PvHNjQO2fn1funVt0C14+C6/qx" +
       "VdcfZdUjGI5zTMQPZ64RQ0/oanzi\n0xPy4lZJ6O75BvZ0HQ9aXpLO7yjZt2" +
       "PLLx3YQP1hNvBdt4NEiswg8WL15QNRdzv1TOV26SbTTT1L\n7araOUL05Vdu" +
       "vz82zOjuo/348iv3PvCK//8WsCiGnv5eluwHTPyDkmkMXSntvuCIkpe6+bAj" +
       "3v2D\nHHEgqM57wozLld9+z3v526cLfDj2+6X8+A8Z9ldj6PGTScvn9+cxdO" +
       "3g35IZffqh39UOv/7IL772\nvpe/4N/88z35fPoLzbUJ9LiW2PZ5Wu9c+5of" +
       "qpq5n/fageQ7LO4jMfT6B384iKHK2cPe8A8fRH8+\nhi4fJ7yP+ScgfeYUpF" +
       "Qeq6Er2adgzf8PyxudHiEcAAA=");
}
