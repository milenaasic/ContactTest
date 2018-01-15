package com.vertial.veritel;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;
import android.util.Log;


public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener{




    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

        addPreferencesFromResource(R.xml.preference_veritel_phones);

        SharedPreferences sharedPreferences=getPreferenceScreen().getSharedPreferences();
        PreferenceScreen preferenceScreen=getPreferenceScreen();
        Preference p=preferenceScreen.getPreference(0);

        String value=sharedPreferences.getString(p.getKey(),getResources().getString(R.string.pref_list_default_value));

        setPreferenceSummary(p,value);


    }

    private  void setPreferenceSummary(Preference preference,String value){

        if(preference instanceof ListPreference){
            ListPreference listPreference=(ListPreference)preference;
            int prefIndex=listPreference.findIndexOfValue(value);

            if(prefIndex>=0){

                listPreference.setSummary(listPreference.getEntries()[prefIndex]);
            }
        }


    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        Preference preference=findPreference(key);

        if(preference!=null) {

            //String value = sharedPreferences.getString(preference.getKey(), "");
            String value = sharedPreferences.getString(key, getResources().getString(R.string.pref_list_default_value));
            setPreferenceSummary(preference, value);

        }



    }

    @Override
    public  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

    }

    @Override
    public void onDestroy(){
        super.onDestroy();

        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);


    }

}




