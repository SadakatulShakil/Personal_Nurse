package com.astronist.personalnurse.View.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.astronist.personalnurse.Adapter.CartAdapter;
import com.astronist.personalnurse.Adapter.OrderAdapter;
import com.astronist.personalnurse.Model.Address;
import com.astronist.personalnurse.Model.DailyOrder;
import com.astronist.personalnurse.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserOrderHistoryActivity extends AppCompatActivity {

    private RecyclerView orderHistoryRecyclerView;
    private ArrayList<DailyOrder> orderArrayList = new ArrayList<>();
    private OrderAdapter orderAdapter;
    private ProgressBar progressBar;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference orderRef;
    private String userId;
    private LinearLayoutManager manager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_order_history);
        inItView();
        progressBar.setVisibility(View.VISIBLE);
        firebaseAuth = FirebaseAuth.getInstance();
        userId = firebaseAuth.getCurrentUser().getUid();

        getOrderList();

        manager = new LinearLayoutManager(UserOrderHistoryActivity.this, RecyclerView.VERTICAL, false);
        orderHistoryRecyclerView.setLayoutManager(manager);
        orderAdapter = new OrderAdapter(UserOrderHistoryActivity.this, orderArrayList);
        orderHistoryRecyclerView.setAdapter(orderAdapter);
    }

    private void getOrderList() {
        orderRef = FirebaseDatabase.getInstance().getReference("DailyOrder");

        orderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                orderArrayList.clear();
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    DailyOrder dailyOrder = userSnapshot.getValue(DailyOrder.class);
                    if(dailyOrder.getUserId().equals(userId)){

                        orderArrayList.add(dailyOrder);
                    }
                }
                progressBar.setVisibility(View.GONE);
                orderAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void inItView() {
        orderHistoryRecyclerView = findViewById(R.id.orderHistoryRecycleView);
        progressBar = findViewById(R.id.progressBar);

    }
}