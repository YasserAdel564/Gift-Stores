package com.gift.app.ui.chat;

import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.gift.app.R;
import com.gift.app.databinding.ChatFragmentBinding;
import com.gift.app.utils.Extensions;
import com.google.android.material.button.MaterialButton;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;


public class ChatFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private ChatViewModel mViewModel;
    private static final int PICK_FROM_GALLERY = 1;
    private static final int PICK_FROM_CAMERA = 2;
    Dialog alertDialogBuilder;
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
        binding.sendImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlertDialog();
            }
        });

        setUiState();
        onPostChatResponse();
        binding.chatSwipe.setOnRefreshListener(this);
        if (!mViewModel.isOpen) {
            mViewModel.isOpen = true;
            mViewModel.getChat();
        }


    }


    private void sendMessage() {
        mViewModel.chatMessage = binding.messageEt.getText().toString();
        binding.messageEt.getText().clear();
        Extensions.hideKeyboard(requireActivity(), binding.messageEt);
        binding.selectedImage.setVisibility(View.GONE);
        binding.loading.setVisibility(View.VISIBLE);
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

    private void onPostChatResponse() {
        mViewModel.liveStatePost.observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String state) {
                switch (state) {
                    case "onSuccess":
                        mViewModel.getChat();
                        mViewModel.chatPhoto = null;
                        break;
                    case "onError":
                        Extensions.generalErrorSnakeBar(binding.chatRoot);
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

    private void requestGalleryPermissions() {
        try {
            if (ActivityCompat.checkSelfPermission(requireActivity(),
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(requireActivity(),
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE}, PICK_FROM_GALLERY);
            } else {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, PICK_FROM_GALLERY);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void requestCameraPermissions() {
        try {
            if (ActivityCompat.checkSelfPermission(requireActivity(),
                    Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(requireActivity(),
                        new String[]{Manifest.permission.CAMERA ,Manifest.permission.WRITE_EXTERNAL_STORAGE}, PICK_FROM_CAMERA);
            } else {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI.getPath());
                startActivityForResult(intent, PICK_FROM_CAMERA);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlertDialog() {

        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View view = layoutInflater.inflate(R.layout.gallery_camera_choose_view, null);
        AlertDialog.Builder alertDialogue = new AlertDialog.Builder(getContext());
        alertDialogBuilder = alertDialogue.create();
        alertDialogBuilder.show();
        Objects.requireNonNull(alertDialogBuilder.getWindow()).
                setBackgroundDrawableResource(android.R.color.white);
        alertDialogBuilder.setContentView(view);
        alertDialogBuilder.setCancelable(true);
        MaterialButton cameraBtn = view.findViewById(R.id.cameraBtn);
        MaterialButton galleryBtn = view.findViewById(R.id.galleryBtn);

        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestCameraPermissions();
                alertDialogBuilder.dismiss();
            }
        });

        galleryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestGalleryPermissions();
                alertDialogBuilder.dismiss();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case PICK_FROM_GALLERY:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(galleryIntent, PICK_FROM_GALLERY);
                } else {
                    Extensions.generalMessage(binding.chatRoot,
                            requireContext().getResources().getString(R.string.permission_gallery));
                }
                break;


        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FROM_GALLERY && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = requireActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            binding.selectedImage.setVisibility(View.VISIBLE);
            binding.selectedImage.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            mViewModel.chatPhoto = picturePath;
        }

        if (requestCode == PICK_FROM_CAMERA && resultCode == RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            Uri tempUri = getImageUri(getActivity(), photo);
            mViewModel.chatPhoto = (getRealPathFromURI(tempUri));
            binding.selectedImage.setVisibility(View.VISIBLE);
            binding.selectedImage.setImageBitmap(BitmapFactory.decodeFile(mViewModel.chatPhoto));


        }
    }


    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        String path = "";
        if (getActivity().getContentResolver() != null) {
            Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                path = cursor.getString(idx);
                cursor.close();
            }
        }
        return path;
    }

    @Override
    public void onRefresh() {
        binding.chatSwipe.setRefreshing(false);
        mViewModel.getChat();
    }


}