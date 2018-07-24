package com.listad;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.Holder> {

    ArrayList<ItemListData> data = new ArrayList<>();
    View.OnClickListener onClickListener;
    Context context;

    public ItemListAdapter(ArrayList<ItemListData> data, View.OnClickListener onClickListener, Context context) {
        this.data = data;
        this.onClickListener = onClickListener;
        this.context = context;
    }


    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        ItemListData ild = data.get(position);
        if(! TextUtils.isEmpty(ild.getImg_url())){
            Glide.with(context).load(ild.getImg_url()).apply(new RequestOptions().placeholder(R.drawable.ic_launcher_foreground).error(R.drawable.ic_launcher_background)).into(holder.img);
        } else{
            holder.img.setImageResource(R.drawable.ic_launcher_foreground);
        }
        holder.name.setText(ild.getName());
        holder.type1.setOnClickListener(onClickListener);
        holder.type1.setTag(ild.getType1_price());
        holder.type2.setOnClickListener(onClickListener);
        holder.type2.setTag(ild.getType2_price());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView name;
        TextView type1;
        TextView type2;
        public Holder(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
            name = itemView.findViewById(R.id.name);
            type1 = itemView.findViewById(R.id.type1);
            type2 = itemView.findViewById(R.id.type2);
        }
    }
}
