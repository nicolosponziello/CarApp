package com.nicolosponziello.carparking.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nicolosponziello.carparking.R;
import com.nicolosponziello.carparking.model.ParkingData;
import com.nicolosponziello.carparking.util.Utils;

import java.util.List;

public class ParkingDataAdapter extends RecyclerView.Adapter<ParkingDataAdapter.ViewHolder> {

    private List<ParkingData> data;

    public ParkingDataAdapter(List<ParkingData> dataList){
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

        holder.cityTextView.setText(parkingData.getCity());
        holder.dateTextView.setText(Utils.formatDate(parkingData.getDate()));
        holder.addressTextView.setText(parkingData.getAddress());
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
}
