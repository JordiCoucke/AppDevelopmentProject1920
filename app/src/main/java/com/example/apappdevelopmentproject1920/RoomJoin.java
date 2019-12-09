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
    private static final String TAG = "MainActivity";
    public static String userID = null;
    public static String username = "username";
    private EditText nickNameInput;
    private EditText sessionCodeInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_join);

        sessionCodeInput = findViewById(R.id.sessioncode_join_edittext);
        nickNameInput = findViewById(R.id.nickname_join_edittext);
    }

    public void Join(View view){
        if(CheckInputs()){
            JoinSession(view);
        }
    }

    private boolean CheckInputs(){

        if (sessionCodeInput.getText().toString().equals("")){
            sessionCodeInput.setError("Session code required to join session");
            return false;
        }
        if (nickNameInput.getText().toString().equals("")){
            nickNameInput.setError("Nickname should not be empty");
            return false;
        }

        return true;
    }

    public void JoinSession(final View view) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("gameSessions").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d(TAG, document.getId() + " => " + document.getData());
                        String[] DataArray = new String[document.getData().size()];

                        String sessionCode = sessionCodeInput.getText().toString();
                        ;
                        String nickName = nickNameInput.getText().toString();

                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        DocumentReference add = db.collection("gameSessions").document("session");


                        int i = 0;
                        for (Object o: document.getData().values())
                        {
                            DataArray[i] = o.toString();
                            if(sessionCode.equals(DataArray[i]))
                            {
                                int j = 1;
                                if( username.equals("username") )
                                {
                                    int userCount = Integer.parseInt( document.get("usercount").toString());
                                    userCount++;
                                    add.update("usercount",userCount);

                                    while(document.get("username"+String.valueOf(j)) != null)
                                    {
                                        j++;
                                    }
                                    username+=j;
                                }
                                Intent intent = new Intent(view.getContext(), WaitingForOtherPlayers.class);
                                add.update(username,nickName );
                                intent.putExtra("username",username);
                                intent.putExtra("ID",sessionCode);
                                intent.putExtra("WasJustCreated",false);
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
