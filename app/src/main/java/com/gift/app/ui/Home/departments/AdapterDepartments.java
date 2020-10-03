package com.gift.app.ui.Home.departments;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.gift.app.R;
import com.gift.app.data.models.Department;
import com.gift.app.databinding.DepartmentItemBinding;
import com.gift.app.databinding.ProductItemBinding;
import com.gift.app.ui.Home.products.AdapterProducts;

import java.util.List;


public class AdapterDepartments extends RecyclerView.Adapter<AdapterDepartments.DepartmentViewHolder> {

    List<Department> list;
    private Context mContext;
    DepartmentCallback depCallback;


    AdapterDepartments(List<Department> list, Context mContext
            , DepartmentCallback depCallback) {
        this.list = list;
        this.mContext = mContext;
        this.depCallback = depCallback;
    }

    @NonNull
    @Override
    public DepartmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AdapterDepartments.DepartmentViewHolder
                (DepartmentItemBinding.inflate(LayoutInflater.from(parent.getContext()),
                        parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DepartmentViewHolder holder, int position) {

        holder.binding.depName.setText(list.get(position).getName());

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.app_logo)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.HIGH);

        Glide.with(mContext).load(list.get(position).getPhoto())
                .apply(options)
                .into(holder.binding.depImage);


    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class DepartmentViewHolder extends RecyclerView.ViewHolder {
        private DepartmentItemBinding binding;

        DepartmentViewHolder(DepartmentItemBinding departmentItemBinding) {
            super(departmentItemBinding.getRoot());
            binding = departmentItemBinding;

            binding.depItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    depCallback.DepartmentClicked(list.get(getAdapterPosition()));
                }
            });
        }


    }

    public interface DepartmentCallback {
        void DepartmentClicked(Department model);
    }
}
