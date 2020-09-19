package com.gift.app.ui.Home.products;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gift.app.R;
import com.gift.app.data.models.Product;
import com.gift.app.databinding.ProductsFragmentBinding;
import com.gift.app.ui.Home.stores.AdapterStores;
import com.gift.app.utils.Extensions;

public class ProductsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener
        , AdapterProducts.ProductCallback {

    private ProductsViewModel mViewModel;
    private ProductsFragmentBinding binding;

    private AdapterProducts adapter;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.products_fragment, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ProductsViewModel.class);
        mViewModel.getProducts();

        binding.productSwipe.setOnRefreshListener(this);

        binding.backImgV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigateUp();
            }
        });

        setUiState();
    }

    private void setUiState() {
        mViewModel.liveState.observe(getViewLifecycleOwner(), state -> {

            if (state.onLoading)
                binding.loading.setVisibility(View.VISIBLE);
            if (state.onSuccess) {
                binding.loading.setVisibility(View.GONE);
                onSuccess();
            }
            if (state.onError) {
                binding.loading.setVisibility(View.GONE);
                Extensions.generalErrorSnakeBar(binding.productRoot);

            }
            if (state.onNoConnection) {
                binding.loading.setVisibility(View.GONE);
                Extensions.noInternetSnakeBar(binding.productRoot);

            }

        });
    }

    private void onSuccess() {

        adapter = new AdapterProducts(mViewModel.listStores, requireActivity(), this);
        binding.productRv.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }


    @Override
    public void onRefresh() {
        mViewModel.getProducts();
        binding.productSwipe.setRefreshing(false);

    }

    @Override
    public void ProductClicked(Product model) {

    }
}