package com.example.bgctub_transport_tracker_app_information.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bgctub_transport_tracker_app_information.R;

import java.util.ArrayList;

public class CustomList extends BaseAdapter {
    LayoutInflater layoutInflater;
    Holder holder;
    ArrayList<String> arrayList=new ArrayList<String>();
    Context context;

    public CustomList(ArrayList<String> arrayList, Context context) {
        this.layoutInflater = LayoutInflater.from(context);
        this.arrayList = arrayList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public String getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView=layoutInflater.inflate(R.layout.users_list_adapter,null);
            holder=new Holder();
            holder.textView=(TextView) convertView.findViewById(R.id.adapterTextView);
            holder.imageView=(ImageView) convertView.findViewById(R.id.adapterImageView);
            convertView.setTag(holder);

        }else{
            holder=(Holder) convertView.getTag();
        }
        holder.textView.setText(getItem(position));
        return convertView;
    }
}
