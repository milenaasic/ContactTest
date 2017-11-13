package com.example.milenaasic.contacttest;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

public class SingleContactActivity extends AppCompatActivity implements DetailFragment.OnDetailFragmentInteractionListener{

    public static final String DEBUG="SinglePictureActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_contact);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.mySingleToolbar);
        myToolbar.setNavigationIcon(R.drawable.ic_chevron_left_white_24dp);
        myToolbar.setTitle("");
        // uradi setHomeButtonasUpEnabled
        setSupportActionBar(myToolbar);

        //systembar ide iza slike
        View decorView=getWindow().getDecorView();
        int uiOptions=View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        if(savedInstanceState==null) {

            Intent intent = getIntent();
            if (intent != null) {
                Bundle bundle = intent.getExtras();
                if(bundle!=null) {
                    int arg1 = bundle.getInt(MainActivity.CONTACT_ID);
                    String arg2 = bundle.getString(MainActivity.CONTACT_LOOKUP_KEY);
                    String arg3 = bundle.getString(MainActivity.CONTACT_NAME);
                    Log.v(DEBUG,"arg1 Id"+((Integer)arg1).toString());
                    Log.v(DEBUG,"arg2 lookup key"+arg2);
                    Log.v(DEBUG,"contact name"+arg3);

                    DetailFragment detailFragment = DetailFragment.newInstance(arg1, arg2, arg3);
                    getSupportFragmentManager().beginTransaction().add(R.id.detailFragmentContainer, detailFragment).commit();

                }
            }

        }
    }

    @Override
    public void onDetailFragmentInteraction(Uri uri) {

    }
}
