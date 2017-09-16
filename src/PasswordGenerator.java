
import java.security.MessageDigest;
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
        
        /*
        System.out.println("KEY: " + key);
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < key.length; ++i) {
            sb.append(Integer.toHexString((key[i] & 0xFF) | 0x100).substring(1,3));
        }
        System.out.println("KEY: " + sb);
        */
        
        
        final SecretKey sKey = new SecretKeySpec(key, "DESede");
        final Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, sKey);

        final byte[] plainTextBytes = (PasswordGenerator.MacFormat(mac)).getBytes("utf-8");
        
        final byte[] cipherText = cipher.doFinal(plainTextBytes);
        final String encodedCipherText = new sun.misc.BASE64Encoder().encode(cipherText);
        
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
        auth = new sun.misc.BASE64Encoder().encode(auth.getBytes());
        
        return auth;
    }
    
}
