package com.astronist.personalnurse.View.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import static androidx.constraintlayout.widget.Constraints.TAG;

public class DailyDrugsActivity extends AppCompatActivity {

    private RecyclerView dailyDrugRecyclerView;
    private ArrayList<ProductInfo> mDailyDrugList = new ArrayList<>();
    private ProductAdapter productAdapter;
    private ProgressBar progressBar;
    private DatabaseReference productRef, cartReference;
    private FirebaseAuth firebaseAuth;
    public static final String TAG = "DailyDrug";
    private ArrayList<CartList> cartListArrayList= new ArrayList<>();
    private TextView cartItemCount;
    private RelativeLayout notifyLay;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_drugs);
        inItView();
        firebaseAuth = FirebaseAuth.getInstance();
        userId = firebaseAuth.getCurrentUser().getUid();
        getCartItemCount();
        getDailyDrugs();

        notifyLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(DailyDrugsActivity.this, CartListActivity.class);
                startActivity(intent1);
            }
        });
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

    private void getCartItemCount() {

        cartReference = FirebaseDatabase.getInstance().getReference().child("CartList").child(userId);

        cartReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cartListArrayList.clear();
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    CartList cartList = userSnapshot.getValue(CartList.class);

                    cartListArrayList.add(cartList);
                    Log.d(TAG, "onDataChange: "+ cartListArrayList.size());
                    if(cartListArrayList.size()>=1){
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
    private void inItView() {
        dailyDrugRecyclerView = findViewById(R.id.dailyDrugRecycleView);
        progressBar = findViewById(R.id.progressBar);
        cartItemCount = findViewById(R.id.notificationCountTv);
        notifyLay = findViewById(R.id.auctionNotificationAction);
    }
}