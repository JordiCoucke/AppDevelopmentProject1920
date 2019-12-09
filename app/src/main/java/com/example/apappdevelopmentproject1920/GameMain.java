package com.example.apappdevelopmentproject1920;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.protobuf.StringValue;

public class GameMain extends AppCompatActivity {
public static int round = 1;
public static int turn = 1;
public static int[] user = {0,0,0,0,0,0,0,0};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_main);
        //initPoints();
    }

    void initPoints()
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final DocumentReference docRef = db.collection("gameSessions").document("session");

        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>()
        {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snpshot,
                                @Nullable FirebaseFirestoreException e)
            {
                if (snpshot != null && snpshot.exists())
                {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    DocumentReference coll = db.collection("gameSessions").document("session");
                    String s = String.valueOf( snpshot.get("usercount"));
                    int userCount = Integer.parseInt(s);
                    for (int i = 1; i < userCount+1; i++ )
                        coll.update("score:username"+i,user[i-1]);
                }
            }
        });
    }

    public void RatedGood(View view) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("gameSessions").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {

                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        DocumentReference coll = db.collection("gameSessions").document("session");

                        user[turn-1]++;
                        coll.update("score:username"+turn,user[turn-1]);
                    }
                }
            }

        });
    }

    public void RatedBad(View view)
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("gameSessions").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {

                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        DocumentReference coll = db.collection("gameSessions").document("session");

                        user[turn-1]--;
                        coll.update("score:username"+turn,user[turn-1]);
                    }
                }
            }

        });
    }

    public void RatedMeh(View view) {
    }
}
