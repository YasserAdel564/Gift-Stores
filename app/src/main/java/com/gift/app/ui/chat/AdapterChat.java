package com.gift.app.ui.chat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.gift.app.R;
import com.gift.app.data.models.ChatModel;


import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class AdapterChat extends RecyclerView.Adapter<AdapterChat.ChatViewHolder> {

    List<ChatModel> list;
    private Context mContext;

    public AdapterChat(List<ChatModel> list, Context mContext) {
        this.list = list;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        switch (viewType) {
            case 0:
                view = LayoutInflater.from(mContext).inflate(R.layout.from_message_item, parent, false);
                break;
            case 1:
                view = LayoutInflater.from(mContext).inflate(R.layout.to_message_item, parent, false);
                break;
            default:
        }
        return new ChatViewHolder(view);

    }

    @Override
    public int getItemViewType(int position) {
        if (list.get(position).getSender().equals("admin")) return 1;
        else return 0;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.app_logo)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.HIGH);


        if (list.get(position).getMessage() != null) {
            holder.messageText.setText(list.get(position).getMessage());
            holder.messageDate.setText(list.get(position).getDate());
        } else
            holder.textLayout.setVisibility(View.GONE);

        if (list.get(position).getPhoto() != null)
            Glide.with(mContext).load(list.get(position).getPhoto())
                    .apply(options)
                    .into(holder.messageImage);
        else
            holder.messageImage.setVisibility(View.GONE);


        holder.messageImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                holder.messageImage.setLayoutParams(new ConstraintLayout.LayoutParams
                        (ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT));
                holder.messageImage.setScaleType(ImageView.ScaleType.FIT_XY);

            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    static class ChatViewHolder extends RecyclerView.ViewHolder {
        private ImageView messageImage;
        private TextView messageText;
        private TextView messageDate;
        private ConstraintLayout textLayout;


        ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            messageImage = itemView.findViewById(R.id.imageMessage);
            messageText = itemView.findViewById(R.id.message);
            messageDate = itemView.findViewById(R.id.messageDate);
            textLayout = itemView.findViewById(R.id.textLayout);

        }


    }


}
