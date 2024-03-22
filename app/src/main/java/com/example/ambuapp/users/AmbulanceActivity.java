package com.example.ambuapp.users;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.ambuapp.AmbulanceAdapter;
import com.example.ambuapp.R;
import com.example.ambuapp.model.Ambulance;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AmbulanceActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DatabaseReference databaseReference;
    private List<Ambulance> ambulanceList;
    private AmbulanceAdapter ambulanceAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ambulance);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ambulanceList = new ArrayList<>();
        ambulanceAdapter = new AmbulanceAdapter(ambulanceList);
        recyclerView.setAdapter(ambulanceAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("ambulances");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ambulanceList.clear();
                Log.d("AmbulanceActivity", "Data changed, retrieving ambulances...");
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String ambulanceId = snapshot.getKey();
                    String availabilityStatus = snapshot.child("availability_status").getValue(String.class);
                    double latitude = snapshot.child("latitude").getValue(Double.class);
                    double longitude = snapshot.child("longitude").getValue(Double.class);

//                    Ambulance ambulance = new Ambulance(ambulanceId, availabilityStatus, latitude, longitude);
//                    ambulanceList.add(ambulance);
                }
                ambulanceAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("AmbulanceActivity", "Failed to read value.", error.toException());
            }
        });

    }
}