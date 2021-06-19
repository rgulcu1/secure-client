package server;

import cryptography.AES;
import cryptography.PublicKey;
import cryptography.SymmetricKey;
import org.apache.commons.io.FilenameUtils;
import org.json.JSONArray;
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
    final static PublicKey serverPublicKey = new PublicKey(new BigInteger(Constants.SERVER_PUBLIC_KEY_N, 16), new BigInteger(Constants.SERVER_PUBLIC_KEY_E, 16));

    public Boolean register(String username, String password) {

        user = new User(username);
        final JSONObject userJson = prepareRegisterRequest(username, password);
        final String response = serverComm.communicateWithServer(userJson.toString());

        if(Objects.isNull(response)) return false;
        return verifyRegisterResponse(response);
    }

    public Boolean login(String username, String password) {

        user = new User(username);
        final JSONObject userJson = prepareLoginRequest(username, password);
        final String response = serverComm.communicateWithServer(userJson.toString());

        if(Objects.isNull(response)) return false;
        return handleStatusCode(response, 200);
    }

    public Boolean logout() {
        if (Objects.isNull(user)) return true;
        final JSONObject logoutJson = prepareLogoutRequest(user.getUserName());
        final String response = serverComm.communicateWithServer(logoutJson.toString());
        this.user=null;
        if(Objects.isNull(response)) return false;
        return handleStatusCode(response, 200);
    }

    public Boolean postImage(File file) {

        SymmetricKey k1 = new SymmetricKey(128);
        System.out.println("actual key :" + k1.getSymetricKeyAsHex());
        final byte[] bytes = extractBytes(file);
        final String[] imageAsBytes = Helper.byteToStringArray(bytes);
        final String[] cipherTextOfImage = AES.streamCipherEncryption(imageAsBytes, k1, Constants.Method.CBC);
        String encryptedImageAsString = String.join("", cipherTextOfImage);
        final String IV = serverPublicKey.encrypt(AES.getIVAsString());
        final String imageDigitalSignature = createImageDigitalSignature(imageAsBytes);
        final String encryptedAESKey = serverPublicKey.encrypt(k1.getSymetricKeyAsHex());

        final String[] split = file.getName().split("\\.");
        final JSONObject postImageRequest = preparePostImageRequest(split[0], encryptedImageAsString, imageDigitalSignature, encryptedAESKey, IV);
        final String response = serverComm.communicateWithServer(postImageRequest.toString());
        if(Objects.isNull(response)) return false;
        return handleStatusCode(response,200);
    }

    public JSONArray askForNewImage() {
        if(Objects.isNull(user)) return null;
        final JSONObject notifyReq = prepareNotificationRequest();
        final String response = serverComm.communicateWithServer(notifyReq.toString());
        if(Objects.isNull(response)) return null;
        return handleNotificationResponse(response);
    }

    public BufferedImage displayImage(String imageName) {
        final JSONObject displayReq = prepareDisplayRequest(imageName);
        final String response = serverComm.communicateWithServer(displayReq.toString());
        if(Objects.isNull(response)) return null;

        return handleDisplayResponse(response);
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

    private Boolean verifyRegisterResponse(String response) {

        if(!handleStatusCode(response,201)) return false;

        final JSONObject userJson = new JSONObject()
                .put("username", user.getUserName());

        final JSONObject userPublicKey = new JSONObject()
                .put("n", user.getKeyPair().getPublicKey().getN().toString(16).toUpperCase())
                .put("e", user.getKeyPair().getPublicKey().getE().toString(16).toUpperCase());
        userJson.put("publicKey", userPublicKey);

        final String expectedCertificate = Helper.decodeStringToHex(userJson.toString()).toUpperCase();

        final JSONObject jsonResponse = new JSONObject(response);
        final String encryptedCertificate = jsonResponse.getString("certificate");

        final String actualCertificate = serverPublicKey.decrypt(encryptedCertificate);

        if (expectedCertificate.equals(actualCertificate)) return true;

        System.err.println("Certificates are not match");
        return false;
    }

    private Boolean handleStatusCode(String response, Integer expectedStatus){

        final JSONObject jsonResponse = new JSONObject(response);

        final int statusCode = jsonResponse.getInt("statusCode");
        if (statusCode != expectedStatus) {
            final String message = jsonResponse.getString("message");
            if(!message.equals(""))System.err.println(message);
            return false;
        }
        return true;
    }

    private JSONArray handleNotificationResponse(String response){

        if(!handleStatusCode(response,200)) return null;
        final JSONObject responseJson = new JSONObject(response);
        return responseJson.getJSONArray("imageInfos");
    }

    private BufferedImage handleDisplayResponse(String response) {

        final JSONObject responseJson = new JSONObject(response);

        final String encryptedKey = responseJson.getString("key");
        System.out.println("encKey:" + encryptedKey);

        final String decryptedAESKey = user.getKeyPair().getPrivateKey().decrypt(encryptedKey);
        System.out.println("decryptedAESKey:" + decryptedAESKey);

        final String encryptedIV = responseJson.getString("IV");
        final String decryptedIV = user.getKeyPair().getPrivateKey().decrypt(encryptedIV);

        final String certificate = responseJson.getString("certificate");
        final String certificateOwner = responseJson.getString("owner");
        final String digitalSignature = responseJson.getString("digitalSignature");


        final PublicKey ownerPublicKey = getPublicKeyFromCertificate(certificate, certificateOwner);
        if(Objects.isNull(ownerPublicKey)) return null;
        final String decryptedImage = responseJson.getString("image");


        final String[] cipherImage = Helper.divideStringToStringArray(decryptedImage, 2);
        final String[] decryptedImageAsByte = AES.streamCipherDecryption(cipherImage, new SymmetricKey(decryptedAESKey), Constants.Method.CBC, decryptedIV);

        if(!verifyDigitalSignature(digitalSignature, ownerPublicKey, decryptedImageAsByte)) return null;

        return Helper.byteArrayToImage(decryptedImageAsByte);
    }

    private Boolean verifyDigitalSignature(String digitalSignature, PublicKey ownerPublicKey, String[] decryptedImage){

        String str = String.join("", decryptedImage);
        final String hashedImage = Helper.hashWithSHA256(str);

        final String decryptedSignature = ownerPublicKey.decrypt(digitalSignature);
        return hashedImage.equals(decryptedSignature);
    }


    private JSONObject preparePostImageRequest(String imageName, String encryptedImage, String digitalSignature, String AESKey, String IV){

        final JSONObject imageJson = new JSONObject()
                .put("imageName", imageName)
                .put("requestType", POST_IMAGE)
                .put("key", AESKey)
                .put("image", encryptedImage)
                .put("digitalSignature", digitalSignature)
                .put("owner", user.getUserName())
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

    private JSONObject prepareLogoutRequest(String username) {

        return new JSONObject()
                .put("username", username)
                .put("requestType", LOGOUT);
    }

    private JSONObject prepareNotificationRequest() {

        return new JSONObject()
                .put("requestType", NOTIFICATION)
                .put("username", user.getUserName());
    }

    private JSONObject prepareDisplayRequest(String imageName) {

        return new JSONObject()
                .put("requestType", DISPLAY)
                .put("imageName", imageName)
                .put("username", user.getUserName());
    }

    private static PublicKey getPublicKeyFromCertificate(String certificate, String owner) {

        final String decrypt = serverPublicKey.decrypt(certificate);

        final String certificateText = Helper.decodeHexToString(decrypt);

        final JSONObject jsonObject = new JSONObject(certificateText);

        final String certificateOwner = jsonObject.getString("username");
        if(!owner.equals(certificateOwner)) return null;

        final JSONObject publicKeyJson = jsonObject.getJSONObject("publicKey");
        final String n = publicKeyJson.getString("n");
        final String e = publicKeyJson.getString("e");

        return new PublicKey(n,e);
    }

    public String  getUsername(){
        return user.getUserName();
    }

}
