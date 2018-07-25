package com.listad;


import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.listad.PAGE.LIST;
import static com.listad.PAGE.POST;


/**
 * A simple {@link Fragment} subclass.
 */
public class PostFragment extends Fragment implements View.OnClickListener{

    RecyclerView recyclerView;
    PostListAdapter adapter;
    LinearLayoutManager linearLayoutManager;

    ArrayList<PostData> datas = new ArrayList<>();

    FrameLayout frameLayout;

    View v;

    public PostFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_post, container, false);

        LocalBroadcastManager.getInstance(getContext()).registerReceiver(receiver, new IntentFilter("close_postDetail"));

        frameLayout = v.findViewById(R.id.panel);

        GetPostAsyncTask getPostAsyncTask = new GetPostAsyncTask(getContext(), new PutArray() {
            @Override
            public void resultListArray(ArrayList<ItemListData> data) {

            }
            @Override
            public void resultPostArray(ArrayList<PostData> data) {

                datas = data;

                recyclerView = v.findViewById(R.id.post_list);
                adapter = new PostListAdapter(datas, PostFragment.this);
                linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(linearLayoutManager);
                adapter.notifyDataSetChanged();
            }
        });
        getPostAsyncTask.execute();
        PAGE.CURRENT = POST;
        return v;
    }

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if("close_postDetail".equals(intent.getAction())){
                recyclerView.setVisibility(View.VISIBLE);
                frameLayout.setVisibility(View.GONE);
                PAGE.CURRENT = POST;
            }
        }
    };


    @Override
    public void onClick(View view) {
        PostData data = (PostData) view.getTag();
        Bundle bundle = new Bundle();
        bundle.putString("title", data.getTitle());
        bundle.putString("content", data.getContent());
        PostDetailFragment postDetailFragment = new PostDetailFragment();
        postDetailFragment.setArguments(bundle);
        FragmentTransaction transaction = getFragmentManager().beginTransaction().replace(R.id.panel, postDetailFragment);
        transaction.commitAllowingStateLoss();
        recyclerView.setVisibility(View.GONE);
        frameLayout.setVisibility(View.VISIBLE);

    }



    public class GetPostAsyncTask extends AsyncTask<String, String, String>{

        Context context;
        ProgressDialog progressDialog;
        PutArray putArray;

        public GetPostAsyncTask(Context context, PutArray putArray){
            this.context = context;
            this.putArray = putArray;
            progressDialog = new ProgressDialog(context);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("로딩중입니다...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            OkHttpClient client = new OkHttpClient();
            Request.Builder builder = new Request.Builder();
            builder.url("http://192.168.1.22/" +
                    "post/get?" +
                    "no=0");
            Request request = builder.build();
            Response response = null;
            String myResponse = "";
            try {
                response = client.newCall(request).execute();
                myResponse = response.body().string();
                return myResponse;
            } catch (Exception e){
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(TextUtils.isEmpty(s)){
                Toast.makeText(getContext(), "공지사항 로딩 중 에러가 발생하였습니다.", Toast.LENGTH_SHORT).show();
                if(progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
                return;
            } else{
                Log.d("result JSON", s);
            }


            ArrayList<PostData> tmpArray = new ArrayList<>();
            try {
                JSONArray jsonArray = new JSONArray(s);
                for(int i = 0 ; i < jsonArray.length() ; i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    PostData data = new PostData(
                            object.getString("title"),
                            object.getString("text"),
                            object.getString("no")
                    );
                    tmpArray.add(data);
                }
                putArray.resultPostArray(tmpArray);
            } catch (Exception e) {
                putArray.resultPostArray(null);
            }
            if (progressDialog.isShowing())
                progressDialog.dismiss();
        }
    }
}
