package com.okm_android.main.Utils.TokenUtils;

import android.util.Base64;
import android.util.Log;

import org.json.JSONObject;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by hu on 14/8/13.
 */
public class QiniuToken {
    public String scope;
    public String accessKey;
    public String secretKey;

    public QiniuToken(String theScope, String theAccessKey, String theSecretKey){
        this.scope = theScope;
        this.accessKey = theAccessKey;
        this.secretKey = theSecretKey;
    }

    public String uploadToken(){
        return uploadToken(300000);
    }

    public String uploadToken(long ttl){
        if (scope.isEmpty() || accessKey.isEmpty() || secretKey.isEmpty()){
            return "empty scope or accesskey or secretkey";
        }
        HashMap<String,Object> info = new HashMap<String, Object>();

        info.put("scope", scope);
        info.put("deadline", Long.valueOf(System.currentTimeMillis()+ttl)/1000);

        String encoded = urlsafe(Base64.encodeToString(new JSONObject(info).toString().getBytes(),Base64.DEFAULT).replaceAll("[\n\r]", ""));
        String encoded_sign = null;
        try {
            encoded_sign = encodeBySHA1(encoded);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        encoded_sign = urlsafe(encoded_sign);
        return new StringBuilder(accessKey).append(":").append(encoded_sign).append(":").append(encoded).toString();
    }

    private String encodeBySHA1(String obj) throws InvalidKeyException, NoSuchAlgorithmException {
        byte[] theKey = secretKey.getBytes();
        SecretKeySpec signingKey = new SecretKeySpec(theKey, "HmacSHA1");
        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(signingKey);
        byte[] rawHmac = mac.doFinal(obj.getBytes());
        return Base64.encodeToString(rawHmac, Base64.DEFAULT).replaceAll("[\n\r]", "");
    }

    public String urlsafe(String obj){
        return obj.replace("+","-").replace("/","_");
    }
}
