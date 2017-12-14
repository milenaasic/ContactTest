package com.example.milenaasic.contacttest;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class SingleContactActivity extends AppCompatActivity implements DetailFragment.OnDetailFragmentInteractionListener {

    public static final String DEBUG = "SinglePictureActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_contact);
        //ActionBar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.mySingleToolbar);
        myToolbar.setTitle(" ");
        setSupportActionBar(myToolbar);
        ActionBar actionBar = this.getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);

        }

        //lazni background za statusBar

        ImageView mStatusBarBackground=findViewById(R.id.status_bar_background);
        mStatusBarBackground.setMinimumHeight(getStatusBarHeight());
       // mStatusBarBackground.setBackgroundColor(Color.GREEN);

        //systembar ide iza slike
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);


        /*int height = getAppBarHeight()+getStatusBarHeight();
        myToolbar.setMinimumHeight(height);
        myToolbar.setPadding(0,getStatusBarHeight()/2,0,0);*/




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

    @Override
    public void onDetailFragmentInteraction(Uri uri) {

    }

    public int getStatusBarHeight() {
        int result = 0;

        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");

        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
            Log.v(DEBUG, "visina status barapixelsize " + result);

        }

        Log.v(DEBUG,"result"+(result));
        return result;
    }

    public int getAppBarHeight() {

        int actionBarHeight = 0;
        TypedValue tv = new TypedValue();
        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
        {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,getResources().getDisplayMetrics());
        }

        Log.v(DEBUG,"result"+actionBarHeight);
        return actionBarHeight;
    }
}
