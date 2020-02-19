package com.nicolosponziello.carparking.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nicolosponziello.carparking.R;
import com.nicolosponziello.carparking.activity.DetailActivity;
import com.nicolosponziello.carparking.model.ParkManager;
import com.nicolosponziello.carparking.model.ParkingData;
import com.nicolosponziello.carparking.util.Const;
import com.nicolosponziello.carparking.util.Utils;

import java.util.List;

public class ParkingDataAdapter extends RecyclerView.Adapter<ParkingDataAdapter.ViewHolder> {

    private List<ParkingData> data;
    private Context context;

    public ParkingDataAdapter(List<ParkingData> dataList, Context c){
        context = c;
        data = dataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View parkingView = inflater.inflate(R.layout.list_item, parent, false);

        return new ViewHolder(parkingView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ParkingData parkingData = this.data.get(position);

        if (parkingData.getCity() != null) {
            holder.cityTextView.setText("Città: " + parkingData.getCity());
        }else{
            holder.cityTextView.setVisibility(View.GONE);
        }

        if(parkingData.getDate() != null){
            holder.dateTextView.setText("Data: " + Utils.formatDate(parkingData.getDate()));
        }else{
            holder.dateTextView.setVisibility(View.GONE);
        }

        if(parkingData.getAddress() != null){
            holder.addressTextView.setText("Indirizzo: " + parkingData.getAddress());
        }else{
            holder.addressTextView.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra(Const.DETAIL_EXTRA, parkingData.getId().toString());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView cityTextView, dateTextView, addressTextView;

        public ViewHolder(View view){
            super(view);

            cityTextView = view.findViewById(R.id.cityItem);
            dateTextView = view.findViewById(R.id.dateItem);
            addressTextView = view.findViewById(R.id.addressItem);

        }
    }

    public void reloadData(){
        Log.d("data", "reloading");
        this.data = ParkManager.getInstance(context).getParkingData();
    }
}
