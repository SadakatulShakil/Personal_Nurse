package com.astronist.personalnurse.View.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.astronist.personalnurse.R;

import pl.droidsonroids.gif.GifImageView;

public class WaitingOfferedPriceActivity extends AppCompatActivity {

    private GifImageView gifImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_offered_price);

        inItView();
    }

    private void inItView() {

        gifImage = findViewById(R.id.gifView);
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