package cryptography;
import lombok.Getter;
import util.Helper;

import java.math.BigInteger;

@Getter
public class PublicKey {

    private BigInteger n;

    private BigInteger e;


    public PublicKey(final BigInteger n, final BigInteger e) {
        this.n = n;
        this.e = e;
    }

    public PublicKey(String n, String e) {
        this.n = new BigInteger(n,16);
        this.e = new BigInteger(e,16);;
    }


    public String encrypt(final String plainText) {

        BigInteger dataAsDecimal = new BigInteger(plainText, 16);
        BigInteger cipherAsDecimal = Helper.modForBigNumbers(dataAsDecimal, e, n);
        return cipherAsDecimal.toString(16).toUpperCase();
    }

    public String decrypt(final String cipherText) {
        BigInteger cipherAsDecimal = new BigInteger(cipherText, 16);
        BigInteger plainAsDecimal = Helper.modForBigNumbers(cipherAsDecimal, e, n);
        return plainAsDecimal.toString(16).toUpperCase();
    }


}
