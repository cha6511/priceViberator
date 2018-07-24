package com.listad;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ItemListFragment extends Fragment implements View.OnClickListener {

    RecyclerView recyclerView;
    ItemListAdapter adapter;
    LinearLayoutManager linearLayoutManager;

    ArrayList<ItemListData> data = new ArrayList<>();

    public ItemListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_item_list, container, false);
        recyclerView = v.findViewById(R.id.recyclerView);
        adapter = new ItemListAdapter(data, this, getContext());
        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(linearLayoutManager);

        GetListAsyncTask getListAsyncTask = new GetListAsyncTask(getContext(), new PutArray() {
            @Override
            public void resultArray(ArrayList<ItemListData> data) {
                ItemListFragment.this.data = data;
                adapter.notifyDataSetChanged();
            }
        });
        getListAsyncTask.execute();

        data.add(new ItemListData("", "test", "3,400", "1,200"));
        adapter.notifyDataSetChanged();

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

    private long[] setVibePattern(String price){
//        int len = 0;
//        for(int i = 0 ; i < price.length() ; i++){
//            len += Integer.parseInt(String.valueOf(price.charAt(i)));
//        }

        char[] p = new char[price.length()];
        for(int i = 0 ; i < price.length() ; i++){
            p[i] = price.charAt(i);
        }

        ArrayList<Long> patternArray = new ArrayList<>();
        for(int i = 0 ; i < p.length ; i++){
            for(int j = 0 ; j < Integer.parseInt(String.valueOf(p[i])) ; j++){
                patternArray.add((long) 100);
                patternArray.add((long) 500);
            }
            patternArray.add((long) 1000);
            patternArray.add((long) 0);
        }
        long[] result = new long[patternArray.size()];
        for(int i = 0 ; i < patternArray.size() ; i++){
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

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            ArrayList<ItemListData> tmpArray = new ArrayList<>();


            try {


                putArray.resultArray(tmpArray);
            } catch (Exception e) {
                putArray.resultArray(null);
            }
            if (progressDialog.isShowing())
                progressDialog.dismiss();
        }
    }
}