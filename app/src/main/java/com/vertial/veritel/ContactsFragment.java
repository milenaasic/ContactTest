package com.vertial.veritel;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.Toast;


public class ContactsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>,
        MyContactRecyclerViewAdapter.OnViewHolderClicked,SearchView.OnQueryTextListener,SearchView.OnCloseListener, SwipeController.ViewHolderSwipedListener,
        SharedPreferences.OnSharedPreferenceChangeListener,View.OnClickListener {

    private static final String DEBUG = "ContactsFragment";

    private static final int REQUEST_PHONE_CALL = 10;
    String veriTelTelefon;
    String chosenPhoneNumber="";
    // interfejs prema Activity koja ga sadrzi
    private OnContactsFragmentInteractionListener mListener;

    //promenljive vezane za UI View elemente
    RecyclerView mRecyclerView;
    MyContactRecyclerViewAdapter mAdapter;
    SwipeController swipeController;
    TextView nbOfFilteredItems;
    MenuItem searchViewItem;
    SearchView searchViewAndroidActionBar;
    ImageView mCloseButtonImage;
    int mSearchCloseButton;


    //SearchView mSearchView;
    //private SearchView mSearchView;
     String mCurrentFilter;

    //loader konstanta
    private static final int LOADER_ID = 10;

    //konstante za pretragu ContactsProvidera koja se radi preko Loader-a
    private static final String[] PROJECTION = {
            ContactsContract.Contacts._ID,
            ContactsContract.Contacts.LOOKUP_KEY,
            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY,
            ContactsContract.Contacts.PHOTO_THUMBNAIL_URI,
            ContactsContract.Contacts.PHOTO_URI};
    private static final String SELECTION = ContactsContract.Contacts.DISPLAY_NAME_PRIMARY + "LIKE ?";

    private String[] mSelectionArgs;

    //Cursor koji prosledjujem u Adapter
    private Cursor filterCursor;
    private static final int CURSOR_COLUMN_ID = 0;
    private static final int CURSOR_COLUMN_LOOKUP = 1;
    private static final int CURSOR_DISPLAY_NAME_PRIMARY = 2;
    private static final int CURSOR_PHOTO_THUMBNAIL_URI = 3;
    private static final int CURSOR_PHOTO_URI = 4;


    public ContactsFragment() {
    }


    public static ContactsFragment newInstance() {
        ContactsFragment fragment = new ContactsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setUpPreferences();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_contact_list, container, false);

        mRecyclerView = rootView.findViewById(R.id.myRecylerView);
        /*mSearchView = rootView.findViewById(R.id.mySearchView);
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setSubmitButtonEnabled(false);*/


        nbOfFilteredItems = rootView.findViewById(R.id.nbFilteredItems);

        Log.v(DEBUG, "onCreateView");
        return rootView;
    }



    private void setUpPreferences() {

        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(getActivity());
        veriTelTelefon=sharedPreferences.getString(String.valueOf(R.string.list_preference_phones_key), getResources().getString(R.string.pref_list_default_value));

        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }




    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mAdapter = new MyContactRecyclerViewAdapter(null, this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);
        //setujem podrski za Swipe

        swipeController = new SwipeController(0, ItemTouchHelper.RIGHT, this);
        ItemTouchHelper mIth = new ItemTouchHelper(swipeController);

        mIth.attachToRecyclerView(mRecyclerView);

        getLoaderManager().initLoader(LOADER_ID, null, this);


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnContactsFragmentInteractionListener) {
            mListener = (OnContactsFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnContactsFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        mAdapter.setFilterCursorAndFilterString(null, null);
        PreferenceManager.getDefaultSharedPreferences(getActivity()).unregisterOnSharedPreferenceChangeListener(this);

    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onStop() {
        super.onStop();
        //searchViewAndroidActionBar.clearFocus();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_contacts_app_bar,menu);
        MenuItem searchViewItem = menu.findItem(R.id.app_bar_search);
        searchViewItem.setOnActionExpandListener((MenuItem.OnActionExpandListener) getActivity());

        searchViewAndroidActionBar = (SearchView) searchViewItem.getActionView();

        searchViewAndroidActionBar.setQueryHint("Search");
        searchViewAndroidActionBar.setOnQueryTextListener(this);
        setSearchWidget(searchViewAndroidActionBar);
        searchViewAndroidActionBar.clearFocus();

    }

    private void setSearchWidget(SearchView searchView) {
        Resources res=searchView.getContext().getResources();

        mSearchCloseButton=res.getIdentifier("android:id/search_close_btn",null,null);
        mCloseButtonImage = (ImageView) searchView.findViewById(mSearchCloseButton);
        mCloseButtonImage.setOnClickListener((View.OnClickListener) this);

    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();

        if(id==R.id.app_bar_search){

            return true;

        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.v(DEBUG, "onCreateCursor");

        Uri baseUri;
        if (mCurrentFilter != null) {
            baseUri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_FILTER_URI,
                    Uri.encode(mCurrentFilter));
        } else {
            baseUri = ContactsContract.Contacts.CONTENT_URI;
        }

        String select = "((" + ContactsContract.Contacts.DISPLAY_NAME_PRIMARY + " NOTNULL) AND ("
                + ContactsContract.Contacts.HAS_PHONE_NUMBER + "=1) AND ("
                + ContactsContract.Contacts.DISPLAY_NAME_PRIMARY + " != '' ))";

        return new CursorLoader(getActivity(), baseUri,
                PROJECTION, select, null,
                ContactsContract.Contacts.DISPLAY_NAME_PRIMARY + " COLLATE LOCALIZED ASC");

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.v(DEBUG, "onLoadFinished");

        if (data != null) {

            int n = data.getCount();
            Log.v(DEBUG, ((Integer) n).toString() + "u onLoadFinish");
        }
        filterCursor = data;

        mAdapter.setFilterCursorAndFilterString(filterCursor, mCurrentFilter);

        nbOfFilteredItems.setText(((Integer) data.getCount()).toString() + " " + getString(R.string.foundContacts));
    }


    @Override
    public void viewHolderClicked(View v, int position) {

        // Toast.makeText(getActivity(),"cliced item nb "+((Integer)position).toString(),Toast.LENGTH_SHORT).show();

        if (filterCursor != null && filterCursor.getCount() != 0) {
            if (filterCursor.moveToPosition(position)) {

                int contactId = filterCursor.getInt(CURSOR_COLUMN_ID);
                String contactLookupKey = filterCursor.getString(CURSOR_COLUMN_LOOKUP);
                String name = filterCursor.getString(CURSOR_DISPLAY_NAME_PRIMARY);
                mListener.onContactsFragmentInteraction(contactId, contactLookupKey, name);


            }
        }

    }

    @Override
    public boolean onQueryTextSubmit(String query) {

        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        // Called when the action bar search text has changed.  Update
        // the search filter, and restart the loader to do a new query
        // with this filter.
        String newFilter = !TextUtils.isEmpty(newText) ? newText : null;
        // Don't do anything if the filter hasn't actually changed.
        // Prevents restarting the loader when restoring state.
        if (mCurrentFilter == null && newFilter == null) {
            return true;
        }
        if (mCurrentFilter != null && mCurrentFilter.equals(newFilter)) {
            return true;
        }

        mCurrentFilter = newFilter;


        Log.v(DEBUG, mCurrentFilter + " =mCurrentFilter");
        getLoaderManager().restartLoader(0, null, this);
        return true;

    }


    @Override
    public boolean onClose() {

        return true;
    }




    @Override
    public void onViewHolderSwiped(RecyclerView.ViewHolder viewHolder,int adapterPosition) {

        if(filterCursor.moveToPosition(adapterPosition)) {
            String swipeContactId = filterCursor.getString(CURSOR_COLUMN_ID);
            Cursor c = getActivity().getContentResolver().query(ContactsContract.Data.CONTENT_URI,
                    new String[]{ContactsContract.Data._ID, ContactsContract.CommonDataKinds.Phone.NUMBER},
                    ContactsContract.Data.CONTACT_ID + "=?" + " AND "
                            + ContactsContract.Data.MIMETYPE + "='" + ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE + "'",
                    new String[]{String.valueOf(swipeContactId)}, null);

            try{
                if(c != null && c.moveToFirst() && !c.isNull(1)) {
                    chosenPhoneNumber=c.getString(1);
                    Log.v(DEBUG, "telefon posel swipe"+chosenPhoneNumber);
                }
            }finally {
                if (c!=null)c.close();

            }

        }

        if (checkPhoneCallPermission()) {

            // ako imas dozvolu pozovi telefonski broj iz prosledjene adapterPosition

            if(chosenPhoneNumber!=null) {

                Intent intentToCall = new Intent(Intent.ACTION_CALL);
                String telefon = veriTelTelefon + chosenPhoneNumber + "#";
                Log.v(DEBUG, "on swipe and has permission : " + telefon);
                intentToCall.setData(Uri.parse(telefon));
                if (intentToCall.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(intentToCall);
                }else{
                    Toast.makeText(getActivity(),getString(R.string.toast_unable_to_resolve_activity),Toast.LENGTH_SHORT).show();
                }
            }


        }

        mAdapter.notifyItemChanged(adapterPosition);

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Log.v(DEBUG,"veriTelTelefon id on Sharedpreferen pre if ");
        if(key.equals(getString(R.string.list_preference_phones_key))){

            veriTelTelefon=sharedPreferences.getString(key,getResources().getString(R.string.pref_list_default_value));
            Log.v(DEBUG,"veriTelTelefon id on Sharedpreferen "+veriTelTelefon);
        }
    }

    @Override
    public void onClick(View v) {
        int id=v.getId();
        if(id==mSearchCloseButton){

        if(searchViewAndroidActionBar.getQuery()==null){
            return;
        }else{
            searchViewAndroidActionBar.setQuery("",false);
        }


        }
    }




    // interfejs prema Activity koja ga sadrzi
    public interface OnContactsFragmentInteractionListener {
        // šaljem ka Mainacitivy informaciju na osnovu koje će otvoriti Detail fragment
        // ne znam još šta ću poslati , koji tip podatka
        void onContactsFragmentInteraction(int id, String lookup, String name);
    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // This is called when the last Cursor provided to onLoadFinished()
        // above is about to be closed.  We need to make sure we are no
        // longer using it.
        mAdapter.setFilterCursorAndFilterString(null, null);
    }


    private boolean checkPhoneCallPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(Manifest.permission.CALL_PHONE)) {
            Snackbar.make(getActivity().findViewById(R.id.containerContactsList), R.string.permission_rationale_phone_call, Snackbar.LENGTH_INDEFINITE)
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
                Log.v(DEBUG, "permission call phone callback premission");

                Intent intentToCall = new Intent(Intent.ACTION_CALL);

                String telefon = veriTelTelefon + chosenPhoneNumber + "#";
                Log.v(DEBUG, "on rq per result and has permission : " + telefon);

                intentToCall.setData(Uri.parse(telefon));

                if (intentToCall.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(intentToCall);
                }else{
                    Toast.makeText(getActivity(),getString(R.string.toast_unable_to_resolve_activity),Toast.LENGTH_SHORT).show();
                }

            }
        } else {
                Toast.makeText(getActivity(), "permission not granted", Toast.LENGTH_SHORT).show();
                Log.v(DEBUG, "nema dozvolu za poziv");
            }


        }






}
