/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatapplication;

import java.security.MessageDigest;
import java.util.Arrays;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author chaitanya
 */
public class aesEncryption {
    public static void main(String[] args) throws Exception {
    String username = "bob@google.org";
    String password = "Password1";
    String secretID = "BlahBlahBlah";
    String SALT2 = "deliciously salty";

    // Get the Key
  

    // Need to pad key for AES
    // TODO: Best way?

    // Generate the secret key specs.
     byte[] key = (SALT2 + username + password).getBytes("UTF-8");
MessageDigest sha = MessageDigest.getInstance("SHA-1");
key = sha.digest(key);
key = Arrays.copyOf(key, 32); // use only first 128 bit
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");

    // Instantiate the cipher
        Cipher cipher = Cipher.getInstance("AES");
    cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
 
    

    byte[] encrypted = cipher.doFinal((secretID).getBytes("UTF-8"));
    System.out.println("encrypted string: " + encrypted.toString());

    cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
    byte[] original = cipher.doFinal(encrypted);
    String originalString = new String(original);
    System.out.println("Original string: " + originalString + "\nOriginal string (Hex): " + original);
}
    
}
