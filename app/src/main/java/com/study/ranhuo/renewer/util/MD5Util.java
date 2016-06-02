package com.study.ranhuo.renewer.util;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by ranhuo on 16/6/1.
 */
public class MD5Util {
    public static String Md5(String originalKey){
        String md5Code = null;
        try{
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(originalKey.getBytes());
            md5Code = bytes2HexString(mDigest.digest());
        }catch (NoSuchAlgorithmException e){
            md5Code = String.valueOf(originalKey.hashCode());
        }
        return md5Code;
    }


    public static String bytes2HexString(byte[] bytes){
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < bytes.length; i++){
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if(hex.length() == 1){
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }
}
