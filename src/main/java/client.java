import cryptography.KeyPair;
import cryptography.RSA;
import gui.MainPage;
import org.apache.commons.codec.DecoderException;
import server.ServerComm;
import util.Helper;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;
import org.apache.commons.codec.binary.Hex;



public class client {


    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Helper.calculateFirstFewPrime();
        MainPage mainPage = new MainPage("sa");
        mainPage.setVisible(true);
        mainPage.setSize(1000, 700);
        mainPage.setLocationRelativeTo(null);
        mainPage.setDefaultCloseOperation(EXIT_ON_CLOSE);

       /* String ss = "{sa: naber}";

        final KeyPair keyPair = RSA.generateKeyset();

        final String plain = toHex(ss);

        final String encrypt = keyPair.getPrivateKey().encrypt(plain);
        final String decrypt = keyPair.getPublicKey().decrypt(encrypt);

        byte[] bytes = new byte[0];
        try {
            bytes = Hex.decodeHex(decrypt.toCharArray());
        } catch (DecoderException e) {
            e.printStackTrace();
        }
        System.out.println(new String(bytes, "UTF-8"));*/


    }
}
