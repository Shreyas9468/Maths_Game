package com.example.androidproject_2mathgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;
import java.util.Random;

public class AddActivity extends AppCompatActivity {
    TextView score, time, life, question;
    EditText answer;
    Button ok, questionBtnNext;

    Random random = new Random();
    int num1, num2, userAnswer, correctAnswer;
    int userScore = 0, userLife = 3;

    CountDownTimer countDownTimer;
    private static final long START_TIMER_IN_MILLIS = 60000;
    boolean timerRunning;
    long timeLeftInMillis = START_TIMER_IN_MILLIS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        score = findViewById(R.id.textViewScoreCounter);
        life = findViewById(R.id.textViewLifecounter);
        time = findViewById(R.id.textViewTimeCounter);
        question = findViewById(R.id.textViewQuestion);
        answer = findViewById(R.id.editTextTextAnswer);
        ok = findViewById(R.id.buttonSubmit);
        questionBtnNext = findViewById(R.id.buttonNextQuestion);

        gameContinue();

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userAnswer = Integer.parseInt(answer.getText().toString());
                pauseTimer();

                if (userAnswer == correctAnswer) {
                    userScore += 10;
                    question.setText("Congratulations! Your answer is correct");
                    answer.setText("");
                    score.setText(String.valueOf(userScore));
                } else {
                    userLife -= 1;
                    life.setText(String.valueOf(userLife));
                    question.setText("Sorry, your answer is incorrect");
                    answer.setText("");
                }
            }
        });

        questionBtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                resetTimer();
                if(userLife == 0){
                    Toast.makeText(getApplicationContext(),"Game Over",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(AddActivity.this,Result.class);
                    intent.putExtra("score",userScore);
                    startActivity(intent);
                    finish();
                }
                else{
                    gameContinue();
                }
            }
        });
    }

    public void gameContinue() {
        num1 = random.nextInt(100);
        num2 = random.nextInt(100);
        correctAnswer = num1 + num2;
        question.setText(num1 + " + " + num2);
        startTimerCounter();
    }

    public void startTimerCounter() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long l) {
                timeLeftInMillis = l;
                updateText();
            }

            @Override
            public void onFinish() {
                timerRunning = false;
                pauseTimer();
                resetTimer();
                updateText();
                userLife = userLife - 1;
                life.setText(String.valueOf(userLife));
                question.setText("Sorry, time is up");
            }
        }.start();
    }

    public void pauseTimer() {
        countDownTimer.cancel();
        timerRunning = false;
    }

    public void resetTimer() {
        timeLeftInMillis = START_TIMER_IN_MILLIS;
        updateText();
        timerRunning = true;
        startTimerCounter();
    }

    public void updateText() {
        int seconds = (int) ((timeLeftInMillis / 1000) % 60);
        String timeLeft = String.format(Locale.getDefault(), "%02d", seconds);
        time.setText(timeLeft);
    }
}
