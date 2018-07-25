package com.example.tomato.lesson6sqlitetdgiang.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.tomato.lesson6sqlitetdgiang.R;
import com.example.tomato.lesson6sqlitetdgiang.model.Contact;

import java.util.ArrayList;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHodelr> {


    private OnCallBack onCallBack;
    private Boolean status = null;

    ArrayList<Contact> arrContact;
    Context context;
    private SparseBooleanArray mSelectedItemsIds;

    public ContactAdapter(ArrayList<Contact> arrContact, Context context, OnCallBack onCallBack, Boolean status) {
        this.arrContact = arrContact;
        this.context = context;
        this.onCallBack = onCallBack;
        this.status = status;
        mSelectedItemsIds = new SparseBooleanArray();
    }

    @NonNull
    @Override
    public ViewHodelr onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_activity, parent, false);
        if (itemView != null) {
            return new ViewHodelr(itemView);
        } else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHodelr holder, final int position) {

        holder.tvName.setText(arrContact.get(position).getName());
        holder.tvNumberPhone.setText(arrContact.get(position).getNumberPhone());
        holder.cbStatus.setChecked(mSelectedItemsIds.get(position));

        holder.cbStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkCheckBox(position, !mSelectedItemsIds.get(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        return (null != arrContact ? arrContact.size() : 0);
    }

    public class ViewHodelr extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        TextView tvName, tvNumberPhone;
        CheckBox cbStatus;

        public ViewHodelr(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tv_name);
            tvNumberPhone = itemView.findViewById(R.id.tv_number_phone);
            cbStatus = itemView.findViewById(R.id.cb_status);
            if (status) {
                cbStatus.setVisibility(View.VISIBLE);

            } else {
                cbStatus.setVisibility(View.GONE);
            }
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

        }

        @Override
        public void onClick(View view) {
            onCallBack.onItemClicked(getAdapterPosition(), false);
        }

        @Override
        public boolean onLongClick(View view) {
            onCallBack.onItemClicked(getAdapterPosition(), true);

            status = true;
            return true;
        }
    }

    public interface OnCallBack {
        void onItemClicked(int position, boolean isLongClick);
    }

    //-------------------------------------------- check box------------------------------

    /**
     * Remove all checkbox Selection
     **/
    public void removeSelection() {
        mSelectedItemsIds = new SparseBooleanArray();
        notifyDataSetChanged();
    }

    /**
     * Check the Checkbox if not checked
     **/
    public void checkCheckBox(int position, boolean value) {
        if (value) {
            mSelectedItemsIds.put(position, true);
        } else {
            mSelectedItemsIds.delete(position);
        }
        notifyDataSetChanged();
    }

    /**
     * Return the selected Checkbox IDs
     **/
    public SparseBooleanArray getSelectedIds() {
        return mSelectedItemsIds;
    }
    //----------------------------------------------------------------------------------------------
}

