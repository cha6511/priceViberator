package com.listad;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class PostListAdapter extends RecyclerView.Adapter<PostListAdapter.Holder>{
    ArrayList<PostData> datas = new ArrayList<>();
    View.OnClickListener onClickListener;

    public PostListAdapter(ArrayList<PostData> datas, View.OnClickListener onClickListener) {
        this.datas = datas;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_list_item, parent, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        PostData data = datas.get(position);
        holder.title.setText(data.getTitle());
        holder.title.setOnClickListener(onClickListener);
        holder.title.setTag(data);
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView title;
        public Holder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
        }
    }
}
