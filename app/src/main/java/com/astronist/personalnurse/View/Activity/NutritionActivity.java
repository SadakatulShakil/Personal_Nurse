package com.astronist.personalnurse.View.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.astronist.personalnurse.Adapter.ProductAdapter;
import com.astronist.personalnurse.Model.CartList;
import com.astronist.personalnurse.Model.ProductInfo;
import com.astronist.personalnurse.R;
import com.google.firebase.auth.FirebaseAuth;
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
    private DatabaseReference productRef, cartReference;
    public static final String TAG = "Product";
    private CircleImageView babyMilk, chocolate;
    private ProgressBar progressBar;
    private FirebaseAuth firebaseAuth;
    private ArrayList<CartList> cartListArrayList = new ArrayList<>();
    private TextView cartItemCount;
    private RelativeLayout notifyLay;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutrition);
        inItView();
        firebaseAuth = FirebaseAuth.getInstance();
        userId = firebaseAuth.getCurrentUser().getUid();
        progressBar.setVisibility(View.VISIBLE);
        getCartItemCount();
        babyMilkRecyclerView.setLayoutManager(new GridLayoutManager(NutritionActivity.this, 2, RecyclerView.VERTICAL, false));
        mProductAdapter = new ProductAdapter(mBabyMilkInfoList, NutritionActivity.this);
        babyMilkRecyclerView.setAdapter(mProductAdapter);

        getBabyMilk();

        subCategoryClickEvents();
        notifyLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(NutritionActivity.this, CartListActivity.class);
                startActivity(intent1);
            }
        });
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

    private void getCartItemCount() {

        userId = firebaseAuth.getCurrentUser().getUid();
        cartReference = FirebaseDatabase.getInstance().getReference().child("CartList").child(userId);

        cartReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cartListArrayList.clear();
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    CartList cartList = userSnapshot.getValue(CartList.class);

                    cartListArrayList.add(cartList);
                    Log.d(TAG, "onDataChange: " + cartListArrayList.size());
                    if (cartListArrayList.size() >= 1) {
                        cartItemCount.setVisibility(View.VISIBLE);
                        String cartCount = String.valueOf(cartListArrayList.size());
                        cartItemCount.setText(cartCount);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
        cartItemCount = findViewById(R.id.notificationCountTv);
        notifyLay = findViewById(R.id.auctionNotificationAction);
    }
}