package com.astronist.personalnurse;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.astronist.personalnurse.View.Activity.UserUiContainerActivity;

public class MainActivity extends AppCompatActivity {

    private Button goBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inItView();

        goBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(MainActivity.this, UserUiContainerActivity.class);
                startActivity(intent);
            }
        });
    }

    private void inItView() {

        goBtn = findViewById(R.id.continueBtn);
    }
}