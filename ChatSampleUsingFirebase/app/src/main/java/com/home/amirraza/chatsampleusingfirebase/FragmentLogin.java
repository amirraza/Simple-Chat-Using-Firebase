package com.home.amirraza.chatsampleusingfirebase;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by AmirRaza on 9/13/2015.
 */
public class FragmentLogin extends Fragment {
    private ArrayList<String> matchNum,matchPass;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        matchNum = new ArrayList<>();
        matchPass = new ArrayList<>();
        FirebaseHandler.getInstance().getUsers().addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                matchNum.add(dataSnapshot.getKey());
                Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                matchPass.add(map.get("pass").toString());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login,container,false);

        final EditText regUser = (EditText) view.findViewById(R.id.regUser);
        final EditText regPass = (EditText) view.findViewById(R.id.regPass);

        Button regBtn = (Button) view.findViewById(R.id.regBtn);
        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                for(int i=0;i<matchNum.size();i++){
                    if( (("+92"+regUser.getText().toString()).equals(matchNum.get(i)) &&
                            (regPass.getText().toString().equals(matchPass.get(i))))) {
                        ChatApp.setUserNum(matchNum.get(i));

                        //for Login status
                        Map<String, Object> status = new HashMap<>();
                        status.put("isLogin","true");
                        FirebaseHandler.getInstance().getLoginStatus().child(ChatApp.getUserNum()).setValue(status);

                        //start fragment
                        ((FragmentActivity) getActivity()).getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, new FragmentFriendsList()).commit();
                        break;
                    }
                }
            }
        });

        TextView reg = (TextView) view.findViewById(R.id.reg);
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getFragmentManager();
                fm.beginTransaction().replace(R.id.container, new FragmentRegistered()).commit();
            }
        });

        return view;
    }
}
