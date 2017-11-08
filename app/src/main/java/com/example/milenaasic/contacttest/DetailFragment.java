package com.example.milenaasic.contacttest;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



public class DetailFragment extends Fragment {

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
        return inflater.inflate(R.layout.fragment_detail, container, false);
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnDetailFragmentInteractionListener) {
            mListener = (OnDetailFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnDetailFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
