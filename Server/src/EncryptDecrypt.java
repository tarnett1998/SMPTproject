import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.util.Arrays;

/**
 * Work in progress.
 */

@SuppressWarnings("deprecated")
class EncryptDecrypt {

    public static void main(String[] args) {

        StringBuilder ss = new StringBuilder();

        for (String arg : args) {
            ss.append(arg).append(" ");
        }

        if(!ss.toString().equals("")) try {

            SecretKey secKey = getSecretEncryptionKey();
            byte[] cipherText = encryptText(ss.toString(), secKey);
            String hex = bytesToHex(cipherText);
            byte[] ba = hexStringToByteArray(hex);
            String decryptedText = decryptText(ba, secKey);
            System.out.println("\n\nText to be encrypted: " + ss);
            System.out.println("\n\nKey: " + bytesToHex(secKey.getEncoded()));
            System.out.println("\n\nEncrypted text: " + bytesToHex(cipherText));
            System.out.println("\n\nDecrypted text: " + decryptedText);

        } catch (Exception ignored) {}
        else System.out.println("No text entered, aborting ...");
    }

    private static SecretKey getSecretEncryptionKey() throws Exception {

        String sss = "BorderlineSociopath"; // We can decide om=n the string later.
        byte[] key = sss.getBytes("UTF-8"); // Coverting it into a byte array. Because Secret keys are basically byte arrays.

        MessageDigest sha = MessageDigest.getInstance("SHA-1"); // Trimming stuff here.
        key = sha.digest(key); // updating the key.
        key = Arrays.copyOf(key, 16); // use only first 128 bit

        return new SecretKeySpec(key, "AES"); // returning the secret key here.

    }

    private static byte[] encryptText(String toEncrypt, SecretKey secKey) throws Exception {

        Cipher aesCipher = Cipher.getInstance("AES"); // Cipher created here.
        aesCipher.init(Cipher.ENCRYPT_MODE, secKey); // Initiated here.
        return aesCipher.doFinal(toEncrypt.getBytes()); // Encrypts the shit you put in.

    }

    private static String decryptText(byte[] byteCipherText, SecretKey secKey) throws Exception {
        /*
          For Decrypting.
         */
        Cipher aesCipher = Cipher.getInstance("AES");
        aesCipher.init(Cipher.DECRYPT_MODE, secKey);
        byte[] bytePlainText = aesCipher.doFinal(byteCipherText);
        return new String(bytePlainText);

    }

    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();
    private static String bytesToHex(byte[] bytes) {
        /*
          Don't worry about this shit as it was just for demonstration.
         */
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    private static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

}