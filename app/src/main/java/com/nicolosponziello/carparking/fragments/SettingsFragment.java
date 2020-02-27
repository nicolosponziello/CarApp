package com.nicolosponziello.carparking.fragments;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;

import com.google.firebase.auth.FirebaseAuth;
import com.nicolosponziello.carparking.MainActivity;
import com.nicolosponziello.carparking.R;
import com.nicolosponziello.carparking.activity.LoginRegistrationActivity;
import com.nicolosponziello.carparking.database.DatabaseHelper;
import com.nicolosponziello.carparking.dialogs.ChangePasswordDialog;
import com.nicolosponziello.carparking.model.ParkManager;
import com.nicolosponziello.carparking.util.Const;

/**
 * fragment per la gestione delle impostazioni
 */
public class SettingsFragment extends PreferenceFragmentCompat {

    private Preference showIntro;
    private Preference deleteDb;
    private SwitchPreference enableNotif;
    private EditTextPreference notifBeforeTime;
    private Preference changePassword;
    private Preference logout;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
        showIntro = findPreference(getString(R.string.show_intro_key));
        notifBeforeTime = findPreference(getString(R.string.notif_key));
        deleteDb = findPreference(getString(R.string.delete_db_key));
        enableNotif = findPreference(getString(R.string.enable_notif_key));
        changePassword = findPreference(getString(R.string.change_pass_key));
        logout = findPreference(getString(R.string.logout_key));

        showIntro.setOnPreferenceClickListener((preference) ->{
            SharedPreferences preferences = getActivity().getSharedPreferences(getString(R.string.shared_prefs), Context.MODE_PRIVATE);
            preferences.edit().putBoolean(getString(R.string.intro_shown), false).commit();
            return true;
        });


        deleteDb.setOnPreferenceClickListener(preference -> {
            getActivity().getApplicationContext().deleteDatabase(DatabaseHelper.DATABASE_NAME);

            //restart app after deleteting database
            Intent start = new Intent(getContext(), MainActivity.class);
            int id = 111;
            //riavvia l'applicazione
            PendingIntent pendingIntent = PendingIntent.getActivity(getContext(), id, start, PendingIntent.FLAG_CANCEL_CURRENT);
            AlarmManager manager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
            manager.set(AlarmManager.RTC, System.currentTimeMillis() + 100, pendingIntent);

            //exit
            System.exit(0);
            return true;
        });

        notifBeforeTime.setEnabled(enableNotif.isChecked());

        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this::OnSharedPreferenceChangeListener);

        changePassword.setOnPreferenceClickListener(preference -> {
            new ChangePasswordDialog(this.getActivity()).show();
            return true;
        });
        logout.setOnPreferenceClickListener(preference ->{
            Log.d("CarParking", "logout");
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getActivity(), LoginRegistrationActivity.class);
            intent.putExtra(Const.LOGIN_REG_REC_MODE, LoginRegistrationActivity.Mode.LOGIN);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ParkManager.getInstance(getActivity()).reset(getActivity());
            startActivity(intent);
            getActivity().finish();
            return true;
        });
    }

    //listen for changes
    public void OnSharedPreferenceChangeListener(SharedPreferences shared, String key) {
        if(key.equals(getString(R.string.enable_notif_key))){
            notifBeforeTime.setEnabled(enableNotif.isChecked());
        }
    }
}
