package com.astronist.personalnurse.View.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import pl.droidsonroids.gif.GifImageView;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.astronist.personalnurse.Model.PrescriptionInfo;
import com.astronist.personalnurse.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class GetPricingActivity extends AppCompatActivity {

    private GifImageView gifImage;
    private TextView timerTextView;
    private long currentmilliseconds, timeleftinmilliseconds, currentMills;
    int secondsToGo, minutesToGo, hoursToGo;
    Date time = null;
    Date time1 = null;
    Date current = null;
    private DatabaseReference prescriptionRef;
    private String currentTime;
    public static final String TAG = "Offer";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_pricing);
        initView();
        prescriptionRef = FirebaseDatabase.getInstance().getReference("PrescriptionInfo");
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat myTimeFormat = new SimpleDateFormat("hh:mm a", Locale.US);
        currentTime = myTimeFormat.format(calendar.getTime());
        try {
            current = myTimeFormat.parse(currentTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
       currentMills = current.getTime();
        Log.d(TAG, "onCreate: "+currentMills+"...."+currentTime);

        getPrescriptionDetail();
    }

    private void getPrescriptionDetail() {
        prescriptionRef.orderByKey().limitToLast(1).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot childInfo : snapshot.getChildren()){
                    PrescriptionInfo prescriptionInfo = childInfo.getValue(PrescriptionInfo.class);
                    Log.d(TAG, "onDataChange: "+prescriptionInfo.toString());
                    String uploadTime = prescriptionInfo.getTime();
                    doCalculation(uploadTime);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "onCancelled: " +error.getMessage());
            }
        });
    }

    private void doCalculation(String uploadTime) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.US);


        try {
            time = timeFormat.parse(uploadTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(time);
        cal.add(Calendar.MINUTE, 1);
        String newTime = timeFormat.format(cal.getTime());

        try {
            time1 = timeFormat.parse(newTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long totalMillis = time1.getTime();
        Log.d(TAG, "newTime: " + newTime);


        Log.d(TAG, "totalMillis: " + totalMillis);

        currentmilliseconds = currentMills;
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

                    timerTextView.setText("getting ready dear!");

                }
            }.start();
        }else{
            timerTextView.setText("We are trying our best!");
        }
    }

    private void initView() {
        gifImage = findViewById(R.id.gifView);
        timerTextView = findViewById(R.id.countdown);
    }
}