package com.astronist.personalnurse.View.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.astronist.personalnurse.R;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

public class PaymentActivity extends AppCompatActivity {

    private String paymentMethod, totalAmount;
    private ExtendedFloatingActionButton goToHome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        inItView();
        Intent intent = getIntent();
        paymentMethod = intent.getStringExtra("paymentMethod");
        totalAmount = intent.getStringExtra("totalAmount");

        goToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent1 = new Intent(PaymentActivity.this, UserUiContainerActivity.class);
                startActivity(intent1);
            }
        });
    }

    private void inItView() {
        goToHome = findViewById(R.id.gotoHomeBtn);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Continue Personal Nurse!(হোমে যাওয়ার আবেদন !)");
        alertDialog.setMessage("You Want To Go back?(আরো ঘুরে দেখতে চান !)");
        alertDialog.setIcon(R.drawable.happy);

        alertDialog.setPositiveButton("Yes(হ্যাঁ)", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
                Intent intent = new Intent(PaymentActivity.this, UserUiContainerActivity.class);
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