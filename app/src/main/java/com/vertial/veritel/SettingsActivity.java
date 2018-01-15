package com.vertial.veritel;

import android.os.Build;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import android.view.MenuItem;
import android.widget.ImageView;

public class SettingsActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar myToolbar = findViewById(R.id.settingToolbar);
        setSupportActionBar(myToolbar);
        ActionBar actionBar=this.getSupportActionBar();

        if(actionBar!=null){
           actionBar.setDisplayHomeAsUpEnabled(true);

        }

        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT || Build.VERSION.SDK_INT==Build.VERSION_CODES.KITKAT_WATCH) {
            ImageView mStatusBarBackground=findViewById(R.id.status_bar_background_settings);
            mStatusBarBackground.setMinimumHeight(getStatusBarHeight());

        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        int id=item.getItemId();

        if(id==android.R.id.home){
            NavUtils.navigateUpFromSameTask(this);

        }

        return super.onOptionsItemSelected(item);
    }


    private int getStatusBarHeight() {
        int result = 0;

        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");

        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);


        }


        return result;
    }



}



