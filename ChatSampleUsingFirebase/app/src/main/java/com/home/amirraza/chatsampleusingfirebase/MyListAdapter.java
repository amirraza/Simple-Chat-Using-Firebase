package com.home.amirraza.chatsampleusingfirebase;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by AmirRaza on 9/14/2015.
 */
public class MyListAdapter extends ArrayAdapter<String> {
    ArrayList<String> name = new ArrayList<>(), num = new ArrayList<>();
    ArrayList imageBool;
    Context c;
    private int a;

    public MyListAdapter(Context context, ArrayList<String> name, ArrayList<String> num) {
        super(context, android.R.layout.simple_list_item_1, name);
        this.name = name;
        this.num = num;
        this.c = context;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.single_item, parent, false);
        }
        final TextView names = (TextView) convertView.findViewById(R.id.listItemName);
        names.setText(name.get(position));

        final TextView nums = (TextView) convertView.findViewById(R.id.listItemNum);
        nums.setText(num.get(position));
        Log.d("TAGgg", num.get(position));
        Log.d("TAGgg", "adf");

        final ImageView wifiImg = (ImageView) convertView.findViewById(R.id.wifiImg);

        FirebaseHandler.getInstance().getLoginStatus().child(num.get(position)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("TAG loginStatus ", dataSnapshot + "");
                Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                if (map == null) {
                    //not set Login Status, returns null
                    Log.d("TAG loginStatus ", dataSnapshot + "");
                    wifiImg.setImageDrawable(c.getResources().getDrawable(R.drawable.wifi_image_offline));
                } else {

                    //get login status true/false
                    Log.d("TAG else loginStatus ", map.get("isLogin").toString());
                    if (map.get("isLogin").toString().equals("true")) {
                        wifiImg.setImageDrawable(c.getResources().getDrawable(R.drawable.wifi_image_online));
                    } else {
                        wifiImg.setImageDrawable(c.getResources().getDrawable(R.drawable.wifi_image_offline));
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Clicked " + name.get(position) + num.get(position), Toast.LENGTH_SHORT).show();

                FirebaseHandler.getInstance().getChatRef().child(ChatApp.getUserNum()).child(num.get(position)).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.d("TAG", dataSnapshot.getKey());
                        Map<String, Object> value = (Map<String, Object>) dataSnapshot.getValue();
                        if (value == null) {
                            Log.d("TAG", "MAP NULL,created ConvID");
                            final Map<String, Object> map = new HashMap<>();
                            map.put("conversationID", ChatApp.getUserNum() + num.get(position));
                            FirebaseHandler.getInstance().getChatRef().child(ChatApp.getUserNum()).child(num.get(position)).setValue(map, new Firebase.CompletionListener() {
                                @Override
                                public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                                    if (firebaseError != null) {
                                        Toast.makeText(c, "Error in Creating Conversation", Toast.LENGTH_SHORT).show();
                                    } else {
                                        FirebaseHandler.getInstance().getChatRef().child(num.get(position)).child(ChatApp.getUserNum()).setValue(map, new Firebase.CompletionListener() {
                                            @Override
                                            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                                                if (firebaseError != null) {
                                                    Toast.makeText(c, "Error in Creating Conversation", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Bundle bundle = new Bundle();
                                                    bundle.putString("selectedUser", num.get(position));
                                                    bundle.putString("conversationID", ChatApp.getUserNum() + num.get(position));
                                                    FragmentSingleChat fragmentSingleChat = new FragmentSingleChat();
                                                    fragmentSingleChat.setArguments(bundle);
                                                    ((FragmentActivity) c).getSupportFragmentManager()
                                                            .beginTransaction()
                                                            .add(R.id.container, fragmentSingleChat)
                                                            .addToBackStack(null).commit();
                                                }
                                            }
                                        });
                                    }
                                }
                            });

                        } else {
                            Log.d("TAG", "else");
                            Log.d("TAG", dataSnapshot.getValue() + "");
                            Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                            Log.d("TAG map", map.get("conversationID").toString());

                            Bundle bundle = new Bundle();
                            bundle.putString("selectedUser", num.get(position));
                            bundle.putString("conversationID", map.get("conversationID").toString());
                            FragmentSingleChat fragmentSingleChat = new FragmentSingleChat();
                            fragmentSingleChat.setArguments(bundle);
                            ((FragmentActivity) c).getSupportFragmentManager()
                                    .beginTransaction()
                                    .add(R.id.container, fragmentSingleChat)
                                    .addToBackStack(null).commit();
                        }
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });
            }
        });
        return convertView;
    }
}
