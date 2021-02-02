package com.astronist.personalnurse.View.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.astronist.personalnurse.Adapter.CartAdapter;
import com.astronist.personalnurse.Model.Address;
import com.astronist.personalnurse.Model.CartList;
import com.astronist.personalnurse.Model.DailyOrder;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class CartListActivity extends AppCompatActivity {
    private RecyclerView cartListRecView;
    private CartAdapter cartAdapter;
    private ArrayList<CartList> cartListArrayList = new ArrayList<>();
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private DatabaseReference cartReference, addressRef;
    private LinearLayoutManager manager;
    public static final String TAG = "Cart";
    private ProgressBar progressBar;
    private TextView grandAmount, grandUnit, saveAddress;
    private int gTotal = 0;
    private String currency;
    private CartList cartList;
    private DailyOrder dailyOrder;
    private ExtendedFloatingActionButton placeOrderBtn;
    private String monthName, userId, paymentMethod, status ="pending";
    private DatabaseReference cartToOrderReference;
    private EditText userName, userPhone, addressLine1, addressLine2, userRoadNo;
    private CheckBox ckBox1, ckBox2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_list);
        inItView();

        progressBar.setVisibility(View.VISIBLE);
        firebaseAuth = FirebaseAuth.getInstance();
        userId = firebaseAuth.getCurrentUser().getUid();
        Log.d(TAG, "onCreate: " + userId);
        getUserAddress();
        getCartItem();

        manager = new LinearLayoutManager(CartListActivity.this, RecyclerView.VERTICAL, false);
        cartListRecView.setLayoutManager(manager);
        cartAdapter = new CartAdapter(CartListActivity.this, cartListArrayList);
        cartListRecView.setAdapter(cartAdapter);

        saveAddress.setOnClickListener(new View.OnClickListener() {
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
        placeOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                if(ckBox1.isChecked() || ckBox2.isChecked()){
                    //////set up cart//////
                    setUpCartOrder(paymentMethod);
                }else{
                    Toast.makeText(CartListActivity.this, "Please Select a Payment Option!", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    private void setUpCartOrder(String paymentMethod) {

        for(int j = 0; j<cartListArrayList.size(); j++)
        {
            dailyOrder = new DailyOrder(cartListArrayList.get(j).getUserId(), cartListArrayList.get(j).getPushId(), cartListArrayList.get(j).getUserName(),
                    cartListArrayList.get(j).getUserPhone(), cartListArrayList.get(j).getUserAddress1(), cartListArrayList.get(j).getUserAddress2(), cartListArrayList.get(j).getUserRoadNo(),
                    cartListArrayList.get(j).getProductTitle(),cartListArrayList.get(j).getProductQuantity(), cartListArrayList.get(j).getTotalPrice(), cartListArrayList.get(j).getProductCategory(),
                    cartListArrayList.get(j).getUpTime(), cartListArrayList.get(j).getUpdate(), paymentMethod, status);

            cartToOrderReference = FirebaseDatabase.getInstance().getReference().child("Order");

            cartToOrderReference.child(userId).child(cartListArrayList.get(j).getPushId()).setValue(dailyOrder).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    finish();
                    Toast.makeText(CartListActivity.this, "Your Order Placed Successfully !", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(CartListActivity.this, OrderCompleteActivity.class);
                    startActivity(intent);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(CartListActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onFailure: " + e.getLocalizedMessage());


                }
            });
        }

        cartReference = FirebaseDatabase.getInstance().getReference().child("CartList");
        cartReference.child(userId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CartListActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onFailure: " + e.getLocalizedMessage());
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
                    Toast.makeText(CartListActivity.this, "Your Address Saved Successfully !", Toast.LENGTH_SHORT).show();
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

    private void getCartItem() {
        cartReference = FirebaseDatabase.getInstance().getReference().child("CartList").child(userId);
        cartReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cartListArrayList.clear();
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    CartList cartList = userSnapshot.getValue(CartList.class);

                    cartListArrayList.add(cartList);

                }

                Log.d(TAG, "onDataChange: "+cartListArrayList.size());
                for(int i = 0; i<cartListArrayList.size(); i++){
                    gTotal+= cartListArrayList.get(i).getTotalPrice();
                }
                Log.d(TAG, "onDataCheck: " +  gTotal);
                String grandTotal = String.valueOf(gTotal);
                grandAmount.setText("৳  "+grandTotal);
                cartAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void inItView() {
        cartListRecView = findViewById(R.id.cartInfoRecView);
        progressBar = findViewById(R.id.progressBar);
        grandAmount = findViewById(R.id.grandItemAmount);
        placeOrderBtn = findViewById(R.id.place_order_fab);
        saveAddress = findViewById(R.id.saveAddress);
        userName = findViewById(R.id.name);
        userPhone = findViewById(R.id.phone);
        addressLine1 = findViewById(R.id.addressLine1);
        addressLine2 = findViewById(R.id.addressLine2);
        userRoadNo = findViewById(R.id.roadNo);
        ckBox1 = findViewById(R.id.checkBox1);
        ckBox2 = findViewById(R.id.checkBox2);
    }
}