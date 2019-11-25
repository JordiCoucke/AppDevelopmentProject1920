package com.example.apappdevelopmentproject1920;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import io.grpc.Context;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    public static String userID = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        if(userID == null) {
            Random r = new Random();
            int rand = r.nextInt(99999) + 100000;
            userID = String.valueOf(rand);
        }

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        fab.setImageResource(R.drawable.addplayer);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void HostSession(final View view)
    {

        EditText nickNameMessage;
        nickNameMessage = findViewById(R.id.editText3);
        String nickName = nickNameMessage.getText().toString();

        Random r = new Random();
        int rand = r.nextInt(899) + 1000;
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> session = new HashMap<>();
        session.put("ID", rand);
        session.put(userID, nickName);
        String IDSessionName = "gameSessions";
        // Add a new document with a generated ID
        db.collection(IDSessionName).document("session")
                .set(session)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void avoid) {
                        Intent intent = new Intent(view.getContext(), DatabaseConnected.class);
                        startActivity(intent);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {
                        //incase of failure debugging
                    }
                });
    }

    public void JoinSession(final View view)
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("gameSessions")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult())
                            {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                String[] IDArray = new String[document.getData().size()];

                                EditText codeMessage;
                                codeMessage = findViewById(R.id.editText2);
                                String sessionCode = codeMessage.getText().toString();


                                EditText nickNameMessage;
                                nickNameMessage = findViewById(R.id.editText3);
                                String nickName = nickNameMessage.getText().toString();

                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                DocumentReference addNickName = db.collection("gameSessions").document("session");


                                int i = 0;
                                for (Object o: document.getData().values())
                                {
                                    IDArray[i] = o.toString();
                                    if(sessionCode.equals(IDArray[i]))
                                    {
                                        Intent intent = new Intent(view.getContext(), DatabaseConnected.class);
                                        startActivity(intent);
                                        addNickName
                                                .update(userID,nickName );
                                    }
                                    i++;
                                }

                            }
                        }
                        else
                            {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }

                });



    }

}
