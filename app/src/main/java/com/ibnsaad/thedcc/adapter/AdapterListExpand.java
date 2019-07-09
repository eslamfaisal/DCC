package com.ibnsaad.thedcc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ibnsaad.thedcc.R;
import com.ibnsaad.thedcc.model.DrugsResponse;
import com.ibnsaad.thedcc.model.Social;
import com.ibnsaad.thedcc.utils.Tools;
import com.ibnsaad.thedcc.utils.ViewAnimation;

import java.util.ArrayList;
import java.util.List;

public class AdapterListExpand extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<DrugsResponse> items = new ArrayList<>();


    private Context ctx;
    private OnItemClickListener mOnItemClickListener;

    public AdapterListExpand(Context context, List<DrugsResponse> items) {
        this.items = items;
        ctx = context;
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_expand, parent, false);
        vh = new Holder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof Holder) {
            final Holder view = (Holder) holder;

            final DrugsResponse drugs = items.get(position);
            if (drugs.getDrugName() != null)
                view.name.setText(drugs.getDrugName());
            if (drugs.getTreatmentBulletin() != null){

                if (drugs.getTreatmentBulletin().getComposition() != null)
                    view.composition.setText(drugs.getTreatmentBulletin().getComposition());
                if (drugs.getTreatmentBulletin().getDosing() != null)
                    view.dosing.setText(drugs.getTreatmentBulletin().getDosing());
                if (drugs.getTreatmentBulletin().getIndications() != null)
                    view.indications.setText(drugs.getTreatmentBulletin().getIndications());
                if (drugs.getTreatmentBulletin().getSideEffects() != null)
                    view.sideEffects.setText(drugs.getTreatmentBulletin().getSideEffects());
                view.bt_expand.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean show = toggleLayoutExpand(!drugs.expanded, v, view.lyt_expand);
                        items.get(position).expanded = show;
                    }
                });
            }
            view.lyt_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {

                    }
                }
            });

            view.bt_expand.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean show = toggleLayoutExpand(!drugs.expanded, v, view.lyt_expand);
                    items.get(position).expanded = show;
                }
            });


            // void recycling view
            if (drugs.expanded) {
                view.lyt_expand.setVisibility(View.VISIBLE);
            } else {
                view.lyt_expand.setVisibility(View.GONE);
            }
            Tools.toggleArrow(drugs.expanded, view.bt_expand, false);

        }
    }

    private boolean toggleLayoutExpand(boolean show, View view, View lyt_expand) {
        Tools.toggleArrow(show, view);
        if (show) {
            ViewAnimation.expand(lyt_expand);
        } else {
            ViewAnimation.collapse(lyt_expand);
        }
        return show;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(List<DrugsResponse> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public void clear() {
        items.clear();
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(View view, Social obj, int position);
    }

    public class Holder extends RecyclerView.ViewHolder {
        public TextView name, composition, indications, dosing, sideEffects;
        public ImageButton bt_expand;
        public View lyt_expand;
        public View lyt_parent;

        public Holder(View v) {
            super(v);
            name = v.findViewById(R.id.name);
            sideEffects = v.findViewById(R.id.sideEffects);
            dosing = v.findViewById(R.id.dosing);
            indications = v.findViewById(R.id.indications);
            composition = v.findViewById(R.id.composition);
            bt_expand = v.findViewById(R.id.bt_expand);
            lyt_expand = v.findViewById(R.id.lyt_expand);
            lyt_parent = v.findViewById(R.id.lyt_parent);
        }
    }
}