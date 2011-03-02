package javacard.framework;

import java.lang.Object;

final public class APDU extends Object {
    static int __JIF_SIG_OF_JAVA_CLASS$20030619 = 0;
    
    public APDU javacard$framework$APDU$() {
        this.jif$init();
        {  }
        return this;
    }
    
    native public byte[] getBuffer();
    
    native public short setIncomingAndReceive();
    
    native public short setOutgoing();
    
    native public void setOutgoingLength(final short v0);
    
    native public void sendBytes(final short v1, final short v2);
    
    final public static String jlc$CompilerVersion$jif = "3.1.1";
    final public static long jlc$SourceLastModified$jif = 1201637788000L;
    final public static String jlc$ClassType$jif =
      ("H4sIAAAAAAAAAL1aC3AV1Rk+9wbyIME8SEiABC4kvIrkJaAS2xIeweAFQgKI" +
       "cch1s/fcm4W9u8vu\nuckFxGqZCjpVi2AoMy2oqChYrTBaBzs+UBELVostoh" +
       "3FVxmtUdGpFR2tPY99773JNeOYmXt29+z/\nn/Of///+xzmbBz8GQzUVVKwR" +
       "ItVovQK16kVCpIVTNRhukcX1y3FXiD954+67nr/kgxf8ICMIcrg4\n6pJVAa" +
       "1HoCC4huvmauJIEGuCgoYagqBAkDTESUjgEAw3qXIMgfFBBQ8VFWVUAxOoRu" +
       "FULlZDJ6tp\nmSdymobZMmmvtg5cB3wJFQQMDl0oJhElZiLNrvym58SWRWMy" +
       "QH47yBekNsQhgZ8nSwhP0Q7yYjDW\nCVWtMRyG4XZQKEEYboOqwInCBkwoS+" +
       "2gSBOiEofiKtRaoSaL3YSwSIsrUKVzGp1BkMfLeE1qnEey\nyiTE8kYEKIaN" +
       "p6ERkYtqCIy0VsrW10T68fKGCVgwNcLx0GAZslaQwgiMc3OYa6y6AhNg1qwY" +
       "xPo2\npxoicbgDFDHNi5wUrWlDqiBFMelQOY5nQWB0ykExUbbC8Wu5KAwhUO" +
       "ama2GvMFUOVQRhQaDETUZH\nwlYa7bKSzT5LM/O+vbnly4CfyhyGvEjkz8JM" +
       "Y11MrTACVSjxkDGej1fvaL4qXu4HABOXuIgZTePE\nP60IfvDUOEYzJgnN0s" +
       "41kEch/ptZ5RUnG9/PySBiZCuyJhDjO1ZOwduiv2lIKNgbRpojkpfVxsun\n" +
       "W49edf1++JEfZDeDTF4W4zGpGeRAKTxPv8/C90FBgqx3aSSiQdQMhoi0K1Om" +
       "z1gdEUGERB2Z+F7h\nUBe9TygAgOH4V4p/+YD90SsC2Y0t81dUYx9FoBLDdr" +
       "qm8jXE/jynhmsi2HFgj6yurTGoEmTA/B6f\nD6+l3O1JIgbh5bIYhmqI3/fe" +
       "X65dcMVNW/0msnRRECg1xq82x68m4wOfj45b6tQRUXqYhIC+gw0F\nt07XHs" +
       "Oxoh3kCLFYHHGdIsRexImi3APDIURBVWgDsBEE8jox/jCUQyIeiIUCBXSrYI" +
       "IbZ5Y/NtNo\nw8OT1333yiehnkMEEsSExWR0Jho2yFomW97UttWLrtk6IYMQ" +
       "9QzBqiUrmeCIfUnGDvHrnymZffjp\nr57wg6HtOMJp82GEi4uoZd5cOS7hsF" +
       "FsdrVCHFGkINcJxSDIZYGDw85vuG+WwlMerOAgnld3KJHQ\n11AurIZc1RqE" +
       "sI3DIK8aWAUh/pObFx86dfzNKZbTIVDliQVeTuLLbh23qDIPwzg6WsPv/Pry" +
       "c9uH\nXvqoHwzBAQKvDeGVkXgz1j2Hw6cbjPhI1oI5cyOyGuNE8srQyjDUpc" +
       "o9Vg8F8HB6X4CtlI1/F+Bf\noe4V9EpeFpFmBIM7MbprDTT6nt+cWfvan3Of" +
       "99sDdb4tcbVBxNy+0MLMchVC3P/mb1u23/Hxlqsp\nYHTEIJyr4p2iwCeofK" +
       "U+DNARSUJQdVnxjt6pv3vNQOQIa/RGVeXWE0AmbjhZsesF7vc4POEwoQkb\n" +
       "II0CgM4EjAlIW03va2wvyXMVdkVLCAvHjZqG7YJD1mWlW999ueKvy5kQ7iGw" +
       "VGMsJgpCnGkFlSIv\nxL/27qYXDy4+co8f+LGJsL0juCQQeJz3yz3YnWe+JQ" +
       "Am2S5qEFd4iJut1wR6pW4Z9Pnnb564Nvvr\n2D6Kt9ww1HhVUMiq9Ng5TBNi" +
       "iijgTBymboKzMpIXYS2TtErnVDlJE3EhwlxrOX25IKGoJMF1c7gc\nwJGJam" +
       "UyQxxpJiYUcrkMK6eSCmYMYqnGGijEz7z+g/8c+sdjk1kuGufk8FCPf6jiXN" +
       "WDHROpPQgq\nxrrX3go5HJqZckL8bauEymVTRi2kJhgq91B3G2dTqIJTPy8o" +
       "HE5hxh2pplQ6CllHI5aqzGNkffgp\nwczH//DduZ9R37CpkyRIt9EMe9kURd" +
       "r5zghqylO9XFZMkUL85e8cOfOrnWXH7WpyMdio60rKygKf\nw2FULtOolS6j" +
       "mgw2w9rFw5JNctrDLpPdLKdvGP3WtAtvO8akc5s9GcfeA1/es2HyfVHLsRbo" +
       "k5JL\nczLLXolrZsuy8rZPL9qxuWmPzbLUXHi9PZSQGY+0cy1tL8UDT0ymvL" +
       "kyQnLMpsL6Y7WljfcvfsSv\nh4omUydTnctzcdoXWZd395P/2v/AncYYi+ji" +
       "WmwLbaXtxQrzmZX06adMH3MUyrOK8Sh2eufTEka4\nmmlRMTToeNJDISkG3A" +
       "VNEynEjSQV69z4xbO7hwUss5Sbeb7ckecdbCHef//bW6aW5b+OzdEOLuji\n" +
       "tGYJ13xkuwBVbB/RnovdFaxrqA1Prth9/iX0FgWvlSYJd2XCK/9KzpbBLznV" +
       "XZj5xz0xP8jChQYt\nH/BeaiUnxkk6asf7CG2e3hkEwx3vnXsCVgA3mGVAuT" +
       "tF26Z1J2irEMT3hJrc5yTJyXX4V6Dn5AJX\nTvYBCom1lGESbadaGVSj27UE" +
       "AoFQaFFzU6iteWFoaVNoUePKxtC8YGNbW1V9be1FtbPqLtUc+YEG\nVBhm24" +
       "C/5V60NTAzUkxdKIeaCO8TkZ4LsgmH8cykz9PhZOBVS4LXeLp43dAvoRcl2M" +
       "NiGFLd+j5n\n29h7zh56r7XYb9sMVnr2Y3YetiE0F6KC8f3NQKmfmzb+weta" +
       "3+pkca3IWbIvkOKxmXtOw6lz8vgk\nG4AMnMRpYUXrDy3hNaQfYdwIEkcdY5" +
       "rhola1Um6VGRWpdopUzC2rPsu7kXtutRFnNiOQg2Rlugi7\noWh5vnuQxXRj" +
       "bKA4/8px7zTN2rfJ7fre4w0nX4g//nr+uQUzjp/94ap7HYLJCvlx/S4ixBd2" +
       "j1mW\n0SUco2ctuvN6DgmcTA1Olx3GZl3ucNwJpuOSLeUE/CvSHbcoaTGdzN" +
       "iZEkfQZbd2itp0wML1Dmzh\nKERz4xG8UaAU6xwClvUjoDOUG1Ur3qTqZePe" +
       "689/dfcW/l2K6eH0XMnQFAKjbHVNi/0Vc6zZphQj\n9Z14KikQqKdTB+gEEC" +
       "fsQDAgRwI8QXYgxeaZsO7VPGUiFcSeeG+4N/rlt8/s/8IsE0dZC3ZIHeKP\n" +
       "nj5TEPtw9y4WBG2bMnsFR/a1tiMgPdyMTaULW6TpMhVCTDLOfTRhUwhgAf+A" +
       "FzoZ5H4rhQ1pZKw7\nX5AS/EKPl79EthSb7zSwV7JbRt37oZh5IJceMRSQjE" +
       "hreZwSyRC0q1lyduUKmlnhsBBDmoPAB/Bk\no63JaHKl2zMWpX++sfeyF7ad" +
       "3U7Lgiw9+dI0vESW6EOSYzAb/8GMqs/8T4ysoluYIZ2cxlzSfX7o\nPR50nP" +
       "pRiXMdDlLSn4NQfyLN4wkrzdl0PaRzPYKKopCzHJurzkzuxxeTppdlzKccGZ" +
       "O9IO0zVgnc\n6yjgPD1LLKajVjnZ68ii9h7SsLrydocCpuPfCF0BI9IMYeR+" +
       "1w8Rvo4jUEKO9iRejglStFEKt0Ie\n4tiYJJSV9iMoJbzLJC/R404q8kHHnF" +
       "N0ivtsDmr3PK1LVlHaSDjBkHDGg4QTVDXvWHY/4UHCCQ8S\nGNNZy+4nPEg4" +
       "MQASKvGvWNdY8Y+NhI8QdkyIlsZRVMZISGH/VOKlsH8q8kHb/792+5PmjbTN" +
       "3cfM\n/Y3H3H10/f+zjNvnMXefx9yUyZdhGbfPY+6+Acw9UVcUMK4/gLl9+i" +
       "bRab1J+Ffdz2zO1MEKkUY1\nqpchN101+uCdR0o+pqHfzwvkIMxzFBuGqYq4" +
       "uKJA1V5M+rsFcrfGVadMG0BIBNrJrDFZVboEPsDw\ngzHDDmEDnBqNx6CEAt" +
       "21pJdlnYAN0UEoRVFXYEonEQWGA1yn3A0DnesDG4ObpirmcYRZy8zjJElG\n" +
       "noOvfy/95OkNCjxmlPg/Y0i6ncCBVaj55HbYwOYhZPnUjL4C0lCr+4oJSn0j" +
       "BzOqd0u+QloryT0S\nS+H1p1468er8xEOG5HjvNWBY8GUjUOhRInkRAKB/x0" +
       "tycKSLo+Mqo/LTOatLbmky5JnNFqvQpftK\n2GWSqxNh+NS6os2FgGXR1MgZ" +
       "XLTxXejINu6Dl6DMc6JVvxZvmnvxvjfhI+x4vt9zFhfn0d7ojD0P\nPzzUUE" +
       "SXw3Hr+lkZO5vw1XsjBbXK1oSlLh1udFk0cHqAR5pLGPhIW0qaWQa9r54ypQ" +
       "EY0lR6CrVu\nWQinG6pNe9vDNOk0AzJ+cIVoZ88S0298C82AbJC0entoiEYg" +
       "o7t28iAXTJqG77NAclmWZJGkbRvM\nQlelvdAkuahCT9jAuNpAtm6QucjPyF" +
       "y5aKye+VLNxmBpklcOQI7AsvSyQp0jK0jhuXjnoCXLBiQZ\nWAGIheAOKwSH" +
       "Bl4rIQvbAjv9jO2LUpN3DWJUQhCgzcBoXI1Ajrm8denE6XVGlO1IFno7nKG3" +
       "jmE4\nbfPQNW/0wPxaKut1Fj6v9SDW0bPEYtqcLpOv1oG5Kf1IqYfRrf2G0T" +
       "q7cemy1jnDqG5m0tzKTE1a\nUuv4fm3Q+7aaTDaf+Ike5tP0iZoByNP2ifrv" +
       "7xOklYg66l1IGEgoytnrQcJOqrRdlgl3eoy604ME\nxrQ7XSYbEoimZwyMhL" +
       "39IqHeQsJvqCS9LiTcTnu3k+YB0uywkLDPoPftXZdefllNGjp2+vmlI1kC\n" +
       "7bAn0A5PXunw5BUan3yPWkrt8Ki5w51A6yYnhTrTlUdDik07hLt+8o+lHnI5" +
       "kkRFpH1uMGp6MW01\nYQwNIRUm2XOUef49kP1LGz/h5DVTnlUKX2Tnbca/nW" +
       "UHQXYkLor271q2+0xFhRGBZpVs9pWLfuHx\nvYJAkbfMxZnCvKcKfJlRv4pA" +
       "tkFNnv/OFFSMQAH9zw/yWa6afZazfc2s6PdL+K6rtylSbaOQ8t83\nSp0fNu" +
       "Ls/ylDfJ/SsfCfrWf26x+0TXXBBKqm5bzx9cHkOPzsxJveeOP1SvY9Gtf4Gz" +
       "aQ+bKCIIsF\nQDo9+d/M8SlHM8YS3j4VuXnz+/lGZU6/xxfaKiK69LGpxyH3" +
       "K4c3nL7i8OH73d9zgE17pSmOiWdM\n+SLr/Eufz3HqzWdFpAOJ/wMzUOfJ+S" +
       "oAAA==");
    
    public APDU(final jif.lang.Label jif$L) {
        super();
        this.jif$javacard_framework_APDU_L = jif$L;
    }
    
    public void jif$invokeDefConstructor() { this.javacard$framework$APDU$(); }
    
    private void jif$init() {  }
    
    public static boolean jif$Instanceof(final jif.lang.Label jif$L,
                                         final Object o) {
        if (o instanceof APDU) {
            APDU c = (APDU) o;
            return jif.lang.LabelUtil.singleton().equivalentTo(
                     c.jif$javacard_framework_APDU_L, jif$L);
        }
        return false;
    }
    
    public static APDU jif$cast$javacard_framework_APDU(
      final jif.lang.Label jif$L, final Object o) {
        if (o == null) return null;
        if (APDU.jif$Instanceof(jif$L, o)) return (APDU) o;
        throw new ClassCastException();
    }
    
    final private jif.lang.Label jif$javacard_framework_APDU_L;
    final public static String jlc$CompilerVersion$jl = "2.3.0";
    final public static long jlc$SourceLastModified$jl = 1201637788000L;
    final public static String jlc$ClassType$jl =
      ("H4sIAAAAAAAAAL16a8zs6HnQnLO7Z3cnm2TPXpIoySZfsiflrCY5tufimckq" +
       "UI/H9nhsj8fj8czY\n0erE9/tlfLdDIhCi6UVKqZpyE7Q/QFRCkYBG0B8gLm" +
       "qhBYpU5UeLQE1BrRAStKI/EBEqFHvm+87l\nO2d3K4gY6fW8tp/3eZ/78/p9" +
       "3m//Xue5JO5cRKFXmV6Y3kurSE/ureU40TXUk5Nk2zy4r37xzT8s\nfu0by0" +
       "880/mw1PmwHfCpnNoqGgapXqZS5yVf9xU9ThBN0zWpczvQdY3XY1v27LoBDA" +
       "Op80pim4Gc\nZrGebPQk9PIW8JUki/T4NOfVQ7rzkhoGSRpnahrGSdp5mXbk" +
       "XAay1PYA2k7St+nOLcPWPS05dr7e\nuUF3njM82WwAP0JfcQGcMAJ4+7wB79" +
       "oNmbEhq/rVkGddO9DSzqevj3jA8R2qAWiGPu/rqRU+mOrZ\nQG4edF45k+TJ" +
       "gQnwaWwHZgP6XJg1s6Sdj78r0gbohUhWXdnU76edj12HW59fNVAvnsTSDkk7" +
       "r18H\nO2Eq487Hr+nsEW2xt176Xz++/h8XN080a7rqtfQ/1wz61LVBG93QYz" +
       "1Q9fPA72f3vkWK2SdvdjoN\n8OvXgM8wyOd+UaD/8z/59BnmE0+BYRVHV9P7" +
       "6h/Cn3zju8jvvvhMS8YLUZjYrSk8xvlJq+vLN2+X\nUWOLH3mAsX157+rlP9" +
       "38C/HP/G39v9zsvEB2bqmhl/kB2XlRDzT0sv9806ftQD8/ZQ0j0VOy86x3\n" +
       "enQrPN034jBsT2/F8WzTj+TUOvXLqNPpfLBpH23ahzvn3+k/7byArOfCPcc2" +
       "0s6bjRF/IYlVoNW/\nKscaYMSNRRRh7AJXUGWL8IPFjRsNL5+87ldeY4SL0N" +
       "P0+L7687/zr/40Rv3Yj958YFmXpKSdj17h\nv/cA/70Wf+fGjRPejz4uo1bo" +
       "Wusb//UX3n75m19I/sHNzjNS50Xb97NUVjy98SnZ88JC1+6nJ6O6\n/YgBn+" +
       "ymMbqXlMb+GlO+7zWITvbeCCWPO5+9bmcPvZNsenJjPN/9+h/9+u/fL77Tmk" +
       "Srwtda7GfS\nGoW4Z9peeot/Z/mVH/3sMy1Q8Wwj2paTO++P/b76+z/OfOc3" +
       "/vVv3X1oz2nnzhNu9uTI1k2uk7+O\nQ1XXmjD0EP1f+p+L//bTz03//s3WJl" +
       "5sok8qNzbTuPKnrs/xmLu8fRV6WmHdpDsfMMLYl7321VW8\n6KZWHBYPn5xs" +
       "4wOn/of+6Pz7321rDa29ORvcK2joR42VxheE3tAhp7oWna2qle01fk5B7vt/" +
       "7hb4\nm//oA//85qPx8MOPBE5eT8/edfuharaxrjfPf+svr3/6Z37vG18+6e" +
       "VSMWnnVpQpnq2WJ1pfv9HY\nwatP8fR7H3vtW3/xrb/2m1eKf/UhdiSO5arV" +
       "e/lnv/vGX/kV+a83UaDxxsSu9ZOz3bg0gRb/K2nn\nQ43n3Gvt8R4tK7p3NW" +
       "977Z36n29lcxrTOcni4hKkNdHrboa36eFKv77y1f/+Sz/bvTjT2I75+AlN\n" +
       "m/euh8PHBt5X638s/Oz3/036vZNYHxpGi+PT5ZPT7uRHbHbyG/ntW3/35/yb" +
       "neelzsunlCYH6U72\nslboUpOUEvTyId354GPvH08w52j69gPD/+R1o3xk2u" +
       "sm+TCqNP0Wuu0//6gVNoJ4oWlQ016+DHun\n//bly+3ldnmjE7WdL54GvHm6" +
       "/tBDO0lOK4Ey7Vzcv78k8fs8Sdxn8ftLZIfcR2mE5+/0QXAAwtD0\nKfJex7" +
       "bfRPf8Mv381Kf+5n/6zu9sXrv5SI5+84k0+eiYc54+MdSNymaGz7zXDCfoX+" +
       "595ttf33xP\nOeevVx6PpFiQ+aOf+7f6Wz/8kvqUuPxMs5I4O2J7hdvLZ8ob" +
       "jRieG9yD7kHtPfGkmJ5p3ht2IJ+y\n8N328ifLNsZ76p0rT981i6cmyd1pvO" +
       "DKI14++dLJJc5rjKdM3DD8oYdgdNgsRH7id//Cr/3km7/d\nEL/sPJe35tRw" +
       "+QiuVdau1H7k2z/zxge+9R9+4uQUjSI/+ivQj32lxcq2F7JZxbTU8WEWqzot" +
       "JykT\nanaz6NIeEPjD0dn/0LTJsOFTiUtf/XuLYUIiVz8aktEBsoM2bm9aKB" +
       "XJYBMes5EZOnRzMnaXMsL3\nS1IvdATnTIpUVQtKAEP1h/tpNsmzbqa6eEkc" +
       "JdSJdnYfCZmqHx5RS4y3M3lWJz1hhtG7MElDa4Dv\nSj8VNCLCN/10LU6n4/" +
       "FgudJZ0T+k/WmvBroBwBoAAPTK2ldm9YTgamG3o4oNme7CnKMg2N2hEbnb\n" +
       "rjZW5qoHqBrgB3s+HQ178DiIAdcsjQmJMqQam8MuHC13iAwtQhQdWlnuy9mS" +
       "xIgMNUNRNo8zxJ1v\nMV5wTAwpBscDyZgGPptxkx2+3wlIyYabDBdoT5ov4T" +
       "Tq+rxW+aEiMb6fp+ZInQ2KJc5xzNwDw6WF\neZJNr+K88kcTfr/ZMxW3GI6r" +
       "me6uEs4ypUnAF9Ya5MaV6Mz3XXS3T5lNRQwPEkdhmKiE7LEgA3Tm\nFiHf65" +
       "v8jLWx0LCpdCOHvEwPRL7gySQWqF1NRNDMZQxax6BjgOlVt7K32MQVMYqUVx" +
       "GMbVwxPaJk\nBe6SeA+BW3hiUAS6M4uVSpcggUiJJ3OFaTPqgBVluIIRa2VC" +
       "x0Is12G/8dbQVhVzjs8XK3tUCPHe\nmx/tGc7oHseKo4Dbz8DKxUpBUdDJPh" +
       "tRSGEJrOzLfSJncIHdcRuLk+fYVpAW3cIT5bFC8fV2RDMA\n28uVwKuGqFFZ" +
       "FZFjjltPlowIQUy8RdAd75TunNAdyMhleng8ZMMFMmMPGWlKW32NddcwSBr5" +
       "XGAx\nsV6awQpMhpCLqRYQpdJoIwTEFFhX/MyW0KxaWnOHhSS20DI773sktT" +
       "KVTRQ6bmTsyiQLuvvhVNcM\nqNdbYoY01+dbfLgfNqIl9A1Cbct1PgHQKYjP" +
       "zGM5kHne5jSOsUIbpCDWryFTwwQC28z6Ug8fW11h\npk+3lYmY7GK4G0ZAT5" +
       "xPQT5B+yKYTiBNEMspHs7ouRYf/YlzwACnCiw/sfsL3HTgKou3RIj5WwSY\n" +
       "wUZ3Dve8AQstSVOxRgopWv6RHACGfoBHmsrsVnsA9JNgs8FQEyqPk34JT/xh" +
       "wRrOcKTAKbcRDkfR\nItGeWYB4NwXHh0XClo3vuSiNi4sFoZHNN11J+vagWb" +
       "mSwma225vzZIZhEycsAQcVuIrUbVJAZwc+\nFklSkUYisEpXYbeYsFtoMBhU" +
       "tm4FLoNhCY+V+w24tMJ+hYf+fjoW0nRPx6kLkyLDTbl6drSF2NdR\n0mj0M8" +
       "8XcVD5kmE4UTcejQsTXqQss4wmfQKzh1koaSOV5cdWguOJFu/QBU645Epdlv" +
       "yclERiXkxG\neUb09oXH6DGLmIGnDajlgutCEJLFpKRCmyOWkIM5SU3kPSI0" +
       "psEqeDmBNaNeUZKyJXdzz+Z2+jaj\nacXqD45zcl3UJrxP1nmTwCp1mi26h/" +
       "wYLop6hnBUPteJnXswa8jpLwpYVZy1RUIhOVIrtiKyFTZJ\nYq5y5kKkhtOd" +
       "OcEnixl8GKKpR0IA3psZ3VAf7qZYpOMR7s9IZswoUTzfbuEg7an7xtCDAWtM" +
       "p8lc\nU4apquxm5arqgQzNqNYOHawBX4Gy/gBYlN4xibriYouRyyoeRn27QP" +
       "XQoITefIVseGHJabugdFI4\nygf5imM5UgY2xE4/7DEQ8RXHkhI9wce5Olyz" +
       "WBFyrtIlSRA1mSHmIB65Bwe53C/y6sDsNHdHo0i+\ncKQjMRaXc8jfe/vpdJ" +
       "rpVF6NXG5sE8WOYJKlGaEB4eIeT+bdkgb646N4mFfDnhpOjB5NLBMadDKU\n" +
       "4/RlPajqgRjs9d6SQ/w+PzpiuDa0l1kKFRs42g+l6ToIlBE8XOV53O3PYSqd" +
       "FgW0sHYCYx+nK3O0\nna1s23bM7azylZCqmYg8iHx/m2Jx0Ch1iQ0wq/DKmD" +
       "30RyRFhEsGh9SxqXaLJbGUfaHiyjJa2qKg\nxi5DwTqP+iIjHgOCsB2sHtc9" +
       "E5iovURzXMjNiMxwJzi6NmDHPqz288WGjdaceeyWcXwYmOFosTxK\nqktPFD" +
       "nDKn+33dh9d2mZxyk0GxB9bXskl9t1NQkX2MgqrX4UeRyMet6CHMnHmli5hm" +
       "FG3UKtFwAw\n1zJpuhmM0citmiw3xRh96gobLR7VpnWsPHZ6wCXGFejJSJ+o" +
       "rMHGwf6wGO+jitHsEAZjqjIpo1v0\nm1SVsqHHw45vFv1iGuPFaLlS03hOR2" +
       "uNprScWA/2KZLm7ER21VEyJVRDRyBAT/qLytgt6GSjhO5o\nSXfVmqYPoRdL" +
       "SVZNYmlNT49ltvfXtUILh4U9XUs8R4g9UK2glJ4I8y1Y+TntVlxAM1FCw+J4" +
       "7zmG\nmhsJn3etYdiIX9sGalqrWnjYS9pgOloD/RxAAlUU7Z5j7/2CVuRDWn" +
       "Jx7UsHEJY1ZjFghCpmqQlv\n9fXM2GKuPOz6vrdhtttQXy3nJO8SzbplhVUB" +
       "5vDcDp7Ww1Jhx1YhJMmIV3RlrLq6qinoQm/WFMoe\nlvcjpcfWGsbSmWN3U0" +
       "JcQoPSzYrZTpkBmyDMNVwu4yMCrePUmgL2AcTNfe7x4lLrH1AKmFJ1pMzo\n" +
       "OtgdJGdU1mC5njioxYlpdwHMdNkWgtJ1YTkxxijR1weWixXlIA8OSZN/kYFP" +
       "7JBsm+J9WIKWfTvG\nmO3AWsbqqNouq8NhuIPnwQhZHrrbXJw1S1DIIzBekT" +
       "WZaJJjfyxgSIhzMo7XAmyn+h4P5d0qFPQ1\nVaboRCRWyh7aTRUdwlNlfaRT" +
       "Qe2JGNHdzSy/P0SB0Q4GV1ukFtxSo0hdRtnJ1pWGdGL6i4laufV0\nbUnsbM" +
       "cP6YNOR8OBhuThRvX6zMomcFrrOaOg259a49oawfH4ICrThNkFcV8ciHpqYy" +
       "AOU+jEGKw4\nyjf8WukVeioPepGrygNyr5C5y1Jls9BjC9irJ7npdokhwgTM" +
       "JhKFQzyvBtZK5gxRK0jMVTVxFFuh\nJ0OCD++IzT7Swkio0X68TrKxe0izdI" +
       "MHK5vlzP7Gy0x73A331igPMq48bByFQobAJCrH9XbVj43l\n2Ij1YQEGh6EJ" +
       "AEHcyzihdiJgOpSj8UKkl2julQA/tMuZ1neagNcFeUEN+2qa5St4bvrqFoFY" +
       "rxwQ\nkjfI1FWxjDKWp3UBA9CcXyhqKG8JrV7ZEuYanKRofTuNwIKhoblfW1" +
       "0K2zSWSUwEGRHgskC9XHDC\nOSsQkBAPjtujpK91eDIFNEPdA16/iU4L8MAE" +
       "HI5QjrLYrWwA3KlwLUUW53SnEjSkbJc7Utp21jMx\nDDUSbo6Ym4KccAfRLH" +
       "VZStjxbk0INq7yuk70hisg9tIJkAATMsFATUIHErnQqXU3cEZbbJ0W02q4\n" +
       "nxF9hQkSfAPIUGoz5lqrmK0s0Ht4GzFNNtrOadbYbiyhsEzE6CF4HUqjxTC2" +
       "OCiSqGLcPYwQHsCo\ntTeeYqnFK+OaL3bjpbJJB+NQskbkUtWZDc85mBXrUs" +
       "FXGxQ8Ql6lZQLle4GVzGZzP1wjM2DmdkN4\nsJQXlNevmX106PUSQ6/wsawN" +
       "hDFt2sW0p5FgjvF7GGN5sUYUYEIwx4bB7bBKQi8TSamvll7NG4dx\n2F1vRm" +
       "Cwjjb2gdHmHs0HwxVnVPRqWLhxrXngWt7Ua5GiwgitF8ssrt06K0t9G0WaV1" +
       "OqSkjslGTB\nemcGaheuHHM9h2iakp2dE0jwxLYbp+ATOq60UEWjbG2gODgj" +
       "pyQj6pseVEw2eTh0BIxip/ZGd0yP\nT3GMSkFS7w5TW1wu+d3UZsKlUjeWCG" +
       "yG4PA4cReLAF+Bdp4mcz8erCmI3nkLmZ87M3cxJ8wht93N\nD/l+IQJDYYGi" +
       "3ITvEquVVPWGwRozhwyH8FRO5PuVJk7E3spqrENzK9JLpM2OkUieWAA7lvKS" +
       "xaSd\nTAHG035vtPYdZbsaqAbTBcTBYrCAQMDo1c4qD5iDbpTTSqgkzSIUYy" +
       "324B6iFlNhpwoMNQRM2pv2\nbGUjK9Cyhxta3a9ZiMEoVxCh7jFSrRUEQr0M" +
       "1TZg2IuOC6MucpdkyhqfZcRRPA4zaolAuxHTixNc\njQ1i1+QAohHjbBUv4z" +
       "ku7ZQQWyfzomvWi/ncx+bB7LgqYRRD0j4ULTUvEJEVKGDhZocfRCwMMG5P\n" +
       "IFAVDBBwHujJnBiySmTHvipupGpSO0vMI7pByOZGBUxHrLtCndL31iYggjYc" +
       "qOhsJzuj0UIRC1a0\npHUgOPQQBT007g3DJR+AzqxmjxqY9TgvjKstyHf53g" +
       "KKoCE80eNJibHEaOHkq/1MnBCCI7DWhgzc\n5utiBpRgWqkUT4c95hgC9JaV" +
       "U1rx3Wq0DFb4MeciI6W6fWLPH8uV1VvxcJj0cZM4bCDGlA70tjzo\nDrnK4L" +
       "kHzxkVXu7yUsr9IC9zx7DIXjnM02mNW0YAauPQmYPr7rquBQHjD8VgEfc8V2" +
       "JHeQEdEmem\nHCgo5D3agjINd0F1cUQ2o4gmmnXuHjzKe06exqNhFXGYCHob" +
       "GI13WXfP2QEBRyt9ms9XrAsi9bKn\nkrtQmkx4sD+doJM+oixRUA/S0MZEMx" +
       "yn/YmNHLbLZkU5XiGq5++lJeEqm5zpcp7u5RriZrKFbqcO\nkq0iJPVhLlOd" +
       "dYVXI5mq9N6OXpX+LHI9tVnZjpEk5ysuAh0KdKzs4AQheqBnNid19whBWjrC" +
       "UgW/\nmHnDmORyBXHyqcP2aSFaDeb7pcBZqFmrs/BAQogT+xNNDzD4sCM8Fi" +
       "LxIBwHFDFwfGzYbYyca7+2\ninUpU81SFXfgfMrP96CyxpovbJWO2BwKiRFl" +
       "QORRNDax0SfA7XGs6n4FOcH8SIx8MK2pLQbJXSHL\nh4Y/4HpjWd7OZaNHuv" +
       "ooXYO2FM6q1bpvG3E+LlnXE00Y9iCSwMywSoOYU5fsMd5onukN9LCCwSGD\n" +
       "NctQbnzgRQULbV8qKsaiB1PyuNC5ZJkfUX5K21t1G2QDtyfOwKwqXHq+DtdG" +
       "epA03fC8FeFE/V7u\nNx8pLtodK0gAIqP1IQuK0Uxovkiq9d6N0wnVfP6PIA" +
       "SuxMDaxltP0Oa2C44tOu1Nwp63CMiR627w\naAFlDBHBzB6EuiJXrMiN4PcP" +
       "rKAbw6m7hmiiBqzRfLcNPIE69LeBospAsBcWTbZy6TDuj6FjvweW\nuLUhZl" +
       "K0S6bRnjayRdnNxSXiKgZTSgGaUyplLL1NPmzMw8VXQQ6wOM43YW4xrQug2s" +
       "uwbEhybaj9\nBG6Wuz0KjtPKa1ZPLDdJJ4MuH4HGKqMToPLmxhLgEwM14r3R" +
       "h8seuD0cZjPq6I+UupyqC5dd10m5\nzHWdXUN8Lk6J1BgStcz1FAolyQTvcv" +
       "OwyUAAM+G9BTMR+YEz3GWHoO4BHiEJMFCWhkzZFDyliais\nFgNqqA4CemxP" +
       "2MV4oE1rE4PGEe+z9l5juj2ejgEsM+rB4ljvNTxC2CVQMLXAGsseHyII8qUv" +
       "tXt3\n+8stwNunDcoHFVHHNtoXzJM7fef9YenJjc+bbZ9KO89HsZ3Lbam480" +
       "aD5s5Vdev+g+rW/ba6dZ9u\n4e+dJijfDdtpD/Vq7/9heeDjl6WBJO688W6F" +
       "0dP27zcOf/DSj8i//M7NyzrCPO28mIbRFzw9172H\nJYXrSJhTHfhqn/3D+0" +
       "//Rxz++a9dryl0m+k//Z4j76u3809wz1j2r97sPPNgT/+JQvTjg95+fCe/\n" +
       "G+tpFgfbx/bzP/XYfn77f/tyP//2tf38k3zby+fes8jyvhWYhu6PXWnyzgNN" +
       "3mk1eeehkTgP6GrL\nqZ9t2iuXdL3yNLqeova0cyuQ2y38R1X/f0t00qjb1N" +
       "NZZhh6nDxZRT+Vrc7ViF945s4f3PyHH7lz\nKgs+q8jJWdrXjx88ebrgsUMD" +
       "p1lffCCETzTtTtNevxTC6+eS30tsEejxmlyd68x/4j3qzI9Clk/s\nxStVqk" +
       "dR9BTRf6Fpr17O+uofU/Rtv/hBiP3Pp53X23J8oIa+HZhIoG10VW90ep2B5x" +
       "IrjNOnkP9m\n0167JP+1/9/k/1Ta+UBDPpulZnhZifnmU2j83HXF/gBovKyT" +
       "nqZ8f0L/ahO2HyGU1gMztZ4wkjy0\ntaeQ/0bTPnJJ/kd+QOTffAj2zT8mD3" +
       "+rcdFED7RZY8ongL9xjdbnm/ZDrVAuab1x9iH7ybL5Fy+O\nmZzYxyxM9bvn" +
       "ivZFy/xFm4XsIA9dfa");
    final public static String jlc$ClassType$jl$1 =
      ("4bj5wcuPvWxVdTy07uvVtgu/vW2197K/qBxdC/08bQd6Hl\naay3sf32k6z/" +
       "qfdj/Zx/H+XdTlteL778Dn/xgKGnVHjb/jv/j97zi2nnhatJn8bUhy5t73Gm" +
       "brz6\nPkydK88Xl2pVwtDT5eDE3VXeDI27Xz4VXi8eP13wVdlXvnaCpD9/cQ" +
       "Y4V9nPL8J38EY0tnFxN7yw\nH6C6aPXfiqz9v1AvvnRx9/QkfPvinI+vTSK0" +
       "9eSkcUJPT8Pg7lv39GPWaMFrvla24V313nsuhD5/\nIq6xtUvUhuwl+tvvoa" +
       "iTh13Wlt/PEe89pcR85v/9dfnPzkc1HhHx9ejy/KUmHlXy5dmWj10p9+qs\n" +
       "S/vysz8YntrLr74//b+edk6KV+UkfTfxn8hp1qrPtjftsZaPPXE88nyIT/3s" +
       "d79y95ei2//yvES4\nOmh3i+68YGSe9+jhi0f6t6JYN+wTNbfORzHOEvj3ae" +
       "eVJ899NcHwQf/E0b87Q3+vdapL6Pb+t6Oz\nBP4PK1ypgNIpAAA=");
}
