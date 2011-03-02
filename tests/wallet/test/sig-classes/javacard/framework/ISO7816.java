package javacard.framework;

public interface ISO7816 {
    int __JIF_SIG_OF_JAVA_CLASS$20030619 = 0;
    byte OFFSET_CLA = 0;
    byte OFFSET_INS = 1;
    short SW_CLA_NOT_SUPPORTED = 28160;
    short SW_INS_NOT_SUPPORTED = 27904;
    byte OFFSET_LC = 4;
    short SW_WRONG_LENGTH = 26368;
    byte OFFSET_CDATA = 5;
    String jlc$CompilerVersion$jif = "3.1.1";
    long jlc$SourceLastModified$jif = 1201637759000L;
    String jlc$ClassType$jif =
      ("H4sIAAAAAAAAAJVYC2wcRxken+3z69zzK7Vp/IxjaivKnV03qcFS1cPxuedc" +
       "bNfnOI2rdLu3N3fZ\neG932Z2zz4aGVpHitAhQRIqoBEkBQ9IQXo1UUIQKIi" +
       "2liEpgiaYtooS2oEi0UCqqRKio/DOze7e3\nftQ5aXdnZ///n2/+99yFd1Gp" +
       "aaCWI3IyQBZ0bAZG5eSEaJg4MaEpC1MwJUgrx09/64WBay96UHEU\nVYgZcl" +
       "gzZLJAUE30iDgnBjNEVoJR2SSDUVQjqyYRVSKLBCfChpYmqCOqg6iUopEgzp" +
       "KgLhpiOsgW\nC04MKaJpApuXzZqfRUdRUdZA7TaHBYojYsQc0qc7P5z/3dLo" +
       "1mLkn0F+WY0RkcjSkKYSWGIG+dI4\nHceGGUokcGIG1aoYJ2LYkEVFXgRCTZ" +
       "1BdaacUkWSMbA5iU1NmaOEdWZGxwZb056MIp+kwZ6MjEQ0\ngyMEvEkZKwn7" +
       "rTSpiCmToFvzO+X7C9N52F6lDMCMpChhm6VkVlYTBLW5OXJ73L4XCIC1LI1B" +
       "37ml\nSlQRJlAd17wiqqlgjBiymgLSUi0DqxB027pCgahcF6VZMYUFgprcdB" +
       "P8E1BVMEVQFoK2uMmYJLDS\nbS4rOewz7vX97/GJ6+0ehjmBJYXiLwOmVhfT" +
       "JE5iA6sS5ow3MoFTkYOZZg9CQLzFRcxpQl0/3R+9\n9os2TrN1DZrx+BEsEU" +
       "H6cHdzy0ro7YpiCqNc10yZGr9g58x5J6wvg1kdouHWnET6MWB//OXkrw8+\n" +
       "ch7/w4PKI8graUomrUZQBVYTQ9a4DMZRWcV8djyZNDGJoBKFTXk19g7qSMoK" +
       "purwwlgXyWE2zuoI\noTK4muCqRvzHngRVRWLjdw307Q5AmBL0SfDcnaYhBa" +
       "kLSKKRCCYhdvC8ZswGHYRZKtY/X1QEO2p2\nx5MCrnivpiSwIUhn3/rt54f3" +
       "PnbCk/MvCxB4kr1EILdEwFoCFRUx0Y2FyqLaT9Bc8M4zgzVf3mk+\nC0ljBl" +
       "XI6XSGiHEFQziJiqLN44RAmHfVOjzZzga+ODgi+LSggCCeE3Q0Z6BtbofLB2" +
       "aEpR0Jrxz9\n6Pf/FOYvUt+gtmyg0jk0sMwsx+briR0afejEtmJKNF9ClZxl" +
       "AdZIV3ErK0xD3Zafjn/ug8unK9u5\nfMrTzASUUy0XZNECNkHynLu61NPkf8" +
       "2DPDPolsOiGVHBq2hCwgaEriLGsUJQYxRkWGHGpoJReh9c\nI3Bc8hef23/6" +
       "xsvkDWbFCkhZRAS3g2zQ6g7fgoijkt0bnhaNvNyBV+ZqvT8+k/agshlUw1Ih" +
       "pPdp\nUcngGIZUWymbQ9ZkFFUXfC9MUzwmB610QFCzG5dj2UE7p1IFlzi9Es" +
       "aUmo4rmIdXM5oaagK4+uC6\nxQoe9qQf6+itPsvctZb5AwUUiEBOTmGj7s2n" +
       "lq8/ujQAioug0jkKHHRSk6cby9BacvzCEy1Vp65+\nkRneltxDPcxtF4r7xj" +
       "Fv75WfV73gcZYHv6Ncgu54sqnNO+iUgalO//z1ia8+8e7SA8w7uXsWExAi\n" +
       "qyKozatn4ooswcBkNS9LULsgjEbCQiwyIoyHhdHQdEgYioZise139Pb29+7u" +
       "+xQN07xvMo/CCZ5L\n/1DVf6J9V7IB3BLchrkcFFsWdJ2QMimH/c717dOzIG" +
       "5rXhzjgQotG0ywIF158+GXntn3q2UmshI8\nIgmthCwtUJO7vXso9xVMXkWr" +
       "ZMombllFHMl/pm7b6MZgrb/nWNds+X/TZz3Uc6oS2JQMWacJ3HKg\nSlNO64" +
       "oMFTxhbdNLtFGwAi3HbE1DVE0FGhgefFPs43BWN2hhnBOhjYBExtwuyF1Qz+" +
       "r0eTcoppOB\nsgXk1ZIXIki7Hrn2n4t/fPZ2Xr/aCjlWUXf8sOW97Rce7GKO" +
       "Rz2m1b3vSSxCIueKEaSv3C933tf9\niRGm/lJtniWBNocydWgXJFkXoezZI9" +
       "qBGUwK3ccQoGpaZWBLfHfU+7MffPTe3cyzHaqkRdVtMNtW\nlpLoIwwb6MrL" +
       "zmEJfEYjREvnEAnSHb/pbQyd2/cTp5ZcPA7qvi1NTe3v40oGK2fPTpc9cwwO" +
       "m9ro\nAFhPoSlckJxG6fN9+7m/nX/6KQ7ObfS1OL7z/evLi7d/L5UvHCM6Lz" +
       "p717LpAeiw8zbVTv6r/9Sx\n8BmHTZmhYKvzjJCbjd73cD3f51pi1YLujsCQ" +
       "01CP5qw27GTr8t8vvjXZ4HH0qp2r2kUnD+9XHSmi\nY6MVGPXzOzouHJ18I8" +
       "6VWFfYSAyrmfSuM6/innt80hqdSTHkCtbgFPHIo/dtuVKwBa4euPxWKfCv\n" +
       "LgV0sMOePsReA+zeZ6VcOu6ntzvpbRfk2crxcDg2PEWTK6O8i6uaLz5tKfcg" +
       "QSXxBYI/HlyNBa5m\nfXCMP3VT4CJjsQJw9JbYGMwAXLUWmNp1wahsWt0UmI" +
       "bYAaolYWx8Sojtn5gYn5wa3rOhzkpNOFR+\njEUpzjoLZ926ONNsOrtpnKCw" +
       "DXDSW2ZjWN1w1Vuw6te3ZQkdfGFTsCosW0aHbtaU/XA1WFga1sWS\nYtPHN4" +
       "XFDyo6MDk+NiJEh8dGpu69We3ssJ7Ifq6tnVI6+NKmEPnsMNwTmgptqKC1nI" +
       "CgcjEOpwZR\nIqDo3LmcreA4BNSvcaoMNDWc+lrPN664e39E013LeodhluqW" +
       "7v+377j4/CHKShlnYG2i6TsVPIeV\n/KpuIfvY2d/uiv0H2v4a3n324dXrE1" +
       "RmHctog9C06t8TfuKXtq081H1Zr32JdUa5U3k59HnJjKI4\ne2zH2KsbOCkz" +
       "1OXOSv5daO9XHw9hX7kxg7jMqc+B3m1q+v4030ADcXba/IjgsELLhlX/yQdO" +
       "6mpv\nSLbPevX5VjpkGOICPeplH11pefJF8ZvFtLsvMeG0xbv3vOEaC05t+z" +
       "L8PylBekd/cORPk385b5X4\nnE5xlgTYv1W2jXIcly53Pfb666918iotKeLi" +
       "It1JWRSVJTUjLSq5/7c61pVmy5KvvpJ8/Njbftth\nqumtlp/p8/ppXV8OHU" +
       "9XD76699Klc2s4bE6EY/uMx3a2O7s/KLvx8vv35Hl/5NDb/wEE+BcwOBQA\n" + "AA==");
    String jlc$CompilerVersion$jl = "2.3.0";
    long jlc$SourceLastModified$jl = 1201637759000L;
    String jlc$ClassType$jl =
      ("H4sIAAAAAAAAAJVZW8zjxnXWrnfXtrzOXmzHhuPLxnbauIKXN4mUYgSISJEi" +
       "JUqURFGiWKR/eBfF\nq0iKpNjUaFEgThugbVo76EOR9CFAgMIPRQO0L0VbIG" +
       "mLFilQ+KHpS9NLgqJAmwJ9KOKHtOlQ//+v\n//29a2wFDTmcOXPOmXO+c2Y4" +
       "fOeHtctJXLsVhd7e9sL0drqPzOT2RI0T06A8NUnmoOFI/9TLP86/\n8+bgYw" +
       "/Vrim1a04gpmrq6FQYpGaRKrWrvulrZpx0DcM0lNqNwDQN0Ywd1XNKQBgGSu" +
       "1m4tiBmu5i\nM5mZSehlFeHNZBeZ8UHmaSNfu6qHQZLGOz0N4yStXec3aqZC" +
       "u9TxIN5J0tf52hXLMT0j2dbeqF3g\na5ctT7UB4Uf501lAB44QU7UD8roD1I" +
       "wtVTdPh1xyncBIay+eH3Fnxq8MAQEY+rBvpuvwjqhLgQoa\najePVfLUwIbE" +
       "NHYCG5BeDndASlp79r5MAdEjkaq7qm0epbVnztNNjrsA1aMHs1RD0tpT58kO" +
       "nIq4\n9uw5n53xlnDl6v/86uRHty4edDZM3av0vwwGvXBu0My0zNgMdPN44H" +
       "u7229xq91zF2s1QPzUOeJj\nmu4n/kji/+1PXzym+dg9aARtY+rpkf5j/Lnn" +
       "3+3+4NGHKjUeicLEqaBw18wPXp2c9LxeRACLH73D\nseq8fdr5Z7O/WP3i75" +
       "n/frH2CFe7oofezg+42qNmYFAn9YdBnXcC87hVsKzETLnaJe/QdCU8PANz\n" +
       "WI5nVua4BOqRmq4P9SKq1WoPg/IMKI/Xjn+He1p7jBMFoo3gtzeOldZ+CuD4" +
       "tSTWoQoCuhobkBUD\nUORh7EJnCIuK7eP5hQtgRs+djy4PQJENPcOMj/RvfP" +
       "+vP08Pf+WLF+/g60QhgKRTEbfviLh9IqJ2\n4cKB9dN3G6uyvlEFyX/8wevX" +
       "f+215A8v1h5Sao86vr9LVc0zQXCpnhfmpnGUHtB14wySDwAC6Luq\nASACTB" +
       "95gNEB+MA6WVx76Tzg3g9TDtRUgKJ33/jJ3/7nUf7NChuVL5+suB+rBjzjHu" +
       "t29VXxs4PP\nffGlhyqi/FJl5OIQYE9VUs4bi6lC/ZS/r/38f3/rq/Vbx/yr" +
       "Mc8eGDyWfBDadw080ss/kb763t+k\n3zvY+VGQYlIVAAPE6wvnA+yumKgi7b" +
       "xKCzV+n2/777IbV37/a/7F2sNK7fohdalBulC9nSmaIDXW\nnYQ6aeRrj9/V" +
       "f3ciOY6a108CNq09d16vM2JfP816lQkunsUNqFfUVf3hAwYfO9B8BBjpEVAQ" +
       "UD5y\nAu/Dveq8Xl1uFAdA3Th4rFLoNgeypm3GN//ld7/+o196sw0Mx9UuZ5" +
       "XiwCbX36cb76rc/4V33n7+\nsbf+6UsH15xy/kSFgfN+qfR+75evwN/948f+" +
       "/OLZBH7tTKYHtjtOBzfeh9A8Niub/sNvT37r7R++\n+bMH/BwD6KEUMHECFZ" +
       "jtSrTTPEcHleSwRhVp7dbR0YBjjkSufyQwR4PuontE8V1RfAWFYQzGkc49\n" +
       "0DOJHR/knewkMX75ha//6ze/P3vy4pnV4+UPJPCzY45XkIML6lEBJHz8wyQc" +
       "qL/d+Pg7b8y+px1n\n1pt3hzYd7PzW1/7efPUzV/V75IqHwBp3SDkHd9+64/" +
       "SnQHkVlGsnTr/2QadXlZ8+bf7U4fFnDtfX\nToxb1aHqAlcXBFi0LjCMSM8r" +
       "Mx4oseg4gvG0dknbp+aHaXL9RJPr99fkQlWh/l+acGOxavrM/SS3\nQblxIv" +
       "nGfSUHh2bugSQ/KS6r+R+NhfmRKE0mwmxO985b43KyDuP7OqZS6uaJUjfvq5" +
       "R/aJ49sFLA\nFHcrVXUK99Phk6A8caLDE/d3yaWqIj+QDo+euISnPtQjGChP" +
       "ngh+8r6C7UPzzz2Q4Gtg8suZMO4f\n8fS4P2c/dN6Nk3vt9H7veV+uKsYDib" +
       "96GhS97rx776n/5Pj3v1WpZFYPx5uMm1ToR2BnEt/qm2BZ\nUlPTiIAeAEHY" +
       "beQ2Uo3ePJAWT288/ZVTZguwJwd7p1fAhuQw7GZ6NnEfb13PKFld3ON14CPv" +
       "k/Eh\n2N9+6Qe/8Z1ff/kfQeYZnC4CFTVAz4Wn/xJ55pnqIXkgBZ+tFBTDXa" +
       "ybvJqko9BwwHbeuKPj2Vzi\nhffUL716lW0mXPf0N1isKDTXZ4VrBg2sxDtl" +
       "OSQWEcQVG9XJhsECxrY6IyL6pNiZhq6NrUDg0Njg\nd9gYs5S62e9gqIKWZV" +
       "PtLyLWSbvr6YzbhiTtktMON5VDRJK46ZBzUM+hI9tRV2kYimhIO7BEbbez\n" +
       "oRNCPaE0CROrW+m0pcpGoEAQFKBQo9PZMwbSj1SVWnIYt5+SCqx1lX7LFGl1" +
       "YDdHKh1RyCgfSHZc\nwB0Iby5Jdd/BJZaW6ouVreieXSr8oCvRGOz07IDcjO" +
       "lFOJihotiTFtPNdr+aLcQhT1IWtY5W/pYS\ntjbuSGFE22U0tPl15AiJvaxT" +
       "cHM0VAdRN9rJpbK1ZpIB/kqXmKssuQo3q2Sqw1udmLRKCDP4EPF5\nN4CZxn" +
       "IFz1x9I7rDlbjsc/2GwTXrrG72+1Lo6qABUcPVAOVXLr1gZHtGzRWENFRVl9" +
       "ylONK6LUdC\nxr4q0MZS6Re5osEtfMRFDTdX8NGGnsJ1ez+iM9pVhnlAxHMx" +
       "cih/HjJZKTRkguqMWyiDe70RNYqo\n9drHF+7CW81JftkS5AGB8U2Z4iiE5x" +
       "ASI3yvLnrT2cDrGgiRO9utZ29IZ+/bvCg5cG9bkDBKRJSt\nrnUGXyijlcTY" +
       "Uawb5KRNI+RiRC+mTDhEdyN/NeB7Yl0Tm7Fg5iWRyR0C6g/wBmyuNz45WMkO" +
       "nwv+\nYm3NB3TL6eKJ2ApzbBQyc3clKIrdoaZ8ycz0Ipp6MITNGp16J0zQeT" +
       "xGekpcNEg6oSUW11mj3MNx\nSKzArLdjHYH3Sj/u2K120RrgeWYjrhHu2GVA" +
       "z/HRkoQgQXbjvN2o62sCypuTIde2Y1UK/VhSpuRi\nNZ921GZvpMyW+qJtbP" +
       "bKuNcfhOSQmcsu0rGJrTgKh9EuznPPlvdLGi13IVcP58Omv6O2s6xkA3u2\n" +
       "mC35jZFjMDFtdX2Sh0Wrpwfqak2qLi8OhzOKWq2NjGorSd5nVzNlmK4gGZnS" +
       "Ahqh9aI1ouLAySJH\nzDlpHWBQnLaaFjYJNMqFRhlVkE03RmiON+icCSlmJh" +
       "XwCmX2W16dd0i9PWqWdrmwMN9tANB6G9KT\nOVwW+w5LM9PSGThbftxszro+" +
       "ay2ETamUttoQerbT4EXbpiiTxqawgpOpFlo8iYL4msGDuCg7dXxi\nQYywyq" +
       "S0Yaf+3oY3LpyM8qRoaLEzg1XL3NCpPxiOmQ7lKQUAkcItMDoewkUPU1RrE0" +
       "A7u70Lhp1d\nq26N4nnZFSVpD8vKmKbGLrsP+B2OjCeu1UZ7k94k9BLB6S9Y" +
       "couuqdxW6djh+4k0mK6Kmbvss/0I\nxLG57uh1DgPzXpsWikF4kWRub6kxlL" +
       "tWZl0Rs9FyituEZYfOxk1hLVir42y8JxcNZZvN2w0XAtvn\ntSflCLeOJkZU" +
       "l/TA7nOKtNz72rSjuAwjyRbVGUll7pM4Ak8Zquy3jA0q0USEcIxXOh06USU/" +
       "bMY+\nZ8ijVqiq9rDZ2FF4vZiGZmc7tfCGk06STE8VrdXNJcmhSWHu9zdEgg" +
       "betg3tNtOuE+4qmw5Zdia6\nuK7tO6SqZyVF75FFhLcadXcWmF5a9CZgX840" +
       "Y6LYMBnRWloLArN1ZcVm+Fi3d61msg3GQsORbNUT\nIKhEHLXdaJuE2G3nxE" +
       "5OlnTRderb/TAs1TRohmGS66Q7Fju8xCf2htaoqZPvuXDPrg12atD2ygkV\n" +
       "JzERrDPR0E6D2DWK6RgmimwUQhSltPg6hPQWFj7oLAiOxEheX8euzjDJ0hig" +
       "05Hjs5BOIIQhlFy/\nFcvrcmNPw7QP73OOQVrtRl8O0gDbopKfrLl2nbP9FD" +
       "g8lQYCSBxJ2CkWI9ldxsoo9DXPY9iRB0U+\noeEjxY41KFiOjaQhQ3OlxCQd" +
       "ByvLYDpaqvoAY4c2yGdjAW+GI2ZerDVjzKdMP+86GKvRQGUaZvpr\n27SYSd" +
       "LEyOUYXa8DUlhvViRdpPluzyDTshHD5lwPo0gg64kN0oDCyZzrEGwcoEEzha" +
       "yJgsoxpFIb\nbwmA0k6EZYeY6SbRHwqzEvURDctEsaC4nTmkVTHDqA6GpVan" +
       "7sz6PuQN0sBZkuNQ3xtLWx4wrhya\npcJ1tmRfyoeYrk9DsPopS5K3WEtqEC" +
       "PLmsxNTN+XrLp1Z8YiHY3TZd2L+JDLc2CTmC99emDRq82M\nzgK6G4isBkXa" +
       "dG+gkD6PWoo+6RLbzEHGgE3WhMP+EAUsI72j5yAld+Kw3mXWk1D0dHdQ9HFr" +
       "DDzu\nQ4EM4cMpIWiWiAg8UUJmxjuEAfEtKWOpEc6XEXAxH3VNJ2+X2mKe4T" +
       "HUEYt6m/TZueKvV8Os3Awo\nbWMQmN7DrBYSOciK320Sagoj1KS1zVGiP4/b" +
       "aTvLNrLSZFl4G6Ex6kpBr9GRWdF36it8tOhP5FIo\nQVYfIG2ujS+WjLUSu/" +
       "vYt8TdCIecIEjb8HpriHPWpVLNZiYsF0JGulsMc1JtmrtJsRxaVFbn7QXI\n" +
       "X4I5SWZmt1GaCr7QhR0epGUWaTPLRfW40Hs2OqGX8yEutB18p/JlKx1auj0V" +
       "5YJccRDr7cek2vLr\nlkyNJthi3qQGm2Q6yDsDbdUf5V2Zl5b+OEJydZ0sU1" +
       "jyOW3VVUNvtSzaYrhJg5a9Clq5isXCunSj\nscDMGvUNLUXlJuSVDooL43Ei" +
       "xGkUDKm8J/fEJapslmMkRxa7HE9NTJm6htVn5gkVJktYZbVUlSC5\niywZW0" +
       "WSpVhfpRN23JSZ3j7ERz23MXYG9Lpt9KMd3lmaWJIMCM7a4mmw4PDGeOjTky" +
       "AZjfO5b0fp\ndkghAUfZ+oYWolyZ1bltL8R4X4/NbpMci5muhqNxb6k3vHgy" +
       "xvGmiXVoGMF4MzTW3m67JLIGRhCb\nJkSxQ3HtBas+6uDzZDHhzEUdAkspZ2" +
       "Ur1IB2TgazaNZUsQBDm4Yc08jMZ/U0BDsBGE/bc2s3lNMt\n3EZYRIkxdGBI" +
       "akvC3FaHDQabIdhSmWjJosNEEyi3RARZELOWoQoAN3lnLRNuYROb0VZvkJiP" +
       "bBsA\n9xjc7emypkLcxk5onl97MrSfGgLuR1ld5LWJmcmtxSZy+wJHTczIYx" +
       "EkMq0B01YyJuWTbGT24ULL\nsEygVQpupY2Sb0/2Hmm6BqQLs4mLtVtaS27W" +
       "A7pVePZ8lSFLdCNzurdvYRNLw3G8g7SdMB2avMOl\nyGA5ExeDubThxW0LIn" +
       "QciRb+vjXMsmjQhsYbzUrTdX2781MvgqI2FEXyoi2wUkfuEYBVZ+jpaNLU\n" +
       "aJxeQbtRgamoQCTQkM8KEFBYi/Wg1rinQTlNN0g9F8hpt97tfvrT1WvB5x/o" +
       "3ePG4eXoziE/eOWo\neraHV4ziHhzS2iOqlqSxqqfgvfbOZ4QDzzNnlk/c4x" +
       "D89jNPvvWVV3/nu+ePKmvVWdDz9zu7P5wD\nvSn/19UvqN/+bDW0GkgA2WkY" +
       "veaZmem9L/U8k9HhU8XpEeG15Yv/zODf+IUPyk9rD5+cIlfna898\n4NPP8Q" +
       "cK/aV3P/fJb0U3/uri4VDx9CPCFb72iLXzvLMHjmfqV6LYtJyD1leOjx+jw+" +
       "03wRvuB0+z\nwbzu1A8qfvmY+m1g91Pq6vkr0T3eXo/PS4v/A5yAv1/CGgAA");
}
