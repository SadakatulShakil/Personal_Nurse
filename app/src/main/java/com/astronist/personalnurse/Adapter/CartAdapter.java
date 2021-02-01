package com.astronist.personalnurse.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.PrecomputedText;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.astronist.personalnurse.Model.CartList;
import com.astronist.personalnurse.R;
import com.astronist.personalnurse.View.Activity.CartListActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.viewHolder> {
    private Context context;
    private ArrayList<CartList> cartListArrayList;
    public static final String TAG ="Cart";
    private FirebaseAuth firebaseAuth;
    private String userId;
    private DatabaseReference cartRef;

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
        holder.quantity.setText("Quantity :"+pQuantity);
        holder.totalPrice.setText("Total price : ৳ "+pTotalPrice);
        cartRef = FirebaseDatabase.getInstance().getReference().child("CartList");
        holder.removeBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ////do removing cart item
                Log.d(TAG, "onClick: " + "item clicked");

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                alertDialog.setTitle("Remove Cart Item !");
                alertDialog.setMessage("Are You Sure To Remove Your Item ?");
                alertDialog.setIcon(R.drawable.ic_remove_cart);

                alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        firebaseAuth = FirebaseAuth.getInstance();
                        userId = firebaseAuth.getCurrentUser().getUid();

                        cartRef.child(userId).child(cartList.getPushId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Intent intent = new Intent(context, CartListActivity.class);
                                    context.startActivity(intent);
                                    ((Activity) context).finish();
                                    Toast.makeText(context, pTitle + " is Deleted from Cart ! ",
                                            Toast.LENGTH_SHORT).show();
                                    Log.d(TAG, "onComplete: " + cartList.getPushId());
                                }

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "onFailure: " + e.getLocalizedMessage());
                            }
                        });
                    }
                });

                alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                alertDialog.create();
                alertDialog.show();
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
