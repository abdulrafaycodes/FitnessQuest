package com.example.fitnessquest;

import android.content.Context;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile extends AppCompatActivity {

    CircleImageView profileImage;
    ImageView changeProfileImage;
    TextView name, email, gender, age, completedWorkouts, weight, height, changePassword, totalTimeWorkout, caloriesBurned;
    MaterialButton logout;
    FirebaseAuth mAuth;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setVariables();
        changeProfileImage.setOnClickListener(v -> {
            String emailAddress = user.getEmail();
            showResetPasswordDialog(this, emailAddress);
        });

        logout.setOnClickListener(v -> {
            mAuth.signOut();
            finish();
        });
    }
    void setVariables() {
        profileImage = findViewById(R.id.profileImage);
        name = findViewById(R.id.txtUserFullname);
        email = findViewById(R.id.txtUserEmail);
        changeProfileImage = findViewById(R.id.changePfp);
        gender = findViewById(R.id.txtGender);
        age = findViewById(R.id.txtAge);
        completedWorkouts = findViewById(R.id.txtCompletedWorkouts);
        weight = findViewById(R.id.txtWeight);
        height = findViewById(R.id.txtHeight);
        logout = findViewById(R.id.btnLogout);
        changePassword = findViewById(R.id.txtChangePassword);
        totalTimeWorkout = findViewById(R.id.txtTotalTimeWorkout);
        caloriesBurned = findViewById(R.id.txtCaloriesBurned);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
    }

    public static void showResetPasswordDialog(Context context, String emailAddress) {
        new AlertDialog.Builder(context)
                .setTitle("Reset Password")
                .setMessage("Are you sure you want to reset your password?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    sendPasswordResetEmail(context, emailAddress);
                })
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private static void sendPasswordResetEmail(Context context, String emailAddress) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.sendPasswordResetEmail(emailAddress)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        showConfirmationDialog(context);
                    } else {
                        Toast.makeText(context, "Failed to send reset email: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private static void showConfirmationDialog(Context context) {
        new AlertDialog.Builder(context)
                .setTitle("Email Sent")
                .setMessage("Check your email to reset your password.")
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (user == null) {
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}