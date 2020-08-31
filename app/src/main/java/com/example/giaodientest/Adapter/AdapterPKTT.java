package com.example.giaodientest.Adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.giaodientest.R;

import java.util.List;

import com.example.giaodientest.DTO.DTOPKTT;

public class AdapterPKTT extends
        RecyclerView.Adapter<AdapterPKTT.ViewHolderPKTT> {
    Context context;
    List<DTOPKTT> pkttList;
    OnPkttListener onItemListener;

    public AdapterPKTT(Context context,
                       List<DTOPKTT> pkttList,
                       OnPkttListener onItemListener) {
        this.context = context;
        this.pkttList = pkttList;
        this.onItemListener = onItemListener;
    }

    @NonNull
    @Override
    public ViewHolderPKTT onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View view = layoutInflater.inflate(R.layout.layout_item_pktt, parent, false);
        return new ViewHolderPKTT(view, onItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderPKTT holder, int position) {
        DTOPKTT phuKienThoiTrang = pkttList.get(position);

        holder.tvGia.setText(
                String.valueOf(phuKienThoiTrang.Gia));

        if (!phuKienThoiTrang.Hinh.isEmpty())
            Glide.with(context).load(phuKienThoiTrang.Hinh).into(holder.imgPhuKien);
    }

    @Override
    public int getItemCount() {
        return pkttList.size();
    }

    static class ViewHolderPKTT extends RecyclerView.ViewHolder
        implements View.OnClickListener, View.OnLongClickListener {
        OnPkttListener onItemListener;

        ImageView imgPhuKien;
        TextView tvTen;
        TextView tvGia;

        public ViewHolderPKTT(
                @NonNull View itemView, OnPkttListener onItemListener) {
            super(itemView);

//            set the components ref
            imgPhuKien = itemView.findViewById(R.id.imgPhuKien);
            tvGia = itemView.findViewById(R.id.tvGia);

            this.onItemListener = onItemListener;

            imgPhuKien.setOnClickListener(this);
            imgPhuKien.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId())
            {
                case R.id.imgPhuKien:
                    onItemListener.onHinhClick(getAdapterPosition());
                    break;
                case R.id.tvTen:
                    onItemListener.onTenClick(getAdapterPosition());
                    break;
            }
        }

        @Override
        public boolean onLongClick(View view) {
            onItemListener.onHinhLongClick(getAdapterPosition());
            return false;
        }
    }

    public interface OnPkttListener {
        void onHinhClick(int position);
        void onTenClick(int position);
        void onHinhLongClick(int position);
    }
}
