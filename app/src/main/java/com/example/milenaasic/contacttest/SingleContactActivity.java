package com.example.milenaasic.contacttest;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

public class SingleContactActivity extends AppCompatActivity implements DetailFragment.OnDetailFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_contact);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.mySingleToolbar);
        myToolbar.setNavigationIcon(R.drawable.slika0_thumb);
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
                    int arg2 = bundle.getInt(MainActivity.CONTACT_LOOKUP_KEY);
                    String arg3 = bundle.getString(MainActivity.CONTACT_NAME);

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
