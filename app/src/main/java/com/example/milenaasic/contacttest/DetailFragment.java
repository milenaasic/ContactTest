package com.example.milenaasic.contacttest;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.annotation.GlideModule;

import static android.Manifest.permission.READ_CONTACTS;


public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener ,
        SharedPreferences.OnSharedPreferenceChangeListener{

    private static final String DEBUG="detailfragment";

    private static final int DETAIL_LOADER_ID=15;
    private static final int REQUEST_PHONE_CALL=100;

    private static final String ARG_PARAM1 = "id";
    private static final String ARG_PARAM2 = "lookup";
    private static final String ARG_PARAM3 = "name";

    // TODO: Rename and change types of parameters
    private int contactId ;
    private String contactLookupKey;
    private String contactName;

    //broj koji se poziva



    private OnDetailFragmentInteractionListener mListener;

    //pretraga telefona na osnovu ID-a i LOOKUP_KEY-a
    private static final String[]PROJECTION_PHONES={
            ContactsContract.CommonDataKinds.Phone._ID,
            ContactsContract.CommonDataKinds.Phone.NUMBER,
            ContactsContract.CommonDataKinds.Phone.TYPE,
            ContactsContract.CommonDataKinds.Phone.LABEL};

    private static final int CURSOR_PHONE_ID=0;
    private static final int CURSOR_PHONE_NUMBER=1;
    private static final int CURSOR_PHONE_TYPE=2;
    private static final int CURSOR_PHONE_LABEL=3;

    private static String SELECTION_PHONES= ContactsContract.Data.LOOKUP_KEY+"=?" +
            " AND " + ContactsContract.Data.MIMETYPE + " = " +
            "'" + ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE + "'";
    private String [] phoneSelectionArguments={""};


    TextView phoneNumber0;
    ConstraintLayout cardView0;
    private String contactNumber;
    private String veriTelTelefon;


    public DetailFragment() {
        // Required empty public constructor
    }

    public static DetailFragment newInstance(int param1,String param2,String param3) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putString(ARG_PARAM3,param3);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            contactId = getArguments().getInt(ARG_PARAM1);
            contactLookupKey = getArguments().getString(ARG_PARAM2);
            contactName = getArguments().getString(ARG_PARAM3);
        }

        setUpPreferences();

    }

    private void setUpPreferences() {

        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(getActivity());
        veriTelTelefon=sharedPreferences.getString( "list_preference_phones", "greska");

        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView=inflater.inflate(R.layout.fragment_detail, container, false);
        TextView displayName =rootView.findViewById(R.id.mytextView);
        displayName.setText(contactName);
        phoneNumber0=rootView.findViewById(R.id.phonetextView0);
        ImageView image=rootView.findViewById(R.id.myimageView);
         cardView0=rootView.findViewById(R.id.myCardContact0);
        cardView0.setOnClickListener(this);
        //prikazi sliku ako je ima

        Uri contactUri= ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI,contactId);
        Uri photoThumbUri=Uri.withAppendedPath(contactUri,ContactsContract.Contacts.Photo.DISPLAY_PHOTO);
        //Uri photoThumbUri2=Uri.withAppendedPath(contactUri,ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);

        GlideApp.with(this)
                .load(photoThumbUri)
                .centerCrop()
                .error(R.color.colorPrimary)
                .into(image);

        return rootView;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnDetailFragmentInteractionListener) {
            mListener = (OnDetailFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnDetailFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        PreferenceManager.getDefaultSharedPreferences(getActivity()).unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getLoaderManager().initLoader(DETAIL_LOADER_ID,null,this);


    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        phoneSelectionArguments[0] = contactLookupKey;
        // Starts the query

             return    new CursorLoader(
                        getActivity(),
                        ContactsContract.Data.CONTENT_URI,
                        PROJECTION_PHONES,
                        SELECTION_PHONES,
                        phoneSelectionArguments,
                        null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        if (data!=null) {

            int n=data.getCount();
            Log.v(DEBUG,((Integer)n).toString()+" broj redova u onLoadFinish");
        }

        if (data.getCount()!=0){
            for(int i=0;i<data.getCount();i++){

                if(data.moveToPosition(i)){
                    contactNumber = data.getString(CURSOR_PHONE_NUMBER);
                    Log.v(DEBUG,"contact number broj " +i+" "+contactNumber);
                    phoneNumber0.setText(contactNumber);
                    int phoneType = data.getInt(CURSOR_PHONE_TYPE);
                    Log.v(DEBUG,"contact number tip " +i+((Integer)phoneType).toString());

                }
            }


        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onClick(View v) {
        //pritisnut je Card View sa brojem, pozovi

        if(checkPhoneCallPermission()) {
            switch (v.getId()) {
                case R.id.myCardContact0: {
                    String normilizedNumber = normilizeNumber(phoneNumber0);
                    Intent intentToCall=new Intent(Intent.ACTION_CALL);

                    String telefon=veriTelTelefon+normilizedNumber;
                    Log.v(DEBUG,"veritel telefon : "+telefon);
                    intentToCall.setData(Uri.parse(telefon));

                    startActivity(intentToCall);

                }
                default: {
                    Log.v(DEBUG,"switch mycardContact0 nije");
                }

            }
        }

    }

    private String normilizeNumber(TextView phoneNumber0) {
       String numberToCall=phoneNumber0.getText().toString();
        Log.v(DEBUG,numberToCall);
        return numberToCall+"#";}


    // preferences ucitavanje
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(key.equals(getString(R.string.list_preference_phones_key))){

            veriTelTelefon=sharedPreferences.getString(key,"greska");
        }

    }


    public interface OnDetailFragmentInteractionListener {
        void onDetailFragmentInteraction(Uri uri);
    }



    private boolean checkPhoneCallPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(Manifest.permission.CALL_PHONE)) {
            Snackbar.make(cardView0, R.string.permission_rationale_phone_call, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override


                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PHONE_CALL);
                        }
                    }).show();
        } else {
            requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PHONE_CALL);
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PHONE_CALL) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.v(DEBUG,"permission call phone callback premission");
                String normilizedNumber = normilizeNumber(phoneNumber0);
                Intent intentToCall=new Intent(Intent.ACTION_CALL);
                String telefon="tel:0113108888,,9";
                intentToCall.setData(Uri.parse(telefon));

                startActivity(intentToCall);

            }
        }else {
            Log.v(DEBUG,"nema dozvolu za poyiv");
        };
    }


}
