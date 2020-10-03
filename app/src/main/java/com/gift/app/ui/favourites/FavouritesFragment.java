package com.gift.app.ui.favourites;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

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

import com.gift.app.R;
import com.gift.app.data.models.Store;
import com.gift.app.databinding.FavouritesFragmentBinding;
import com.gift.app.ui.Home.SharedViewModel;
import com.gift.app.ui.Home.stores.AdapterStores;
import com.gift.app.utils.Extensions;

public class FavouritesFragment extends Fragment implements AdapterStores.StoreCallback,
        SwipeRefreshLayout.OnRefreshListener {

    private FavouritesViewModel mViewModel;
    private FavouritesViewModel mFavViewModel;
    private SharedViewModel mSharedViewModel;

    private AdapterStores adapter;
    private FavouritesFragmentBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.favourites_fragment, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(requireActivity()).get(FavouritesViewModel.class);
        mSharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        mFavViewModel = new ViewModelProvider(requireActivity()).get(FavouritesViewModel.class);
        binding.favSwipe.setOnRefreshListener(this);

        mFavViewModel.getFavourites();

        setUiState();
        onFavResponse();
        binding.backImgV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back();
            }
        });
    }

    private void back() {
        NavHostFragment.findNavController(this).navigateUp();
    }


    private void setUiState() {

        mViewModel.liveStateFav.observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String state) {
                switch (state) {
                    case "onLoading":
                        binding.loading.setVisibility(View.VISIBLE);
                        binding.favRv.setVisibility(View.GONE);
                        binding.emptyViewLayout.setVisibility(View.GONE);
                        break;

                    case "onSuccess":
                        binding.loading.setVisibility(View.GONE);
                        binding.favRv.setVisibility(View.VISIBLE);
                        binding.emptyViewLayout.setVisibility(View.GONE);
                        onSuccess();
                        break;

                    case "onEmpty":
                        binding.favRv.setVisibility(View.GONE);
                        binding.loading.setVisibility(View.GONE);
                        binding.emptyViewLayout.setVisibility(View.VISIBLE);
                        break;

                    case "onError":
                        binding.loading.setVisibility(View.GONE);
                        binding.favRv.setVisibility(View.GONE);
                        Extensions.generalErrorSnakeBar(binding.storesRoot);
                        break;

                    case "onNoConnection":
                        binding.loading.setVisibility(View.GONE);
                        binding.favRv.setVisibility(View.GONE);
                        Extensions.noInternetSnakeBar(binding.storesRoot);
                        break;

                    default:
                }
            }
        });

    }

    private void onFavResponse() {
        mViewModel.liveState.observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String state) {
                switch (state) {
                    case "onLoading":
                        binding.loading.setVisibility(View.VISIBLE);
                        break;

                    case "onSuccess":
                        binding.loading.setVisibility(View.GONE);
                        if (!mFavViewModel.response.getMsg().isEmpty())
                            Extensions.Success(binding.storesRoot, mFavViewModel.response.getMsg());
                        mViewModel.getFavourites();
                        mFavViewModel.response.setMsg("");
                        break;
                    case "onError":
                        binding.loading.setVisibility(View.GONE);
                        Extensions.Success(binding.storesRoot, mFavViewModel.response.getMsg());
                        break;

                    case "onNoConnection":
                        binding.loading.setVisibility(View.GONE);
                        Extensions.noInternetSnakeBar(binding.storesRoot);
                        break;

                    default:
                }
            }
        });
    }


    private void onSuccess() {

        adapter = new AdapterStores(mViewModel.favList, requireActivity(), this);
        binding.favRv.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }


    @Override
    public void onRefresh() {
        binding.favSwipe.setRefreshing(false);
        mViewModel.getFavourites();
    }

    @Override
    public void storeCardClicked(Store model) {
        mSharedViewModel.setStore(model);
        NavHostFragment.findNavController(this).navigate(R.id.action_from_favFragment_to_productsFragment);
    }

    @Override
    public void favIconClicked(Store model) {
        mFavViewModel.storeId = model.getId();
        if (model.getLiked())
            mFavViewModel.delFavourites();
        else
            mFavViewModel.addFavourites();
    }
}