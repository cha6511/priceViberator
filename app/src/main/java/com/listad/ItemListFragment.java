package com.listad;


import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.listad.PAGE.LIST;


/**
 * A simple {@link Fragment} subclass.
 */
public class ItemListFragment extends Fragment implements View.OnClickListener {

    RecyclerView recyclerView;
    ItemListAdapter adapter;
    LinearLayoutManager linearLayoutManager;

    ArrayList<ItemListData> listData = new ArrayList<>();

    View v;

    public ItemListFragment() {
        // Required empty public constructor
    }


    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(receiver, new IntentFilter("refresh"));
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            GetListAsyncTask getListAsyncTask = new GetListAsyncTask(getContext(), new PutArray() {
                @Override
                public void resultListArray(ArrayList<ItemListData> data) {
                    listData = data;

                    recyclerView = v.findViewById(R.id.recyclerView);
                    adapter = new ItemListAdapter(listData, ItemListFragment.this, getContext());
                    linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(linearLayoutManager);
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void resultPostArray(ArrayList<PostData> data) {

                }
            });
            getListAsyncTask.execute();
        }
    };

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(receiver);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_item_list, container, false);

        GetListAsyncTask getListAsyncTask = new GetListAsyncTask(getContext(), new PutArray() {
            @Override
            public void resultListArray(ArrayList<ItemListData> data) {
                listData = data;

                recyclerView = v.findViewById(R.id.recyclerView);
                adapter = new ItemListAdapter(listData, ItemListFragment.this, getContext());
                linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(linearLayoutManager);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void resultPostArray(ArrayList<PostData> data) {

            }
        });
        getListAsyncTask.execute();

//        data.add(new ItemListData("", "test", "3,400", "1,200"));
//        adapter.notifyDataSetChanged();
        PAGE.CURRENT = LIST;
        return v;
    }

    @Override
    public void onClick(View view) {
        Vibrator vibrator = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
//        String price = "";
        switch (view.getId()) {
            case R.id.type1:
//                price = removeComma(String.valueOf(view.getTag()));
                vibrator.vibrate(setVibePattern(removeComma(String.valueOf(view.getTag()))), -1);
                break;


            case R.id.type2:
//                price = removeComma(String.valueOf(view.getTag()));
                vibrator.vibrate(setVibePattern(removeComma(String.valueOf(view.getTag()))), -1);
                break;
        }
        //진동기능
    }

    private long[] setVibePattern(String price) {
//        int len = 0;
//        for(int i = 0 ; i < price.length() ; i++){
//            len += Integer.parseInt(String.valueOf(price.charAt(i)));
//        }

        char[] p = new char[price.length()];
        for (int i = 0; i < price.length(); i++) {
            p[i] = price.charAt(i);
        }

        ArrayList<Long> patternArray = new ArrayList<>();
        for (int i = 0; i < p.length; i++) {
            if(Integer.parseInt(String.valueOf(p[i])) == 0){
                patternArray.add((long) 300);
                patternArray.add((long) 2000);
            } else {
                for (int j = 0; j < Integer.parseInt(String.valueOf(p[i])); j++) {
                    patternArray.add((long) 300);
                    patternArray.add((long) 1000);
                }
            }
            patternArray.add((long) 1000);
            patternArray.add((long) 0);
        }
        long[] result = new long[patternArray.size()];
        for (int i = 0; i < patternArray.size(); i++) {
            result[i] = patternArray.get(i);
        }
        return result;
    }

    private String removeComma(String origin) {
        String[] tmp = origin.split(",");
        String result = "";
        for (int i = 0; i < tmp.length; i++) {
            result += tmp[i];
        }
        return result;
    }


    public class GetListAsyncTask extends AsyncTask<String, String, String> {
        Context context;
        ProgressDialog progressDialog;
        PutArray putArray;

        public GetListAsyncTask(Context context, PutArray putArray) {
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
            builder.url("http://52.78.60.28:3000/apps/product");
            Request request = builder.build();
            Response response = null;
            String myResponse = "";
            try {
                response = client.newCall(request).execute();
                myResponse = response.body().string();
                return myResponse;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (TextUtils.isEmpty(s)) {
                Toast.makeText(getContext(), "리스트 로딩 중 에러가 발생하였습니다.", Toast.LENGTH_SHORT).show();
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                return;
            } else {
                Log.d("result JSON", s);
            }


            ArrayList<ItemListData> tmpArray = new ArrayList<>();
            try {
                JSONArray jsonArray = new JSONArray(s);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    ItemListData data = new ItemListData(
                            object.getString("no"),
                            object.getString("img_path"),
                            object.getString("name"),
                            object.getString("price"),
                            object.getString("price1")
                    );
                    tmpArray.add(data);
                }
                putArray.resultListArray(tmpArray);
            } catch (Exception e) {
                putArray.resultListArray(null);
            }
            if (progressDialog.isShowing())
                progressDialog.dismiss();
        }
    }
}
