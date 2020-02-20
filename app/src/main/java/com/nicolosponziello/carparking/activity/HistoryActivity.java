package com.nicolosponziello.carparking.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.nicolosponziello.carparking.R;
import com.nicolosponziello.carparking.adapter.ParkingDataAdapter;
import com.nicolosponziello.carparking.model.ParkManager;

/**
 * activity che mostra lo storico dei parcheggi salvati
 */
public class HistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ParkingDataAdapter adapter;
    private TextView noDataView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        recyclerView = findViewById(R.id.recyclerView);
        noDataView = findViewById(R.id.noDataHistory);
        toolbar = findViewById(R.id.toolbar3);

        //imposta la toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.history_activity_title));

        //imposta il recyclerview
        adapter = new ParkingDataAdapter(ParkManager.getInstance(this).getParkingData(), this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        setupView();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * refresh dei dati quando l'activity è riavviata
     */
    @Override
    protected void onResume() {
        super.onResume();

        adapter.reloadData();
        adapter.notifyDataSetChanged();
        setupView();
    }

    /**
     * imposta la view
     */
    private void setupView(){
        if(adapter.getItemCount() == 0){
            noDataView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }else{
            noDataView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }
}
