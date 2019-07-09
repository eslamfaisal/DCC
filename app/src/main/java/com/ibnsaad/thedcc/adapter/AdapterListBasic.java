package com.ibnsaad.thedcc.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.ibnsaad.thedcc.R;
import com.ibnsaad.thedcc.activities.ChatActivity;
import com.ibnsaad.thedcc.enums.Enums;
import com.ibnsaad.thedcc.heper.SharedHelper;
import com.ibnsaad.thedcc.model.Message;
import com.ibnsaad.thedcc.model.User;

import java.util.ArrayList;
import java.util.List;

public class AdapterListBasic extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Message> items = new ArrayList<>();

    private Context ctx;
    private OnItemClickListener mOnItemClickListener;

    public AdapterListBasic(Context context, List<Message> items) {
        this.items = items;
        ctx = context;
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_people_chat, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof OriginalViewHolder) {
            OriginalViewHolder view = (OriginalViewHolder) holder;

            Message p = items.get(position);
            view.name.setText(p.getSenderKnownAs());
            view.message.setText(p.getContent());
            view.image.setImageURI(p.getSenderPhotoUrl());
            view.lyt_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(ctx, ChatActivity.class);
                    intent.putExtra(Enums.USER.name(), new User(
                            p.getSenderId(),
                           p.getSenderKnownAs(),
                            p.getSenderPhotoUrl()
                    ));
                    ctx.startActivity(intent);

                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public interface OnItemClickListener {
        void onItemClick(View view, Message obj, int position);
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public SimpleDraweeView image;
        public TextView name;
        public TextView message;
        public View lyt_parent;

        public OriginalViewHolder(View v) {
            super(v);
            image = v.findViewById(R.id.image);
            message = v.findViewById(R.id.message);
            name = v.findViewById(R.id.name);
            lyt_parent = v.findViewById(R.id.lyt_parent);
        }
    }

}