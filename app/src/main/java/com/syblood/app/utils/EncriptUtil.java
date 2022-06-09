package com.syblood.app.utils;

import com.syblood.app.application.CrashHandler;

import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * 各种加密解密相关的方法
 * Created by xw on 2021/8/3.
 */
public class EncriptUtil
{
    /**
     * 加密
     *
     * @param encData   待加密数据
     * @param secretKey 密钥
     * @param vector    向量
     * @return
     * @throws Exception
     */
    public static String encryptAES(String encData, String secretKey, String vector)
    {
        if (secretKey == null)
        {
            return null;
        }
        if (secretKey.length() != 16)
        {
            return null;
        }
        try
        {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            byte[] raw = secretKey.getBytes();
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            IvParameterSpec iv = new IvParameterSpec(vector.getBytes());// 使用CBC模式，需要一个向量iv，可增加加密算法的强度
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
            byte[] encrypted = cipher.doFinal(encData.getBytes("utf-8"));
            return Base64Util.encode(encrypted);
        } catch (Exception ex)
        {
            CrashHandler.getInstance().io(ex);
            return null;
        }
    }

    /**
     * 解密
     *
     * @param sSrc      待解密数据
     * @param secretKey 密钥
     * @param vector    向量
     * @return
     * @throws Exception
     */
    public static String decryptAES(String sSrc, String secretKey, String vector) throws Exception
    {
        if (secretKey == null)
        {
            return null;
        }
        if (secretKey.length() != 16)
        {
            return null;
        }

        try
        {
            byte[] raw = secretKey.getBytes();
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec iv = new IvParameterSpec(vector.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] encrypted1 = Base64Util.decode(sSrc);// 先用base64解密
            // 先用base64解密
            byte[] original = cipher.doFinal(encrypted1);
            String originalString = new String(original, "utf-8");
            return originalString;
        } catch (Exception ex)
        {
            CrashHandler.getInstance().io(ex);
            return null;
        }
    }

    /**
     * byte转String
     *
     * @param bytes
     * @return
     */
    public static String encodeBytes(byte[] bytes)
    {
        StringBuffer strBuf = new StringBuffer();

        for (int i = 0; i < bytes.length; i++)
        {
            strBuf.append((char) (((bytes[i] >> 4) & 0xF) + ((int) 'a')));
            strBuf.append((char) (((bytes[i]) & 0xF) + ((int) 'a')));
        }

        return strBuf.toString();
    }

    /**
     * MD5加密
     *
     * @param paramString 待加密字符串
     * @return
     */
    public static String encryptMD5(String paramString)
    {
        char[] hexDigits = {48, 49, 50, 51, 52, 53, 54, 55,
                56, 57, 97, 98, 99, 100, 101, 102};

        byte[] paramArrayOfByte = paramString.getBytes();
        try
        {
            MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");
            localMessageDigest.update(paramArrayOfByte);
            byte[] arrayOfByte = localMessageDigest.digest();
            char[] arrayOfChar = new char[32];
            int i = 0;
            int j = 0;
            while (true)
            {
                if (i >= 16)
                    return new String(arrayOfChar);
                int k = arrayOfByte[i];
                int m = j + 1;
                arrayOfChar[j] = hexDigits[(0xF & k >>> 4)];
                j = m + 1;
                arrayOfChar[m] = hexDigits[(k & 0xF)];
                i++;
            }
        } catch (Exception localException)
        {
        }
        return null;
    }

}
