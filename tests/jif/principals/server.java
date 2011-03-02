package jif.principals;

public class server extends jif.lang.ExternalPrincipal {
    
    public server jif$principals$server$() {
        this.jif$init();
        { this.jif$lang$ExternalPrincipal$("server"); }
        return this;
    }
    
    private static server P;
    
    public static jif.lang.Principal getInstance() {
        if (server.P == null) {
            server.P = new server().jif$principals$server$();
        }
        return server.P;
    }
    
    final public static String jlc$CompilerVersion$jif = "3.1.1";
    final public static long jlc$SourceLastModified$jif = 1202236209000L;
    final public static String jlc$ClassType$jif =
      ("H4sIAAAAAAAAAK0Ya2wcxXl8ts/P4NhxsON3/CC2Qu5CIZRiWsVxbHLOhRy2" +
       "McEoHOu9OXvjvd3t\n7tz57DYIFJWE/CiipKVIbdIWi6SBVi0plKYVpaSU0J" +
       "Q+LDUPKp6lVaQCTVERKaKi38zs+872n550\ns7Mz3/fN9/6+2SffQ8WGjpr3" +
       "SMkQmdWwERqSkjFBN3Aipsqzo7AUFxfuP/zdF2+4+FIAFUZRmZAm\nU6oukV" +
       "mCVkb3CBkhnCaSHI5KBumNopWSYhBBIZJAcGJQV1MErY1qQGpSVkkYZ0lYE3" +
       "QhFWaHhWP9\nsmAYgBZkq8YX0T2oIKujNgvDZIpzxIA5Szd2fDLzu/1DjYWo" +
       "ahxVScoIEYgk9qsKgSPGUWUKpyaw\nbvQlEjgxjqoVjBMjWJcEWZoDQFUZRz" +
       "WGNKkIJK1jYxgbqpyhgDVGWsM6O9NajKJKUQWZ9LRIVJ1z\nCPwmJSwnrLfi" +
       "pCxMGgRd6UjK5Ruk6yBeuQSM6UlBxBZK0bSkJAhq9WPYMnZuBwBALUlh0Ld9" +
       "VJEi\nwAKq4ZqXBWUyPEJ0SZkE0GI1DacQ1LAoUQAq1QRxWpjEcYLq/XAxvg" +
       "VQZUwRFIWg1X4wRgms1OCz\nkss+O4OV/z0Y+6gtwHhOYFGm/JcAUosPaRgn" +
       "sY4VEXPEy+nQocgd6aYAQgC82gfMYfq6fnpb9OIv\nWzlMYx6YnRN7sEji4i" +
       "fXNzUv9L1TVkjZKNVUQ6LG90jOnDdm7vRmNYiGK22KdDNkbT4//Js77j2O\n" +
       "/xFApREUFFU5nVIiqAwriX5zXgLzqKRgvrozmTQwiaAimS0FVfYO6khKMqbq" +
       "CMJcE8gUm2c1hFAJ\n/FfBvxDxH3sSVG5gPYP1EEQpQWtgDGtgclHSBNkIO3" +
       "tZSqhqpqAAZGjyR5AMzrdNlRNYj4tH//rb\nLw9sf+BAwPYokwUwNs0EDvUQ" +
       "p44KChjVOq9mqKoTNPDffap35Vc3GM9AhhhHZVIqlSbChIwhdgRZ\nVmdwIk" +
       "6YK1W73NYK/coJ8Dpw4LgMhHgC0FBGR+1+73KiMMJyjIgX7vn0j+/HZ05QR6" +
       "CGq6XUOWtg\nhmnOW2XPyO6huw+0F1KgmSJQKJWk3ZPx8tCOi7O/Wn3jyef/" +
       "8/MAKh6HvGZsxUkhLZNY/xY1rUCy\nqLWXhjHkESUqTGA5iip4uhAg5K2gLd" +
       "FEhkNQXZRaj4eRTOHDDAvUUKE7RChaK7h25/IqiIvvH9xx\n4uyZ17qdUCOo" +
       "MycD5GLSCPbrOKarIk5ATnTIP/LxtksPF3/u6QAqgrQAshGQjGaZFv8Znkju" +
       "tbIi\nlQUwK5KqnhJkumVppZxM6eqMs8LcdwWbrwQrVZixUGTGAnvSzRo6rO" +
       "LOTo3uk4Hl3Mv7ghvP/aLi\nxYA7PVe5ytUIJjzYqx2fGdUxhvXXvhl7+Ovv" +
       "7b+TOYzpMQQqVHpClsQs46+uABx0VZ7EE6qvPfSN\nnm+dszxylUO9T9eFWe" +
       "qQ2fsWmh99Sfg2JCVIDoY0h1nsI3YSsg6gY4jNw65N+t4Joegw4fhxnwHB\n" +
       "ShPVTXUH3v5D8+9HORN+EsBVo4PEnBDqq6Qzz4uL597e+/JTO16YD6AAmAjs" +
       "nYRGQBKh2jfl+G6/\nvUsdmNa4SQu4OQc44mxT16vz82Cev3Vf13Tpx6mjzN" +
       "8qEtgQdUmjUpkZs9yQUposQf1NsDCBWkzU\nIdAyLabsTF1QDBnaDx5ao2xz" +
       "IKvptKxlBGgCIDMxrazjHkeHrqxGHzeBcjoYYxYRRzUOobi46d6L\n/z7x52" +
       "fW8QrU6sXIgV77w+ZLnU/e1cXsQb2ixS/7MBYgMXPlxMUHd0kdt3avuZmZoF" +
       "idYeHW6lKo\nnZ/DMWtGeyidUaFy9AFX9TlGNsl3R4PP/uDTS19gseFSJy2L" +
       "fqNZ9nIpio5bvRnU5ic0qmo2S3Fx\n21svvPGVR+rPuNXkQ3BBX7O6vr7tA1" +
       "zO+LKN2uEzqo3gMqybPeDsKq893Dy5zXL+vobX11/94GnO\nnd/s+TAee+Kj" +
       "+bl1j086gTVgHkofkXyWvR06Zcey6kP/vPbQvsEjLssyc4G8MwyQG4+OWxxt" +
       "7wTC\nXfmUt0UlRE25VPiZ0xvr+o7t+HHATBWDtk56vOL5MN1CXlP5vef+dv" +
       "z737FoDDHhYi5Bh9n4WY3H\nzBh7+zzXx2aN4eziOJob3vt2CwfczbWoWRr0" +
       "vJmpkDYD/nZmkLbfVpFKTXzpw1OHy9scszTZdb7J\nU+c9aHExcOzN/T31VR" +
       "fAHOPoiinBiCjQ6dFLAtbBPrK7Fvv7Vh+pueduO3z5FfI6c16nTFLsjmwu\n" +
       "/2OCq4LfcDZTHfzRkVQAlUCjwdoHuEGNCXKalqNxuD0Y/eZiFK3w7HtvArzt" +
       "7bXbgCZ/iXYd6y/Q\nThsIcwpN52W+mkzrcbvZpyLr6arJBYi5xDRDuIqNPb" +
       "yCBgh0QboEEQ08Bw12XcsSVBAzPKWA5U6c\n4H3+nyquPdC2KVnLoqWMWQMu" +
       "gsRM+6UUw3rnjFZ6GG1dglHuwTb4mmXAmVRpj6PTIcNOzToBkvE4\nfM7KLf" +
       "aBDfBvWeJAOu7l0eDBaVxOpogPqXmZgzgS66XYwPbS7PxuM/ycTqTJaSGaF7" +
       "v7sXvr/l3/\nqrxf+PVunlprvLeGASWd2nTkPO7ZXCnmuYGUEVXbIOMMlp3w" +
       "95+2g92JLVeuur31rcHrj+7NF//e\nLxtevLh45kLVpYHrzvz9/9fim86Zr5" +
       "tvXVKIuFidaby1cEo6zT6zmBGc833Ai9TrjdtyfuqoJ3rb\nbV+ogf86+Jea" +
       "vlCat6PODV06X08HbenmdNnO9WsEVUzCldjknsHoNnslLh9H1pOgFXaV4vff" +
       "+lCY\ndgQs4Xm2sk41qoW8SA1PgZxSjgpcHG7Kz76TE6imNiyhLZYTHsvJCf" +
       "OMjcedDDCfkxPm8+eEbvhf\nvYx5nvCFN8XpWQInb05Yv8xBTk4Yy9JkzT4B" +
       "0L61PuezHP+UJLYv3N19Sqt+mTXt9ueeUkjSybQs\nuyuLax7UdJyUmFilvM" +
       "7wnuBpgq7wfoUgqNx5Ydz9hIM+S1AhgNLpzzTL8g225Qey0FIpgmx7gKuj\n" +
       "aF6yG330zoc0ZWOftOgVqs6bV9L8S2ZcfFe76+a/DL9x3GwqbYXhLAmxb5xW" +
       "8NsYJ091PfDqqxc6\neE8oysLcHD2vBHIKvzPbX0XXLkrNoiW9eTZ5cN87VV" +
       "bvxnrialdgM9FbFqdD52Mres9vP3nymD+d\nIpf2XOIzHCukr+v+sOTyKx9s" +
       "zq+3/wGCz81xbhYAAA==");
    
    public server() { super(); }
    
    public void jif$invokeDefConstructor() { this.jif$principals$server$(); }
    
    private void jif$init() {  }
    
    final public static String jlc$CompilerVersion$jl = "2.3.0";
    final public static long jlc$SourceLastModified$jl = 1202236209000L;
    final public static String jlc$ClassType$jl =
      ("H4sIAAAAAAAAAK1ZWczk2FV2r9NT08lMd7ZhMktnpifMyKRdq12VJgou125X" +
       "2a4qr1HU8W6X97Vc\nDhmBkDJZRAAlYZEgeUGKhPKAGEFeECAl7AGheUh4IA" +
       "GUCCEgETwgIhQIdtW/9d+d5CGUVK5r33PP\nPfec757j+9XnvwVciSPgVuA7" +
       "O8PxkzvJLtDiO5QUxZqKOVIcr8sH95R3v/Dd7Zdfnb39EvC4CDxu\neatESi" +
       "wF871EyxMRuO5qrqxFMaqqmioCNzxNU1daZEmOVZSCvicCN2PL8KQkjbR4qc" +
       "W+k1WCN+M0\n0KL9nMcPCeC64ntxEqVK4kdxAjxBbKRMgtLEciDCipO7BHBV" +
       "tzRHjUPgFeACAVzRHckoBd9KHK8C\n2muERtXzUrxmlWZGuqRox0Mu25anJs" +
       "Bz50ecrPg2XgqUQx9xtcT0T6a67EnlA+DmwSRH8gxolUSW\nZ5SiV/y0nCUB" +
       "nvq+Skuha4Gk2JKh3UuAJ8/LUYeuUurRvVuqIQnwlvNie015BDx1LmZnokVe" +
       "vf4/\nH6P+69bFvc2qpjiV/VfKQc+eG7TUdC3SPEU7DPxOeudTUyF9+iIAlM" +
       "JvOSd8kEFf/AJD/PMfPneQ\neftDZEh5oynJPeW78NPPvI5+89FLlRnXAj+2" +
       "Kijct/J9VKmjnrt5UGLxrScaq847x51/tPwT4Wd+\nS/vXi8C1KXBV8Z3U9a" +
       "bAo5qnYkftR8o2YXna4Smp67GWTIHLzv7RVX9/X7pDtxytcsflsh1Iiblv\n" +
       "5wEAAI+U3zeV30vA4bP/TYBarEWZFt3ZWHoC/Fh5hYIy5IoVSE4MnfbllaI3" +
       "bC9cKNfw9Pn95JTg\nm/iOqkX3lM994y9+eoh/9CMXTxB1ZEIZ7FLPnVPtdw" +
       "7agQsX9lrfdr9nKler1Y74t9+5+8Qn3hX/\n3kXgkgg8arlumkiyo5U7SXIc" +
       "f6up95I9lG6cge0eLSXUrssl6koA33NKRXuUl67IIuD58+g63ZPT\nsiWVkH" +
       "n9le/9zbfvbV+rgFAF7s2V9oNpZRjsg23XX169f/aBjzx/qRLaXi4dWq3k9g" +
       "/Xfk/59sfm\nr33lL7/20imKE+D2A5vrwZHV5jhvPhX5iqaWyedU/a/89+Tf" +
       "P3ml97sXKyQ8WuacRCqRUm7gZ8/P\ncd8muXuccCpnXSSAx3Q/ciWn6jrOEr" +
       "XEjPzt6ZM9Mh7bt9/4vcPnf6tvBa/q5gCzm5jvBiU2o1tj\nrbRDSjQ1OGCq" +
       "8u259exT23d+7mr9q7//2B9fPJsFHz+TLldacthTN05Ds440rXz+tV+lPvnp" +
       "b736\nvn1cjgKTAFeDVHYsJd/b+pYLJQ7e9JD9fefJN3/ql1/+9a8eB/5Np9" +
       "rRKJJ2Vdzzn339mV/7U+k3\nyr1f7sHYKrT9FgP2MwHHE1RXcN/+iTOd1f2t" +
       "I5EKi+d306jK/seBdOUP/ucXP1O7dTCmGvPUXs3l\n+MFsd9/Ae0rxB8xnvv" +
       "NXydf3/jtFQKXjufzBaVnpDDi7X8luXP3tz7oXgUdE4Il9xZK8hJWctPKu\n" +
       "WNacGDt6SABvuK///vpxSJZ3TxD+9Hn0nZn2PPZOk0fZrqSr9iNn4XaU0Z4/" +
       "ym7A8W/V+UR1uZFf\nAIKq0d0PeGF/fecBEBcT4JEyGWUlFEtoxPuSnyfABW" +
       "qPygM0q2u7urwjv1AC6ErrTuNOo7p/74P6\nLpX9uuVJ+2r0UnV5T6nubRtH" +
       "uX2MfbZ8iSiT/e0yD+5H3izr/x5dla/uHGrtQyYuY/3GUzHCLwvy\nx7/5i1" +
       "/+hRf+voztDLiSVX4vQ3pG1yKt3lg+/PlPP/PYp/7h43v0lFvgbf/yd1/YV4" +
       "RxdUHLal5Z\nt/LTSNEIKU7mvmqVLx/q3sAHAUZFlltWq+yonP7Ss7/5T699" +
       "Y/nmi2feOV54oOyfHXN479hHsBbk\n5Qzv+EEz7KW/BL7j868svy4f6vHN+2" +
       "vE0Evdzmf/Vnv5p64rD6k3lx3/of5Mrv/1pB1P0eMPXhek\n5lbJeaeblBa3" +
       "F2Yek4sWhGFNOu1gU3FpcTGbyxOCNJe75lwZ+hMy7ZF8tsnUVlJDkqKDDITp" +
       "aMYN\nIcyyApydyuqUs+BVn7XqgYMLbCCtmPqMBokBQ+EB7LASja3Z4RKXcK" +
       "vVLWKwizS9Tqdei5F50YL0\nRQ+CweZK1+kuN94EUWs1t2lX5bbDZbiyN6P+" +
       "ooVaZUYofARlRrmeUlChjppgGxxN0REjLl0RdWsM\n7Kc262KsLbewvm16mL" +
       "SaTDt4P3UXfsCtBLPfn4ozVBnPaLYxH0ZCI6THwYq1bUGsO8KwRZsjTpqY\n" +
       "HNWrxS4qGH3RDrsCX183/SmebhZGNhCnQZFP0c6qUJv6gChSqDHYCJwvmTm7" +
       "UvzpUDDsXhjjxooa\nMxyDBztpW5Mz2hlujHwT+uymP99wM2XIif6ArmwvuP" +
       "ECt6czvLERckMOg51pDNgxF89hsreQNvgM\nZ2hO7BHLKTtrFDWM2rqz1daL" +
       "CN0KsHw+NpYqzGzwzjK14V1fS+nVHGacft4X8bq8smeoGfY4Sph7\nYtrf9l" +
       "GCccw6ofbY2dCqbVE8bIsh3+eYHTWc1VeiPayPp42BpNANAvUZGg0b9DLngs" +
       "1QEYgQ4tE8\nM1haZLbL0ZQzGnx7uzTcuEFiRc2OY5BskyxU1AUQNpLtxCSF" +
       "0diFZgrK+DElLQ0vFtrskKeGO9Cj\nfcVQYUlAaQPrNg19LughrHuB3COnWY" +
       "0rnLmQw4K3XCDxcGO1LZ1fFEzWIxpr1t6I+IJlaJgY9+ob\n0huoLpIt3YVg" +
       "jfFmE1cHC1tetyFoFiPIpknWoHyEBsshMxoxHY+dWiG6jSy+U/AGUYT40sgi" +
       "v3DX\n9NaSUMtd1UesLpCwbeLzUA6xeRmtrsG4TaVhjtBZDRrp/Swf5JNWvG" +
       "LHHYMSqMZwSAc0lxiDcWIo\nzmTVX9hTspWIq9AMgxBFxfFmt+yg8yJSF85y" +
       "IWzayQSCIrlGbHuxDvbRLSX49pbZLuL6cJAxdNBu\n45i5Ycmw54a4v15QA1" +
       "2XglaHVVrjeQ81TXMistPQWcmYOd2aqrEUa6yrbqc2LOANDmktBBxS/Fl3\n" +
       "EGzbhDE3JwZWH/XHS7ABIi3cms9nu83U4JHMq29AaCLWO/QSxsYhzDNkuZKa" +
       "6Xok1+SpHQmb6Ras\njxq44a5mND7j7JZI5d0EIy2EhDivtdlpOtSF5ihuwp" +
       "NsLMY5Y5uEuBCgXGhEDLarhQTHm5uJPVwx\n4XqMdsq80U5N1OjgTYNQR0PT" +
       "XtC8uJQ2bW25JieDAZoxDD5Elpgp4YjZ7EDT0Jpm2aSX57V2uhMb\nSKgyGO" +
       "nPd8qgMyZWGCVHJF7WV7uDrr31lIKIPLFjatSCUrgEsRHRaGyO6c1YEkdZV/" +
       "TsTZrIU6vm\n0MwW7+ChZ87s1GWDQMznsBbO4y6hqFtOjrZ0xIpWzoxLu71s" +
       "VSx9OCIJcu23Urs9cVrzDDHMjIIk\nwqrlAopQrNbZJcx0timEMG/qY5XJzY" +
       "znM9NBQDLLEBlck0IRzGk6ZCcB5sR03IbBFO3ndVZEhkM4\nzCh9tah5LqjG" +
       "Rb5zGLK37mkjTgvpph5zrtnrrXSjOcPY6cAX6mbDi6ctYRUbsCG0N4st3FmA" +
       "SXvm\na8a2rZu5t530ayuTYtc8M2llnUEX7E3I5QIcxn3Wb1NDrz8IQ5guin" +
       "ZKdHqIBkKzNdmAhQGMoi66\niT1hOab7uNnc6A44hqFaxsSqu5DaKzptk7TS" +
       "brv5ugFnRbeJaVmL1RutdZDD7qZYdMNhYtAq426Y\nwBbj3o5mysMjrTcW8Q" +
       "Drc3bB1KYquiMkOV6PGvSg70mDvCP5zbyY4lmQ4LnTHxvYdDDPI6koAaRN\n" +
       "ojAfTwqtr2x8QciF5bJgSF+22rAKEzWoQPJOU+e6dMTAMT81WATZgiLhMZDW" +
       "UPiYlptWvoKQeV8w\n5jzl5HNmotZtNWlJWSq6oj+yVEGbcLCD+zVPhAZzLh" +
       "lrO0tlpNGuh4743toZL2KntWxTFJQO1qtG\nz4i10Wq2axJ6uBB73jSw/I6c" +
       "LT2mYyn10PWbq9YorzUbcZZRY3YJRlS64G261e/zCAlnEZKgjYIE\nCXzRTI" +
       "dF6LeMebiTKW0Q42G31xrsyjxFgJkv840G1GuPPbLmqVAMrlFNA/ENKDuNTb" +
       "0LG2vWmNi+\nbvM7LlvWYVHRdrBNQRkl01mZQYk43intcXe+3dHjNkhyeZ4g" +
       "2zVW43WXSeqMsxw2OyxlifU8i0Af\nScDJBF33uUFid+CMJDrzRQPjHaqTaT" +
       "qoZgXSQkb+YDAxBavTN3temxNak1rRNxGwKPi8KS3aWmGR\nM19uZUkT2+pK" +
       "IXM+t4zq2cwN7IEwgDzRtQlZZ4MRvuaLYNsgJ7terMTFWlRlZFZjQKUnTYa5" +
       "QhYL\naN1qsEsdnZCUb1le38I2NKyOCDSR+1SEI6NyZ7EmGKTl2ZBsFZ2RPo" +
       "WVBuytR0S8cbdpDWqSWNgm\ndGUgrid6sbRZGnUYbLNV/Uk6HC8wHi9fZxd5" +
       "OqgLkCZaI7y1RSZJhkZFgsi8bGAiZPGCCu8gurY2\n46myDRadJge6dhEpci" +
       "YGOyKCpWjWLx2SD5aDdBouKHmH9Q2zTst+WSLq3f620eSVsm700YLCObTD\n" +
       "wEqtPWmB3i5MKRVr4qxH+CE35+h5asTLOQgzzLYVeZuRCI/HDdVwJul2Lbdd" +
       "l0dddhlsYGo9VRZk\nsfb7djZs1/Q5ghO61B+URYrTZiiZIk5dG/QlNicD36" +
       "BC3hWNwljBMC9sKXmFUHk6h3bKOlzPpNVY\nGiEZEvNZBDktqBZGuQ5SbOo5" +
       "QZQR2hhdcWrTW9sUiYg85rmpMGaGLC9PyfncCVrbXgdsGr3mjGC1\nNI1ajU" +
       "iZRBsQWYwgSKuVCVnLCLPdsQk2ECZRkRU9qKdbbhcWo06S8Sw+Tcv9J8m7Rv" +
       "nWCRdruHD7\nY6XbKt/+eutooyY7PQ39WausmzIL+obehcGZJmUOJiUwRQ7Y" +
       "OszXuxNZ81x14ZE9Lh6yTIE56sb2\n4om/8GKHMHuTRkAaI15i25mugw4m1F" +
       "SjTMG824ztdiP0ue4KbVCGmK82RqBMxgheIPB6CFvuwtqV\nZU1YT+Jshunk" +
       "fKNo6mCA6XZOuV1QG8qZqdcyIe94GTZadPmAo6eeXO8IWTfvr+22sOvN7GLd" +
       "7tcJ\nuEGlWjGhQGZGCFlr0eitwgR013zSSzWNsTernUzHtXSXElKOMOpsx7" +
       "gjy8nmyNZL+4ERIlvdZ4wS\nrx1BLXrmdA6uGqhpLIigKNZE12g2O7S2q4+m" +
       "geHmiFG+vtakEbwbt1r1wlfqIkfqCWmwYQzLbI/o\nrpqioo80BAYlaED08t" +
       "Z4G8oCbxKom8udnOsuYmFW7zUovawVuQLXtGHX38WEMhk0s/6kTNYFJfN1\n" +
       "nifsaOFbXE9bix1l0UVYVs5yr+KPVWPeG8mjUB10mvNIi3sZ1fJ27gBUa9NA" +
       "b/LktJhgqR2BGRiT\n6x2e9+IiBFvQdowV3UYum0J5fHnPe6qDzeroSHdjf+" +
       "A8YXrLk1zVMdwfg/YnrxePqIdTduKpY9oi\nAp75frTr/jD2Kv8f1z8sfen9" +
       "F49oDCoBHk384F2OlmnOKaNxXsl8zzIfH/Mf5577xxH8uQ89jNJ4\n7geOvK" +
       "fcyN5OXzKtP7sIXDqhFB6gue8fdPd+IqEWaUkaeev76IRnT+iEx44ohctHdM" +
       "Llc3TCCTfw\n4g/keH4oASQlwFvL0Nw+ZUNvH9jQ26fn1XsnVt0svz9efq8d" +
       "WXXtYVY9hOE4w0T8aOaaCfCYoSXH\nPj0mL25WhO6eb6BO1nG/5RUd886KfT" +
       "uy/MKBDTQeZAPffStMpdgKUz/RXjoQdbcy31JvVW6yvMy3\ntYGmnyFEX3r5" +
       "1gcT04rvPNyPL71890MvB/9vAYsT4MnvZ8l+ABEclJAJcLmy+5wjqpDdeNAR" +
       "7/1h\njjgQVGc9YSXVym+97/2rWycLfDD2+6X85I8Y9lcS4NrxpNX9B/OKKd" +
       "v7t2JGn3zgf7XDvz/K869/\n4KUvBjf+fE8+n/xDc5UArump45yl9c60rwaR" +
       "plv7ea8eSL7D4j6WAG+8/4+DBKid3uwN/+hB9OcT\n4NJRwvtEcAzSp05AOi" +
       "yrbeRJzglY8/8Du2W6vyEcAAA=");
}
