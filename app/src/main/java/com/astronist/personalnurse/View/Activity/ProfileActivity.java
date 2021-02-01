package com.astronist.personalnurse.View.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.astronist.personalnurse.Model.Address;
import com.astronist.personalnurse.Model.CartList;
import com.astronist.personalnurse.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {

    private TextView logOutBtn, phoneTv, nameTv, addressLine1Tv, addressLine2Tv, roadNoTv, warningText, cartItemCount;
    private ImageView addHomeBtn, warningIcon;
    private LinearLayout additionalLay;
    private RelativeLayout notifyLay;
    private String userId;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference cartReference, addressRef;
    private ArrayList<CartList> cartListArrayList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        inItView();
        firebaseAuth = FirebaseAuth.getInstance();
        userId = firebaseAuth.getCurrentUser().getUid();
        getUserAddress();
        getCartItemCount();
        notifyLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(ProfileActivity.this, CartListActivity.class);
                startActivity(intent1);
            }
        });
        addHomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent address = new Intent(ProfileActivity.this, AddAddressActivity.class);
                startActivity(address);
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

    private void getUserAddress() {
        addressRef = FirebaseDatabase.getInstance().getReference().child("UserAddress");
        addressRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    Address addressInfo = userSnapshot.getValue(Address.class);
                    if(addressInfo != null){
                        additionalLay.setVisibility(View.VISIBLE);
                        warningIcon.setVisibility(View.GONE);
                        warningText.setVisibility(View.GONE);
                        if (userId.equals(addressInfo.getUserId())) {
                            nameTv.setText(addressInfo.getName());
                            phoneTv.setText(addressInfo.getPhone());
                            addressLine1Tv.setText(addressInfo.getAddressLine1());
                            addressLine2Tv.setText(addressInfo.getGetAddressLine2());
                            roadNoTv.setText(addressInfo.getRoadNo());
                        }
                    }else{
                        additionalLay.setVisibility(View.GONE);
                        warningIcon.setVisibility(View.VISIBLE);
                        warningText.setVisibility(View.VISIBLE);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void inItView() {
        logOutBtn = findViewById(R.id.goToHome);
        nameTv = findViewById(R.id.userName);
        phoneTv = findViewById(R.id.userPhone);
        addressLine1Tv = findViewById(R.id.userAddress1);
        addressLine2Tv = findViewById(R.id.userAddress2);
        roadNoTv = findViewById(R.id.userRoad);
        warningText = findViewById(R.id.warningText);
        warningIcon = findViewById(R.id.warning);
        addHomeBtn = findViewById(R.id.addHome);
        additionalLay = findViewById(R.id.additionalLay);
        cartItemCount = findViewById(R.id.notificationCountTv);
        notifyLay = findViewById(R.id.auctionNotificationAction);
    }
}