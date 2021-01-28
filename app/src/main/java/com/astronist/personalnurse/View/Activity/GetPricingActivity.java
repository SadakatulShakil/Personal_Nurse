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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.astronist.personalnurse.Model.OfferPrice;
import com.astronist.personalnurse.Model.PrescriptionInfo;
import com.astronist.personalnurse.R;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.nio.file.FileVisitOption;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class GetPricingActivity extends AppCompatActivity {

    private GifImageView gifImage;
    private TextView timerTextView, oneDayPrice, oneWeakPrice, fifteenDayPrice, oneMonthPrice, medicineList, customDayPrice, customMonthPrice;
    private long currentmilliseconds, timeleftinmilliseconds, currentMills;
    int secondsToGo, minutesToGo, hoursToGo;
    Date time = null;
    Date time1 = null;
    Date current = null;
    private DatabaseReference prescriptionRef;
    private DatabaseReference offeredPriceRef;
    private String currentTime, userId, mainPrice, orderDayCount, pushId;
    public static final String TAG = "Offer";
    private RelativeLayout holdingLay, waitingLay;
    private FirebaseAuth firebaseAuth;
    private ProgressBar progressBar;
    private CheckBox ckBox1, ckBox2, ckBox3, ckBox4, ckBox5;
    private LinearLayout customLay, customDayLay, customMonthLay;
    private ExtendedFloatingActionButton customDayBtn, customMonthBtn, proceedBtn;
    private EditText customDayEt, customMonthEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_pricing);
        initView();
        prescriptionRef = FirebaseDatabase.getInstance().getReference("PrescriptionInfo");
        firebaseAuth = FirebaseAuth.getInstance();
        userId = firebaseAuth.getCurrentUser().getUid();
        offeredPriceRef = FirebaseDatabase.getInstance().getReference("OfferedPrice");
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

        getOfferedPriceList();
    }

    private void getOfferedPriceList() {
        progressBar.setVisibility(View.VISIBLE);
        offeredPriceRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot offerSnap : snapshot.getChildren()){
                    OfferPrice offerPriceList = offerSnap.getValue(OfferPrice.class);
                    if(offerPriceList.getPushId().equals(pushId)){

                        if(offerPriceList != null){
                            String price1 = offerPriceList.getOneDayPrice();
                            String price2  = offerPriceList.getOneWeakPrice();
                            String price3  = offerPriceList.getFifteenDaysPrice();
                            String price4  = offerPriceList.getOneMonthPrice();

                            String medicine = offerPriceList.getMedicineList();

                            oneDayPrice.setText(price1+" taka");
                            oneWeakPrice.setText(price2+" taka");
                            fifteenDayPrice.setText(price3+" taka");
                            oneMonthPrice.setText(price4+" taka");
                            medicineList.setText(medicine);

                            progressBar.setVisibility(View.GONE);

                            holdingLay.setVisibility(View.VISIBLE);
                            waitingLay.setVisibility(View.GONE);
                            Log.d(TAG, "onOfferData: "+ offerPriceList.toString());

                            checkBoxFunction(price1, price2, price3, price4, medicine);
                            GoForCheckOut(price1, medicine);

                        }else{

                            holdingLay.setVisibility(View.GONE);
                            waitingLay.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void checkBoxFunction(String Price1, String Price2, String Price3, String Price4, String medicine) {

        ckBox1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ckBox1.isChecked()){
                    ckBox2.setChecked(false);
                    ckBox3.setChecked(false);
                    ckBox4.setChecked(false);
                    ckBox5.setChecked(false);
                    customDayEt.setText("");
                    customDayPrice.setText("");
                    customMonthEt.setText("");
                    customMonthPrice.setText("");
                    customDayPrice.setVisibility(View.GONE);
                    customMonthPrice.setVisibility(View.GONE);
                    mainPrice= Price1;
                    orderDayCount = "1";
                }
            }
        });
        ckBox2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ckBox2.isChecked()){
                    ckBox1.setChecked(false);
                    ckBox3.setChecked(false);
                    ckBox4.setChecked(false);
                    ckBox5.setChecked(false);
                    oneWeakPrice.setVisibility(View.VISIBLE);
                    fifteenDayPrice.setVisibility(View.GONE);
                    oneMonthPrice.setVisibility(View.GONE);
                    customLay.setVisibility(View.GONE);
                    customDayEt.setText("");
                    customDayPrice.setText("");
                    customMonthEt.setText("");
                    customMonthPrice.setText("");
                    customDayPrice.setVisibility(View.GONE);
                    customMonthPrice.setVisibility(View.GONE);
                    mainPrice= Price2;
                    orderDayCount = "7";
                }else{
                    ckBox1.setChecked(true);
                }
            }
        });
        ckBox3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ckBox3.isChecked()){
                    ckBox1.setChecked(false);
                    ckBox2.setChecked(false);
                    ckBox4.setChecked(false);
                    ckBox5.setChecked(false);
                    oneWeakPrice.setVisibility(View.GONE);
                    fifteenDayPrice.setVisibility(View.VISIBLE);
                    oneMonthPrice.setVisibility(View.GONE);
                    customLay.setVisibility(View.GONE);
                    customDayEt.setText("");
                    customDayPrice.setText("");
                    customMonthEt.setText("");
                    customMonthPrice.setText("");
                    customDayPrice.setVisibility(View.GONE);
                    customMonthPrice.setVisibility(View.GONE);
                    mainPrice= Price3;
                    orderDayCount = "15";
                }else{
                    ckBox1.setChecked(true);
                }
            }
        });
        ckBox4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ckBox4.isChecked()){
                    ckBox1.setChecked(false);
                    ckBox3.setChecked(false);
                    ckBox2.setChecked(false);
                    ckBox5.setChecked(false);
                    oneWeakPrice.setVisibility(View.GONE);
                    fifteenDayPrice.setVisibility(View.GONE);
                    oneMonthPrice.setVisibility(View.VISIBLE);
                    customLay.setVisibility(View.GONE);
                    customDayEt.setText("");
                    customDayPrice.setText("");
                    customMonthEt.setText("");
                    customMonthPrice.setText("");
                    customDayPrice.setVisibility(View.GONE);
                    customMonthPrice.setVisibility(View.GONE);
                    mainPrice = Price4;
                    orderDayCount = "30";
                }else{
                    ckBox1.setChecked(true);
                }
            }
        });
        ckBox5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ckBox5.isChecked()){
                    ckBox1.setChecked(false);
                    ckBox3.setChecked(false);
                    ckBox4.setChecked(false);
                    ckBox2.setChecked(false);
                    oneWeakPrice.setVisibility(View.GONE);
                    fifteenDayPrice.setVisibility(View.GONE);
                    oneMonthPrice.setVisibility(View.GONE);
                   customLay.setVisibility(View.VISIBLE);
                    customMonthLay.setVisibility(View.VISIBLE);
                    customDayLay.setVisibility(View.VISIBLE);

                   customDayBtn.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View v) {
                           customDayLay.setVisibility(View.VISIBLE);
                           customDayPrice.setVisibility(View.VISIBLE);
                           customMonthLay.setVisibility(View.GONE);
                           String mDayCount = customDayEt.getText().toString().trim();

                           if(mDayCount.isEmpty()){
                               customDayEt.setError("Field is required!");
                               customDayEt.requestFocus();
                               return;
                           }else{
                               int dayCount = Integer.parseInt(mDayCount);
                               int mPrice1 = Integer.parseInt(Price1)*dayCount;
                               mainPrice = String.valueOf(mPrice1);
                               orderDayCount = mDayCount;
                               customDayPrice.setText(mainPrice+" taka");
                           }
                       }
                   });

                    customMonthBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            customDayLay.setVisibility(View.GONE);
                            customMonthLay.setVisibility(View.VISIBLE);
                            customMonthPrice.setVisibility(View.VISIBLE);
                            String mMonthCount = customMonthEt.getText().toString().trim();
                            if(mMonthCount.isEmpty()){
                                customMonthEt.setError("Field is required!");
                                customMonthEt.requestFocus();
                                return;
                            }else{
                                int monthCount = Integer.parseInt(mMonthCount);
                                int monthDayCount = monthCount*30;
                                String dayOfMonth = String.valueOf(monthDayCount);
                                int mPrice2 = Integer.parseInt(Price1)*(monthDayCount);
                                mainPrice = String.valueOf(mPrice2);
                                orderDayCount = dayOfMonth;
                                customMonthPrice.setText(mainPrice+" taka");
                            }

                        }
                    });



                }else{
                    ckBox1.setChecked(true);
                    customLay.setVisibility(View.GONE);
                }
            }
        });

    }

    private void GoForCheckOut(String price1, String medicine) {

        proceedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GetPricingActivity.this, ConfirmMedicineOrderActivity.class);
                intent.putExtra("singlePrice", price1);
                intent.putExtra("mainPrice", mainPrice);
                intent.putExtra("dayCount", orderDayCount);
                intent.putExtra("medicine", medicine);
                Log.d(TAG, "onClick: " + price1+".."+mainPrice+".."+orderDayCount);
                startActivity(intent);
            }
        });
    }

    private void getPrescriptionDetail() {
        prescriptionRef.child(userId).orderByKey().limitToLast(1).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot childInfo : snapshot.getChildren()){
                    PrescriptionInfo prescriptionInfo = childInfo.getValue(PrescriptionInfo.class);
                    Log.d(TAG, "onDataChange: "+prescriptionInfo.toString());
                    String uploadTime = prescriptionInfo.getTime();
                    pushId = prescriptionInfo.getPushId();
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
        holdingLay = findViewById(R.id.holdingLay);
        waitingLay = findViewById(R.id.waitingLay);

        oneDayPrice = findViewById(R.id.oneDayPrice);
        oneWeakPrice = findViewById(R.id.oneWeakPrice);
        fifteenDayPrice = findViewById(R.id.fifteenDayPrice);
        oneMonthPrice = findViewById(R.id.oneMonthPrice);

        medicineList = findViewById(R.id.listOfMedicine);
        progressBar = findViewById(R.id.progressBar);

        ckBox1 = findViewById(R.id.checkBox1);
        ckBox2 = findViewById(R.id.checkBox2);
        ckBox3 = findViewById(R.id.checkBox3);
        ckBox4 = findViewById(R.id.checkBox4);
        ckBox5 = findViewById(R.id.checkBox5);

        customLay = findViewById(R.id.customLay);
        customDayLay = findViewById(R.id.customDayLay);
        customMonthLay = findViewById(R.id.customMonthLay);

        customDayBtn = findViewById(R.id.customDayOk);
        customMonthBtn = findViewById(R.id.customMonthOk);

        customDayEt = findViewById(R.id.customDayEt);
        customMonthEt = findViewById(R.id.customMonthEt);

        customDayPrice = findViewById(R.id.customDayPrice);
        customMonthPrice = findViewById(R.id.customMonthPrice);

        proceedBtn = findViewById(R.id.proceedBtn);
    }
}