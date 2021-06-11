package user;

import cryptography.KeyPair;
import cryptography.RSA;
import lombok.Getter;

@Getter
public class User {

    private String userName;

    private KeyPair keyPair;

    public User(String userName) {
        this.userName = userName;
        this.keyPair = RSA.generateKeyset();
    }
}
