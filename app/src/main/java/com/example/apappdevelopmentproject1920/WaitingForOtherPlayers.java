package com.example.apappdevelopmentproject1920;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.LinkedList;

public class WaitingForOtherPlayers extends AppCompatActivity {
    private String SessionName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_for_other_players);

        Intent intent = getIntent();
        SessionName = intent.getStringExtra("SessionName");

        DBListen(this);
    }

    public void DBListen(final Context c) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final DocumentReference docRef = db.collection("gameSessions").document(SessionName);

        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>()
        {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e)
            {

                if (snapshot != null && snapshot.exists())
                {
                    int i = 1;
                    while(snapshot.contains("dare"+i))
                    {
                        i++;
                    }
                    int j = 1;
                    while(snapshot.contains("username"+j))
                    {
                        j++;
                    }

                    if(i == j*4){
                        StartGame();
                    }
                }
            }
        });
    }
    public void StartGame() {
        Intent intent = new Intent(this, GameMain.class);
        intent.putExtra("SessionName",SessionName);
        startActivity(intent);
    }
}
