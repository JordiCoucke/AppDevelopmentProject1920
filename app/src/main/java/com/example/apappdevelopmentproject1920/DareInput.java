package com.example.apappdevelopmentproject1920;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class DareInput extends AppCompatActivity {
    private EditText InputDare1;
    private EditText InputDare2;
    private EditText InputDare3;
    private EditText InputDare4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dare_input);

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
}
