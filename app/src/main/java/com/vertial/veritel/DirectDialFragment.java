package com.vertial.veritel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.preference.PreferenceManager;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.telephony.PhoneNumberUtils;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;


public class DirectDialFragment extends Fragment implements View.OnClickListener{


    private static final String LOG = "DirectDialFragment";
    private static final String ARG_PARAM1 = "param1";


    EditText mEditPhoneView;
    String veriTelTelefon;

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public DirectDialFragment() {



    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setUpPreferences();
    }

    private void setUpPreferences() {
        if(getActivity()!=null) {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            veriTelTelefon = sharedPreferences.getString(getResources().getString(R.string.list_preference_phones_key), getResources().getString(R.string.pref_list_default_value));
            Log.v(LOG, "value of list key: " + String.valueOf(R.string.list_preference_phones_key));
            Log.v(LOG, "veritelTelefon u set up pref " + veriTelTelefon);
        }
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

       View rootView=inflater.inflate(R.layout.fragment_direct_dial, container, false);
        mEditPhoneView=rootView.findViewById(R.id.editPhoneView);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mEditPhoneView.addTextChangedListener(new PhoneNumberFormattingTextWatcher("US"));
            mEditPhoneView.setShowSoftInputOnFocus(false);

        }else{
            /*InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(mEditPhoneView.getWindowToken(), 0);
            //mEditPhoneView.setFocusable(false)*/
            mEditPhoneView.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
            mEditPhoneView.setInputType(InputType.TYPE_NULL);

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
                if(mEditPhoneView.getText().length()>0 && mEditPhoneView!=null) {
                    editPhoneNumberDelete();
                    return;
                }else {
                    return;
                }
            }

            case R.id.imageButtonPhone:{
                Log.v(LOG," pozovi");
                //proveri jos jednom broj i napravu poziv
                makeCall();
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



        if(addedText.equals("+")&& currentText.length()>=1) {

        }else{
             currentText.append(addedText);
            Log.v(LOG, " editPhoneNumber " + currentText);
            //mEditPhoneView.setText(currentText, TextView.BufferType.NORMAL);
        }


    }

    private void editPhoneNumberDelete() {

        Editable currentText=mEditPhoneView.getText();
        String normalized=currentText.toString();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            normalized=PhoneNumberUtils.normalizeNumber(normalized);

        }else{
            normalized=normalizeRawPhoneNumber(normalized);
        }

        if ( currentText.length()>0) {

            StringBuilder stringBuilder=new StringBuilder(normalized);
            stringBuilder.deleteCharAt(stringBuilder.length()-1);

            currentText.clear();
            currentText.append(stringBuilder);



        }
    }


    private void makeCall() {

        String rawPhoneNumber=mEditPhoneView.getText().toString();

        if(rawPhoneNumber.length()>=10) {
            Intent intentToCall = new Intent(Intent.ACTION_CALL);

            String normalizedPhoneNumber;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                normalizedPhoneNumber = PhoneNumberUtils.normalizeNumber(rawPhoneNumber);

            } else {
                normalizedPhoneNumber = normalizeRawPhoneNumber(rawPhoneNumber);
            }

            String telefon = veriTelTelefon + normalizedPhoneNumber + "#";
            Log.v(LOG, "veritel telefon : " + telefon);
            intentToCall.setData(Uri.parse(telefon));

            if (intentToCall.resolveActivity(getActivity().getPackageManager()) != null) {
                startActivity(intentToCall);
            } else {
                Toast.makeText(getActivity(), getString(R.string.toast_unable_to_resolve_activity), Toast.LENGTH_SHORT).show();
            }

        }


    }

    private String normalizeRawPhoneNumber(String rawPhoneNumber) {

        char[] rawNumberArray=rawPhoneNumber.toCharArray();
        StringBuilder normalizedNumber=new StringBuilder();

        if (rawNumberArray[0]=='+'){
            normalizedNumber.append(rawNumberArray[0]);
        }

        for(char item:rawNumberArray){

            if(Character.isDigit(item)){
                normalizedNumber.append(item);

            }
        }


        return normalizedNumber.toString();
    }

}




