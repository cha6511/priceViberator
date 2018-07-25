package com.listad;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.listad.PAGE.CURRENT;
import static com.listad.PAGE.POST_DETAIL;

public class MainActivity extends AppCompatActivity {
    TabLayout tabs;
    ViewPager pager;
    PagerAdapter adapter;

    long backKeyPressedTime = 0;

    int registerTokenCnt = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabs = findViewById(R.id.tabs);
        pager = findViewById(R.id.pager);

        adapter = new PagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new ItemListFragment(), "상품목록");
        adapter.addFragment(new PostFragment(), "공지사항");
        pager.setAdapter(adapter);
        tabs.setupWithViewPager(pager);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        if (POST_DETAIL.equals(CURRENT)) {
            LocalBroadcastManager.getInstance(MainActivity.this).sendBroadcast(new Intent("close_postDetail"));
        } else {
            if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
                backKeyPressedTime = System.currentTimeMillis();
//                showGuide(); return;
                Toast.makeText(getApplicationContext(), "\'뒤로\'버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();
                return;
            } else if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
                finish();
//                toast.cancel();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        SendTokenToServer SendTokenToServer = new SendTokenToServer();
        SendTokenToServer.execute(FirebaseInstanceId.getInstance().getToken());
    }

    public class SendTokenToServer extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String... strings) {
            OkHttpClient client = new OkHttpClient();
            Request.Builder builder = new Request.Builder();
            builder.url("ip/push/add?" +
                    "fcm_token=" + strings[0]);
            Request request = builder.build();
            Response response = null;
            String myResponse = "";
            try{
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
                if(registerTokenCnt > 5){
                    Toast.makeText(MainActivity.this, "푸시 서버에 등록하지 못하였습니다.", Toast.LENGTH_SHORT).show();
                    return;
                }
                SendTokenToServer SendTokenToServer = new SendTokenToServer();
                SendTokenToServer.execute(FirebaseInstanceId.getInstance().getToken());
                registerTokenCnt++;
            } else{
                Toast.makeText(MainActivity.this, "푸시 서버에 등록이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                return;
            }
        }
    }
}
