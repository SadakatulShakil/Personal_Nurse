package com.astronist.personalnurse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.astronist.personalnurse.View.Activity.UserUiContainerActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private Button goBtn, nextBtn;
    private ConstraintLayout demoLay, verificationLay;
    private LinearLayout fbLayOut;
    private EditText phoneNumberEt, inputCode0, inputCode1, inputCode2, inputCode3, inputCode4, inputCode5;
    private String countryCode = "+88";
    private String verificationOTP;
    public static final String TAG = "Main";
    private FirebaseAuth firebaseAuth;
    private ProgressBar progressBar1, progressBar2;
    private String verificationOTPId="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inItView();
        firebaseAuth = FirebaseAuth.getInstance();
        setUpOTPInputs();

        if (firebaseAuth.getCurrentUser() != null) {
            finish();
            Intent intent = new Intent(MainActivity.this, UserUiContainerActivity.class);
            startActivity(intent);
        }

        goBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*finish();
                Intent intent = new Intent(MainActivity.this, UserUiContainerActivity.class);
                startActivity(intent);*/
                String phoneNumber = phoneNumberEt.getText().toString().trim();
                if (phoneNumber.isEmpty()) {
                    phoneNumberEt.setError("Phone number is required!");
                    phoneNumberEt.requestFocus();
                    return;
                } else {
                    progressBar1.setVisibility(View.VISIBLE);
                    demoLay.setVisibility(View.GONE);
                    verificationLay.setVisibility(View.VISIBLE);
                    DoOTPLogIn(phoneNumber);
                }

            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(inputCode0.getText().toString().trim().isEmpty()
                ||inputCode0.getText().toString().trim().isEmpty()
                        ||inputCode1.getText().toString().trim().isEmpty()
                        ||inputCode2.getText().toString().trim().isEmpty()
                        ||inputCode3.getText().toString().trim().isEmpty()
                        ||inputCode4.getText().toString().trim().isEmpty()
                        ||inputCode5.getText().toString().trim().isEmpty()){
                    Toast.makeText(MainActivity.this, "Please Enter Valid Code!", Toast.LENGTH_SHORT).show();
                    return;
                }
                String code = inputCode0.getText().toString().trim()+
                        inputCode1.getText().toString().trim()+
                        inputCode2.getText().toString().trim()+
                        inputCode3.getText().toString().trim()+
                        inputCode4.getText().toString().trim()+
                        inputCode5.getText().toString().trim();

                if(verificationOTPId  != null){
                    progressBar2.setVisibility(View.VISIBLE);
                    PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(verificationOTPId, code);

                    FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        progressBar2.setVisibility(View.GONE);
                                        finish();
                                        Intent intent = new Intent(MainActivity.this, UserUiContainerActivity.class);
                                        startActivity(intent);
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "onFailure: " +e.getLocalizedMessage());
                        }
                    });
                }
            }
        });

    }

    private void setUpOTPInputs() {

        inputCode0.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    inputCode1.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        inputCode1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    inputCode2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        inputCode2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    inputCode3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        inputCode3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    inputCode4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        inputCode4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    inputCode5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void verifyOTP(String otp) {

        PhoneAuthCredential otpCredential = PhoneAuthProvider.getCredential(verificationOTP, otp);
        firebaseAuth.signInWithCredential(otpCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    FirebaseUser otpUser = task.getResult().getUser();
                    SendUserData((otpUser));
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "onFailure: " + e.getLocalizedMessage());
            }
        });
    }

    private void SendUserData(FirebaseUser otpUser) {
        finish();
        Intent intent = new Intent(MainActivity.this, UserUiContainerActivity.class);
        startActivity(intent);
    }

    private void DoOTPLogIn(String phoneNumber) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                countryCode + phoneNumber, 60,
                TimeUnit.SECONDS,
                MainActivity.this,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        progressBar1.setVisibility(View.GONE);

                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        progressBar1.setVisibility(View.GONE);
                        Log.d(TAG, "onVerificationFailed: " + e.getLocalizedMessage());
                    }

                    @Override
                    public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        progressBar1.setVisibility(View.GONE);
                        verificationOTPId = verificationId;

                    }
                }
        );
    }


    private void inItView() {

        goBtn = findViewById(R.id.continueBtn);
        demoLay = findViewById(R.id.demoNumberPage);
        fbLayOut = findViewById(R.id.fbLay);
        verificationLay = findViewById(R.id.verificationLay);

        phoneNumberEt = findViewById(R.id.phone);

        nextBtn = findViewById(R.id.nextBtn);
        inputCode0 = findViewById(R.id.inputCode0);
        inputCode1 = findViewById(R.id.inputCode1);
        inputCode2 = findViewById(R.id.inputCode2);
        inputCode3 = findViewById(R.id.inputCode3);
        inputCode4 = findViewById(R.id.inputCode4);
        inputCode5 = findViewById(R.id.inputCode5);

        progressBar1 = findViewById(R.id.progressBar);
        progressBar2 = findViewById(R.id.progressBar1);
    }
}