package com.nicolosponziello.carparking.activity;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import com.nicolosponziello.carparking.R;
import com.nicolosponziello.carparking.fragments.SettingsFragment;

public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().add(R.id.settings_frame, new SettingsFragment()).commit();
    }
}
