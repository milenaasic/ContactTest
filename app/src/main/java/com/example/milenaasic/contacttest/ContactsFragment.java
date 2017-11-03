package com.example.milenaasic.contacttest;

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
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class ContactsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>,
        MyContactRecyclerViewAdapter.OnViewHolderClicked {


    // interfejs prema Activity koja ga sadrzi
    private OnContactsFragmentInteractionListener mListener;

    //promenljive vezane za UI View elemente
    RecyclerView mRecyclerView;
    MyContactRecyclerViewAdapter mAdapter;
    SearchView mSearchView;
    private String mSearchString="";

    //loader konstanta
    private static final int LOADER_ID=10;

    //konstante za pretragu ContactsProvidera koja se radi preko Loader-a
    private static final String[] PROJECTION={ContactsContract.Contacts._ID , ContactsContract.Contacts.LOOKUP_KEY,
                                            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY, ContactsContract.Contacts.PHOTO_THUMBNAIL_URI};
    private static final String SELECTION=ContactsContract.Contacts.DISPLAY_NAME_PRIMARY+ " LIKE ?";
    private String[] mSelectionArgs;





    public ContactsFragment() {
    }


    public static ContactsFragment newInstance() {
        ContactsFragment fragment = new ContactsFragment();
       /* Bundle args = new Bundle();
        args.putInt(ARG, arg);
        fragment.setArguments(args);*/
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

        return rootView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getLoaderManager().initLoader(LOADER_ID,null,this);

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

        mSelectionArgs[0] = "%" + mSearchString + "%";
        // da li treba provera mSearchStringa da ne udje nesto bezveze
        return new CursorLoader(
                getActivity(),
                ContactsContract.Contacts.CONTENT_URI,
                PROJECTION,
                SELECTION,
                mSelectionArgs,
                null);


    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        mAdapter=new MyContactRecyclerViewAdapter(data,this);
        mRecyclerView.setAdapter(mAdapter);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }


    @Override
    public void viewHolderClicked(View v,int position) {

    }

    // interfejs prema Activity koja ga sadrzi
    public interface OnContactsFragmentInteractionListener {
        // šaljem ka Mainacitivy informaciju na osnovu koje će otvoriti Detail fragment
        // ne znam još šta ću poslati , koji tip podatka
        void onContactsFragmentInteraction(Uri itemUri);
    }
}
