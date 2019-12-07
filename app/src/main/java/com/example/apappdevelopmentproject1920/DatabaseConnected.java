package com.example.apappdevelopmentproject1920;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.collection.ArraySortedMap;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.LinkedList;

public class DatabaseConnected extends AppCompatActivity {
    private final LinkedList<String> mWordList = new LinkedList<>();
    private RecyclerView mRecyclerView;
    private WordListAdapter mAdapter;
    public static String ID;
    public static String nickName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DBListen(this);
        setContentView(R.layout.activity_database_connected);
        Intent intent=getIntent();
        nickName = intent.getStringExtra("username");
        ID = intent.getStringExtra("ID");

        final TextView showTextViewRoomID = (TextView) findViewById(R.id.ShowTextViewRoomID);

        ((Activity) this).runOnUiThread(new Runnable() {
            @Override
            public void run()
            {
                showTextViewRoomID.setText(ID);
                showTextViewRoomID.invalidate();
            }
        });
    }




    public void DBListen(final Context c) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final DocumentReference docRef = db.collection("gameSessions").document("session");

        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>()
        {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e)
            {

                if (snapshot != null && snapshot.exists())
                {

                    setContentView(R.layout.activity_database_connected);
                    String[] names = new String[8];
                    int i = 0;
                    while(snapshot.contains("username"+i))
                    {
                        names[i] = snapshot.get("username"+i).toString();
                        i++;
                    }

                    for ( i = 0; i < names.length; i++)
                    {
                        //names[i] = snapshot.get("username"+i).toString();
                        mWordList.addLast(names[i]);
                    }
                    mRecyclerView = findViewById(R.id.recyclerview);
                    mAdapter = new WordListAdapter(c, mWordList);
                    mRecyclerView.setAdapter(mAdapter);
                    mRecyclerView.setLayoutManager(new LinearLayoutManager(c));
                }
            }
        });
    }

    public void GoToDareInput(View view)
    {
        Intent intent = new Intent(view.getContext(), DareInput.class);
        startActivity(intent);
    }
}

