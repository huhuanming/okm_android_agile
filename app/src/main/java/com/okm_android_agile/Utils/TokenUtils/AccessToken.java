package com.okm_android_agile.Utils.TokenUtils;

import android.util.Base64;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.lang.String;
import java.lang.System;
import java.net.URLEncoder;
import java.util.HashMap;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by hu on 14/8/13.
 *
 * AccessToken is a generic class base NSObject that build a token which used to authenticated.
 * Subclasses can branched their custom attributes, or methods.
 *
 */

public class AccessToken {
    public String token;
    public String key;

    /**
     * Get a AccessToken Instance with the token and the key
     * @param theToken  theToken the token which belongs to user
     * @param theKey    thekey the key which belongs to user
     * @return AccessToken Instance
     */
    public AccessToken(String theToken, String theKey){
        this.token = theToken;
        this.key = theKey;
    }

    /**
     * Build a access token by the token and key. default token live is 300000ms.
     * @return it is used to authenticated.
     */

    public String accessToken(){
        return accessToken(300000);
    }

    /**
     *  Build a access token by the token and key. default token live is 300s.
     *  @param ttl it is time for token live
     *  @return it is used to authenticated.
     */
    public String accessToken(long ttl){
        if (token.isEmpty() || key.isEmpty()){
            return "empty token or key";
        }
        HashMap<String,Object> info = new HashMap<String, Object>();
        info.put("deadline", Long.valueOf(System.currentTimeMillis()+ttl)/1000);
        info.put("device", Integer.valueOf(2));
        String encoded = new JSONObject(info).toString();
        try {
            encoded = URLEncoder.encode(encoded,"UTF-8");
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

        return new StringBuilder(token).append(":").append(encoded_sign).append(":").append(encoded).toString();
    }

    private String encodeBySHA1(String obj) throws InvalidKeyException, NoSuchAlgorithmException {
        byte[] theKey = key.getBytes();
        SecretKeySpec signingKey = new SecretKeySpec(theKey, "HmacSHA1");
        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(signingKey);
        byte[] rawHmac = mac.doFinal(obj.getBytes());
        return Base64.encodeToString(rawHmac, Base64.DEFAULT).replaceAll("[\n\r]", "");
    }

}
