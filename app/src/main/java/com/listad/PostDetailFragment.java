package com.listad;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import static com.listad.PAGE.LIST;
import static com.listad.PAGE.POST_DETAIL;


/**
 * A simple {@link Fragment} subclass.
 */
public class PostDetailFragment extends Fragment {

    TextView title;
    TextView content;

    ImageView back;

    public PostDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_post_detail, container, false);
        back = v.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LocalBroadcastManager.getInstance(getContext()).sendBroadcast(new Intent("close_postDetail"));
            }
        });
        title = v.findViewById(R.id.title);
        content = v.findViewById(R.id.content);

        title.setVisibility(View.GONE);
        content.setVisibility(View.GONE);


        if(! getArguments().isEmpty()){
            title.setText(getArguments().getString("title"));
            content.setText(getArguments().getString("content"));

            title.setVisibility(View.VISIBLE);
            content.setVisibility(View.VISIBLE);
        }
        PAGE.CURRENT = POST_DETAIL;
        return v;
    }

}
