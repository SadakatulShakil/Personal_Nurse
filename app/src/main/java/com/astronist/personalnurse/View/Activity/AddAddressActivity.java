package com.astronist.personalnurse.View.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.astronist.personalnurse.Model.Address;
import com.astronist.personalnurse.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddAddressActivity extends AppCompatActivity {
    private EditText userName, userPhone, addressLine1, addressLine2, userRoadNo;
    private ExtendedFloatingActionButton saveAddress;
    public static final String TAG ="address";
    private FirebaseAuth firebaseAuth;
    private String userId;
    private DatabaseReference addressRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);
        inItView();
        firebaseAuth = FirebaseAuth.getInstance();
        userId = firebaseAuth.getCurrentUser().getUid();
        getUserAddress();

        saveAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpUserAddress();
                getUserAddress();
            }
        });

    }

    private void setUpUserAddress() {
        String uName = userName.getText().toString().trim();
        String uPhone = userPhone.getText().toString().trim();
        String uAddress1 = addressLine1.getText().toString().trim();
        String uAddress2 = addressLine2.getText().toString().trim();
        String uRoadNo = userRoadNo.getText().toString().trim();

        if (uName.isEmpty()) {
            userName.setError("Name is required!");
            userName.requestFocus();
            return;
        }
        if (uPhone.isEmpty()) {
            userPhone.setError("Phone no is required!");
            userPhone.requestFocus();
            return;
        }

        if (uAddress1.isEmpty()) {
            addressLine1.setError("Address line 1 no is required!");
            addressLine1.requestFocus();
            return;
        }

        if (uAddress2.isEmpty()) {
            addressLine2.setError("Address line 2 no is required!");
            addressLine2.requestFocus();
            return;
        }

        if (uRoadNo.isEmpty()) {
            userRoadNo.setError("Road no is required!");
            userRoadNo.requestFocus();
            return;
        }

        StoreAddress(uName, uPhone, uAddress1, uAddress2, uRoadNo);
    }

    private void StoreAddress(String uName, String uPhone, String uAddress1, String uAddress2, String uRoadNo) {

        addressRef = FirebaseDatabase.getInstance().getReference().child("UserAddress");
        Address userAddress= new Address(userId, uName, uPhone, uAddress1, uAddress2, uRoadNo);

        addressRef.child(userId).setValue(userAddress).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    finish();
                    Toast.makeText(AddAddressActivity.this, "Your Address Saved Successfully !", Toast.LENGTH_SHORT).show();
                    Intent profile = new Intent(AddAddressActivity.this, ProfileActivity.class);
                    startActivity(profile);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: "+e.getMessage());
            }
        });
    }

    private void inItView() {
        userName = findViewById(R.id.name);
        userPhone = findViewById(R.id.phone);
        addressLine1 = findViewById(R.id.addressLine1);
        addressLine2 = findViewById(R.id.addressLine2);
        userRoadNo = findViewById(R.id.roadNo);

        saveAddress = findViewById(R.id.save_fab);
    }

    private void getUserAddress() {
        addressRef = FirebaseDatabase.getInstance().getReference().child("UserAddress");
        addressRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    Address addressInfo = userSnapshot.getValue(Address.class);

                    if (userId.equals(addressInfo.getUserId())) {
                        userName.setText(addressInfo.getName());
                        userPhone.setText(addressInfo.getPhone());
                        addressLine1.setText(addressInfo.getAddressLine1());
                        addressLine2.setText(addressInfo.getGetAddressLine2());
                        userRoadNo.setText(addressInfo.getRoadNo()+" no. road");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}