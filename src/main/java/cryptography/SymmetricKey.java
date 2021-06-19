package cryptography;

import java.math.BigInteger;
import java.security.SecureRandom;

public class SymmetricKey{


    private String symetricKey;

    private int bitSize;

    public int getBitSize() {
        return bitSize;
    }

    public SymmetricKey(int bitsize) {

        this.bitSize = bitsize;
        this.symetricKey = generateSymmetricKey(bitsize);
    }

    public SymmetricKey(String symetricKey) {

        this.symetricKey = new BigInteger(symetricKey, 16).toString(2);
    }

    private String generateSymmetricKey(int bitsize) {


        SecureRandom secureRandom = new SecureRandom();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < bitsize ; i++) {
            if (secureRandom.nextBoolean()) {
                sb.append("1");
            }
            else {
                sb.append("0");
            }
        }

        return sb.toString();
    }

    public String getSymetricKeyAsBinary() {
        return symetricKey;
    }

    public String getSymetricKeyAsHex() {

        BigInteger integerValue = new BigInteger(this.symetricKey, 2);
        String hexStr = integerValue.toString(16);

        int differ = (this.symetricKey.length() / 4) - hexStr.length();

        if(differ != 0) {
            StringBuilder stringBuilder = new StringBuilder();
            while(differ != 0) {
                stringBuilder.append("0");
                differ--;
            }
            stringBuilder.append(hexStr);

            return stringBuilder.toString().toUpperCase();
        }

        return hexStr.toUpperCase();
    }

    public String encrypt(final String plainText) {
        return null;
    }

    public String decrypt(final String cipherText) {
        return null;
    }
}
