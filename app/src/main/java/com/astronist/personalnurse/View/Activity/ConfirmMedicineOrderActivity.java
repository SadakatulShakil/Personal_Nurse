package com.astronist.personalnurse.View.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.astronist.personalnurse.Model.Address;
import com.astronist.personalnurse.Model.MedicineOrder;
import com.astronist.personalnurse.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ConfirmMedicineOrderActivity extends AppCompatActivity {
    private EditText nameEt, phoneEt, addressLine1Et, addressLine2Et, roadNoEt;
    private TextView saveAddressBtn, medicineList, singlePriceTv, dayCount, lastSinglePrice, totalPrice;
    private ExtendedFloatingActionButton checkOutBtn;
    private String getSinglePrice, getMedicineList, getMainPrice, getOrderDayCount,userId, status = "pending", orderTime, orderDate, paymentMethod;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private DatabaseReference addressRef, orderReference;
    private CheckBox ckBox1, ckBox2;
    public static final String TAG = "checkOut";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_medicine_order);
        inItView();
        firebaseAuth = FirebaseAuth.getInstance();
        userId = firebaseAuth.getCurrentUser().getUid();
        Log.d(TAG, "onCreate: " +userId);
        Intent intent = getIntent();
        getSinglePrice = intent.getStringExtra("singlePrice");
        getMedicineList = intent.getStringExtra("medicine");
        getMainPrice = intent.getStringExtra("mainPrice");
        getOrderDayCount = intent.getStringExtra("dayCount");


        medicineList.setText(getMedicineList);
        singlePriceTv.setText(getSinglePrice+" taka");
        dayCount.setText(getOrderDayCount+" days");
        lastSinglePrice.setText(getSinglePrice+" taka");
        totalPrice.setText(getMainPrice+" taka");
        getUserAddress();
        saveAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setUpUserAddress();
                getUserAddress();
            }
        });

        ckBox1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ckBox1.isChecked()){
                    paymentMethod = "ssl";
                    ckBox2.setChecked(false);
                }else{
                    paymentMethod = "";
                }
            }
        });
        ckBox2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ckBox2.isChecked()){
                    paymentMethod = "cod";
                    ckBox1.setChecked(false);
                }else{
                    paymentMethod = "";
                }
            }
        });

        checkOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uName = nameEt.getText().toString().trim();
                String uPhone = phoneEt.getText().toString().trim();
                String uAddress1 = addressLine1Et.getText().toString().trim();
                String uAddress2 = addressLine2Et.getText().toString().trim();
                String uRoadNo = roadNoEt.getText().toString().trim();

                if (uName.isEmpty()) {
                    nameEt.setError("Name is required!");
                    nameEt.requestFocus();
                    return;
                }
                if (uPhone.isEmpty()) {
                    phoneEt.setError("Phone no is required!");
                    phoneEt.requestFocus();
                    return;
                }

                if (uAddress1.isEmpty()) {
                    addressLine1Et.setError("Address line 1 no is required!");
                    addressLine1Et.requestFocus();
                    return;
                }

                if (uAddress2.isEmpty()) {
                    addressLine2Et.setError("Address line 2 no is required!");
                    addressLine2Et.requestFocus();
                    return;
                }

                if (uRoadNo.isEmpty()) {
                    roadNoEt.setError("Road no is required!");
                    roadNoEt.requestFocus();
                    return;
                }
                if(ckBox1.isChecked() || ckBox2.isChecked()){
                    GoForCheckOut(paymentMethod);
                }else{
                    Toast.makeText(ConfirmMedicineOrderActivity.this, "Please Select a Payment Option!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void setUpUserAddress() {
        String uName = nameEt.getText().toString().trim();
        String uPhone = phoneEt.getText().toString().trim();
        String uAddress1 = addressLine1Et.getText().toString().trim();
        String uAddress2 = addressLine2Et.getText().toString().trim();
        String uRoadNo = roadNoEt.getText().toString().trim();

        if (uName.isEmpty()) {
            nameEt.setError("Name is required!");
            nameEt.requestFocus();
            return;
        }
        if (uPhone.isEmpty()) {
            phoneEt.setError("Phone no is required!");
            phoneEt.requestFocus();
            return;
        }

        if (uAddress1.isEmpty()) {
            addressLine1Et.setError("Address line 1 no is required!");
            addressLine1Et.requestFocus();
            return;
        }

        if (uAddress2.isEmpty()) {
            addressLine2Et.setError("Address line 2 no is required!");
            addressLine2Et.requestFocus();
            return;
        }

        if (uRoadNo.isEmpty()) {
            roadNoEt.setError("Road no is required!");
            roadNoEt.requestFocus();
            return;
        }

        StoreAddress(uName, uPhone, uAddress1, uAddress2, uRoadNo);
    }

    private void StoreAddress(final String uName, final String uPhone, final String uAddress1, final String uAddress2, final String uRoadNo) {

        addressRef = FirebaseDatabase.getInstance().getReference().child("UserAddress");
        Address userAddress= new Address(userId, uName, uPhone, uAddress1, uAddress2, uRoadNo);

        addressRef.child(userId).setValue(userAddress).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(ConfirmMedicineOrderActivity.this, "Your Address Saved Successfully !", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: "+e.getMessage());
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

                    if (userId.equals(addressInfo.getUserId())) {
                        nameEt.setText(addressInfo.getName());
                        phoneEt.setText(addressInfo.getPhone());
                        addressLine1Et.setText(addressInfo.getAddressLine1());
                        addressLine2Et.setText(addressInfo.getGetAddressLine2());
                        roadNoEt.setText(addressInfo.getRoadNo()+" no. road");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void GoForCheckOut(String paymentMethod) {
        String uName = nameEt.getText().toString().trim();
        String uPhone = phoneEt.getText().toString().trim();
        String uAddress1 = addressLine1Et.getText().toString().trim();
        String uAddress2 = addressLine2Et.getText().toString().trim();
        String uRoadNo = roadNoEt.getText().toString().trim();

        if (uName.isEmpty()) {
            nameEt.setError("Name is required!");
            nameEt.requestFocus();
            return;
        }
        if (uPhone.isEmpty()) {
            phoneEt.setError("Phone no is required!");
            phoneEt.requestFocus();
            return;
        }

        if (uAddress1.isEmpty()) {
            addressLine1Et.setError("Address line 1 no is required!");
            addressLine1Et.requestFocus();
            return;
        }

        if (uAddress2.isEmpty()) {
            addressLine2Et.setError("Address line 2 no is required!");
            addressLine2Et.requestFocus();
            return;
        }

        if (uRoadNo.isEmpty()) {
            roadNoEt.setError("Road no is required!");
            roadNoEt.requestFocus();
            return;
        }
        storeMedicineOrder(uName, uPhone, uAddress1, uAddress2, uRoadNo, getMedicineList, getSinglePrice, getMainPrice, getOrderDayCount, paymentMethod);
    }

    private void storeMedicineOrder(final String uName, final String uPhone, final String uAddress1,
                                    final String uAddress2, final String uRoadNo,
                                    final String getMedicineList, final String getSinglePrice,
                                    final String getMainPrice, final String getOrderDayCount, final String paymentMethod) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat myTimeFormat = new SimpleDateFormat("hh:mm a", Locale.US);
        orderTime = myTimeFormat.format(calendar.getTime());
        SimpleDateFormat myDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        orderDate = myDateFormat.format(calendar.getTime());

        userId = firebaseAuth.getCurrentUser().getUid();
        orderReference = FirebaseDatabase.getInstance().getReference().child("Order").child(userId);
        String pushId = orderReference.push().getKey();

        MedicineOrder medicineOrder = new MedicineOrder(userId, pushId, uName, uPhone, uAddress1, uAddress2,
                uRoadNo, getMedicineList, getSinglePrice, getMainPrice, getOrderDayCount, status, paymentMethod, orderDate, orderTime);

        orderReference.child(pushId).setValue(medicineOrder).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(ConfirmMedicineOrderActivity.this, "Order Submit successful !", Toast.LENGTH_SHORT).show();
                    finish();
                    Intent intent = new Intent(ConfirmMedicineOrderActivity.this, PaymentActivity.class);
                    intent.putExtra("paymentMethod", paymentMethod);
                    intent.putExtra("totalAmount", getMainPrice);
                    startActivity(intent);
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    private void inItView() {

        nameEt = findViewById(R.id.name);
        phoneEt = findViewById(R.id.phone);
        addressLine1Et = findViewById(R.id.addressLine1);
        addressLine2Et = findViewById(R.id.addressLine2);
        roadNoEt = findViewById(R.id.roadNo);
        saveAddressBtn = findViewById(R.id.saveAddress);
        medicineList = findViewById(R.id.listOfMedicine);
        singlePriceTv = findViewById(R.id.singlePrice);
        dayCount = findViewById(R.id.dayCount);
        lastSinglePrice = findViewById(R.id.singleDayPrice);
        totalPrice = findViewById(R.id.totalPrice);
        checkOutBtn = findViewById(R.id.checkOutBtn);

        ckBox1 = findViewById(R.id.checkBox1);
        ckBox2 = findViewById(R.id.checkBox2);

    }
}