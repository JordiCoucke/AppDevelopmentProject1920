package com.example.apappdevelopmentproject1920;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class RoomJoin extends AppCompatActivity {
    private static final String TAG = "RoomJoin";
    public static String username = "username";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_join);
    }
    public void JoinSession(final View view) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("gameSessions")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                String[] DataArray = new String[document.getData().size()];

                                EditText codeMessage;
                                codeMessage = findViewById(R.id.sessioncode_edittext);
                                String sessionCode = codeMessage.getText().toString();


                                EditText nickNameMessage;
                                nickNameMessage = findViewById(R.id.nicknamejoin_edittext);
                                String nickName = nickNameMessage.getText().toString();

                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                DocumentReference add = db.collection("gameSessions").document("session");


                                int i = 0;
                                for (Object o: document.getData().values())
                                {
                                    DataArray[i] = o.toString();
                                    if(sessionCode.equals(DataArray[i]))
                                    {
                                        int j = 0;
                                        if( username.equals("username") )
                                        {
                                            while(document.get("username"+String.valueOf(j)) != null)
                                            {
                                                j++;
                                            }
                                            username+=j;
                                        }
                                        Intent intent = new Intent(view.getContext(), DatabaseConnected.class);
                                        add.update(username,nickName );
                                        startActivity(intent);
                                    }
                                    i++;
                                }

                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }

                });
    }

}
