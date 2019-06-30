package com.ibnsaad.thedcc.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.ibnsaad.thedcc.R;
import com.ibnsaad.thedcc.activities.ChatActivity;
import com.ibnsaad.thedcc.enums.Enums;
import com.ibnsaad.thedcc.heper.SharedHelper;
import com.ibnsaad.thedcc.model.User;
import com.ibnsaad.thedcc.server.BaseClient;
import com.ibnsaad.thedcc.utils.ItemAnimation;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UsersAdapterGridScrollProgress extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_ITEM = 1;
    private final int VIEW_PROGRESS = 0;
    private final String TAG = "UsersAdapterGridScrollP";
    private int item_per_display = 0;
    private List<User> items;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener = null;
    private Context ctx;
    private OnItemClickListener mOnItemClickListener;
    private int animation_type = 1;
    private int lastPosition = -1;
    private boolean on_attach = true;

    public UsersAdapterGridScrollProgress(Context context, int item_per_display, List<User> items) {
        this.items = items;
        this.item_per_display = item_per_display;
        ctx = context;
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_user_item, parent, false);
            vh = new OriginalViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_progress, parent, false);
            vh = new ProgressViewHolder(v);
        }
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final User s = items.get(position);
        if (holder instanceof OriginalViewHolder) {
            OriginalViewHolder view = (OriginalViewHolder) holder;
            if (SharedHelper.getKey(ctx,Enums.UserType.name()).equals(ctx.getString(R.string.doctors)))
            {
                view.image.getHierarchy().setPlaceholderImage(ctx.getDrawable(R.drawable.doctor));
            }else {
                view.image.getHierarchy().setPlaceholderImage(ctx.getDrawable(R.drawable.patient));
            }
            view.image.setImageURI(s.getPhotoUrl());

            view.likes.setText(String.valueOf(s.getLikerCount()));
            view.message.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ctx, ChatActivity.class);
                    intent.putExtra(Enums.USER.name(), s);
                    ctx.startActivity(intent);

                }
            });
            view.parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener == null) return;
                    mOnItemClickListener.onItemClick(view, s.getId().toString(), position);
                }
            });
            view.like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    BaseClient.getApi().like(
                            SharedHelper.getKey(ctx, Enums.AUTH_TOKEN.name()),
                            SharedHelper.getKey(ctx, Enums.ID.name()),
                            s.getId()
                    ).enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.code() == 200) {
                                int count = Integer.parseInt(view.likes.getText().toString());
                                count += 1;
                                view.likes.setText(String.valueOf(count));
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Log.d(TAG, "onFailure: " + t.getMessage());
                        }
                    });
                }
            });
            view.user_name.setText(s.getKnownAs());
            view.likes.setText("" + s.getLikerCount());
            setAnimation(view.itemView, position);
        } else {
            ((ProgressViewHolder) holder).progress_bar.setIndeterminate(true);

        }
        if (s.progress) {
            StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
            layoutParams.setFullSpan(true);
        } else {
            StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
            layoutParams.setFullSpan(false);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        return this.items.get(position).progress ? VIEW_PROGRESS : VIEW_ITEM;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                on_attach = false;
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
        lastItemViewDetector(recyclerView);
        super.onAttachedToRecyclerView(recyclerView);
    }

    public void insertData(List<User> items) {
        setLoaded();
        int positionStart = getItemCount();
        int itemCount = items.size();
        this.items.addAll(items);
        notifyItemRangeInserted(positionStart, itemCount);
    }

    public void setLoaded() {
        loading = false;
        for (int i = 0; i < getItemCount(); i++) {
            if (items.get(i).progress) {
                items.remove(i);
                notifyItemRemoved(i);
            }
        }
    }

    public void setLoading() {
        if (getItemCount() != 0) {
            this.items.add(new User(true));
            notifyItemInserted(getItemCount() - 1);
            loading = true;
        }
    }

    public void resetListData() {
        this.items = new ArrayList<>();
        notifyDataSetChanged();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    private void lastItemViewDetector(RecyclerView recyclerView) {
        if (recyclerView.getLayoutManager() instanceof StaggeredGridLayoutManager) {
            final StaggeredGridLayoutManager layoutManager = (StaggeredGridLayoutManager) recyclerView.getLayoutManager();
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    int lastPos = getLastVisibleItem(layoutManager.findLastVisibleItemPositions(null));
                    if (!loading && lastPos == getItemCount() - 1 && onLoadMoreListener != null) {
                        int current_page = getItemCount() / item_per_display;
                        onLoadMoreListener.onLoadMore(current_page);
                        loading = true;
                    }
                }
            });
        }
    }

    private int getLastVisibleItem(int[] into) {
        int last_idx = into[0];
        for (int i : into) {
            if (last_idx < i) last_idx = i;
        }
        return last_idx;
    }

    private void setAnimation(View view, int position) {
        if (position > lastPosition) {
            ItemAnimation.animate(view, on_attach ? position : -1, animation_type);
            lastPosition = position;
        }
    }

    public List<User> getItems() {
        return items;
    }

    public void setItems(List<User> items) {
        this.items = items;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, String id, int position);
    }

    public interface OnLoadMoreListener {
        void onLoadMore(int current_page);
    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progress_bar;

        public ProgressViewHolder(View v) {
            super(v);
            progress_bar = v.findViewById(R.id.progress_bar);
        }
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        SimpleDraweeView image;
        View like, message;
        View parent;
        TextView user_name, likes;
        TextView bio;

        public OriginalViewHolder(View v) {
            super(v);
            parent = v;
            image = v.findViewById(R.id.user_image);
            user_name = v.findViewById(R.id.user_name);
            bio = v.findViewById(R.id.bio);

            message = v.findViewById(R.id.message);
            like = v.findViewById(R.id.like);
            likes = v.findViewById(R.id.likes);

        }
    }
}