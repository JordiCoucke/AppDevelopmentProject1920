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

public class DareInput extends AppCompatActivity {
    private String SessionName;
    private EditText InputDare1;
    private EditText InputDare2;
    private EditText InputDare3;
    private EditText InputDare4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dare_input);
        Intent intent=getIntent();
        SessionName = intent.getStringExtra("SessionName");

        InputDare1 = findViewById(R.id.editText_dare1);
        InputDare2 = findViewById(R.id.editText_dare2);
        InputDare3 = findViewById(R.id.editText_dare3);
        InputDare4 = findViewById(R.id.editText_dare4);


    }

    private boolean CheckDareInputs() {
        if (InputDare1.getText().toString().equals("")){
            InputDare1.setError("Dare should not be empty");
            return false;
        }
        if (InputDare2.getText().toString().equals("")){
            InputDare2.setError("Dare should not be empty");
            return false;
        }
        if (InputDare3.getText().toString().equals("")){
            InputDare3.setError("Dare should not be empty");
            return false;
        }
        if (InputDare4.getText().toString().equals("")){
            InputDare4.setError("Dare should not be empty");
            return false;
        }
        return true;
    }

    private String[] GetDares(){
        String[] Dares = {
                InputDare1.getText().toString(),
                InputDare2.getText().toString(),
                InputDare3.getText().toString(),
                InputDare4.getText().toString()
        };
        return Dares;
    }

    private void SubmitDares(View view){
        if(CheckDareInputs()){
            String[] dares = GetDares();
            //Insert firebase code here

            LaunchWaitingForOtherPlayers(view);
        }

    }
    private  void LaunchWaitingForOtherPlayers(View view){
        Intent intent = new Intent(view.getContext(), WaitingForOtherPlayers.class);
        startActivity(intent);
    }

    public void PlayerIsReady(final View view)
    {
        final String dare1 = InputDare1.getText().toString();
        final String dare2 = InputDare2.getText().toString();
        final String dare3 = InputDare3.getText().toString();
        final String dare4 = InputDare4.getText().toString();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("gameSessions").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {

                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        DocumentReference add = db.collection("gameSessions").document(SessionName);

                        int i = 0;
                        while(document.contains("dare"+i))
                        {
                            i++;
                        }
                        add.update("dare"+i,dare1);
                        i++;
                        add.update("dare"+i,dare2);
                        i++;
                        add.update("dare"+i,dare3);
                        i++;
                        add.update("dare"+i,dare4);
                        Intent intent = new Intent(view.getContext(), GameMain.class);
                        intent.putExtra("SessionName",SessionName);
                        startActivity(intent);
                    }
                }
            }

        });
    }


}
