package com.example.apappdevelopmentproject1920;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;

public class GameOver extends AppCompatActivity {
    private static TextView winner;
    private String SessionName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);
        Intent intent=getIntent();
        SessionName = intent.getStringExtra("SessionName");
        winner = (TextView) findViewById(R.id.winner);
        String usernameWinner = intent.getStringExtra("winner");
        winner.setText(usernameWinner);
    }

    public void EndGameAndReturn(View view)
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("gameSessions").document(SessionName).delete();
        navigateUpTo(new Intent(getBaseContext(), MainActivity.class));
    }
}
