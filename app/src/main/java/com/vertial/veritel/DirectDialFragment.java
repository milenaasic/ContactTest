package com.vertial.veritel;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.telephony.PhoneNumberUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Locale;


public class DirectDialFragment extends Fragment implements View.OnClickListener{


    private static final String LOG = "DirectDialFragment";
    private static final String ARG_PARAM1 = "param1";


    EditText mEditPhoneView;

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public DirectDialFragment() {
        // Required empty public constructor
    }

    public static DirectDialFragment newInstance(String param1, String param2) {
        DirectDialFragment fragment = new DirectDialFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

       View rootView=inflater.inflate(R.layout.fragment_direct_dial, container, false);
        mEditPhoneView=rootView.findViewById(R.id.editPhoneView);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mEditPhoneView.addTextChangedListener(new PhoneNumberFormattingTextWatcher("US"));
        }else{

            mEditPhoneView.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        }


        rootView.findViewById(R.id.button0).setOnClickListener(this);
        rootView.findViewById(R.id.button1).setOnClickListener(this);
        rootView.findViewById(R.id.button2).setOnClickListener(this);
        rootView.findViewById(R.id.button3).setOnClickListener(this);
        rootView.findViewById(R.id.button4).setOnClickListener(this);
        rootView.findViewById(R.id.button5).setOnClickListener(this);
        rootView.findViewById(R.id.button6).setOnClickListener(this);
        rootView.findViewById(R.id.button7).setOnClickListener(this);
        rootView.findViewById(R.id.button8).setOnClickListener(this);
        rootView.findViewById(R.id.button9).setOnClickListener(this);
        rootView.findViewById(R.id.buttonPlus).setOnClickListener(this);
        rootView.findViewById(R.id.buttonDelete).setOnClickListener(this);
        rootView.findViewById(R.id.imageButtonPhone).setOnClickListener(this);
        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
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

    @Override
    public void onClick(View v) {
        int id=v.getId();

        switch(id){

            case R.id.button0:{
                Log.v(LOG," broj 0");
                editPhoneNumber("0");
                return;
            }
            case R.id.button1:{
                Log.v(LOG," broj 1");
                editPhoneNumber("1");
                return;
            }
            case R.id.button2:{
                Log.v(LOG," broj 2");
                editPhoneNumber("2");
                return;
            }
            case R.id.button3:{
                Log.v(LOG," broj 3");
                editPhoneNumber("3");
                return;
            }

            case R.id.button4:{
                Log.v(LOG," broj 4");
                editPhoneNumber("4");
                return;
            }
            case R.id.button5:{
                Log.v(LOG," broj 5");
                editPhoneNumber("5");
                return;
            }
            case R.id.button6:{
                Log.v(LOG," broj 6");
                editPhoneNumber("6");
                return;
            }
            case R.id.button7:{
                Log.v(LOG," broj 7");
                editPhoneNumber("7");

                return;
            }
            case R.id.button8:{
                Log.v(LOG," broj 8");
                editPhoneNumber("8");
                return;
            }
            case R.id.button9:{
                Log.v(LOG," broj 9");
                editPhoneNumber("9");
                return;
            }

            case R.id.buttonPlus:{
                Log.v(LOG," broj +");
                editPhoneNumber("+");
                return;
            }

            case R.id.buttonDelete:{
                Log.v(LOG," broj +");
                editPhoneNumberDelete();
                return;
            }

            case R.id.imageButtonPhone:{
                Log.v(LOG," pozovi");
                //proveri jos jednom broj i napravu poziv
                makeCall();
                return;
            }
        }

    }




    /*@Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        Log.v(LOG," before text Change");
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        Log.v(LOG," on text Change");
    }

    @Override
    public void afterTextChanged(Editable s) {
        Log.v(LOG," after text Change");
    }*/


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void editPhoneNumber(String addedText){
        Editable currentText=mEditPhoneView.getText();
        String rawPhone=currentText+addedText;
        Log.v(LOG," editPhoneNumber "+rawPhone);
        mEditPhoneView.setText(rawPhone, TextView.BufferType.NORMAL);

        /*String formatedPhoneNumber=null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

           formatedPhoneNumber=PhoneNumberUtils.formatNumber(rawPhone, "US");
        Log.v(LOG,"formatedPhoneNumber ="+formatedPhoneNumber);

        } else {

            formatedPhoneNumber=PhoneNumberUtils.formatNumber(rawPhone);

        }

        if(formatedPhoneNumber!=null) {
            Log.v(LOG,"formatedPhoneNumber nije null ="+formatedPhoneNumber);
            mEditPhoneView.setText(formatedPhoneNumber, TextView.BufferType.NORMAL);
        }*/
    }

    private void editPhoneNumberDelete() {


    }


    private void makeCall() {


    }

}




