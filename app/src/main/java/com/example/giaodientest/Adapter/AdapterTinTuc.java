package com.example.giaodientest.Adapter;


//need adapter for news
//and viewholder

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

import com.example.giaodientest.DTO.DTOTinTuc;

public class AdapterTinTuc extends RecyclerView.Adapter<AdapterTinTuc.ViewHolderNews> {
    Context context;
    List<DTOTinTuc> newsList;
    OnTinTucListener onItemListener;

    public AdapterTinTuc(Context context,
                         List<DTOTinTuc> newsList,
                         OnTinTucListener onItemListener) {
        this.context = context;
        this.newsList = newsList;
        this.onItemListener = onItemListener;
    }

    @NonNull
    @Override
    public ViewHolderNews onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.layout_item_tintuc, parent, false);
        return new ViewHolderNews(view, onItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderNews holder, int position) {
        DTOTinTuc news = newsList.get(position);

        holder.tvNewsTitle.setText(news.TieuDe);
        holder.tvNewsDate.setText(news.ThoiGianTao);
        Glide.with(context).load(news.HinhAnh).into(holder.imgNewsImage);
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    static class ViewHolderNews extends RecyclerView.ViewHolder
        implements View.OnClickListener {
        AdapterTinTuc.OnTinTucListener onItemListener;
        ImageView imgNewsImage;
        TextView tvNewsTitle;
        TextView tvNewsDate;

        public ViewHolderNews(@NonNull View itemView, AdapterTinTuc.OnTinTucListener onItemListener) {
            super(itemView);

//            set the components ref
            imgNewsImage = itemView.findViewById(R.id.imgNewsImage);
            tvNewsTitle = itemView.findViewById(R.id.tvNewsTitle);
            tvNewsDate = itemView.findViewById(R.id.tvNewsDate);

            this.onItemListener = onItemListener;

            tvNewsTitle.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onItemListener.OnImageClick(getAdapterPosition());
        }
    }

    public interface OnTinTucListener {
        void OnImageClick(int position);
    }
}
