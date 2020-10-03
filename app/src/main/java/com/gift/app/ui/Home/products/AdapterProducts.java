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


    AdapterProducts(List<Product> list, Context mContext
            , ProductCallback productCallback) {
        this.list = list;
        this.mContext = mContext;
        this.productCallback = productCallback;
    }

    @NonNull
    @Override
    public DepartmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DepartmentViewHolder(ProductItemBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull DepartmentViewHolder holder, int position) {

        holder.productItemBinding.productName.setText(list.get(position).getName());
        holder.productItemBinding.productPrice.setText(list.get(position).getPrice());

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.app_logo)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.HIGH);
        Glide.with(mContext).load(list.get(position).getPhoto())
                .apply(options)
                .into(holder.productItemBinding.productImage);


    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class DepartmentViewHolder extends RecyclerView.ViewHolder {

        private ProductItemBinding productItemBinding;

        DepartmentViewHolder(@NonNull ProductItemBinding binding) {
            super(binding.getRoot());
            productItemBinding = binding ;


            binding.addCartImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    productCallback.addCartClicked(list.get(getAdapterPosition()));
                }
            });

            binding.removeCartImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    productCallback.removeCartClicked(list.get(getAdapterPosition()));
                }
            });
        }


    }

    public interface ProductCallback {
        void addCartClicked(Product model);
        void removeCartClicked(Product model);

    }
}
