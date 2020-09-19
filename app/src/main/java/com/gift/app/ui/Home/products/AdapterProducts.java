package com.gift.app.ui.Home.products;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.gift.app.R;
import com.gift.app.data.models.Product;
import com.gift.app.data.models.Store;
import com.gift.app.databinding.ProductItemBinding;
import com.gift.app.databinding.StoreItemBinding;

import java.util.List;


public class AdapterProducts extends RecyclerView.Adapter<AdapterProducts.DepartmentViewHolder> {

    List<Product> list;
    private Context mContext;
    ProductCallback productCallback;

    private static ProductItemBinding binding;


    AdapterProducts(List<Product> list, Context mContext
            , ProductCallback productCallback) {
        this.list = list;
        this.mContext = mContext;
        this.productCallback = productCallback;
    }

    @NonNull
    @Override
    public DepartmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DepartmentViewHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.product_item, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull DepartmentViewHolder holder, int position) {

        binding.productName.setText(list.get(position).getName());
        binding.productPrice.setText(list.get(position).getPrice());

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.laptop)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.HIGH);
        Glide.with(mContext).load(list.get(position).getPhoto())
                .apply(options)
                .into(binding.productImage);


    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class DepartmentViewHolder extends RecyclerView.ViewHolder {


        DepartmentViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ProductItemBinding.bind(itemView);

            binding.productItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    productCallback.ProductClicked(list.get(getAdapterPosition()));
                }
            });
        }


    }

    public interface ProductCallback {
        void ProductClicked(Product model);
    }
}
