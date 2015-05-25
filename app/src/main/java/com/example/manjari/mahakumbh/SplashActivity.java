package com.example.manjari.mahakumbh;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;


public class SplashActivity extends Activity {

    public static final String TAG="InboxDemo";

    public static final int PLAY_SERVICES_RESOLUTION_REQUEST=9000;

    private String regId;

    SharedPreferences sp;

    public int appVersion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        if(checkPlayServices()){
            getRegistrationId();
        }
        else{
            Log.i(TAG, "Google Play Services are not available");
        }
    }

    public void getRegistrationId(){
        sp=getGcmSharedPreference();
        regId=sp.getString("REG_ID", "");
        Log.i(TAG, "Registration Id is" + regId);
        if(regId.isEmpty()){
            registerInBackgroud();
        }
        Log.i(TAG,"Stored app version is "+sp.getInt("APP_VERSION",Integer.MIN_VALUE));

        appVersion=sp.getInt("APP_VERSION",Integer.MIN_VALUE);
        if(getAppVersion(this)!=appVersion){
            Log.i(TAG,"App version has been changed");
            regId="";
        }

    }

    public SharedPreferences getGcmSharedPreference(){
        return getSharedPreferences(SplashActivity.class.getSimpleName(), Context.MODE_PRIVATE);
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    public void registerInBackgroud(){
        appVersion=getAppVersion(this);
        InitializeBackground regGcm=new InitializeBackground(this,sp,appVersion);
        regGcm.execute(null,null,null);
    }

    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);

            Log.i(TAG,"Current App Version Is "+packageInfo.versionCode);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_splash, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
