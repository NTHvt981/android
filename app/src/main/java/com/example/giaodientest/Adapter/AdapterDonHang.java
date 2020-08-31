package com.example.giaodientest.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.giaodientest.R;

import java.util.List;

import com.example.giaodientest.DTO.DTODonHang;

public class AdapterDonHang extends
        RecyclerView.Adapter<AdapterDonHang.ViewHolderDonHang>{
    Context context;
    List<DTODonHang> donHangList;
    AdapterDonHang.OnDonHangListener onItemListener;

    //chữ thể hiện trước giá trị
    String tvTextTen = "Tên người đặt: ";
    String tvTextGia = "Tổng giá trị: 0 VNĐ";
    String tvTextThoiGian = "Thời gian đặt: ";
    String tvTextTinhTrang = "Tình trạng: ";

    public AdapterDonHang( Context context,
                           List<DTODonHang> donHangList,
                           AdapterDonHang.OnDonHangListener onItemListener) {
        this.context = context;
        this.donHangList = donHangList;
        this.onItemListener = onItemListener;
    }

    @NonNull
    @Override
    public ViewHolderDonHang onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View view = layoutInflater.inflate(R.layout.layout_item_donhang, parent, false);
        return new AdapterDonHang.ViewHolderDonHang(view, onItemListener);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolderDonHang holder, int position) {
        DTODonHang donHang = donHangList.get(position);

        holder.tvTen.setText(tvTextTen + donHang.TenKhachHang);
        holder.tvGia.setText(tvTextGia.replace("0", String.valueOf(donHang.TongCong)));
        holder.tvThoiGian.setText(tvTextThoiGian +   donHang.ThoiGianDatHang);
        holder.tvTinhTrang.setText(tvTextTinhTrang + donHang.TinhTrang);
    }

    @Override
    public int getItemCount() {
        return donHangList.size();
    }

    static class ViewHolderDonHang extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        AdapterDonHang.OnDonHangListener onItemListener;
        TextView tvTen;
        TextView tvGia;
        TextView tvThoiGian;
        TextView tvTinhTrang;

        public ViewHolderDonHang(@NonNull View itemView, AdapterDonHang.OnDonHangListener onItemListener) {
            super(itemView);

//            set the components
            tvTen = itemView.findViewById(R.id.tvTen);
            tvGia = itemView.findViewById(R.id.tvGia);
            tvThoiGian = itemView.findViewById(R.id.tvThoiGian);
            tvTinhTrang = itemView.findViewById(R.id.tvTinhTrang);

            this.onItemListener = onItemListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onItemListener.OnItemClick(getAdapterPosition());
        }
    }

    public interface OnDonHangListener {
        void OnItemClick(int position);
    }
}
