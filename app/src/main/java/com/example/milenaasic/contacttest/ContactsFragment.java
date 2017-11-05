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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class ContactsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>,
        MyContactRecyclerViewAdapter.OnViewHolderClicked {

    private static final String DEBUG="ContactsFragment";
    // interfejs prema Activity koja ga sadrzi
    private OnContactsFragmentInteractionListener mListener;

    //promenljive vezane za UI View elemente
    RecyclerView mRecyclerView;
    MyContactRecyclerViewAdapter mAdapter;
    //SearchView mSearchView;
    private String mSearchString="";

    //loader konstanta
    private static final int LOADER_ID=10;

    //konstante za pretragu ContactsProvidera koja se radi preko Loader-a
    private static final String[] PROJECTION={ContactsContract.Contacts._ID , ContactsContract.Contacts.LOOKUP_KEY,
                                            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY, ContactsContract.Contacts.PHOTO_THUMBNAIL_URI};
    private static final String SELECTION=ContactsContract.Contacts.DISPLAY_NAME_PRIMARY+ " LIKE ?";

    private String[] mSelectionArgs = { "s" };




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
        //mSearchView=rootView.findViewById(R.id.mySearchView);
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
        //mSelectionArgs[0] = "m";
        // da li treba provera mSearchStringa da ne udje nesto bezveze
        return new CursorLoader(
                getActivity(),
                ContactsContract.Contacts.CONTENT_URI,
                PROJECTION,
                null,
                null,
                null);


    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.v(DEBUG,"onLoadFinished");

        if (data!=null) {

            int n=data.getCount();
            Log.v(DEBUG,((Integer)n).toString());
        }
        mAdapter.setOriginalCursor(data);
        /*ovde nekom metodom setujem cursor u vec napravljenom adapteru
        mAdapter=new MyContactRecyclerViewAdapter(data,this);
        mRecyclerView.setAdapter(mAdapter);*/

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
