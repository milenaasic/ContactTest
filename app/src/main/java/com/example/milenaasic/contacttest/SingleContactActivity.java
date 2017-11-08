package com.example.milenaasic.contacttest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class SingleContactActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_contact);

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
}
