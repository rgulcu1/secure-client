package server;

import cryptography.PublicKey;
import org.json.JSONObject;
import user.User;
import util.Constants;
import util.Helper;

import java.math.BigInteger;

import static util.Constants.RequestType.REGISTER;

public class MainService {

    ServerComm serverComm = new ServerComm();
    User user;
    final PublicKey serverPublicKey = new PublicKey(new BigInteger(Constants.SERVER_PUBLIC_KEY_N, 16), new BigInteger(Constants.SERVER_PUBLIC_KEY_E, 16));

    public Boolean register(String username) {

        user = new User(username);
        final JSONObject userJson = new JSONObject()
                .put("username", username)
                .put("requestType", REGISTER);

        final JSONObject userPublicKey = new JSONObject()
                .put("n", user.getKeyPair().getPublicKey().getN().toString(16).toUpperCase())
                .put("e", user.getKeyPair().getPublicKey().getE().toString(16).toUpperCase());
        userJson.put("publicKey", userPublicKey);
        final String response = serverComm.communicateWithServer(userJson.toString());

        final String userNameConcatPublicKey = user.getUserName() + user.getKeyPair().getPublicKey().getE().toString(16).toUpperCase()
                + user.getKeyPair().getPublicKey().getN().toString(16).toUpperCase();

        return serverPublicKey.verifyDigitalSignature(response, userNameConcatPublicKey);
    }
}
