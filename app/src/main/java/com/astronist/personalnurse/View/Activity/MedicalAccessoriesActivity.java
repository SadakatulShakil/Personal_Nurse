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

import de.hdodenhof.circleimageview.CircleImageView;

public class MedicalAccessoriesActivity extends AppCompatActivity {

    private CircleImageView bloodPressure, diabetic, mask, handGloves, antibacterial;
    private RecyclerView bloodPressureRev, diabeticRev, maskRev, handGlovesRev, antibacterialRev;
    private ProductAdapter mProductAdapter;
    private ArrayList<ProductInfo> mBloodPressureList = new ArrayList<>();
    private ArrayList<ProductInfo> mDiabeticList = new ArrayList<>();
    private ArrayList<ProductInfo> mMaskList = new ArrayList<>();
    private ArrayList<ProductInfo> mHandGlovesList = new ArrayList<>();
    private ArrayList<ProductInfo> mAntibacterialList = new ArrayList<>();

    private DatabaseReference productRef;
    public static final String TAG = "Product";
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_accessories);
        inItView();
        progressBar.setVisibility(View.VISIBLE);
        bloodPressureRev.setLayoutManager(new GridLayoutManager(MedicalAccessoriesActivity.this, 2, RecyclerView.VERTICAL, false));
        mProductAdapter = new ProductAdapter(mBloodPressureList, MedicalAccessoriesActivity.this);
        bloodPressureRev.setAdapter(mProductAdapter);

        getBloodPressureList();
        subCategoryClickEvents();
    }

    private void getBloodPressureList() {
        FirebaseDatabase fdb = FirebaseDatabase.getInstance();
        productRef = fdb.getReference("ProductInfo");

        productRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mBloodPressureList.clear();
                for (DataSnapshot productSnap : snapshot.getChildren()) {

                    ProductInfo productInfo = productSnap.getValue(ProductInfo.class);
                    if (productInfo.getSubCategory().equals("Blood pressure machine")) {

                        mBloodPressureList.add(productInfo);
                        progressBar.setVisibility(View.GONE);
                    }

                }
                Log.d(TAG, "onDataChange: " + mBloodPressureList.size());
                mProductAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void subCategoryClickEvents() {

        bloodPressure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                diabeticRev.setVisibility(View.GONE);
                maskRev.setVisibility(View.GONE);
                handGlovesRev.setVisibility(View.GONE);
                bloodPressureRev.setVisibility(View.VISIBLE);
                antibacterialRev.setVisibility(View.GONE);
                getBloodPressureList();
            }
        });
        diabetic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                diabeticRev.setVisibility(View.VISIBLE);
                maskRev.setVisibility(View.GONE);
                handGlovesRev.setVisibility(View.GONE);
                bloodPressureRev.setVisibility(View.GONE);
                antibacterialRev.setVisibility(View.GONE);
                getDiabeticList();
            }
        });
        mask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                diabeticRev.setVisibility(View.GONE);
                maskRev.setVisibility(View.VISIBLE);
                handGlovesRev.setVisibility(View.GONE);
                bloodPressureRev.setVisibility(View.GONE);
                antibacterialRev.setVisibility(View.GONE);
                getMaskList();
            }
        });
        handGloves.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                diabeticRev.setVisibility(View.GONE);
                maskRev.setVisibility(View.GONE);
                handGlovesRev.setVisibility(View.VISIBLE);
                bloodPressureRev.setVisibility(View.GONE);
                antibacterialRev.setVisibility(View.GONE);
                getHandGlovesList();
            }
        });
        antibacterial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                diabeticRev.setVisibility(View.GONE);
                maskRev.setVisibility(View.GONE);
                handGlovesRev.setVisibility(View.GONE);
                bloodPressureRev.setVisibility(View.GONE);
                antibacterialRev.setVisibility(View.VISIBLE);
                getAntibacterialList();
            }
        });

    }

    private void getDiabeticList() {
        FirebaseDatabase fdb = FirebaseDatabase.getInstance();
        productRef = fdb.getReference("ProductInfo");

        productRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mDiabeticList.clear();
                for (DataSnapshot productSnap : snapshot.getChildren()) {

                    ProductInfo productInfo = productSnap.getValue(ProductInfo.class);
                    if (productInfo.getSubCategory().equals("Diabetics machine ")) {

                        mDiabeticList.add(productInfo);
                        progressBar.setVisibility(View.GONE);
                    }

                }
                Log.d(TAG, "onDataChange: " + mDiabeticList.size());
                        diabeticRev.setLayoutManager(new GridLayoutManager(MedicalAccessoriesActivity.this, 2, RecyclerView.VERTICAL, false));
                mProductAdapter = new ProductAdapter(mDiabeticList, MedicalAccessoriesActivity.this);
                diabeticRev.setAdapter(mProductAdapter);
                mProductAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getMaskList() {
        FirebaseDatabase fdb = FirebaseDatabase.getInstance();
        productRef = fdb.getReference("ProductInfo");

        productRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mMaskList.clear();
                for (DataSnapshot productSnap : snapshot.getChildren()) {

                    ProductInfo productInfo = productSnap.getValue(ProductInfo.class);
                    if (productInfo.getSubCategory().equals("Medical mask")) {

                        mMaskList.add(productInfo);
                        progressBar.setVisibility(View.GONE);
                    }

                }
                Log.d(TAG, "onDataChange: " + mMaskList.size());
                maskRev.setLayoutManager(new GridLayoutManager(MedicalAccessoriesActivity.this, 2, RecyclerView.VERTICAL, false));
                mProductAdapter = new ProductAdapter(mMaskList, MedicalAccessoriesActivity.this);
                maskRev.setAdapter(mProductAdapter);
                mProductAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getHandGlovesList() {
        FirebaseDatabase fdb = FirebaseDatabase.getInstance();
        productRef = fdb.getReference("ProductInfo");

        productRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mHandGlovesList.clear();
                for (DataSnapshot productSnap : snapshot.getChildren()) {

                    ProductInfo productInfo = productSnap.getValue(ProductInfo.class);
                    if (productInfo.getSubCategory().equals("Hand gloves")) {

                        mHandGlovesList.add(productInfo);
                        progressBar.setVisibility(View.GONE);
                    }

                }
                Log.d(TAG, "onDataChange: " + mHandGlovesList.size());
                handGlovesRev.setLayoutManager(new GridLayoutManager(MedicalAccessoriesActivity.this, 2, RecyclerView.VERTICAL, false));
                mProductAdapter = new ProductAdapter(mHandGlovesList, MedicalAccessoriesActivity.this);
                handGlovesRev.setAdapter(mProductAdapter);
                mProductAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getAntibacterialList() {
        FirebaseDatabase fdb = FirebaseDatabase.getInstance();
        productRef = fdb.getReference("ProductInfo");

        productRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mAntibacterialList.clear();
                for (DataSnapshot productSnap : snapshot.getChildren()) {

                    ProductInfo productInfo = productSnap.getValue(ProductInfo.class);
                    if (productInfo.getSubCategory().equals("Antibacterial")) {

                        mAntibacterialList.add(productInfo);
                        progressBar.setVisibility(View.GONE);
                    }

                }
                Log.d(TAG, "onDataChange: " + mAntibacterialList.size());
                antibacterialRev.setLayoutManager(new GridLayoutManager(MedicalAccessoriesActivity.this, 2, RecyclerView.VERTICAL, false));
                mProductAdapter = new ProductAdapter(mAntibacterialList, MedicalAccessoriesActivity.this);
                antibacterialRev.setAdapter(mProductAdapter);
                mProductAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void inItView() {
        bloodPressure = findViewById(R.id.bloodPressureBt);
        diabetic = findViewById(R.id.diabeticBt);
        mask = findViewById(R.id.maskBt);
        handGloves = findViewById(R.id.handGlovesBt);
        antibacterial = findViewById(R.id.antibacterialBt);

        bloodPressureRev = findViewById(R.id.bloodPressureRecycleView);
        diabeticRev = findViewById(R.id.diabeticRecycleView);
        maskRev = findViewById(R.id.maskRecycleView);
        handGlovesRev = findViewById(R.id.glovesRecycleView);
        antibacterialRev = findViewById(R.id.antibacterialRecycleView);
        progressBar = findViewById(R.id.progressBar);
    }
}