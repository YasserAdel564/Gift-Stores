package com.gift.app.ui.cart;

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
import com.gift.app.data.models.CartModel;
import com.gift.app.data.models.Store;
import com.gift.app.databinding.CartItemBinding;
import com.gift.app.databinding.StoreItemBinding;

import java.util.List;


public class AdapterCart extends RecyclerView.Adapter<AdapterCart.CartViewHolder> {

    List<CartModel> list;
    private Context mContext;
    CartCallback cartCallback;

    private static CartItemBinding binding;


    public AdapterCart(List<CartModel> list, Context mContext
            , CartCallback cartCallback) {
        this.list = list;
        this.mContext = mContext;
        this.cartCallback = cartCallback;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CartViewHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.cart_item, parent, false));

    }

    @SuppressLint({"UseCompatLoadingForDrawables", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {


        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.app_logo)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.HIGH);

        Glide.with(mContext).load(list.get(position).getPhoto())
                .apply(options)
                .into(binding.productImage);

        binding.productName.setText(list.get(position).getName());
        binding.productPrice.setText(list.get(position).getPrice());
        binding.productQuantity.setText(list.get(position).getQty().toString());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class CartViewHolder extends RecyclerView.ViewHolder {


        CartViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = CartItemBinding.bind(itemView);

            binding.addCartImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cartCallback.addCartClicked(list.get(getAdapterPosition()));
                }
            });
            binding.removeCartImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cartCallback.removeCartClicked(list.get(getAdapterPosition()));
                }
            });
        }


    }

    public interface CartCallback {
        void addCartClicked(CartModel model);
        void removeCartClicked(CartModel model);
    }
}
