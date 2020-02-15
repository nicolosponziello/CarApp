package com.nicolosponziello.carparking;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import android.os.Bundle;
import android.view.Menu;
import android.widget.FrameLayout;

import com.nicolosponziello.carparking.fragments.CurrentPosFragment;
import com.nicolosponziello.carparking.fragments.NoPosFragment;

public class MainActivity extends AppCompatActivity {

    private FrameLayout posFrame;
    private FrameLayout bottomBar;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        posFrame = findViewById(R.id.current_pos_fragment);
        bottomBar = findViewById(R.id.bottom_bar_frag);
        toolbar = findViewById(R.id.toolbar);


        setSupportActionBar(toolbar);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.current_pos_fragment, new CurrentPosFragment()).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }
}
