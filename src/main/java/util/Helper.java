package util;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import static util.Constants.SIEVE_OF_ERATOSTHENES_N;
import static util.Constants.firstFewPrime;

public class Helper {

    public static void calculateFirstFewPrime(){ // use Sieve of Eratosthenes

        boolean[] isNotPrime = new boolean[SIEVE_OF_ERATOSTHENES_N + 1];

        for (int i = 2; i <= Math.sqrt(SIEVE_OF_ERATOSTHENES_N ) ; i++) {

            if (!isNotPrime[i]) {
                for (int j = i*i; j <= SIEVE_OF_ERATOSTHENES_N ; j+=i) {
                    isNotPrime[j] = true;
                }
            }
        }

        for (int i = 2; i <= SIEVE_OF_ERATOSTHENES_N ; i++) {

            if (!isNotPrime[i]) firstFewPrime.add(i);
        }

    }

    //it calculates x^y mod p
    public static BigInteger modForBigNumbers(BigInteger x, BigInteger y, BigInteger p) {

        BigInteger res = BigInteger.ONE; // Initialize result

        //Update x if it is more than or
        // equal to p
        x = x.mod(p);

        while (y.compareTo(BigInteger.ZERO) == 1) {

            // If y is odd, multiply x with result
            if ((y.mod(BigInteger.valueOf(2))).equals(BigInteger.ONE))
                res = res.multiply(x).mod(p);

            // y must be even now
            y = y.divide(BigInteger.TWO); // y = y/2
            x = x.pow(2).mod(p);
        }

        return res;
    }

    public static String hashWithSHA256(String data) {

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(data.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(digest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if(hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString().toUpperCase();
    }

    public static BigInteger nextRandomBigInteger(BigInteger n) {
        Random rand = new Random();
        BigInteger result = new BigInteger(rand.nextInt(n.bitLength()), rand);
        while( result.compareTo(n.subtract(BigInteger.valueOf(2))) >= 0 ) {
            result = new BigInteger(n.bitLength(), rand);
        }
        return result;
    }

}
