package com.nicolosponziello.carparking.activity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.nicolosponziello.carparking.R;
import com.nicolosponziello.carparking.fragments.NewParkFragment;

import java.io.File;

public class FullImageActivity extends AppCompatActivity {

    private ImageView imageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_image_activity);

        imageView = findViewById(R.id.fullImageView);

        String filepath = getIntent().getExtras().getString(NewParkFragment.PHOTO_EXTRA);
        File f = new File(filepath);
        Uri uri = Uri.fromFile(f);
        imageView.setImageURI(uri);

        imageView.setOnClickListener(v -> {
            finish();
        });
    }
}
