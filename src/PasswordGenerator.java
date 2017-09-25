
import java.security.MessageDigest;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author alberto
 */
public enum PasswordGenerator {
    
    INSTANCE;
    
    public static String encrypt(String mac) throws Exception {
        final MessageDigest md = MessageDigest.getInstance("md5");
        final byte[] hash = md.digest("MiLiveboxApp".getBytes("utf-8"));
        byte[] key = new byte[hash.length + 8];
        
        for(int i=0;i<key.length;i++){
            key[i] = hash[i % hash.length];
        }
        
        final SecretKey sKey = new SecretKeySpec(key, "DESede");
        final Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, sKey);

        final byte[] plainTextBytes = (PasswordGenerator.MacFormat(mac)).getBytes("utf-8");
        
        final byte[] cipherText = cipher.doFinal(plainTextBytes);
        final String encodedCipherText = Base64.getEncoder().encodeToString(cipherText);
        
        return encodedCipherText;
    }
    
    public static String MacFormat(String mac){
        String formattedMac = mac.replaceAll(":", "");
        
        return formattedMac;
    }
    
    public static String GenerateAuth(String mac) throws Exception{
        String auth = "UsrAdmin:" + PasswordGenerator.encrypt(mac);
        
        return auth;
    }
    
    public static String GenerateBasicAuth(String mac) throws Exception{
        String auth = PasswordGenerator.GenerateAuth(mac);
        auth = Base64.getEncoder().encodeToString(auth.getBytes());
        
        return auth;
    }
    
}
