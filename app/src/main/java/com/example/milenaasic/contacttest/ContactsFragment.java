package com.example.milenaasic.contacttest;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


public class ContactsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>,
        MyContactRecyclerViewAdapter.OnViewHolderClicked,SearchView.OnQueryTextListener {

    private static final String DEBUG="ContactsFragment";
    // interfejs prema Activity koja ga sadrzi
    private OnContactsFragmentInteractionListener mListener;

    //promenljive vezane za UI View elemente
    RecyclerView mRecyclerView;
    MyContactRecyclerViewAdapter mAdapter;

    //SearchView mSearchView;
    private SearchView mSearchView;
    private String mCurrentFilter;

    //loader konstanta
    private static final int LOADER_ID=10;

    //konstante za pretragu ContactsProvidera koja se radi preko Loader-a
    private static final String[] PROJECTION={  ContactsContract.Contacts._ID ,
                                                ContactsContract.Contacts.LOOKUP_KEY,
                                                ContactsContract.Contacts.DISPLAY_NAME_PRIMARY,
                                                ContactsContract.Contacts.PHOTO_THUMBNAIL_URI};
    private static final String SELECTION=ContactsContract.Contacts.DISPLAY_NAME_PRIMARY+ "LIKE ?";

    private String[] mSelectionArgs;

    //Cursor koji prosledjujem u Adapter
    private Cursor filterCursor;
    private static final int CURSOR_COLUMN_ID=0;
    private static final int CURSOR_COLUMN_LOOKUP=1;
    private static final int CURSOR_DISPLAY_NAME_PRIMARY=2;
    private static final int CURSOR_PHOTO_THUMBNAIL_URI=3;





    public ContactsFragment() {
    }


    public static ContactsFragment newInstance() {
        ContactsFragment fragment = new ContactsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_contact_list, container, false);

        mRecyclerView=rootView.findViewById(R.id.myRecylerView);
        mSearchView=rootView.findViewById(R.id.mySearchView);
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setSubmitButtonEnabled(false);
        Log.v(DEBUG,"onCreateView");
        return rootView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getLoaderManager().initLoader(LOADER_ID,null,this);

        mAdapter=new MyContactRecyclerViewAdapter(null,this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);

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
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.v(DEBUG,"onCreateCursor");

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
        Log.v(DEBUG,"onLoadFinished");

        if (data!=null) {

            int n=data.getCount();
            Log.v(DEBUG,((Integer)n).toString()+"u onLoadFinish");
        }
        filterCursor=data;
        mAdapter.setFilterCursor(filterCursor);

    }




    @Override
    public void viewHolderClicked(View v,int position) {

        Toast.makeText(getActivity(),"cliced item nb "+((Integer)position).toString(),Toast.LENGTH_SHORT).show();

        if (filterCursor != null && filterCursor.getCount() != 0) {
            if (filterCursor.moveToPosition(position)) {

                int contactId=filterCursor.getInt(CURSOR_COLUMN_ID);
                int contactLookupKey=filterCursor.getInt(CURSOR_COLUMN_LOOKUP);
                String name = filterCursor.getString(CURSOR_DISPLAY_NAME_PRIMARY);
                mListener.onContactsFragmentInteraction(contactId,contactLookupKey,name);


            }
        }

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return true;
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
        Log.v(DEBUG,mCurrentFilter+" =mCurrentFilter");
        getLoaderManager().restartLoader(0, null, this);
        return true;

    }

    // interfejs prema Activity koja ga sadrzi
    public interface OnContactsFragmentInteractionListener {
        // šaljem ka Mainacitivy informaciju na osnovu koje će otvoriti Detail fragment
        // ne znam još šta ću poslati , koji tip podatka
        void onContactsFragmentInteraction(int id, int lookup,String name);
    }



    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // This is called when the last Cursor provided to onLoadFinished()
        // above is about to be closed.  We need to make sure we are no
        // longer using it.
        mAdapter.setFilterCursor(null);
    }


}
