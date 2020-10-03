package com.gift.app.ui.chat;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.gift.app.App;
import com.gift.app.R;
import com.gift.app.databinding.ChatFragmentBinding;
import com.gift.app.ui.Home.stores.AdapterStores;
import com.gift.app.utils.Extensions;

public class ChatFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private ChatViewModel mViewModel;

    ChatFragmentBinding binding;
    private AdapterChat adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.chat_fragment, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ChatViewModel.class);

        binding.backImgv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back();
            }
        });

        binding.sendMessagesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validation())
                    sendMessage();
            }
        });

        setUiState();
        binding.chatSwipe.setOnRefreshListener(this);
        mViewModel.getChat();

    }


    private void sendMessage() {
        mViewModel.chatMessage = binding.messageEt.getText().toString();
        mViewModel.postChat();


    }


    private void back() {
        NavHostFragment.findNavController(this).navigateUp();
    }


    private Boolean validation() {
        if (binding.messageEt.getText().toString().trim().isEmpty()) {
            binding.messageEt.setError(requireActivity().getString(R.string.message_empty));
            return false;
        } else
            return true;
    }

    private void setUiState() {

        mViewModel.liveState.observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String state) {
                switch (state) {
                    case "onLoading":
                        binding.loading.setVisibility(View.VISIBLE);
                        binding.chatRv.setVisibility(View.GONE);
                        break;

                    case "onSuccess":
                        binding.loading.setVisibility(View.GONE);
                        binding.chatRv.setVisibility(View.VISIBLE);
                        onSuccess();
                        break;

                    case "onEmpty":
                        binding.chatRv.setVisibility(View.GONE);
                        binding.loading.setVisibility(View.GONE);
                        binding.emptyView.setVisibility(View.VISIBLE);
                        break;

                    case "onError":
                        binding.loading.setVisibility(View.GONE);
                        binding.chatRv.setVisibility(View.GONE);
                        Extensions.generalErrorSnakeBar(binding.chatRoot);
                        break;

                    case "onNoConnection":
                        binding.loading.setVisibility(View.GONE);
                        binding.chatRv.setVisibility(View.GONE);
                        Extensions.noInternetSnakeBar(binding.chatRoot);
                        break;

                    default:
                }
            }
        });

    }

    private void onSuccess() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireActivity());
        layoutManager.setReverseLayout(true);
        binding.chatRv.setLayoutManager(layoutManager);
        adapter = new AdapterChat(mViewModel.List, requireActivity());
        binding.chatRv.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }


    @Override
    public void onRefresh() {
        binding.chatSwipe.setRefreshing(false);
        mViewModel.getChat();
    }

}