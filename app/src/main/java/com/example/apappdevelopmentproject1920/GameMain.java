package com.example.apappdevelopmentproject1920;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Locale;

public class GameMain extends AppCompatActivity {
    private TextView timerTextView;
    private TextView DareTextView;
    private TextView PlayerLabelTextview;
    private int timer;
    private static final long TIMER_START_VALUE = 60000;
    private long timeInMilliseconds = TIMER_START_VALUE;
    public boolean canVote = true;
    private CountDownTimer mCountDownTimer;
public static int round = 1;
public static int turn = 1;
public static int[] user = {0,0,0,0,0,0,0,0};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_main);
        timerTextView = (TextView) findViewById(R.id.timer);
        StartTimer();
    }

    public void RatedGood(View view) {
        if(canVote == true) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            db.collection("gameSessions").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {

                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            DocumentReference coll = db.collection("gameSessions").document("session");

                            user[turn - 1]++;
                            coll.update("score:username" + turn, user[turn - 1]);

                        }
                    }
                }

            });
            canVote = false;
        }
    }

    public void RatedBad(View view)
    {
        if(canVote == true) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            db.collection("gameSessions").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {

                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            DocumentReference coll = db.collection("gameSessions").document("session");

                            user[turn - 1]--;
                            coll.update("score:username" + turn, user[turn - 1]);

                        }
                    }
                }

            });
            canVote = false;
        }
    }

    public void RatedMeh(View view)
    {
        if(canVote == true) {
            canVote = false;
        }
    }

    private void StartTimer() {
        mCountDownTimer = new CountDownTimer(timeInMilliseconds, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeInMilliseconds = millisUntilFinished;
                updateTimerText();
            }

            @Override
            public void onFinish()
            {
                GoOverTurns();
            }
        }.start();
    }

    private void updateTimerText() {
        int seconds = (int) (timeInMilliseconds / 1000) % 60;
        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d", seconds);
        timerTextView.setText(timeLeftFormatted);
    }

    private void GoOverTurns()
    {
        canVote = true;
        turn++;
        resetTimer();
    }

    private void resetTimer() {
        timeInMilliseconds = TIMER_START_VALUE;
        StartTimer();
    }


}
