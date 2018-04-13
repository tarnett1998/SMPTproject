import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class EncryptDecrypt {
    public static void main(String[] args) {

        String toEncrypt = "";

        for (int i = 0; i < args.length; i++) {
            toEncrypt += args[i] + " ";
        }

        if(toEncrypt!="") {

            try {

                SecretKey secKey = getSecretEncryptionKey();
                byte[] cipherText = encryptText(toEncrypt, secKey);
                String decryptedText = decryptText(cipherText, secKey);
                System.out.println("Text to be encrypted: " + toEncrypt);
                System.out.println("\nDecrypted text: " + decryptedText);
                System.exit(0);

            } catch (Exception e) {}
            
        } else {

            System.out.println("No text entered, aborting ...");
            System.exit(0);

        }
    }

    public static SecretKey getSecretEncryptionKey() throws Exception {

        KeyGenerator generator = KeyGenerator.getInstance("AES");
        generator.init(128);
        SecretKey secKey = generator.generateKey();
        return secKey;

    }

    public static byte[] encryptText(String toEncrypt, SecretKey secKey) throws Exception {

        Cipher aesCipher = Cipher.getInstance("AES");
        aesCipher.init(Cipher.ENCRYPT_MODE, secKey);
        byte[] byteCipherText = aesCipher.doFinal(toEncrypt.getBytes());
        return byteCipherText;

    }

    public static String decryptText(byte[] byteCipherText, SecretKey secKey) throws Exception {
        
        Cipher aesCipher = Cipher.getInstance("AES");
        aesCipher.init(Cipher.DECRYPT_MODE, secKey);
        byte[] bytePlainText = aesCipher.doFinal(byteCipherText);
        return new String(bytePlainText);

    }

}