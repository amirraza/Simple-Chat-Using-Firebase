package com.home.amirraza.chatsampleusingfirebase;

import com.firebase.client.Firebase;

/**
 * Created by AmirRaza on 9/10/2015.
 */
public class FirebaseHandler {
    String firebaseRootRef = "https://chating-sample.firebaseio.com/";
    private Firebase firebase;
    private static FirebaseHandler firebaseHandler;
    private Firebase chatRef;
    private Firebase thisUser; //AppUser
    private Firebase otherUser;
    private Firebase conversations;
    private Firebase users;
    private Firebase loginStatus;
    private FirebaseHandler(){
        firebase = new Firebase(firebaseRootRef);
        initData();
    }

    private void initData() {
        users = firebase.child("users");
        chatRef = firebase.child("ChatUsers");
        loginStatus = firebase.child("loginStatus");
        conversations = firebase.child("conversations");

    }

    public static FirebaseHandler getInstance(){
        if(firebaseHandler==null){
            firebaseHandler = new FirebaseHandler();
        }
        return firebaseHandler;
    }

    public Firebase getChatRef() {
        return chatRef;
    }

    public Firebase getConversations() {
        return conversations;
    }

    public Firebase getUsers() {
        return users;
    }

    public Firebase getLoginStatus() {
        return loginStatus;
    }
}
