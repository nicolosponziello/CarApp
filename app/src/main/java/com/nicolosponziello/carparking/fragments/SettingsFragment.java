package com.nicolosponziello.carparking.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.nicolosponziello.carparking.R;

public class SettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
        findPreference("app_intro").setOnPreferenceClickListener((preference) ->{
            SharedPreferences preferences = getActivity().getSharedPreferences(getString(R.string.shared_prefs), Context.MODE_PRIVATE);
            preferences.edit().putBoolean(getString(R.string.intro_shown), false).commit();
            return true;
        });
    }
}
