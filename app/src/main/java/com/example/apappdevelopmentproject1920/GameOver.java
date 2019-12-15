package com.example.apappdevelopmentproject1920;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class GameOver extends AppCompatActivity {
    private static TextView winner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);
        Intent intent=getIntent();
        winner = (TextView) findViewById(R.id.winner);
        String usernameWinner = intent.getStringExtra("winner");
        winner.setText(usernameWinner);
    }
}
