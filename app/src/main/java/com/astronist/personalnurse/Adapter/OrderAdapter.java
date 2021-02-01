package com.astronist.personalnurse.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.astronist.personalnurse.Model.CartList;
import com.astronist.personalnurse.Model.DailyOrder;
import com.astronist.personalnurse.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.viewHolder> {
    private Context context;
    private ArrayList<DailyOrder> orderArrayList;

    public OrderAdapter(Context context, ArrayList<DailyOrder> orderArrayList) {
        this.context = context;
        this.orderArrayList = orderArrayList;
    }

    @NonNull
    @Override
    public OrderAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_list_view, parent, false);
        return new OrderAdapter.viewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderAdapter.viewHolder holder, int position) {
        DailyOrder dailyOrder = orderArrayList.get(position);
        String pTitle = dailyOrder.getProductTitle();
        String uTime = dailyOrder.getUpTime();
        String uDate = dailyOrder.getUpdate();
        String dateNTime = uDate+" "+uTime;
        String pQuantity = dailyOrder.getProductQuantity();
        String pTotalPrice = String.valueOf(dailyOrder.getTotalPrice());

        holder.productTitle.setText(pTitle);
        holder.timeAndDate.setText(dateNTime);
        holder.quantity.setText("Quantity :"+pQuantity);
        holder.totalPrice.setText("Total price : ৳ "+pTotalPrice);
        holder.removeBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ////do removing cart item
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderArrayList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        private TextView timeAndDate, productTitle, quantity, totalPrice, removeBt;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            timeAndDate = itemView.findViewById(R.id.orderDateTime);
            productTitle = itemView.findViewById(R.id.productTitle);
            quantity = itemView.findViewById(R.id.productQuantity);
            totalPrice = itemView.findViewById(R.id.productPrice);
            removeBt = itemView.findViewById(R.id.removeCart);
        }
    }
}
