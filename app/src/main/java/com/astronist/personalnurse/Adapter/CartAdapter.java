package com.astronist.personalnurse.Adapter;

import android.content.Context;
import android.text.PrecomputedText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.astronist.personalnurse.Model.CartList;
import com.astronist.personalnurse.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.viewHolder> {
    private Context context;
    private ArrayList<CartList> cartListArrayList;

    public CartAdapter(Context context, ArrayList<CartList> cartListArrayList) {
        this.context = context;
        this.cartListArrayList = cartListArrayList;
    }

    @NonNull
    @Override
    public CartAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_list_view, parent, false);
        return new CartAdapter.viewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.viewHolder holder, int position) {
        CartList cartList = cartListArrayList.get(position);
        String pTitle = cartList.getProductTitle();
        String uTime = cartList.getUpTime();
        String uDate = cartList.getUpdate();
        String dateNTime = uDate+" "+uTime;
        String pQuantity = cartList.getProductQuantity();
        String pTotalPrice = String.valueOf(cartList.getTotalPrice());

        holder.productTitle.setText(pTitle);
        holder.timeAndDate.setText(dateNTime);
        holder.quantity.setText(pQuantity);
        holder.totalPrice.setText(pTotalPrice);
        holder.removeBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ////do removing cart item
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartListArrayList.size();
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
