package com.astronist.personalnurse.View.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.astronist.personalnurse.R;

public class OrderCompleteActivity extends AppCompatActivity {

    private TextView goToHomeBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_complete);
        goToHomeBtn = findViewById(R.id.goToHome);

        goToHomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(OrderCompleteActivity.this, UserUiContainerActivity.class);
                startActivity(intent);
            }
        });
    }
}