package com.vertial.veritel;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class SingleContactActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener{

    public static final String DEBUG = "SinglePictureActivity";
    String veriTelTelefon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_single_contact);
        //ActionBar
        Toolbar myToolbar = findViewById(R.id.mySingleToolbar);
        myToolbar.setTitle(" ");
        setSupportActionBar(myToolbar);
        ActionBar actionBar = getSupportActionBar();
        Log.v(DEBUG,"api on cerate");
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);

        }

        setUpPreferences();

        Configuration config = getResources().getConfiguration();
        FrameLayout mDetailFragmentContainer=findViewById(R.id.detailFragmentContainer);



        // za Api>=21 setujem flag da sadrzaj ide iza status bara
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(uiOptions);
            //getWindow().setStatusBarColor(getResources().getColor(android.R.color.transparent));
            Log.v(DEBUG,"api 21");

            if(config.orientation==Configuration.ORIENTATION_PORTRAIT) {
                getWindow().setStatusBarColor(getResources().getColor(android.R.color.transparent));
                myToolbar.setPadding(0,getStatusBarHeight(),0,0);

            }else{
                myToolbar.setPadding(0,getStatusBarHeight(),0,0);
                myToolbar.setBackgroundResource(R.color.colorPrimaryDark);
                mDetailFragmentContainer.setPadding(0,getStatusBarHeight()+getAppBarHeight(),0,0);
                Log.v(DEBUG,"status + app hajt"+getAppBarHeight()+getStatusBarHeight());
            }

        }


        // za Api=19 i 20
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT || Build.VERSION.SDK_INT==Build.VERSION_CODES.KITKAT_WATCH) {

            ImageView mStatusBarBackground = findViewById(R.id.status_bar_background_main);
             mStatusBarBackground.setMinimumHeight(getStatusBarHeight());
            myToolbar.setPadding(0,getStatusBarHeight(),0,0);

             if(config.orientation==Configuration.ORIENTATION_LANDSCAPE){

                 mDetailFragmentContainer.setPadding(0,getStatusBarHeight()+getAppBarHeight(),0,0);
                 myToolbar.setPadding(0,getStatusBarHeight(),0,0);
             }
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {

            if(config.orientation==Configuration.ORIENTATION_LANDSCAPE){
                mDetailFragmentContainer.setPadding(0,getAppBarHeight(),0,0);
            }
        }


        if (savedInstanceState == null) {

            Intent intent = getIntent();
            if (intent != null) {
                Bundle bundle = intent.getExtras();
                if (bundle != null) {
                    int arg1 = bundle.getInt(MainActivity.CONTACT_ID);
                    String arg2 = bundle.getString(MainActivity.CONTACT_LOOKUP_KEY);
                    String arg3 = bundle.getString(MainActivity.CONTACT_NAME);
                    Log.v(DEBUG, "arg1 Id" + ((Integer) arg1).toString());
                    Log.v(DEBUG, "arg2 lookup key" + arg2);
                    Log.v(DEBUG, "contact name" + arg3);

                    DetailFragment detailFragment = DetailFragment.newInstance(arg1, arg2, arg3);

                    getSupportFragmentManager().beginTransaction().add(R.id.detailFragmentContainer, detailFragment).commit();

                }
            }

        }



    }


    private int getStatusBarHeight() {
        int result = 0;

        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");

        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
            Log.v(DEBUG, "visina status barapixelsize " + result);

        }

        Log.v(DEBUG,"result"+(result));
        return result;
    }

    private int getAppBarHeight() {

        int actionBarHeight = 0;
        TypedValue tv = new TypedValue();
        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
        {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,getResources().getDisplayMetrics());
        }

        Log.v(DEBUG,"result"+actionBarHeight);
        return actionBarHeight;
    }

    private void setUpPreferences() {

        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);
        veriTelTelefon=sharedPreferences.getString( getString(R.string.list_preference_phones_key), getResources().getString(R.string.pref_list_default_value));
        Log.v(DEBUG,"veritelTelefon u set up pref u singlr coyact act"+veriTelTelefon);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Log.v(DEBUG,"veritelTelefon u on change listener u single contact activity "+veriTelTelefon);


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    }



}



