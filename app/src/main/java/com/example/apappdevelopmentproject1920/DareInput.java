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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dare_input);
    }

    public void PlayerIsReady(final View view)
    {
        EditText editTextDare1 = findViewById(R.id.editText0);
        EditText editTextDare2 = findViewById(R.id.editText1);
        EditText editTextDare3 = findViewById(R.id.editText2);

        final String dare1 = editTextDare1.getText().toString();
        final String dare2 = editTextDare2.getText().toString();
        final String dare3 = editTextDare3.getText().toString();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("gameSessions").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {

                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        DocumentReference add = db.collection("gameSessions").document("session");

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
                        Intent intent = new Intent(view.getContext(), GameMain.class);
                        startActivity(intent);
                    }
                }
            }

        });
    }


}
