package com.nicolosponziello.carparking.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.card.MaterialCardView;
import com.nicolosponziello.carparking.R;

public class CurrentParkingFragment extends Fragment {

    private MaterialCardView card;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.current_parking_fragment, container, false);
        card = view.findViewById(R.id.currentCard);
        card.setOnClickListener(v -> {
            Toast.makeText(getActivity(), "Clicked",Toast.LENGTH_SHORT).show();
        });
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
}
