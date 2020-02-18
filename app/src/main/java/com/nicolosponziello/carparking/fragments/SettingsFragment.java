package com.nicolosponziello.carparking.fragments;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.nicolosponziello.carparking.MainActivity;
import com.nicolosponziello.carparking.R;
import com.nicolosponziello.carparking.database.DatabaseHelper;

public class SettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
        findPreference(getString(R.string.show_intro_key)).setOnPreferenceClickListener((preference) ->{
            SharedPreferences preferences = getActivity().getSharedPreferences(getString(R.string.shared_prefs), Context.MODE_PRIVATE);
            preferences.edit().putBoolean(getString(R.string.intro_shown), false).commit();
            return true;
        });
        findPreference(getString(R.string.delete_db_key)).setOnPreferenceClickListener(preference -> {
            getActivity().getApplicationContext().deleteDatabase(DatabaseHelper.DATABASE_NAME);

            //restart app after deleteting database
            Intent start = new Intent(getContext(), MainActivity.class);
            int id = 111;
            PendingIntent pendingIntent = PendingIntent.getActivity(getContext(), id, start, PendingIntent.FLAG_CANCEL_CURRENT);
            AlarmManager manager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
            manager.set(AlarmManager.RTC, System.currentTimeMillis() + 100, pendingIntent);

            //exit
            System.exit(0);
            return true;
        });
    }
}
