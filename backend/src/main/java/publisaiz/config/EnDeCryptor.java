package publisaiz.config;

import org.springframework.stereotype.Component;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import static javax.crypto.Cipher.*;

@Component
class EnDeCryptor {

    private static final String ALGORITHM = "AES";
    private static final String MY_ENCRYPTION_KEY = "Th1sjsF4unc%tion";
    private static final String UNICODE_FORMAT = "UTF8";


    public static String encrypt(String valueToEnc) throws UnsupportedEncodingException, NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, IllegalBlockSizeException, InvalidKeyException {
        Key key = generateKey();
        Cipher c = getInstance(ALGORITHM);
        c.init(ENCRYPT_MODE, key);
        byte[] encValue = c.doFinal(valueToEnc.getBytes());
        return new String(Base64.getEncoder().encode(encValue));
    }

    public static String decrypt(String encryptedValue)
            throws IOException, NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, IllegalBlockSizeException, InvalidKeyException {
        Key key = generateKey();
        Cipher c = getInstance(ALGORITHM);
        c.init(DECRYPT_MODE, key);
        byte[] decordedValue = Base64.getDecoder().decode(encryptedValue);
        byte[] decValue = c.doFinal(decordedValue);
        return new String(decValue);
    }

    private static Key generateKey() throws UnsupportedEncodingException {
        byte[] keyAsBytes = MY_ENCRYPTION_KEY.getBytes(UNICODE_FORMAT);
        return new SecretKeySpec(keyAsBytes, ALGORITHM);
    }
}
