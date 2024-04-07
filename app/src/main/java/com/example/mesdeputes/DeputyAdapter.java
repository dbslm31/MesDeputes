package com.example.mesdeputes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class DeputyAdapter extends BaseAdapter {

    private ArrayList<Deputy> deputies;
    private Context context;

    public DeputyAdapter(ArrayList<Deputy> deputies, Context context) {
        this.deputies = deputies;
        this.context = context;
    }

    public void setDeputies(ArrayList<Deputy> deputies) {
        this.deputies = deputies;
    }

    @Override
    public int getCount() {
        return deputies.size();
    }

    @Override
    public Object getItem(int position) {
        return deputies.get(position);
    }

    @Override
    public long getItemId(int position) {
        return deputies.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_deputy, parent, false);
        }
        TextView textViewName= convertView.findViewById(R.id.textViewItemDeputyName);
        textViewName.setText(deputies.get(position).getFirstname()+
                " "+deputies.get(position).getLastname());
        TextView textViewCirco= convertView.findViewById(R.id.textViewItemDeputyCirco);
        textViewCirco.setText(deputies.get(position).getDepartment()+", "+
                deputies.get(position).getNameCirco()+ " "+ deputies.get(position).getNumCirco()+
                (deputies.get(position).getNumCirco()==1? "er": "eme")+" circoncription");


        ImageView imageView= convertView.findViewById(R.id.imageViewItemDeputy);
        ApiServices.loadDeputyAvatar(context, deputies.get(position).getNameForAvatar(), imageView);
        return convertView;
    }
}
