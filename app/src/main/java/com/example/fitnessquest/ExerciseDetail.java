package com.example.fitnessquest;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.fitnessquest.adapters.InstructionAdapter;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

public class ExerciseDetail extends AppCompatActivity {

    private TextView timerTextView;
    private Button startButton;
    private Button stopButton;
    private CountDownTimer countDownTimer;
    private boolean isTimerRunning = false;
    private long startTimeInMillis;
    private long timeLeftInMillis = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_exercise_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Retrieve data from Intent
        String exerciseName = getIntent().getStringExtra("EXERCISE_NAME");
        String bodyPart = getIntent().getStringExtra("BODY_PART");
        String equipment = getIntent().getStringExtra("EQUIPMENT");
        String gifImage = getIntent().getStringExtra("GIF_IMAGE");
        String target = getIntent().getStringExtra("TARGET");
        String ex_id = getIntent().getStringExtra("EX_ID");

        List<String> instructions = getIntent().getStringArrayListExtra("INSTRUCTIONS");
        List<String> secondaryMuscles = getIntent().getStringArrayListExtra("SECONDARY_MUSCLE");

        // Display data in TextViews
        TextView exerciseNameTextView = findViewById(R.id.tv_name);
        TextView bodyPartTextView = findViewById(R.id.body_part_value);
        TextView equipmentTextView = findViewById(R.id.equipment_value);
        TextView targetTextView = findViewById(R.id.target_value);
        ListView secondaryListView = findViewById(R.id.lv_secondary_muscles);
        ListView listViewInstructions = findViewById(R.id.lv_instructions);

        timerTextView = findViewById(R.id.timerTextView);
        startButton = findViewById(R.id.startBtn);
        stopButton = findViewById(R.id.stopBtn);

        exerciseNameTextView.setText(exerciseName);
        bodyPartTextView.setText(bodyPart);
        equipmentTextView.setText(equipment);
        targetTextView.setText(target);

        // Load GIF image using Glide library
        ImageView gifImageView = findViewById(R.id.gif_image);
        Glide.with(this).load(gifImage).into(gifImageView);

        // Set up instruction and secondary muscle list adapters
        if (instructions != null) {
            InstructionAdapter instructionAdapter = new InstructionAdapter(this, instructions);
            listViewInstructions.setAdapter(instructionAdapter);
        }

        if (secondaryMuscles != null) {
            InstructionAdapter sAdapter = new InstructionAdapter(this, secondaryMuscles);
            secondaryListView.setAdapter(sAdapter);
        }

        // Set up click listeners for start and stop buttons
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isTimerRunning) {
                    startTimer();
                } else {
                    pauseTimer();
                }
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopTimer();
            }
        });
    }

    private void startTimer() {
        startTimeInMillis = System.currentTimeMillis();
        countDownTimer = new CountDownTimer(timeLeftInMillis > 0 ? timeLeftInMillis : Long.MAX_VALUE, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateTimerText();
            }

            @Override
            public void onFinish() {
                // Handle timer finish if needed
            }
        }.start();

        isTimerRunning = true;
        startButton.setText("Pause");
        stopButton.setVisibility(View.VISIBLE);
    }

    private void pauseTimer() {
        countDownTimer.cancel();
        isTimerRunning = false;
        startButton.setText("Start");
        timeLeftInMillis = Long.MAX_VALUE - (System.currentTimeMillis() - startTimeInMillis);
    }

    private void stopTimer() {
        countDownTimer.cancel();
        isTimerRunning = false;
        startButton.setText("Start");
        stopButton.setVisibility(View.GONE);
        timerTextView.setText("00:00:00");
        timeLeftInMillis = 0;
    }

    private void updateTimerText() {
        long currentTimeInMillis = System.currentTimeMillis();
        long elapsedTimeInMillis = currentTimeInMillis - startTimeInMillis;

        int hours = (int) (elapsedTimeInMillis / 3600000);
        int minutes = (int) ((elapsedTimeInMillis % 3600000) / 60000);
        int seconds = (int) ((elapsedTimeInMillis % 60000) / 1000);

        String timeLeftFormatted = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        timerTextView.setText(timeLeftFormatted);
        System.out.println(timeLeftFormatted);
    }
}
