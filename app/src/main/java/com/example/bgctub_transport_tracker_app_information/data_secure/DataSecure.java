package com.example.bgctub_transport_tracker_app_information.data_secure;

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class DataSecure {


    //Data encryption with AES(Advanced Encryption System Algorithm in CBC mode)
    //AES is symmetric encryption system
    //used 16 byte key to generate 128
    //IV(Initialize Vector) also 16 byte

    private static final String KEY = "****";
    private static final String INITVECTOR = "****";

    //encoding process**
    public String encryption(String value) {
        try {

            // initialize vector, secret key, encode with AES/CBC
            IvParameterSpec ivParameterSpec = new IvParameterSpec(INITVECTOR.getBytes("UTF_8"));
            SecretKeySpec secretKeySpec = new SecretKeySpec(KEY.getBytes("UTF_8"), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);

            byte[] encryptedData = cipher.doFinal(value.getBytes("UTF_8"));

            return Base64.encodeToString(encryptedData, Base64.NO_WRAP);


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String dataEncode(String value) {
        if (value != null) {
            return encryption(value);
        } else {
            return value;
        }
    }

    //decoding process**

    public String decryption(String value) {
        try {

            // initialize vector, secret key, decode with AES/CBC with same key
            IvParameterSpec ivParameterSpec = new IvParameterSpec(INITVECTOR.getBytes("UTF_8"));
            SecretKeySpec secretKeySpec = new SecretKeySpec(KEY.getBytes("UTF_8"), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
            byte[] decryptedData = cipher.doFinal(Base64.decode(value, Base64.NO_WRAP));

            return new String(decryptedData);

        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String dataDecode(String value) {
        if (value != null) {
            return decryption(value);
        } else {
            return value;
        }
    }
}

