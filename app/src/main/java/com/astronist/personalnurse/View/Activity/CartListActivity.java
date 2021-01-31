package com.astronist.personalnurse.View.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.astronist.personalnurse.Adapter.CartAdapter;
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
    private DatabaseReference cartReference;
    private LinearLayoutManager manager;
    public static final String TAG = "Cart";
    private ProgressBar progressBar;
    private TextView grandAmount, grandUnit;
    private int gTotal = 0;
    private String currency;
    private CartList cartList;
    private DailyOrder dailyOrder;
    private ExtendedFloatingActionButton placeOrderBtn;
    private String monthName, userId;
    private DatabaseReference cartToOrderReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_list);
        inItView();

        progressBar.setVisibility(View.VISIBLE);
        firebaseAuth = FirebaseAuth.getInstance();
        userId = firebaseAuth.getCurrentUser().getUid();
        Log.d(TAG, "onCreate: " + userId);
        getCartItem();

        manager = new LinearLayoutManager(CartListActivity.this, RecyclerView.VERTICAL, false);
        cartListRecView.setLayoutManager(manager);
        cartAdapter = new CartAdapter(CartListActivity.this, cartListArrayList);
        cartListRecView.setAdapter(cartAdapter);

        placeOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setUpCartOrder();
            }
        });
    }

    private void setUpCartOrder() {
        for(int j = 0; j<cartListArrayList.size(); j++)
        {
            dailyOrder = new DailyOrder(cartListArrayList.get(j).getUserId(), cartListArrayList.get(j).getPushId(), cartListArrayList.get(j).getUserName(),
                    cartListArrayList.get(j).getUserPhone(), cartListArrayList.get(j).getUserAddress1(), cartListArrayList.get(j).getUserAddress2(), cartListArrayList.get(j).getUserRoadNo(),
                    cartListArrayList.get(j).getProductTitle(),cartListArrayList.get(j).getProductQuantity(), cartListArrayList.get(j).getTotalPrice(), cartListArrayList.get(j).getProductCategory(),
                    cartListArrayList.get(j).getUpTime(), cartListArrayList.get(j).getUpdate());

            cartToOrderReference = FirebaseDatabase.getInstance().getReference().child("Order");

            cartToOrderReference.child(userId).child(cartListArrayList.get(j).getPushId()).setValue(dailyOrder).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(CartListActivity.this, "Your Order Placed Successfully !", Toast.LENGTH_SHORT).show();
                   /* Intent intent = new Intent(CartListActivity.this, OrderSuccessfulActivity.class);
                    startActivity(intent);
                    finish();*/
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
    }
}