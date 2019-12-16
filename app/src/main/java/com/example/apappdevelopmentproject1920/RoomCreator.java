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
    private static String username = "username";
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

        EditText nickNameMessage;
        nickNameMessage = findViewById(R.id.nickname_host_edittext);
        String nickName = nickNameMessage.getText().toString();

        Random r = new Random();
        final int rand = r.nextInt(8999) + 1000;
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> session = new HashMap<>();
        session.put("ID", rand);
        username+= "1";
        session.put(username, nickName);
        session.put("usercount", 1);

        String IDSessionName = "gameSessions";
        final String collName = PickDocumentName(rand);
        db.collection(IDSessionName).document(collName)
                .set(session)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void avoid) {
                        Intent intent = new Intent(view.getContext(), DatabaseConnected.class);
                        intent.putExtra("username",username);
                        intent.putExtra("ID",rand);
                        intent.putExtra("WasJustCreated",true);
                        intent.putExtra("SessionName", collName);
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
    private String PickDocumentName(int r)
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String documentName = "session";

        return documentName+r;
    }
}
