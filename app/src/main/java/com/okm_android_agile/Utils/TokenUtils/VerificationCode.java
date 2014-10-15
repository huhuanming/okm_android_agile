package com.okm_android_agile.Utils.TokenUtils;

import android.util.Base64;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by hu on 14/9/28.
 */
public class VerificationCode {

    public String code;

    /**
     * Get a AccessToken Instance with the token and the key
     * @param theCode  theCode the code is mobile verification code
     * @return VerificationCode Instance
     */
    public VerificationCode(String theCode){
        this.code = theCode;
    }


    /**
     * Build encryption code by the code. default token's life is 300000ms.
     * @return it is used to authenticated.
     */

    public String encryptionCode(){
        return encryptionCode(300000);
    }

    /**
     *  Build a encryption code by the token and key. default token‘s life is 300s.
     *  @param ttl it is time for token's life
     *  @return it is used to authenticated.
     */
    public String encryptionCode(long ttl){
        if (code.isEmpty()){
            return "empty code";
        }
        HashMap<String,Object> info = new HashMap<String, Object>();
        info.put("deadline", Long.valueOf(System.currentTimeMillis()+ttl)/1000);
        info.put("device", Integer.valueOf(2));
        String encoded = new JSONObject(info).toString();
        try {
            encoded = URLEncoder.encode(encoded, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String encoded_sign = new String();
        try {
            encoded_sign = encodeBySHA1(encoded);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try {
            encoded_sign = URLEncoder.encode(encoded_sign,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return new StringBuilder(encoded_sign).append(":").append(encoded).toString();
    }

    private String encodeBySHA1(String obj) throws InvalidKeyException, NoSuchAlgorithmException {
        byte[] theKey = code.getBytes();
        SecretKeySpec signingKey = new SecretKeySpec(theKey, "HmacSHA1");
        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(signingKey);
        byte[] rawHmac = mac.doFinal(obj.getBytes());
        return Base64.encodeToString(rawHmac, Base64.DEFAULT).replaceAll("[\n\r]", "");
    }

}
