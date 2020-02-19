package com.nicolosponziello.carparking.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nicolosponziello.carparking.R;
import com.nicolosponziello.carparking.adapter.ParkingDataAdapter;
import com.nicolosponziello.carparking.model.ParkManager;

public class HistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ParkingDataAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        recyclerView = findViewById(R.id.recyclerView);
        adapter = new ParkingDataAdapter(ParkManager.getInstance(this).getParkingData(), this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.reloadData();
        adapter.notifyDataSetChanged();
    }
}
