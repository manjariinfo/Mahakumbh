package com.example.manjari.mahakumbh;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by manjari on 5/25/15.
 */
public class InitializeBackground extends AsyncTask<String,String,String> {

    Context context;
    public static final String SENDER_ID = "585595630600";
    public String regId="";
    public String apiUrl;
    SharedPreferences sp;
    int appVersion;
    GoogleCloudMessaging gcm;
    public static final String TAG="MahaKumbhDemo";

    public InitializeBackground(Context context,SharedPreferences sp,int appVersion){
        this.context=context;
        this.sp=sp;
        this.appVersion=appVersion;
        this.apiUrl=ConfigInterface.apiUrl;
    }
    @Override
    protected String doInBackground(String[] str) {

        if(gcm==null){
            gcm= GoogleCloudMessaging.getInstance(context);
        }

        try {
            regId=gcm.register(SENDER_ID);
            Log.i(TAG, "Regis ID is" + regId);
            sendRegistrationIdToBackend(regId);

            storeRegistrationId(regId,appVersion);

        } catch (IOException e) {
            e.printStackTrace();
        }
        long time=5000;
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return regId;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Intent intt=new Intent(context,MenuActivity.class);
        context.startActivity(intt);
    }

    public void sendRegistrationIdToBackend(String regId) {
        Log.e(TAG,"in network wala function");
        if (isNetworkAvailable()) {
            try {
                Log.e(TAG,"Network is available");
                DefaultHttpClient dhc = new DefaultHttpClient();
                HttpPost hp = new HttpPost(apiUrl);
                List<NameValuePair> post = new ArrayList<NameValuePair>();
                post.add(new BasicNameValuePair("regId", regId));
                Log.e(TAG,"Reg id mil gya shayad"+regId+" API Url is"+apiUrl);
                hp.setEntity(new UrlEncodedFormEntity(post));
                HttpResponse hr = dhc.execute(hp);

            } catch (Exception e) {

            }
        }
    }

    public void storeRegistrationId(String regId,int appVersion){

        SharedPreferences.Editor edit=sp.edit();
        edit.putString("REG_ID",regId);
        edit.putInt("APP_VERSION", appVersion);
        edit.commit();
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        // if no network is available networkInfo will be null
        // otherwise check if we are connected
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }
}
