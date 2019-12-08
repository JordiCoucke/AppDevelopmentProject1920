package com.example.apappdevelopmentproject1920;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class WaitingForOtherPlayers extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_for_other_players);
    }

    public void LaunchDareInput(View view) {
        Intent intent = new Intent(view.getContext(), DareInput.class);
        startActivity(intent);
    }
}
