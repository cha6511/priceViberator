package com.listad;

import android.os.AsyncTask;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    int registerTokenCnt = 0;
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        sendServer(FirebaseInstanceId.getInstance().getToken());
    }

    public void sendServer(String token){
        SendTokenToServer sendTokenToServer = new SendTokenToServer();
        sendTokenToServer.execute(token);
    }

    public class SendTokenToServer extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            OkHttpClient client = new OkHttpClient();
            Request.Builder builder = new Request.Builder();
            builder.url("http://52.78.60.28:3000/push/add?" +
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
                    Toast.makeText(getApplicationContext(), "푸시 서버에 등록하지 못하였습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    SendTokenToServer SendTokenToServer = new SendTokenToServer();
                    SendTokenToServer.execute(FirebaseInstanceId.getInstance().getToken());
                    registerTokenCnt++;
                }
            }
        }
    }
}
