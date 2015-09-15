package com.myfp.myfund.utils;

import java.io.IOException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import Decoder.BASE64Decoder;
import Decoder.BASE64Encoder;

public class DESEncrypt {
	private static final String DES="DES";
	private static final String key="01234567";
	 /** 
     * Description 根据键值进行加密 
     * @param data  
     * @param key  加密键byte数组 
     * @return 
     * @throws Exception 
     */  
    public static String encrypt(String data) throws Exception {  
    	
        byte[] bt = encrypt(data.getBytes(), key.getBytes());  
        String strs = new BASE64Encoder().encode(bt);  
        return strs;  
    }  
   
    /** 
     * Description 根据键值进行解密 
     * @param data 
     * @param key  加密键byte数组 
     * @return 
     * @throws IOException 
     * @throws Exception 
     */  
    public static String decrypt(String data) throws IOException,  Exception {  
        if (data == null)  
            return null;  
        BASE64Decoder decoder = new BASE64Decoder();  
        byte[] buf = decoder.decodeBuffer(data);  
        byte[] bt = decrypt(buf,key.getBytes());  
        return new String(bt);  
    }  
   
    /** 
     * Description 根据键值进行加密 
     * @param data 
     * @param key  加密键byte数组 
     * @return 
     * @throws Exception 
     */  
    private static byte[] encrypt(byte[] data, byte[] key) throws Exception {  
        // 生成一个可信任的随机数源  
        SecureRandom sr = new SecureRandom();  
   
        // 从原始密钥数据创建DESKeySpec对象  
        /*DESKeySpec dks = new DESKeySpec(key);  
   
        // 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象  
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);  
        SecretKey securekey = keyFactory.generateSecret(dks);*/  
   
        SecretKeySpec securekey = new SecretKeySpec(key, "DES");

        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        // Cipher对象实际完成加密操作  
        //Cipher cipher = Cipher.getInstance(DES);  
   
        // 用密钥初始化Cipher对象  
        cipher.init(Cipher.ENCRYPT_MODE, securekey);  
   
        return cipher.doFinal(data);  
    }  
       
       
    /** 
     * Description 根据键值进行解密 
     * @param data 
     * @param key  加密键byte数组 
     * @return 
     * @throws Exception 
     */  
    private static byte[] decrypt(byte[] data, byte[] key) throws Exception {  
        // 生成一个可信任的随机数源  
        SecureRandom sr = new SecureRandom();  
   
        // 从原始密钥数据创建DESKeySpec对象  
       /* DESKeySpec dks = new DESKeySpec(key);  
   
        // 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象  
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);  
        SecretKey securekey = keyFactory.generateSecret(dks); */ 
        
        SecretKeySpec securekey = new SecretKeySpec(key, "DES");

        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        
        // Cipher对象实际完成解密操作  
       // Cipher cipher = Cipher.getInstance(DES);  
   
        // 用密钥初始化Cipher对象  
        cipher.init(Cipher.DECRYPT_MODE, securekey);  
   
        return cipher.doFinal(data);  
    }  

}
