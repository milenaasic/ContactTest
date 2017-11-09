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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final String DEBUG="detailfragment";

    private static final int DETAIL_LOADER_ID=15;

    private static final String ARG_PARAM1 = "id";
    private static final String ARG_PARAM2 = "lookup";
    private static final String ARG_PARAM3 = "name";

    // TODO: Rename and change types of parameters
    private int contactId ;
    private int contactLookupKey;
    private String contactName;


    private OnDetailFragmentInteractionListener mListener;

    //pretraga telefona na osnovu ID-a i LOOKUP_KEY-a
    private static final String[]PROJECTION_PHONES={ContactsContract.CommonDataKinds.Phone._ID,
            ContactsContract.CommonDataKinds.Phone.NUMBER,
            ContactsContract.CommonDataKinds.Phone.TYPE,
            ContactsContract.CommonDataKinds.Phone.LABEL};

    private static final int CURSOR_PHONE_ID=0;
    private static final int CURSOR_PHONE_NUMBER=1;
    private static final int CURSOR_PHONE_TYPE=2;
    private static final int CURSOR_PHONE_LABEL=3;

    private static String SELECTION_PHONES= ContactsContract.Data.LOOKUP_KEY+"=?";
    private String [] phoneSelectionArguments={""};
    private String mLookupKey;


    TextView phoneNumber;


    public DetailFragment() {
        // Required empty public constructor
    }

    public static DetailFragment newInstance(int param1,int param2,String param3) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        args.putInt(ARG_PARAM2, param2);
        args.putString(ARG_PARAM3,param3);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            contactId = getArguments().getInt(ARG_PARAM1);
            contactLookupKey = getArguments().getInt(ARG_PARAM2);
            contactName = getArguments().getString(ARG_PARAM3);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView=inflater.inflate(R.layout.fragment_detail, container, false);
        TextView displayName =rootView.findViewById(R.id.mytextView);
        displayName.setText(contactName);
        phoneNumber=rootView.findViewById(R.id.phonetextView);



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
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getLoaderManager().initLoader(DETAIL_LOADER_ID,null,this);


    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        phoneSelectionArguments[0] = mLookupKey;
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
            Log.v(DEBUG,((Integer)n).toString()+"u onLoadFinish");
        }

        if (data.moveToPosition(0)) {

            String contactNumber = data.getString(CURSOR_PHONE_NUMBER);
            phoneNumber.setText(contactNumber);

        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }


    public interface OnDetailFragmentInteractionListener {
        void onDetailFragmentInteraction(Uri uri);
    }
}
