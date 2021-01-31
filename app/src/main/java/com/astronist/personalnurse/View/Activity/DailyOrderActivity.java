package com.astronist.personalnurse.View.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.astronist.personalnurse.Model.Address;
import com.astronist.personalnurse.Model.CartList;
import com.astronist.personalnurse.Model.DailyOrder;
import com.astronist.personalnurse.Model.ProductInfo;
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
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DailyOrderActivity extends AppCompatActivity {

    private TextView productTitle, sellingPrice, regularPrice, stock, description;
    private EditText userName, userPhone, addressLine1, addressLine2, userRoadNo;
    private TextView saveAddress, countPlus, countMinus, countAmount, totalPrice;
    private ImageView productImage;
    private ExtendedFloatingActionButton cartBtn, orderBtn;
    private ProductInfo productInfo;
    private String userId, upTime, upDate;
    private DatabaseReference addressRef, cartReference, orderReference;
    private FirebaseAuth firebaseAuth;
    private int counter = 1, checkCart = 0;
    private double totalAmount;
    public static final String TAG ="DailyOrder";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_order);
        inItView();
        firebaseAuth = FirebaseAuth.getInstance();
        userId = firebaseAuth.getCurrentUser().getUid();
        Intent intent = getIntent();
        productInfo = (ProductInfo) intent.getSerializableExtra("productInfo");

        String sellPrice = String.valueOf(productInfo.getSellingPrice());
        totalAmount = productInfo.getSellingPrice();
        String regPrice = String.valueOf(productInfo.getRegularPrice());
        String uStock = String.valueOf(productInfo.getStockAvailable());
        Picasso.get().load(productInfo.getImageUrl()).into(productImage);
        productTitle.setText(productInfo.getTitle());
        sellingPrice.setText("Price: ৳ "+sellPrice);
        regularPrice.setText("৳ "+regPrice);
        totalPrice.setText("৳ "+sellPrice);
        if(productInfo.getStockAvailable()>0){

            stock.setText("Stock Available");
        }else{
            stock.setText("Stock Unavailable");
        }
        description.setText(productInfo.getDescription());

        getUserAddress();

        saveAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpUserAddress();
                getUserAddress();
            }
        });

        countPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter++;
                countAmount.setText(Integer.toString(counter));
                amountCalculation();
            }
        });

        countMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (counter > 1) {
                    counter--;
                    countAmount.setText(Integer.toString(counter));
                    amountCalculation();
                }
            }
        });

        cartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
/////////Do work for multiple order in cart/////////////////
                checkCart++;
                if (checkCart == 1) {
                    goToCartProductSetUp();
                } else {
                    Toast.makeText(DailyOrderActivity.this, "This Product is already in Cart!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        orderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToOrderSetup();
            }
        });
    }

    private void goToOrderSetup() {
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

        storeOrderDetails(userId, uName, uPhone, uAddress1, uAddress2, uRoadNo, productTitle.getText().toString().trim(),
                countAmount.getText().toString().trim(), totalAmount, productInfo.getCategory());
    }

    private void storeOrderDetails(final String userId, final String uName, final String uPhone, final String uAddress1,
                                   final String uAddress2, final String uRoadNo, final String productTitle, final String quantity,
                                   final double totalAmount, final String category) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat myTimeFormat = new SimpleDateFormat("hh:mm a", Locale.US);
        upTime = myTimeFormat.format(calendar.getTime());
        SimpleDateFormat myDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        upDate = myDateFormat.format(calendar.getTime());

        orderReference = FirebaseDatabase.getInstance().getReference().child("DailyOrder");
        String pushId = orderReference.push().getKey();

        DailyOrder dailyOrder = new DailyOrder(userId, pushId, uName, uPhone, uAddress1, uAddress2, uRoadNo,
                productTitle, quantity, totalAmount, category, upTime, upDate);
        orderReference.child(pushId).setValue(dailyOrder).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(DailyOrderActivity.this, "Your Order Submit Successfully !", Toast.LENGTH_SHORT).show();
                /*Intent intent = new Intent(DailyOrderActivity.this, OrderSuccessfulActivity.class);
                startActivity(intent);
                finish();*/
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: " +e.getMessage());
            }
        });
    }

    private void goToCartProductSetUp() {
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

        storeCartDetails(userId, uName, uPhone, uAddress1, uAddress2, uRoadNo, productTitle.getText().toString().trim(),
                countAmount.getText().toString().trim(), totalAmount, productInfo.getCategory());

    }

    private void storeCartDetails(final String userId, final String userName, final String userPhone, final String addressLine1,
                                  final String addressLine2, final String userRoadNo, final String productTitle,
                                  final String quantity, final double totalAmount, final String category) {

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat myTimeFormat = new SimpleDateFormat("hh:mm a", Locale.US);
        upTime = myTimeFormat.format(calendar.getTime());
        SimpleDateFormat myDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        upDate = myDateFormat.format(calendar.getTime());

        cartReference = FirebaseDatabase.getInstance().getReference().child("CartList");
        String pushId =cartReference.push().getKey();

        CartList cartList = new CartList(userId, pushId, userName, userPhone, addressLine1,
                addressLine2, userRoadNo, productTitle, quantity, totalAmount, category, upTime, upDate);

        cartReference.child(userId).child(pushId).setValue(cartList).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {
                    Toast.makeText(DailyOrderActivity.this, "Your Product is added to Cart!", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: " + e.getLocalizedMessage());
            }
        });

    }

    private void amountCalculation() {
        double baseAmount = productInfo.getSellingPrice();
        totalAmount = baseAmount*counter;
        String setTotalAmount = String.valueOf(totalAmount);
        totalPrice.setText("৳ "+setTotalAmount);
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
                    Toast.makeText(DailyOrderActivity.this, "Your Address Saved Successfully !", Toast.LENGTH_SHORT).show();
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

    private void inItView() {
        productTitle = findViewById(R.id.productTitle);
        sellingPrice = findViewById(R.id.productSellingPrice);
        regularPrice = findViewById(R.id.regularPrice);
        stock = findViewById(R.id.stock);
        description = findViewById(R.id.description);
        userName = findViewById(R.id.name);
        userPhone = findViewById(R.id.phone);
        addressLine1 = findViewById(R.id.addressLine1);
        addressLine2 = findViewById(R.id.addressLine2);
        userRoadNo = findViewById(R.id.roadNo);
        saveAddress = findViewById(R.id.saveAddress);
        productImage = findViewById(R.id.productImage);
        cartBtn = findViewById(R.id.cart_fab);
        orderBtn = findViewById(R.id.order_fab);

        countPlus = findViewById(R.id.plus);
        countMinus = findViewById(R.id.minus);
        countAmount = findViewById(R.id.count);
        totalPrice = findViewById(R.id.totalPrice);
    }
}