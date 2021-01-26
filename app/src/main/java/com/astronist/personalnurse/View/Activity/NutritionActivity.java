package com.astronist.personalnurse.View.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

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

public class NutritionActivity extends AppCompatActivity {
    private RecyclerView babyMilkRecyclerView, chocolateRecyclerView;
    private ProductAdapter mProductAdapter;
    private ArrayList<ProductInfo> mBabyMilkInfoList = new ArrayList<>();
    private ArrayList<ProductInfo> mChocolateInfoList = new ArrayList<>();
    private DatabaseReference productRef;
    public static final String TAG = "Product";
    private CircleImageView babyMilk, chocolate;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutrition);
        inItView();

        progressBar.setVisibility(View.VISIBLE);

        babyMilkRecyclerView.setLayoutManager(new GridLayoutManager(NutritionActivity.this, 2, RecyclerView.VERTICAL, false));
        mProductAdapter = new ProductAdapter(mBabyMilkInfoList, NutritionActivity.this);
        babyMilkRecyclerView.setAdapter(mProductAdapter);

        getBabyMilk();

        subCategoryClickEvents();
    }

    private void subCategoryClickEvents() {
        chocolate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chocolateRecyclerView.setVisibility(View.VISIBLE);
                babyMilkRecyclerView.setVisibility(View.GONE);
                getChocolate();
            }
        });

        babyMilk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chocolateRecyclerView.setVisibility(View.GONE);
                babyMilkRecyclerView.setVisibility(View.VISIBLE);
                getBabyMilk();
            }
        });
    }

    private void getChocolate() {
        FirebaseDatabase fdb = FirebaseDatabase.getInstance();
        productRef = fdb.getReference("ProductInfo");

        productRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mChocolateInfoList.clear();
                for (DataSnapshot productSnap : snapshot.getChildren()) {

                    ProductInfo productInfo = productSnap.getValue(ProductInfo.class);
                    if (productInfo.getSubCategory().equals("Chocolate")) {

                        mChocolateInfoList.add(productInfo);
                        progressBar.setVisibility(View.GONE);
                    }

                }
                Log.d(TAG, "onDataChange: " + mChocolateInfoList.size());
                chocolateRecyclerView.setLayoutManager(new GridLayoutManager(NutritionActivity.this, 2, RecyclerView.VERTICAL, false));
                mProductAdapter = new ProductAdapter(mChocolateInfoList, NutritionActivity.this);
                chocolateRecyclerView.setAdapter(mProductAdapter);
                mProductAdapter.notifyDataSetChanged();
                mProductAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void getBabyMilk() {
        FirebaseDatabase fdb = FirebaseDatabase.getInstance();
        productRef = fdb.getReference("ProductInfo");

        productRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mBabyMilkInfoList.clear();
                for (DataSnapshot productSnap : snapshot.getChildren()) {

                    ProductInfo productInfo = productSnap.getValue(ProductInfo.class);
                    if (productInfo.getSubCategory().equals("Baby Milk")) {

                        mBabyMilkInfoList.add(productInfo);
                        progressBar.setVisibility(View.GONE);
                    }

                }
                Log.d(TAG, "onDataChange: " + mBabyMilkInfoList.size());
                mProductAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void inItView() {
        babyMilkRecyclerView = findViewById(R.id.babyMilkRecycleView);
        chocolateRecyclerView = findViewById(R.id.chocolateRecycleView);
        babyMilk = findViewById(R.id.babyMilkBt);
        chocolate = findViewById(R.id.chocolateBt);
        progressBar = findViewById(R.id.progressBar);
    }
}