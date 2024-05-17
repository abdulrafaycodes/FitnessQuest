package com.example.fitnessquest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Main extends AppCompatActivity {

    private FirebaseAuth mAuth;
    FirebaseUser currentUser;
    MaterialToolbar topAppBar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    CardView cardView;
    CardView cardView_by_equip;
    CardView cardView_by_target;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        setSupportActionBar(topAppBar);
        setVariables();

        // Setting up the hamburger icon to open the drawer
        topAppBar.setNavigationOnClickListener(v -> {
            if (drawerLayout.isDrawerOpen(navigationView)) {
                drawerLayout.closeDrawer(navigationView); // Close drawer if open
            } else {
                drawerLayout.openDrawer(navigationView); // Open drawer
            }
        });

        // Mark the current activity's menu item as selected
        navigationView.setCheckedItem(R.id.home);
        cardView= findViewById(R.id.categoryCard);
        cardView_by_equip=findViewById(R.id.categoryCard2);
        cardView_by_target=findViewById(R.id.categoryCard3);



        navigationView.setNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.home) {
                drawerLayout.closeDrawer(navigationView);
                // Already in home, do nothing
            } else if (itemId == R.id.goals) {
                Intent intent = new Intent(this, GoalsTracking.class);
                startActivity(intent);
            } else if (itemId == R.id.leaderboard) {
                Intent intent = new Intent(this, Leaderboard.class);
                startActivity(intent);
            } else if (itemId == R.id.about) {
                Intent intent = new Intent(this, About.class);
                startActivity(intent);
            }
            drawerLayout.closeDrawer(navigationView);
            return true;
        });

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main.this, ExcerciseListScreen.class);
                intent.putExtra("TYPE","bodyPartList" );
                intent.putExtra("HEADING","Exercise by Body Parts" );
                intent.putExtra("SLUG","bodyPart" );

                startActivity(intent);
            }
        });

        cardView_by_equip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main.this, ExcerciseListScreen.class);
                intent.putExtra("TYPE","equipmentList" );
                intent.putExtra("HEADING","Exercise by Equipments" );
                intent.putExtra("SLUG","equipment" );


                startActivity(intent);
            }
        });

        cardView_by_target.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main.this, ExcerciseListScreen.class);
                intent.putExtra("TYPE","targetList" );
                intent.putExtra("HEADING","Exercise by Target" );
                intent.putExtra("SLUG","target" );


                startActivity(intent);
            }
        });


    }
    void setVariables(){
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        drawerLayout = findViewById(R.id.drawer_layout);
        topAppBar = findViewById(R.id.topAppBar);
        navigationView = findViewById(R.id.navigationView);
        setSupportActionBar(topAppBar);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Mark the current activity's menu item as selected
        navigationView.setCheckedItem(R.id.home);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Mark the current activity's menu item as selected
        navigationView.setCheckedItem(R.id.home);

    }
}