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
import com.google.protobuf.StringValue;

import java.util.LinkedList;

public class DatabaseConnected extends AppCompatActivity {
    private final LinkedList<String> mWordList = new LinkedList<>();
    private RecyclerView mRecyclerView;
    private WordListAdapter mAdapter;
    public static String ID;
    public static String SessionName;
    public static String nickName;
    private TextView showTextViewRoomID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent=getIntent();
        SessionName = intent.getStringExtra("SessionName");
        DBListen(this);
        setContentView(R.layout.activity_database_connected);
        nickName = intent.getStringExtra("username");
        if(intent.getBooleanExtra("WasJustCreated", true))
        {
            int iId = intent.getIntExtra("ID",0);
            ID = String.valueOf( iId);
        }
        else {
            ID = intent.getStringExtra("ID");
        }
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

                    setContentView(R.layout.activity_database_connected);
                    String[] names = new String[9];
                    int i = 1;
                    while(snapshot.contains("username"+i))
                    {
                        names[i] = snapshot.get("username"+i).toString();
                        i++;
                    }

                    for ( i = 1; i < names.length; i++)
                    {
                        mWordList.addLast(names[i]);
                    }
                    mRecyclerView = findViewById(R.id.recyclerview);
                    mAdapter = new WordListAdapter(c, mWordList);
                    mRecyclerView.setAdapter(mAdapter);
                    mRecyclerView.setLayoutManager(new LinearLayoutManager(c));
                    showTextViewRoomID = (TextView) findViewById(R.id.ShowTextViewRoomID);
                    showTextViewRoomID.setText(ID);
                }
            }
        });
    }

    public void GoToDareInput(View view)
    {
        Intent intent = new Intent(view.getContext(), DareInput.class);
        intent.putExtra("SessionName",SessionName);
        startActivity(intent);
    }
}

