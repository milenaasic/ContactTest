package com.example.milenaasic.contacttest;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import static android.Manifest.permission.READ_CONTACTS;

public class MainActivity extends AppCompatActivity implements ContactsFragment.OnContactsFragmentInteractionListener {

    private static final int REQUEST_READ_CONTACTS=100;
    View mCoordinatorLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.mytoolbar);

        setSupportActionBar(myToolbar);
        mCoordinatorLayout=findViewById(R.id.myCoordinatorLayout);

        // ako imas permission ucitaj fragment
        if (checkReadContacsPermission()) {

            getSupportFragmentManager().beginTransaction().add(R.id.containerContactsList, new ContactsFragment()).commit();


        }



    }

    @Override
    public void onContactsFragmentInteraction (Uri itemUri){

    }


    private boolean checkReadContacsPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS)) {
            Snackbar.make(mCoordinatorLayout, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override


                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    }).show();
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }



}
