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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>,
        SharedPreferences.OnSharedPreferenceChangeListener,PhonesRecyclerViewAdapter.OnPhoneViewHolderClicked{

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


    /*TextView phoneNumber0;
    ConstraintLayout cardView0;
    private String contactNumber;*/
    private String veriTelTelefon;

    ConstraintLayout mMyConstraintLayout;
    RecyclerView mPhonesRecyclerView;
    PhonesRecyclerViewAdapter mPhonesRecyclerViewAdapter;
    private String chosenPhoneNumber;


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
        mMyConstraintLayout=rootView.findViewById(R.id.myConstraintLayout);
        mPhonesRecyclerView=rootView.findViewById(R.id.phonesRecyclerView);



        ImageView mFullPictureImage=rootView.findViewById(R.id.fullPictureImage);
        ImageView mimageView_ThumbPhoto_outer=rootView.findViewById(R.id.imageView_ThumbPhoto_outer);
        ImageView mimageView_ThumbPhoto_inner=rootView.findViewById(R.id.imageView_ThumbPhoto_Inner);

        ImageView mStatusBarBackground=getActivity().findViewById(R.id.status_bar_background);

        // Uri kontakta za koji se otvara DetailFragment
        Uri contactUri= ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI,contactId);
        //Uri thumnaila i velike slike za taj kontakt
        Uri fullSizePhoto=Uri.withAppendedPath(contactUri,ContactsContract.Contacts.Photo.DISPLAY_PHOTO);
        //Uri thumbSizePhoto=Uri.withAppendedPath(contactUri,ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);

        //provera da li postoji velika slika
        Cursor photocursor=getActivity().getContentResolver().query(contactUri,
                new String[] {ContactsContract.Contacts.PHOTO_URI},
                null, null, null);

        if(photocursor!=null && photocursor.moveToFirst()&& !photocursor.isNull(0)){

                    GlideApp.with(this)
                        .load(fullSizePhoto)
                        .error(R.color.colorPrimary)
                        .into(mFullPictureImage);

                    mimageView_ThumbPhoto_inner.setVisibility(View.GONE);
                    mimageView_ThumbPhoto_outer.setVisibility(View.GONE);
                    Log.v(DEBUG,"full size photo loaded");

                    displayName.setBackgroundColor(getResources().getColor(R.color.greyTextBackgroundColor));
                    mStatusBarBackground.setBackgroundColor(Color.TRANSPARENT);

        }else{

            displayName.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            //

            mimageView_ThumbPhoto_inner.setVisibility(View.GONE);
            mimageView_ThumbPhoto_outer.setVisibility(View.GONE);
            mStatusBarBackground.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            Log.v(DEBUG,"set backround color");

        }

        displayName.setText(contactName);


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
        // ovde setujem Recyclerview za telefone
        mPhonesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mPhonesRecyclerViewAdapter=new PhonesRecyclerViewAdapter(null,this);
        mPhonesRecyclerView.setAdapter(mPhonesRecyclerViewAdapter);

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
            mPhonesRecyclerViewAdapter.setPhoneCursor(data);
        }

    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mPhonesRecyclerViewAdapter.setPhoneCursor(null);
    }

    //@Override
   /* public void onClick(View v) {
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

    }*/

    /*private String normilizeNumber(String phoneNumber0) {
       String numberToCall=phoneNumber0.getText().toString();
        Log.v(DEBUG,numberToCall);
        return numberToCall+"#";}*/


    // preferences ucitavanje
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(key.equals(getString(R.string.list_preference_phones_key))){

            veriTelTelefon=sharedPreferences.getString(key,"greska");
        }

    }

    @Override
    public void onPhoneItemClicked(String s) {
        chosenPhoneNumber=s;
        if (checkPhoneCallPermission()) {

            Intent intentToCall = new Intent(Intent.ACTION_CALL);

            String telefon = veriTelTelefon + chosenPhoneNumber + "#";
            Log.v(DEBUG, "veritel telefon : " + telefon);
            intentToCall.setData(Uri.parse(telefon));

            startActivity(intentToCall);


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
            Snackbar.make(mMyConstraintLayout, R.string.permission_rationale_phone_call, Snackbar.LENGTH_INDEFINITE)
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
                //String normilizedNumber = normilizeNumber("phoneNumber0");
                Intent intentToCall = new Intent(Intent.ACTION_CALL);
                String telefon = veriTelTelefon + chosenPhoneNumber + "#";
                Log.v(DEBUG, "veritel telefon : " + telefon);
                intentToCall.setData(Uri.parse(telefon));
                startActivity(intentToCall);

            }
        }else {
            Log.v(DEBUG,"nema dozvolu za poyiv");
        };
    }


}
