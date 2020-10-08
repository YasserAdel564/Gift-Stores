package com.gift.app.ui.Home.products;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gift.app.App;
import com.gift.app.R;
import com.gift.app.data.models.Product;
import com.gift.app.data.models.Store;
import com.gift.app.databinding.ProductsFragmentBinding;
import com.gift.app.ui.Home.SharedViewModel;
import com.gift.app.utils.Extensions;

public class ProductsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener
        , AdapterProducts.ProductCallback {

    private ProductsViewModel mViewModel;
    private SharedViewModel mSharedViewModel;

    private ProductsFragmentBinding binding;

    private AdapterProducts adapter;
    private Integer REQUEST_PHONE_CALL = 1;


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

        binding.productSwipe.setOnRefreshListener(this);
        binding.backImgV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigateUp();
            }
        });
        getProducts();
        setUiState();
        onCartActionResponse();
        clicksListeners();

    }

    private void getProducts() {
        mSharedViewModel.selectedStore.observe(getViewLifecycleOwner(), new Observer<Store>() {
            @Override
            public void onChanged(Store store) {
                mViewModel.storeId = store.getId();
                mViewModel.getProducts();
            }
        });

    }

    private void clicksListeners() {
        binding.phoneImgV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PHONE_CALL);
                } else {
                    Extensions.makeCall(requireActivity(), "+201152444842");

                }
            }
        });
        binding.chatImgV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (App.getPreferencesHelper().getIsLogin())
                    goToChat();
                else
                    Extensions.generalMessage(binding.productRoot, requireActivity().getString(R.string.should_Login));
            }
        });
        binding.cartImgV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (App.getPreferencesHelper().getIsLogin())
                    goToCart();
                else
                    Extensions.generalMessage(binding.productRoot, requireActivity().getString(R.string.should_Login));
            }
        });
    }

    private void goToCart() {
        NavHostFragment.findNavController(this).navigate(R.id.CartFragment);
    }

    private void goToChat() {
        NavHostFragment.findNavController(this).navigate(R.id.ChattingFragment);
    }


    private void setUiState() {
        mViewModel.liveState.observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String state) {
                switch (state) {
                    case "onLoading":
                        binding.loading.setVisibility(View.VISIBLE);
                        break;

                    case "onSuccess":
                        binding.loading.setVisibility(View.GONE);
                        onSuccess();
                        break;

                    case "onError":
                        binding.loading.setVisibility(View.GONE);
                        Extensions.generalErrorSnakeBar(binding.productRoot);
                        break;

                    case "onNoConnection":
                        binding.loading.setVisibility(View.GONE);
                        Extensions.noInternetSnakeBar(binding.productRoot);
                        break;
                    default:
                }
            }
        });

    }

    private void onSuccess() {
        adapter = new AdapterProducts(mViewModel.listStores, requireActivity(), this);
        binding.productRv.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }


    private void onCartActionResponse() {

        mViewModel.liveStateCart.observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String state) {
                switch (state) {
                    case "onLoading":
                        binding.loading.setVisibility(View.VISIBLE);
                        break;

                    case "onSuccess":
                        binding.loading.setVisibility(View.GONE);
                        if (!mViewModel.response.getMsg().isEmpty())
                            Extensions.generalMessage(binding.productRoot, mViewModel.response.getMsg());
                        mViewModel.response.setMsg("");
                        break;

                    case "onError":
                        binding.loading.setVisibility(View.GONE);
                        Extensions.generalMessage(binding.productRoot, mViewModel.response.getMsg());
                        break;

                    case "onNoConnection":
                        binding.loading.setVisibility(View.GONE);
                        Extensions.noInternetSnakeBar(binding.productRoot);
                        break;
                    default:
                }
            }
        });

    }


    @Override
    public void onRefresh() {
        mViewModel.getProducts();
        binding.productSwipe.setRefreshing(false);

    }

    @Override
    public void addCartClicked(Product model) {

        if (App.getPreferencesHelper().getInServices()) {
            mViewModel.productId = model.getId().toString();
            mViewModel.department_id = model.getDepartment_id().toString();
            mViewModel.addCart();
        } else {
            String message = requireActivity().getString(R.string.out_of_service) + " " + App.getPreferencesHelper().getOpenFrom() +
                    " "+ requireActivity().getString(R.string.to) + " " + App.getPreferencesHelper().getOpenTo();
            Extensions.generalMessage(binding.productRoot, message);
        }
    }

    @Override
    public void removeCartClicked(Product model) {
        if (App.getPreferencesHelper().getInServices()) {
            mViewModel.productId = model.getId().toString();
            mViewModel.delCart();
        } else {
            String message = requireActivity().getString(R.string.out_of_service) + " " + App.getPreferencesHelper().getOpenFrom() +
                   " "+ requireActivity().getString(R.string.to) + " " + App.getPreferencesHelper().getOpenTo();
            Extensions.generalMessage(binding.productRoot, message);
        }
    }

}