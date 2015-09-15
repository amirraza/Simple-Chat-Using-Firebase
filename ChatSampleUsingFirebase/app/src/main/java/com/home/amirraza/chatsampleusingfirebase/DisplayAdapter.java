package com.home.amirraza.chatsampleusingfirebase;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by AmirRaza on 9/10/2015.
 */
public class DisplayAdapter extends ArrayAdapter<String> {

    private Context context;
    private ArrayList<Map<String,Object>> list;
    LayoutInflater inflater;

    public DisplayAdapter(Context context, ArrayList<Map<String,Object>> resource,ArrayList list1) {
        super(context, android.R.layout.simple_list_item_1,list1);
        this.context = context;
        this.list = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
             inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if(list.get(position).get("userID").equals("amir")){
            convertView = inflater.inflate(R.layout.left_item,parent,false);
            TextView textView = (TextView) convertView.findViewById(R.id.leftText);
            textView.setText(list.get(position).get("msg").toString());
        }
        else{
            convertView = inflater.inflate(R.layout.item_right,parent,false);
            TextView textView = (TextView) convertView.findViewById(R.id.rightText);
            textView.setText(list.get(position).get("msg").toString());
        }
        return convertView;
    }
}
