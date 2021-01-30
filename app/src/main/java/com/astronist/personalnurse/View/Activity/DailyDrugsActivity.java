package com.astronist.personalnurse.View.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.astronist.personalnurse.Adapter.ProductAdapter;
import com.astronist.personalnurse.Model.ProductInfo;
import com.astronist.personalnurse.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DailyDrugsActivity extends AppCompatActivity {

    private RecyclerView dailyDrugRecyclerView;
    private ArrayList<ProductInfo> mDailyDrugList = new ArrayList<>();
    private ProductAdapter productAdapter;
    private ProgressBar progressBar;
    private DatabaseReference productRef;
    public static final String TAG = "DailyDrug";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_drugs);
        inItView();

        getDailyDrugs();
    }

    private void getDailyDrugs() {
        FirebaseDatabase fdb = FirebaseDatabase.getInstance();
        productRef = fdb.getReference("ProductInfo");

        productRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mDailyDrugList.clear();
                for (DataSnapshot productSnap : snapshot.getChildren()) {

                    ProductInfo productInfo = productSnap.getValue(ProductInfo.class);
                    if (productInfo.getSubCategory().equals("Normal")) {

                        mDailyDrugList.add(productInfo);
                        progressBar.setVisibility(View.GONE);
                    }

                }
                Log.d(TAG, "onDataChange: " + mDailyDrugList.size());
                dailyDrugRecyclerView.setLayoutManager(new GridLayoutManager(DailyDrugsActivity.this, 2, RecyclerView.VERTICAL, false));
                productAdapter = new ProductAdapter(mDailyDrugList, DailyDrugsActivity.this);
                dailyDrugRecyclerView.setAdapter(productAdapter);
                productAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void inItView() {
        dailyDrugRecyclerView = findViewById(R.id.dailyDrugRecycleView);
        progressBar = findViewById(R.id.progressBar);
    }
}