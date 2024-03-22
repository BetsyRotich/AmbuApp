package com.example.ambuapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.ambuapp.usermaps.UserMapActivity;

public class BaasicActvity extends AppCompatActivity {

    private CardView cardOne;
    private CardView cardTwo;

    private CardView cardThree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baasic_actvity);

        cardOne = findViewById(R.id.one);
        cardTwo = findViewById(R.id.two);
        cardThree = findViewById(R.id.three);

        cardOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BaasicActvity.this, UserMapActivity.class));
            }
        });

        cardTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BaasicActvity.this, ChatActivity.class));
            }
        });

        cardThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BaasicActvity.this, UserProfile.class));
            }
        });

    }
}