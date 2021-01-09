package com.astronist.personalnurse.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.astronist.personalnurse.Model.ProductInfo;
import com.astronist.personalnurse.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.viewHolder> {
    private ArrayList<ProductInfo> productInfoArrayList;
    private Context context;

    public ProductAdapter(ArrayList<ProductInfo> productInfoArrayList, Context context) {
        this.productInfoArrayList = productInfoArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ProductAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_view, parent, false);
        return new viewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.viewHolder holder, int position) {
        ProductInfo productInfo = productInfoArrayList.get(position);
        String pTitle = productInfo.getTitle();
        int pStock = productInfo.getStockAvailable();
        String proStck = String.valueOf(pStock);
        double pActualPrice = productInfo.getActualSellingPrice();
        double pRegularPrice = productInfo.getRegularPrice();
        String proRegularPrice = String.valueOf(pRegularPrice);
        String proActualPrice = String.valueOf(pActualPrice);

        holder.title.setText(pTitle);
        holder.regularPrice.setText(proRegularPrice);
        holder.sellingPrice.setText(proActualPrice);
        Picasso.get().load(productInfo.getImageUrl()).into(holder.proImage);

    }

    @Override
    public int getItemCount() {
        return productInfoArrayList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        private ImageView proImage;
        private TextView title, regularPrice, sellingPrice;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            proImage = itemView.findViewById(R.id.productImage);
            title = itemView.findViewById(R.id.productTitle);
            regularPrice = itemView.findViewById(R.id.regularPrice);
            sellingPrice = itemView.findViewById(R.id.sellingPrice);
        }
    }
}
