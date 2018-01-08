package com.vertial.veritel;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;




import static android.Manifest.permission.READ_CONTACTS;

public class MainActivity extends AppCompatActivity implements ContactsFragment.OnContactsFragmentInteractionListener,
        MenuItem.OnActionExpandListener {

    private static final String LOG="MainActivity";
    private static final int REQUEST_READ_CONTACTS=100;
    View mCoordinatorLayout;
    ActionBar actionBar;
    SearchView searchViewActionBar;
    private MenuItem mActionSettings;

    public static final String CONTACT_ID="contactID";
    public static final String CONTACT_LOOKUP_KEY="contactLookup";
    public static final String CONTACT_NAME="contactName";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar =findViewById(R.id.mytoolbar);
        setSupportActionBar(myToolbar);

        actionBar=getSupportActionBar();

        myToolbar.setBackgroundResource(R.drawable.veritel_background);

        mCoordinatorLayout=findViewById(R.id.containerContactsList);



        // za Api=19 i 20
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT || Build.VERSION.SDK_INT==Build.VERSION_CODES.KITKAT_WATCH) {
            ImageView mStatusBarBackground=findViewById(R.id.status_bar_background_main);
            mStatusBarBackground.setMinimumHeight(getStatusBarHeight());
            Log.v(LOG,getStatusBarHeight()+"visina status bara");
        }

        // ako imas permission ucitaj fragment
        if(savedInstanceState==null){
            if (checkReadContacsPermission()) {
                Log.v(LOG, "permission read contacts true on create");
                getSupportFragmentManager().beginTransaction().add(R.id.containerContactsList, new ContactsFragment()).commit();
            }

        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.main_activity_app_bar,menu);
       mActionSettings= menu.findItem(R.id.action_settings);
        Log.v(LOG,menu.size()+"broj elemenata u meniju");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem){
        int id=menuItem.getItemId();
        if(id==R.id.action_settings){
            Intent startSettingsActivity=new Intent(this,SettingsActivity.class);
            startActivity(startSettingsActivity);
            return true;

        }
        /*if(id==android.R.id.home){

            searchViewActionBar.setQuery("",true);

            Log.v(LOG,"home as up clickde");

            return true;

        }*/
        return true;

    }


    @Override
    public void onContactsFragmentInteraction (int id,String lookup,String name){
        Bundle detailBundle=new Bundle();
        detailBundle.putInt(CONTACT_ID,id);
        detailBundle.putString(CONTACT_LOOKUP_KEY,lookup);
        detailBundle.putString(CONTACT_NAME,name);
        Intent intent=new Intent(this,SingleContactActivity.class);
        intent.putExtras(detailBundle);
        startActivity(intent);

    }


    private boolean checkReadContacsPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS)) {
            Snackbar.make(mCoordinatorLayout, R.string.permission_rationale_contacts, Snackbar.LENGTH_INDEFINITE)
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


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.v(LOG, "permission read contacts callback premission");
                getSupportFragmentManager().beginTransaction().add(R.id.containerContactsList, new ContactsFragment()).commit();

            } else {
                this.finish();
            }

        }
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


    @Override
    public boolean onMenuItemActionExpand(MenuItem item) {

       Log.v(LOG,"u action expand");
        searchViewActionBar = (SearchView) item.getActionView();
        searchViewActionBar.setIconified(false);
        mActionSettings.setVisible(false);
        //Resources res=getContext().getResources();

       // int mHintIcon=getResources().getIdentifier("android:id/search_mag_icon",null,null);
        /*ImageView mHintIconImage = (ImageView) searchViewActionBar.findViewById(mHintIcon);
        mHintIconImage.setImageResource(R.drawable.icons8_phone);*/



        return true;
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem item) {

        Log.v(LOG,"u action colapse");
        searchViewActionBar.setQuery("",true);
        searchViewActionBar.clearFocus();
        mActionSettings.setVisible(true);
        //ovde setujem fragment mCurrentFilterString na prazno
        /*if(searchViewActionBar.isIconified()){
            Log.v(LOG,"isIcon");
        }*/

            return true;

    }


}
