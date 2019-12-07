package com.example.apappdevelopmentproject1920;

//import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import io.grpc.Context;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "MainActivity";
    public static String username = "username";
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle =
                new ActionBarDrawerToggle(this, drawer, toolbar,
                        R.string.navigation_drawer_open,
                        R.string.navigation_drawer_close);
        if (drawer != null) {
            drawer.addDrawerListener(toggle);
        }
        toggle.syncState();

        NavigationView navigationView =
                findViewById(R.id.nav_view);
        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(this);
        }

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new HomeFragment()).commit();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer((GravityCompat.START));
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void HostSession(final View view) {

        EditText nickNameMessage;
        nickNameMessage = findViewById(R.id.nickname_edittext);
        String nickName = nickNameMessage.getText().toString();

        Random r = new Random();
        final int rand = r.nextInt(899) + 1000;
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> session = new HashMap<>();
        session.put("ID", rand);
        username+= "0";
        session.put(username, nickName);

        String IDSessionName = "gameSessions";
        // Add a new document with a generated ID
        db.collection(IDSessionName).document("session")
                .set(session)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void avoid) {
                        Intent intent = new Intent(view.getContext(), DatabaseConnected.class);
                        intent.putExtra("username",username);
                        intent.putExtra("ID",getString(rand));
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

    public void JoinSession(final View view) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("gameSessions").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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
                                        intent.putExtra("username",username);
                                        intent.putExtra("ID",sessionCode);
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


    public void displayToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        // Handle navigation view item clicks here.
        switch (item.getItemId()) {
            case R.id.home:
                // Handle the camera import action (for now display a toast).
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new HomeFragment()).commit();
                displayToast(getString(R.string.chose_home));
                break;
            case R.id.rooms:
                // Handle the gallery action (for now display a toast).
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new RoomsFragment()).commit();
                displayToast(getString(R.string.chose_rooms));
                break;
            case R.id.profile:
                // Handle the slideshow action (for now display a toast).

                displayToast(getString(R.string.chose_profile));
                break;
            case R.id.tests:
                // Handle the slideshow action (for now display a toast).
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new TestsFragment()).commit();
                displayToast(getString(R.string.chose_tests));
                break;
            case R.id.info:
                // Handle the tools action (for now display a toast).

                displayToast(getString(R.string.chose_info));
                break;
            case R.id.settings:
                // Handle the send action (for now display a toast).

                displayToast(getString(R.string.chose_settings));
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
