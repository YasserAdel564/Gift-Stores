package com.gift.app.ui.Home.products;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
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
import com.gift.app.data.models.Department;
import com.gift.app.data.models.Product;
import com.gift.app.data.models.Store;
import com.gift.app.databinding.ProductsFragmentBinding;
import com.gift.app.ui.Home.SharedViewModel;
import com.gift.app.ui.Home.stores.AdapterStores;
import com.gift.app.utils.Extensions;

public class ProductsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener
        , AdapterProducts.ProductCallback {

    private ProductsViewModel mViewModel;
    private SharedViewModel mSharedViewModel;

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
        mSharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        mSharedViewModel.selectedStore.observe(getViewLifecycleOwner(), new Observer<Store>() {
            @Override
            public void onChanged(Store store) {
                mViewModel.storeId = store.getId();
//                mViewModel.department_id = store.();
                mViewModel.getProducts();
            }
        });

        binding.productSwipe.setOnRefreshListener(this);

        binding.backImgV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigateUp();
            }
        });

        setUiState();
        onCartActionResponse();

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


    private void onCartActionResponse() {
        mViewModel.liveStateCart.observe(getViewLifecycleOwner(), state -> {
            if (state.onLoading) {
                binding.loading.setVisibility(View.VISIBLE);
            }
            if (state.onSuccess) {
                binding.loading.setVisibility(View.GONE);
                if (!mViewModel.response.getMsg().isEmpty())
                    Extensions.Success(binding.productRoot, mViewModel.response.getMsg());
                mViewModel.response.setMsg("");

            }
            if (state.onError) {
                binding.loading.setVisibility(View.GONE);
                Extensions.Success(binding.productRoot, mViewModel.response.getMsg());
            }
            if (state.onNoConnection) {
                binding.loading.setVisibility(View.GONE);
                Extensions.noInternetSnakeBar(binding.productRoot);

            }

        });
    }



    @Override
    public void onRefresh() {
        mViewModel.getProducts();
        binding.productSwipe.setRefreshing(false);

    }

    @Override
    public void ProductClicked(Product model) {

    }

    @Override
    public void addCartClicked(Product model) {
        mViewModel.productId = model.getId().toString();
        mViewModel.addCart();
    }

    @Override
    public void removeCartClicked(Product model) {
        mViewModel.productId = model.getId().toString();
        mViewModel.delCart();
    }
}