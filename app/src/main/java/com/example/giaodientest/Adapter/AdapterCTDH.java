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

import com.example.giaodientest.DTO.DTOCTDH;

public class AdapterCTDH extends
        RecyclerView.Adapter<AdapterCTDH.ViewHolderCTDH> {
    Context context;
    List<DTOCTDH> ctdhList;

    public AdapterCTDH(Context context,
                       List<DTOCTDH> ctdhList)
    {
        this.context = context;
        this.ctdhList = ctdhList;
    }

    @NonNull
    @Override
    public ViewHolderCTDH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View view = layoutInflater.inflate(R.layout.layout_item_ctdh, parent, false);
        return new AdapterCTDH.ViewHolderCTDH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderCTDH holder, int position) {
        DTOCTDH ctdh = ctdhList.get(position);

        Glide.with(context).load(ctdh.Hinh).into(holder.imgCTDH);
        holder.tvTen.setText(ctdh.Ten);
        holder.tvGia.setText(String.valueOf(ctdh.Gia));
    }

    @Override
    public int getItemCount() {
        return ctdhList.size();
    }

    static class ViewHolderCTDH extends RecyclerView.ViewHolder
    {
        ImageView imgCTDH;
        TextView tvTen;
        TextView tvGia;

        public ViewHolderCTDH(@NonNull View itemView) {
            super(itemView);

//            set the components ref
            imgCTDH = itemView.findViewById(R.id.imgCTDH);
            tvTen = itemView.findViewById(R.id.tvTen);
            tvGia = itemView.findViewById(R.id.tvGia);
        }
    }
}
