package com.vertial.veritel;

import android.net.Uri;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;

public class DirectDialActivity extends AppCompatActivity implements DirectDialFragment.OnFragmentInteractionListener {

    private static final String LOG = "DirectDialActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direct_dial);

        Toolbar myToolbar =findViewById(R.id.mytoolbar);
        setSupportActionBar(myToolbar);
        ActionBar actionBar=this.getSupportActionBar();

        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);

        }

        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT || Build.VERSION.SDK_INT==Build.VERSION_CODES.KITKAT_WATCH) {
            ImageView mStatusBarBackground=findViewById(R.id.status_bar_background_dial);
            mStatusBarBackground.setMinimumHeight(getStatusBarHeight());

            Log.v(LOG,getStatusBarHeight()+"visina status bara");
        }


        if(savedInstanceState==null){

            Log.v(LOG, "ne postoji direct dial fragment");
            getSupportFragmentManager().beginTransaction().add(R.id.containerDirectDial, new DirectDialFragment()).commit();


        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private int getStatusBarHeight() {
        int result = 0;

        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");

        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
            Log.v(LOG, "visina status barapixelsize " + result);

        }

        Log.v(LOG,"result"+(result));
        return result;
    }



}
