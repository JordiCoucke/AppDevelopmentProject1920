package com.example.apappdevelopmentproject1920;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Locale;
import java.util.Random;

public class GameMain extends AppCompatActivity {
    private TextView timerTextView;
    private TextView DareTextView;
    private TextView PlayerNameTextview;
    private TextView RoundTextView;
    private TextView TurnTextView;
    private int timer;
    private String SessionName;
    private int userCount;
    private int TurnControl = 0;
    private static final long TIMER_START_VALUE = 61000;
    private long timeInMilliseconds = TIMER_START_VALUE;
    public boolean canVote = true;
    public static String[] dares;
    private String[] userNames;
    private CountDownTimer mCountDownTimer;
    public static int round = 1;
    public static int turn = 1;
    public static int[] user = {0,0,0,0,0,0,0,0};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_main);
        Intent intent=getIntent();
        SessionName = intent.getStringExtra("SessionName");
        timerTextView = (TextView) findViewById(R.id.timer);
        RoundTextView = (TextView) findViewById(R.id.PlayersRoundTV);
        TurnTextView = (TextView) findViewById(R.id.PlayersTurnTV);
        DareTextView = (TextView) findViewById(R.id.DareDescriptionTV);
        PlayerNameTextview = (TextView) findViewById(R.id.PlayerGameLabel);
        TurnTextView.setText(String.valueOf(turn));
        RoundTextView.setText(String.valueOf(round));
        StartTimer();
    }

    public void RatedGood(View view) {
        if(canVote == true) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            DocumentReference docRef = db.collection("gameSessions").document(SessionName);

            user[turn - 1] += 50;
            docRef.update("score:username" + turn, user[turn - 1]);

            canVote = false;
        }
    }

    public void RatedBad(View view)
    {
        if(canVote == true) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            DocumentReference docRef = db.collection("gameSessions").document(SessionName);

            user[turn - 1] += 0;
            docRef.update("score:username" + turn, user[turn - 1]);

            canVote = false;
        }
    }

    public void RatedMeh(View view)
    {
        if(canVote == true) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            DocumentReference docRef = db.collection("gameSessions").document(SessionName);

            user[turn - 1] += 25;
            docRef.update("score:username" + turn, user[turn - 1]);

            canVote = false;
        }
    }

    private void StartTimer() {
        mCountDownTimer = new CountDownTimer(timeInMilliseconds, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                GetDares();
                GetUsernames();
                GenerateRandomDare();
                if(userNames != null)
                    PlayerNameTextview.setText(userNames[turn-1]);
                if(dares != null) {
                    timeInMilliseconds = millisUntilFinished;
                    updateTimerText();
                }
            }

            @Override
            public void onFinish()
            {
                GoOverTurns();
            }
        }.start();
    }

    private void updateTimerText() {
        int seconds = (int) (timeInMilliseconds / 1000) % 61;
        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d", seconds);
        timerTextView.setText(timeLeftFormatted);
    }

    private void GoOverTurns()
    {
        canVote = true;
        if(userCount == turn)
        {
            if(round == 4)
            {
                ComparePlayerPoint();
            }
            turn = 0;
            round++;
            RoundTextView.setText(String.valueOf(round));
            TurnControl = 0;
        }
        turn++;
        PlayerNameTextview.setText(userNames[turn-1]);
        TurnTextView.setText(String.valueOf(turn));
        resetTimer();
    }

    private void resetTimer() {
        timeInMilliseconds = TIMER_START_VALUE;
        StartTimer();
    }



    public void GetDares() {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("gameSessions").document(SessionName);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    int i = 0;
                    int dareCount = 0;
                    String sUserCount = String.valueOf(document.get("usercount"));
                    int iUserCount = Integer.parseInt(sUserCount);
                    userCount = iUserCount;
                    dares = new String[(iUserCount * 4)];
                    for (Object o : document.getData().values()) {
                        if (document.getData().containsKey("dare" + i)) {
                            dares[i] = String.valueOf(document.get("dare" + i));
                            dareCount++;
                        }
                        i++;


                    }
                }
            }

        });
    }

    public void GetUsernames() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("gameSessions").document(SessionName);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    String sUserCount = String.valueOf(document.get("usercount"));
                    int iUserCount = Integer.parseInt(sUserCount);
                    userCount = iUserCount;

                    String[] usernames = new String[iUserCount];
                    for (int j = 0; j < iUserCount; j++) {
                        String s = "username" + (j + 1);
                        String username = String.valueOf(document.get(s));
                        usernames[j] = username;
                    }
                    userNames = usernames;
                }
            }


        });
    }



    public void GenerateRandomDare()
    {
        if(TurnControl != turn && dares != null )
        {
            Random r = new Random();
            int rand = r.nextInt(dares.length);
            DareTextView.setText(dares[rand]);
            TurnControl++;
        }
    }

    private void ComparePlayerPoint()
    {
        String winner = "";
        for (int i = 0; i < userCount-1; i++)
        {
            if((user[i] > user[i+1]) || user[i+1] == 0 )
            {
                winner = String.valueOf( userNames[i]);
            }
            else
                winner = String.valueOf(userNames[i+1]);
        }
        Intent intent = new Intent(this, GameOver.class);
        intent.putExtra("SessionName",SessionName);
        intent.putExtra("winner", winner);
        startActivity(intent);
    }

}
