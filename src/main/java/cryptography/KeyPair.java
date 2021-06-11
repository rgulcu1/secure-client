package cryptography;

import lombok.Getter;

@Getter
public class KeyPair {

    private PublicKey publicKey;

    private PrivateKey privateKey;


    public KeyPair(final PublicKey publicKey, final PrivateKey privateKey) {
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }
}
