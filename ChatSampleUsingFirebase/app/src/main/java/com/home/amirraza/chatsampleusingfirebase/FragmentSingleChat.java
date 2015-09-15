package com.home.amirraza.chatsampleusingfirebase;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
 * Created by AmirRaza on 9/13/2015.
 */
public class FragmentSingleChat extends Fragment {

    private String selectedUser;
    private ArrayList<Map<String, Object>> con;
    private ArrayList<String> conversationKey;
    private String conversationID;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        selectedUser = getArguments().getString("selectedUser");
        conversationID = getArguments().getString("conversationID");
        Log.d("TAGd", selectedUser);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("TAG", "paused");
        conversationKey = null;
        con = null;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d("TAG", "Attach");
        con = new ArrayList<>();
        conversationKey = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_single_chat, container, false);

        final EditText chatText = (EditText) view.findViewById(R.id.chatText);
        ImageView btnSend = (ImageView) view.findViewById(R.id.btnSend);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, Object> map = new HashMap<>();
                map.put("msg", chatText.getText().toString());
                map.put("userID", ChatApp.getUserNum());
                FirebaseHandler.getInstance().getConversations().child(conversationID).push().setValue(map);
                chatText.setText("");
            }
        });

        FirebaseHandler.getInstance().getConversations().child(conversationID).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d("TAG key", dataSnapshot.getKey());
                Log.d("TAG val", dataSnapshot.getValue().toString());
                try {
                    Map<String, Object> conv = (Map<String, Object>) dataSnapshot.getValue();
                    conversationKey.add(dataSnapshot.getKey());
                    con.add(conv);
                    ListView listView = (ListView) view.findViewById(R.id.chatList);
                    MyAdapter adapter = new MyAdapter(getActivity(), conversationKey, con);
                    listView.setAdapter(adapter);
                    listView.setSelection(adapter.getCount()-1);
                } catch (NullPointerException e) {
                    Log.d("TAG key", "Null Exception at conversationKey ArrayList");
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

    class MyAdapter extends ArrayAdapter<String> {

        ArrayList<Map<String, Object>> map;
        Context c;
        private LayoutInflater inflater;

        public MyAdapter(Context context, ArrayList<String> count, ArrayList<Map<String, Object>> map) {
            super(context, android.R.layout.simple_list_item_1, count);
            this.map = map;
            this.c = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            }
            if (map.get(position).get("userID").equals(ChatApp.getUserNum())) {
                convertView = inflater.inflate(R.layout.left_item, parent, false);
                TextView textView = (TextView) convertView.findViewById(R.id.leftText);
                textView.setText(map.get(position).get("msg").toString());
            } else {
                convertView = inflater.inflate(R.layout.item_right, parent, false);
                TextView textView = (TextView) convertView.findViewById(R.id.rightText);
                textView.setText(map.get(position).get("msg").toString());
            }
            return convertView;
        }
    }
}
