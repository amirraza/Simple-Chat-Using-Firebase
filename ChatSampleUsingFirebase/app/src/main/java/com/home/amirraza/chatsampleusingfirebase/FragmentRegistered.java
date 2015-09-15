package com.home.amirraza.chatsampleusingfirebase;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by AmirRaza on 9/13/2015.
 */
public class FragmentRegistered extends Fragment {

    private ArrayList<String> matchNum;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        matchNum = new ArrayList<>();
        FirebaseHandler.getInstance().getUsers().addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                matchNum.add(dataSnapshot.getKey());
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
        View view = inflater.inflate(R.layout.fragment_registered,container,false);

        final Button regBtn = (Button) view.findViewById(R.id.regiBtn);
        final EditText name = (EditText) view.findViewById(R.id.name);
        final EditText pass = (EditText) view.findViewById(R.id.pass);

        final ImageView already = (ImageView) view.findViewById(R.id.already);
        final EditText num = (EditText) view.findViewById(R.id.num);

        num.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                for(int a=0;a<matchNum.size();a++){
                    if(("+92"+charSequence.toString()).equals(matchNum.get(a))){
                        already.setVisibility(View.VISIBLE);
                        regBtn.setEnabled(false);
                        break;
                    }else{
                        regBtn.setEnabled(true);
                        already.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Map<String, Object> map = new HashMap<String, Object>();
                map.put("name",name.getText().toString());
                map.put("pass",pass.getText().toString());

                FirebaseHandler.getInstance().getUsers().child("+92"+num.getText()).setValue(map, new Firebase.CompletionListener() {
                    @Override
                    public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                        if(firebaseError!=null){
                            Toast.makeText(getActivity(), "Error While Registering", Toast.LENGTH_SHORT).show();
                        }else{
                            FragmentManager fm = getFragmentManager();
                            fm.beginTransaction().replace(R.id.container,new FragmentLogin()).commit();
                        }
                    }
                });
            }
        });
        return view;
    }
}
