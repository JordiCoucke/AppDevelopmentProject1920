package com.example.apappdevelopmentproject1920;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class RoomCreator extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_creator);
    }

    public void CreateRoom(View view) {

    }

    private  void LaunchWaitingForOtherPlayers(View view){
        Intent intent = new Intent(view.getContext(), WaitingForOtherPlayers.class);
        startActivity(intent);
    }
}
