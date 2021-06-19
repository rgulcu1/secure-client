package server;

import cryptography.AES;
import cryptography.PublicKey;
import cryptography.SymmetricKey;
import org.apache.commons.io.FilenameUtils;
import org.json.JSONObject;
import user.User;
import util.Constants;
import util.Helper;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Objects;

import static util.Constants.RequestType.*;

public class MainService {

    ServerComm serverComm = new ServerComm();
    User user;
    final PublicKey serverPublicKey = new PublicKey(new BigInteger(Constants.SERVER_PUBLIC_KEY_N, 16), new BigInteger(Constants.SERVER_PUBLIC_KEY_E, 16));

    public Boolean register(String username, String password) {

        user = new User(username);
        final JSONObject userJson = prepareRegisterRequest(username, password);
        final String response = serverComm.communicateWithServer(userJson.toString());

        final String userNameConcatPublicKey = user.getUserName() + user.getKeyPair().getPublicKey().getE().toString(16).toUpperCase()
                + user.getKeyPair().getPublicKey().getN().toString(16).toUpperCase();
        //TODO add response check

        //if(Objects.isNull(response)) return false;
        return true;//serverPublicKey.verifyDigitalSignature(response, userNameConcatPublicKey);
    }

    public Boolean login(String username, String password) {

        user = new User(username);
        final JSONObject userJson = prepareLoginRequest(username, password);
        final String response = serverComm.communicateWithServer(userJson.toString());

        final String userNameConcatPublicKey = user.getUserName() + user.getKeyPair().getPublicKey().getE().toString(16).toUpperCase()
                + user.getKeyPair().getPublicKey().getN().toString(16).toUpperCase();
        //TODO add response check
        //if(Objects.isNull(response)) return false;
        return true;//serverPublicKey.verifyDigitalSignature(response, userNameConcatPublicKey);
    }

    public Boolean postImage(File file) {

        SymmetricKey k1 = new SymmetricKey(128);
        final byte[] bytes = extractBytes(file);
        final String[] imageAsBytes = Helper.byteToStringArray(bytes);

        final String[] cipherTextOfImage = AES.streamCipherEncryption(imageAsBytes, k1, Constants.Method.CBC);
        String encryptedImageAsString = String.join("", cipherTextOfImage);
        final String IV = AES.getIVAsString();
        final String imageDigitalSignature = createImageDigitalSignature(imageAsBytes);
        final String encryptedAESKey = serverPublicKey.encrypt(k1.getSymetricKeyAsHex());

        final JSONObject postImageRequest = preparePostImageRequest(file.getName().split(".")[0], encryptedImageAsString, imageDigitalSignature, encryptedAESKey, IV);
        final String response = serverComm.communicateWithServer(postImageRequest.toString());
        //TODO add response check
        return true;
    }

    public Boolean askForNewImage() {
        final JSONObject notifyReq = prepareNotificationRequest();
        final String response = serverComm.communicateWithServer(notifyReq.toString());
        //TODO add response check

        return true;
    }

    public File displayImage(String imageName) {
        final JSONObject displayReq = prepareDisplayRequest(imageName);
        final String response = serverComm.communicateWithServer(displayReq.toString());
        //TODO add response check

        return null;
    }

    private byte[] extractBytes (File imgPath)  {
        // open image
        BufferedImage bufferedImage = null;
        try {
            bufferedImage = ImageIO.read(imgPath);

            // get DataBufferBytes from Raster
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, FilenameUtils.getExtension(imgPath.toString()), bos );
            return bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String createImageDigitalSignature(String[] imageByte) {
        String str = String.join("", imageByte);
        final String hashedImage = Helper.hashWithSHA256(str);
        return user.getKeyPair().getPrivateKey().encrypt(hashedImage);
    }

    private JSONObject preparePostImageRequest(String imageName, String encryptedImage, String digitalSignature, String AESKey, String IV){

        final JSONObject imageJson = new JSONObject()
                .put("imageName", imageName)
                .put("requestType", POST_IMAGE)
                .put("key", AESKey)
                .put("image", encryptedImage)
                .put("digitalSignature", digitalSignature)
                .put("IV", IV);

        return imageJson;
    }

    private JSONObject prepareRegisterRequest(String username, String password) {

        final JSONObject userJson = new JSONObject()
                .put("username", username)
                .put("password", Helper.hashWithSHA256(password))
                .put("requestType", REGISTER);

        final JSONObject userPublicKey = new JSONObject()
                .put("n", user.getKeyPair().getPublicKey().getN().toString(16).toUpperCase())
                .put("e", user.getKeyPair().getPublicKey().getE().toString(16).toUpperCase());
        userJson.put("publicKey", userPublicKey);

        return userJson;
    }

    private JSONObject prepareLoginRequest(String username, String password) {

        return new JSONObject()
                .put("username", username)
                .put("password", Helper.hashWithSHA256(password))
                .put("requestType", LOGIN);
    }

    private JSONObject prepareNotificationRequest() {

        return new JSONObject()
                .put("requestType", NOTIFICATION);
    }

    private JSONObject prepareDisplayRequest(String imageName) {

        return new JSONObject()
                .put("requestType", DISPLAY)
                .put("imageName", imageName);
    }
}
