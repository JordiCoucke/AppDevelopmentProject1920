package com.example.apappdevelopmentproject1920;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class RoomCreator extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    public static String userID = null;
    private EditText nickNameInput;
    private EditText roomNameInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_creator);

        nickNameInput = findViewById(R.id.nickname_host_edittext);
        roomNameInput = findViewById(R.id.roomname_edittext);
    }

    public void CreateRoom(View view) {
        if(CheckInputs()){
            HostSession(view);
        }
    }

    private boolean CheckInputs(){

        if (roomNameInput.getText().toString().equals("")){
            roomNameInput.setError("Room name should not be empty");
            return false;
        }
        if (nickNameInput.getText().toString().equals("")){
            nickNameInput.setError("Nickname should not be empty");
            return false;
        }

        return true;
    }

    public void HostSession(final View view) {

        String nickName = nickNameInput.getText().toString();

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
                        Intent intent = new Intent(view.getContext(), WaitingForOtherPlayers.class);
                        startActivity(intent);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //incase of failure debugging
                    }
                });
    }

}
