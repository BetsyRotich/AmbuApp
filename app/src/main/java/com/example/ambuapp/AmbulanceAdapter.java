package com.example.ambuapp;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ambuapp.model.Ambulance;

import java.util.List;

public class AmbulanceAdapter extends RecyclerView.Adapter<AmbulanceAdapter.AmbulanceViewHolder> {
    private List<Ambulance> ambulanceList;

    public AmbulanceAdapter(List<Ambulance> ambulanceList) {
        this.ambulanceList = ambulanceList;
    }

    @NonNull
    @Override
    public AmbulanceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.ambulance_item, parent, false);
        return new AmbulanceViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AmbulanceViewHolder holder, int position) {
        Ambulance ambulance = ambulanceList.get(position);
        holder.bind(ambulance);
    }

    @Override
    public int getItemCount() {
        return ambulanceList.size();
    }

    public static class AmbulanceViewHolder extends RecyclerView.ViewHolder {
        private TextView ambulanceIdTextView;
        private TextView availabilityStatusTextView;

        public AmbulanceViewHolder(@NonNull View itemView) {
            super(itemView);
            ambulanceIdTextView = itemView.findViewById(R.id.ambulance_id_text_view);
            availabilityStatusTextView = itemView.findViewById(R.id.availability_status_text_view);
        }

        public void bind(Ambulance ambulance) {
            Log.d("AmbulanceAdapter", "Binding ambulance: " + ambulance.getAmbulanceId());
            ambulanceIdTextView.setText(ambulance.getAmbulanceId());
            availabilityStatusTextView.setText(ambulance.getAvailabilityStatus());
        }
    }
}