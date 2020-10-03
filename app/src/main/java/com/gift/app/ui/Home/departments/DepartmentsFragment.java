package com.gift.app.ui.Home.departments;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.gift.app.App;
import com.gift.app.R;
import com.gift.app.data.models.Department;
import com.gift.app.databinding.DepartmentsFragmentBinding;
import com.gift.app.ui.Home.SharedViewModel;
import com.gift.app.ui.MainActivity;
import com.gift.app.utils.Constants;
import com.gift.app.utils.Extensions;
import com.google.android.material.navigation.NavigationView;

public class DepartmentsFragment extends Fragment implements
        NavigationView.OnNavigationItemSelectedListener, SwipeRefreshLayout.OnRefreshListener
        , AdapterDepartments.DepartmentCallback {

    private DepartmentsViewModel mViewModel;
    private SharedViewModel mSharedViewModel;

    private AdapterDepartments adapter;
    DepartmentsFragmentBinding binding;

    private Integer REQUEST_PHONE_CALL = 1;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.departments_fragment, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(DepartmentsViewModel.class);
        mSharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        if (!mViewModel.isOpen) {
            mViewModel.isOpen = true;
            mViewModel.getDepartments();
        }

        clickListeners();
        initDrawer();
        dataBinding();
        setUiState();

    }

    private void dataBinding() {
        binding.depSwipe.setOnRefreshListener(this);
        binding.navigationDrawer.setNavigationItemSelectedListener(this);
        binding.drawerLayout.setScrimColor(Color.TRANSPARENT);
        binding.drawerLayout.setDrawerElevation(0f);
    }

    private void setUiState() {
        mViewModel.liveState.observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String state) {
                switch (state) {
                    case "onLoading":
                        binding.loading.setVisibility(View.VISIBLE);
                        binding.depRv.setVisibility(View.GONE);
                        break;

                    case "onSuccess":
                        binding.loading.setVisibility(View.GONE);
                        binding.depRv.setVisibility(View.VISIBLE);
                        onSuccess();
                        break;

                    case "onEmpty":
                        binding.loading.setVisibility(View.GONE);
                        binding.emptyViewLayout.setVisibility(View.VISIBLE);
                        binding.depRv.setVisibility(View.GONE);
                        break;

                    case "onError":
                        binding.loading.setVisibility(View.GONE);
                        Extensions.generalErrorSnakeBar(binding.drawerLayout);
                        binding.depRv.setVisibility(View.GONE);
                        break;

                    case "onNoConnection":
                        binding.loading.setVisibility(View.GONE);
                        binding.depRv.setVisibility(View.GONE);
                        Extensions.noInternetSnakeBar(binding.drawerLayout);
                        break;

                    default:
                }
            }
        });

    }

    private void onSuccess() {
        adapter = new AdapterDepartments(mViewModel.listDepartments, requireActivity(), this);
        binding.depRv.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }


    private void clickListeners() {
        binding.menuImgV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        binding.navigationDrawer.getMenu().findItem(R.id.nav_english)
                .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        App.getPreferencesHelper().setLanguage(Constants.ENGLISH);
                        refreshApp();
                        return false;
                    }
                });
        binding.navigationDrawer.getMenu().findItem(R.id.nav_arabic)
                .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {

                        App.getPreferencesHelper().setLanguage(Constants.ARABIC);
                        refreshApp();
                        return false;
                    }
                });

        binding.navigationDrawer.getMenu().findItem(R.id.nav_share)
                .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        String appPackageName = requireContext().getPackageName();
                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW,
                                    Uri.parse("market://details?id=" + appPackageName)));
                        } catch (Exception e) {
                            startActivity(new Intent(Intent.ACTION_VIEW,
                                    Uri.parse("https://play.google.com/store/apps/details?id=" +
                                            appPackageName)));
                        }
                        return false;
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
        binding.chatImgV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (App.getPreferencesHelper().getIsLogin())
                    goToChat();
                else
                    Extensions.Success(binding.drawerLayout, requireActivity().getString(R.string.should_Login));
            }
        });
        binding.cartImgV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (App.getPreferencesHelper().getIsLogin())
                    goToCart();
                else
                    Extensions.Success(binding.drawerLayout, requireActivity().getString(R.string.should_Login));
            }
        });
    }
    private void goToCart() {
        NavHostFragment.findNavController(this).navigate(R.id.action_from_DepartmentsFragment_to_CartFragment);
    }
    private void goToChat() {
        NavHostFragment.findNavController(this).navigate(R.id.action_from_DepartmentsFragment_to_ChattingFragment);
    }




    private void initDrawer() {
        if (App.getPreferencesHelper().getLanguage().equals(Constants.ARABIC))
            binding.navigationDrawer.getMenu().findItem(R.id.nav_arabic)
                    .setActionView(R.layout.navigation_drawer_language_checked);

        if (App.getPreferencesHelper().getLanguage().equals(Constants.ENGLISH))
            binding.navigationDrawer.getMenu().findItem(R.id.nav_english)
                    .setActionView(R.layout.navigation_drawer_language_checked);
        if (App.getPreferencesHelper().getIsLogin()) {
            binding.navigationDrawer.getMenu().findItem(R.id.LoginFragment).setTitle(getString(R.string.nav_drawer_logout));
            TextView navUsername = binding.navigationDrawer.getHeaderView(0).findViewById(R.id.userName);
            navUsername.setText(App.getPreferencesHelper().getUserName());
        } else {
            binding.navigationDrawer.getMenu().findItem(R.id.LoginFragment).setTitle(getString(R.string.nav_drawer_login));

        }

    }

    private void refreshApp() {
        startActivity(new Intent(getActivity(), MainActivity.class));
        requireActivity().finish();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        binding.drawerLayout.close();
        NavController navController = Navigation.findNavController(requireActivity(), R.id.host_fragment);
        return NavigationUI.onNavDestinationSelected(menuItem, navController)
                || super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void onRefresh() {
        binding.depSwipe.setRefreshing(false);
        mViewModel.getDepartments();
    }

    @Override
    public void DepartmentClicked(Department model) {
        mSharedViewModel.setDep(model);
        NavHostFragment.findNavController(this).navigate(R.id.action_from_DepartmentsFragment_to_StoresFragment);
    }


}