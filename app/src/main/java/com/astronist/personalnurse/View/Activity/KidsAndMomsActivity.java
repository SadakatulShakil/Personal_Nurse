package com.astronist.personalnurse.View.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class KidsAndMomsActivity extends AppCompatActivity {

    private RecyclerView babyDiaperRecyclerView, babyWipesRecyclerView, womenCareRecyclerView;
    private ProductAdapter mProductAdapter;
    private ArrayList<ProductInfo> mBabyDiaperInfoList = new ArrayList<>();
    private ArrayList<ProductInfo> mBabyWipesInfoList = new ArrayList<>();
    private ArrayList<ProductInfo> mWomenCareInfoList = new ArrayList<>();
    private DatabaseReference productRef, cartReference;
    public static final String TAG = "Product";
    private CircleImageView babyDiaper, babyWipes, womenCare;
    private ProgressBar progressBar;
    private FirebaseAuth firebaseAuth;
    private ArrayList<CartList> cartListArrayList= new ArrayList<>();
    private TextView cartItemCount;
    private RelativeLayout notifyLay;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kids_and_moms);
        inItView();
        firebaseAuth = FirebaseAuth.getInstance();
        userId = firebaseAuth.getCurrentUser().getUid();
        getCartItemCount();
        progressBar.setVisibility(View.VISIBLE);

        babyDiaperRecyclerView.setLayoutManager(new GridLayoutManager(KidsAndMomsActivity.this, 2, RecyclerView.VERTICAL, false));
        mProductAdapter = new ProductAdapter(mBabyDiaperInfoList, KidsAndMomsActivity.this);
        babyDiaperRecyclerView.setAdapter(mProductAdapter);
        getBabyDiaper();

        subCategoryClickEvents();

        notifyLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(KidsAndMomsActivity.this, CartListActivity.class);
                startActivity(intent1);
            }
        });
    }

    private void subCategoryClickEvents() {

        babyDiaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                babyWipesRecyclerView.setVisibility(View.GONE);
                womenCareRecyclerView.setVisibility(View.GONE);
                babyDiaperRecyclerView.setVisibility(View.VISIBLE);
                getBabyDiaper();
            }
        });

        babyWipes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                babyWipesRecyclerView.setVisibility(View.VISIBLE);
                womenCareRecyclerView.setVisibility(View.GONE);
                babyDiaperRecyclerView.setVisibility(View.GONE);
                getBabyWipes();
            }
        });

        womenCare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                babyWipesRecyclerView.setVisibility(View.GONE);
                womenCareRecyclerView.setVisibility(View.VISIBLE);
                babyDiaperRecyclerView.setVisibility(View.GONE);
                getWomenCare();
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
    private void getWomenCare() {
        FirebaseDatabase fdb = FirebaseDatabase.getInstance();
        productRef = fdb.getReference("ProductInfo");

        productRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mWomenCareInfoList.clear();
                for (DataSnapshot productSnap : snapshot.getChildren()) {

                    ProductInfo productInfo = productSnap.getValue(ProductInfo.class);
                    if (productInfo.getSubCategory().equals("Women's care")) {

                        mWomenCareInfoList.add(productInfo);
                        progressBar.setVisibility(View.GONE);
                    }

                }
                Log.d(TAG, "onDataChange: " + mWomenCareInfoList.size());
                womenCareRecyclerView.setLayoutManager(new GridLayoutManager(KidsAndMomsActivity.this, 2, RecyclerView.VERTICAL, false));
                mProductAdapter = new ProductAdapter(mWomenCareInfoList, KidsAndMomsActivity.this);
                womenCareRecyclerView.setAdapter(mProductAdapter);
                mProductAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getBabyWipes() {
        FirebaseDatabase fdb = FirebaseDatabase.getInstance();
        productRef = fdb.getReference("ProductInfo");

        productRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mBabyWipesInfoList.clear();
                for (DataSnapshot productSnap : snapshot.getChildren()) {

                    ProductInfo productInfo = productSnap.getValue(ProductInfo.class);
                    if (productInfo.getSubCategory().equals("Baby wipers")) {

                        mBabyWipesInfoList.add(productInfo);
                        progressBar.setVisibility(View.GONE);
                    }

                }
                Log.d(TAG, "onDataChange: " + mBabyWipesInfoList.size());
                babyWipesRecyclerView.setLayoutManager(new GridLayoutManager(KidsAndMomsActivity.this, 2, RecyclerView.VERTICAL, false));
                mProductAdapter = new ProductAdapter(mBabyWipesInfoList, KidsAndMomsActivity.this);
                babyWipesRecyclerView.setAdapter(mProductAdapter);
                mProductAdapter.notifyDataSetChanged();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getBabyDiaper() {
        FirebaseDatabase fdb = FirebaseDatabase.getInstance();
        productRef = fdb.getReference("ProductInfo");

        productRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mBabyDiaperInfoList.clear();
                for (DataSnapshot productSnap : snapshot.getChildren()) {

                    ProductInfo productInfo = productSnap.getValue(ProductInfo.class);
                    if (productInfo.getSubCategory().equals("Baby diapers")) {

                        mBabyDiaperInfoList.add(productInfo);
                        progressBar.setVisibility(View.GONE);
                    }

                }
                Log.d(TAG, "onDataChange: " + mBabyDiaperInfoList.size());
                mProductAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void inItView() {
        babyDiaper = findViewById(R.id.babyDiaperBt);
        babyWipes = findViewById(R.id.babyWipesBt);
        womenCare = findViewById(R.id.womenCareBt);
        babyDiaperRecyclerView = findViewById(R.id.babyDiaperRecycleView);
        babyWipesRecyclerView = findViewById(R.id.babyWipesRecycleView);
        womenCareRecyclerView = findViewById(R.id.womenCareRecycleView);
        progressBar = findViewById(R.id.progressBar);
        cartItemCount = findViewById(R.id.notificationCountTv);
        notifyLay = findViewById(R.id.auctionNotificationAction);
    }
}