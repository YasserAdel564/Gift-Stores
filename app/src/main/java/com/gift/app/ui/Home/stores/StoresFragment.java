package com.gift.app.ui.Home.stores;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.MutableLiveData;
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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gift.app.R;
import com.gift.app.data.models.Department;
import com.gift.app.data.models.Store;
import com.gift.app.databinding.StoresFragmentBinding;
import com.gift.app.ui.Home.SharedViewModel;
import com.gift.app.ui.Home.departments.AdapterDepartments;
import com.gift.app.ui.favourites.FavouritesViewModel;
import com.gift.app.utils.Extensions;
import com.gift.app.utils.UiStates;

public class StoresFragment extends Fragment implements AdapterStores.StoreCallback,
        SwipeRefreshLayout.OnRefreshListener ,Observer<UiStates> {

    private StoresViewModel mViewModel;
    private FavouritesViewModel mFavViewModel;
    private SharedViewModel mSharedViewModel;
    private AdapterStores adapter;
    private Integer REQUEST_PHONE_CALL = 1;
    private StoresFragmentBinding binding;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.stores_fragment, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(requireActivity()).get(StoresViewModel.class);
        mFavViewModel = new ViewModelProvider(requireActivity()).get(FavouritesViewModel.class);
        mSharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        mSharedViewModel.selectedDep.observe(getViewLifecycleOwner(), new Observer<Department>() {
            @Override
            public void onChanged(Department department) {
                mViewModel.departmentId = department.getId();
                mViewModel.getStores();
            }
        });


        setUiState();

        clickListeners();
        onFavResponse();
        dataBinding();
    }

    private void dataBinding() {
        binding.storeSwipe.setOnRefreshListener(this);
    }

    private void clickListeners() {
        binding.backImgV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigateUp();
            }
        });
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

    }

    private void setUiState() {


        mViewModel.liveState.observe(getViewLifecycleOwner(), new Observer<UiStates>() {
            @Override
            public void onChanged(UiStates state) {
                if (state.onLoading) {
                    binding.loading.setVisibility(View.VISIBLE);
                    binding.storeRv.setVisibility(View.GONE);
                }
                if (state.onSuccess) {
                    binding.loading.setVisibility(View.GONE);
                    binding.storeRv.setVisibility(View.VISIBLE);
                    onSuccess();

                }
                if (state.onError) {
                    binding.loading.setVisibility(View.GONE);
                    binding.storeRv.setVisibility(View.GONE);
                    Extensions.generalErrorSnakeBar(binding.storesRoot);
                }
                if (state.onEmpty) {
                    binding.storeRv.setVisibility(View.GONE);
                    binding.loading.setVisibility(View.GONE);
                    binding.emptyViewLayout.setVisibility(View.VISIBLE);
                }
                if (state.onNoConnection) {
                    binding.loading.setVisibility(View.GONE);
                    binding.storeRv.setVisibility(View.GONE);
                    Extensions.noInternetSnakeBar(binding.storesRoot);

                }



            }
        });


        mViewModel.liveState.observe(getViewLifecycleOwner(), state -> {


        });
    }


    private void onSuccess() {
        adapter = new AdapterStores(mViewModel.listStores, requireActivity(), this);
        binding.storeRv.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }


    @Override
    public void onRefresh() {
        binding.storeSwipe.setRefreshing(false);
        mViewModel.getStores();
    }

    @Override
    public void storeCardClicked(Store model) {
        mSharedViewModel.setStore(model);
        NavHostFragment.findNavController(this).navigate(R.id.action_from_StoresFragment_to_ProductsFragment);
    }

    private void onFavResponse() {
        mFavViewModel.liveState.observe(getViewLifecycleOwner(), state -> {
            if (state.onLoading) {
                binding.loading.setVisibility(View.VISIBLE);
            }
            if (state.onSuccess) {
                binding.loading.setVisibility(View.GONE);
                if (!mFavViewModel.response.getMsg().isEmpty())
                    Extensions.Success(binding.storesRoot, mFavViewModel.response.getMsg());
                mViewModel.getStores();
                mFavViewModel.response.setMsg("");

            }
            if (state.onError) {
                binding.loading.setVisibility(View.GONE);
                Extensions.Success(binding.storesRoot, mFavViewModel.response.getMsg());
            }
            if (state.onNoConnection) {
                binding.loading.setVisibility(View.GONE);
                Extensions.noInternetSnakeBar(binding.storesRoot);

            }

        });
    }


    @Override
    public void favIconClicked(Store model) {
        mFavViewModel.storeId = model.getId();
        if (model.getLiked())
            mFavViewModel.delFavourites();
        else
            mFavViewModel.addFavourites();


    }

    @Override
    public void onChanged(UiStates uiStates) {
        Log.e("xxxxxx","xxxxxxxxx");
    }
}