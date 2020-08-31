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

import com.example.giaodientest.DTO.DTOQuanAo;

public class AdapterQuanAo extends
        RecyclerView.Adapter<AdapterQuanAo.ViewHolderQuanAo>{
    Context context;
    List<DTOQuanAo> quanAoList;
    AdapterQuanAo.OnQuanAoListener onItemListener;


    public AdapterQuanAo( Context context,
                          List<DTOQuanAo> quanAoList,
                          AdapterQuanAo.OnQuanAoListener onItemListener) {
        this.context = context;
        this.quanAoList = quanAoList;
        this.onItemListener = onItemListener;
    }

    @NonNull
    @Override
    public ViewHolderQuanAo onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View view = layoutInflater.inflate(R.layout.layout_item_quanao, parent, false);
        return new AdapterQuanAo.ViewHolderQuanAo(view, onItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderQuanAo holder, int position) {
        DTOQuanAo quanAo = quanAoList.get(position);

        holder.tvTen.setText(quanAo.TenToanBoPKTT);

        if (!quanAo.HinhAnh.isEmpty())
            Glide.with(context).load(quanAo.HinhAnh).into(holder.imgHinh);
    }

    @Override
    public int getItemCount() {
        return quanAoList.size();
    }

    static class ViewHolderQuanAo extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        AdapterQuanAo.OnQuanAoListener onItemListener;
        TextView tvTen;
        ImageView imgHinh;
        ImageView imgDelete;

        public ViewHolderQuanAo(@NonNull View itemView, AdapterQuanAo.OnQuanAoListener onItemListener) {
            super(itemView);

//            set the components
            tvTen = itemView.findViewById(R.id.tvTen);
            imgHinh = itemView.findViewById(R.id.imgHinh);
            imgDelete = itemView.findViewById(R.id.imgDelete);

            this.onItemListener = onItemListener;

            imgHinh.setOnClickListener(this);
            imgDelete.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.imgDelete:
                    onItemListener.OnDeleteClick(getAdapterPosition());
                    break;
                default:
                    onItemListener.OnItemClick(getAdapterPosition());
                    break;
            }
        }
    }

    public interface OnQuanAoListener {
        void OnItemClick(int position);
        void OnDeleteClick(int position);
    }
}
