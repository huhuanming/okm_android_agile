package com.okm_android.main.Model;

/**
 * Created by chen on 14-10-5.
 */
public class RegisterBackData {

    public String name;

    public String user_id;

    public String last_login_at;

    public Access_token access_token;

    public static class Access_token{
        public String token;
        public String key;

    }
}
