package com.home.amirraza.chatsampleusingfirebase;

import android.app.Application;

import com.firebase.client.Firebase;

/**
 * Created by AmirRaza on 9/11/2015.
 */
public class ChatApp extends Application {

    private static String userNum;

    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
    }

    public static void setUserNum(String userName) {
        ChatApp.userNum = userName;
    }

    public static String getUserNum() {
        return userNum;
    }
}
