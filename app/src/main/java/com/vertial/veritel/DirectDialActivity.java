package com.vertial.veritel;

import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

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
        if(savedInstanceState==null){

            Log.v(LOG, "ne postoji direct dial fragment");
            getSupportFragmentManager().beginTransaction().add(R.id.containerDirectDial, new DirectDialFragment()).commit();


        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
