package com.astronist.personalnurse.View.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.astronist.personalnurse.R;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import pl.droidsonroids.gif.GifImageView;

public class WaitingOfferedPriceActivity extends AppCompatActivity {

    private GifImageView gifImage;
    private TextView timerTextView;
    private String uploadTime, addingDate;
    private long currentmilliseconds, timeleftinmilliseconds;
    int secondsToGo, minutesToGo, hoursToGo;
    Date time = null;
    Date time1 = null;
    Date newDate = null;
    Date currentMills = null;
    private ExtendedFloatingActionButton checkOffer;


    public static final String TAG = "Count";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_offered_price);

        inItView();
        Intent intent = getIntent();
        uploadTime = intent.getStringExtra("addingTime");
        addingDate = intent.getStringExtra("addingDate");
        Log.d(TAG, "onCreate: " + addingDate + "      " + uploadTime);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.US);


        try {
            time = timeFormat.parse(uploadTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(time);
        cal.add(Calendar.SECOND, 5);
        String newTime = timeFormat.format(cal.getTime());

        try {
            time1 = timeFormat.parse(newTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long totalMillis = time1.getTime();
        Log.d(TAG, "newTime: " + newTime);
        try {
            newDate = dateFormat.parse(addingDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long dateMillis = newDate.getTime();

        Log.d(TAG, "totalMillis: " + totalMillis);
        Log.d(TAG, "totalDateMillis: " + dateMillis);

        Log.d(TAG, "onCurrent: " + uploadTime);
        try {
            currentMills = timeFormat.parse(uploadTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        currentmilliseconds = currentMills.getTime();
        Log.d(TAG, "onCurrent: " + currentmilliseconds);
        if (currentmilliseconds <= totalMillis) {
            Log.d(TAG, "compare: " + "success");

            timeleftinmilliseconds = totalMillis - currentmilliseconds;
            Log.d(TAG, "timeLeft: " + timeleftinmilliseconds);
            secondsToGo = (int) (timeleftinmilliseconds/1000);

            minutesToGo = secondsToGo/60;
            secondsToGo = secondsToGo-(minutesToGo*60);

            hoursToGo = minutesToGo/60;
            minutesToGo = minutesToGo-(hoursToGo*60);


        int millisToGo = secondsToGo * 1000 + minutesToGo * 1000 * 60 + hoursToGo * 1000 * 60 * 60;

        new CountDownTimer(millisToGo, 1000) {

            @Override
            public void onTick(long millis) {
                int seconds = (int) (millis / 1000) % 60;
                int minutes = (int) ((millis / (1000 * 60)) % 60);
                int hours = (int) ((millis / (1000 * 60 * 60)) % 24);
                String text = String.format("%02dh: %02dm: %02ds", hours, minutes, seconds);
                Log.d(TAG, "onTick: " +String.format("%02dh, %02dm, %02ds", hours, minutes, seconds));
                timerTextView.setText(text);
            }

            @Override
            public void onFinish() {

                timerTextView.setText("Be patient please!");
                checkOffer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                        Intent intent1 = new Intent(WaitingOfferedPriceActivity.this, GetPricingActivity.class);
                        startActivity(intent1);
                    }
                });
            }
        }.start();
    }
//      return v;
    }


    private void inItView() {

        gifImage = findViewById(R.id.gifView);
        timerTextView = findViewById(R.id.countdown);
        checkOffer = findViewById(R.id.checkBtn);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Prescribe Termination!(সমাপ্তির আবেদন !)");
        alertDialog.setMessage("Are You Sure To Go back?(আপনি কি হোম পেজ এ ফিরে যেতে চান?)");
        alertDialog.setIcon(R.drawable.ic_block);

        alertDialog.setPositiveButton("Yes(হ্যাঁ)", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
                Intent intent = new Intent(WaitingOfferedPriceActivity.this, UserUiContainerActivity.class);
                startActivity(intent);
            }
        });

        alertDialog.setNegativeButton("No(না)", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) { {dialog.dismiss(); }
            }
        });

        alertDialog.create();
        alertDialog.show();
    }
}