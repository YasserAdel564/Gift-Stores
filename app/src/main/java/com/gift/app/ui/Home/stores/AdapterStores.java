package com.gift.app.ui.Home.stores;

import android.annotation.SuppressLint;
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
import com.gift.app.data.models.Department;
import com.gift.app.data.models.Store;
import com.gift.app.databinding.DepartmentItemBinding;
import com.gift.app.databinding.ProductItemBinding;
import com.gift.app.databinding.StoreItemBinding;
import com.gift.app.ui.Home.products.AdapterProducts;

import java.util.List;


public class AdapterStores extends RecyclerView.Adapter<AdapterStores.DepartmentViewHolder> {

    List<Store> list;
    private Context mContext;
    StoreCallback storeCallback;


    public AdapterStores(List<Store> list, Context mContext
            , StoreCallback storeCallback) {
        this.list = list;
        this.mContext = mContext;
        this.storeCallback = storeCallback;
    }

    @NonNull
    @Override
    public DepartmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AdapterStores.DepartmentViewHolder(StoreItemBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false));

    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(@NonNull DepartmentViewHolder holder, int position) {

        holder.binding.storeName.setText(list.get(position).getName());

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.app_logo)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.HIGH);

        Glide.with(mContext).load(list.get(position).getPhoto())
                .apply(options)
                .into(holder.binding.storeLogo);

        Glide.with(mContext).load(list.get(position).getCover())
                .apply(options)
                .into(holder.binding.storeCover);

        if (list.get(position).getLiked())
            holder.binding.addStoreFav.setBackground(mContext.getDrawable(R.drawable.ic_baseline_favorite_24));
        else
           holder.binding.addStoreFav.setBackground(mContext.getDrawable(R.drawable.ic_baseline_favorite_white));


    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class DepartmentViewHolder extends RecyclerView.ViewHolder {

        private StoreItemBinding binding;

        DepartmentViewHolder(StoreItemBinding storeItemBinding) {
            super(storeItemBinding.getRoot());
            binding = storeItemBinding;

            binding.storeItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    storeCallback.storeCardClicked(list.get(getAdapterPosition()));
                }
            });
            binding.addStoreFav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    storeCallback.favIconClicked(list.get(getAdapterPosition()));
                }
            });
        }


    }

    public interface StoreCallback {
        void storeCardClicked(Store model);

        void favIconClicked(Store model);

    }
}
