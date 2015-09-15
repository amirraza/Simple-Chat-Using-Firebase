package com.home.amirraza.chatsampleusingfirebase;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by AmirRaza on 9/11/2015.
 */
public class FragmentFriendsList extends Fragment {

    private ArrayList<String> listNames;
    private ArrayList<String> listKey;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listNames = new ArrayList<>();
        listKey = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_friends,container,false);

        final TextView titleText = (TextView) view.findViewById(R.id.titleText);
        FirebaseHandler.getInstance().getUsers().child(ChatApp.getUserNum()).child("name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("TAG", dataSnapshot.getValue() + "");
                titleText.setText("Hi " + dataSnapshot.getValue() + "!");
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        ImageView logout = (ImageView) view.findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, Object> status = new HashMap<>();
                status.put("isLogin","false");
                FirebaseHandler.getInstance().getLoginStatus().child(ChatApp.getUserNum()).setValue(status);
                getActivity().getSupportFragmentManager().popBackStack();
                getActivity().getSupportFragmentManager().beginTransaction().add(R.id.container, new FragmentLogin()).commit();
            }
        });

        FirebaseHandler.getInstance().getUsers().addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.getKey().equals(ChatApp.getUserNum())){

                }else{
                    listKey.add(dataSnapshot.getKey());
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    listNames.add(map.get("name").toString());
                    ListView listView = (ListView) view.findViewById(R.id.listOfFriends);
                    listView.setAdapter(new MyListAdapter(getContext(), listNames, listKey));
                }
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


        return view;
    }
}
