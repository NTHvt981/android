package com.example.giaodientest.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.giaodientest.DTO.DTOPKTT;
import com.example.giaodientest.R;

public class ActivityThongTinPKTT extends AppCompatActivity {
    ImageView imgHinh;
    TextView tvTen;
    TextView tvLoai;
    TextView tvHang;
    TextView tvMau;
    TextView tvGia;
    TextView tvMoTa;

    String txtLoai = "Loại: ";
    String txtHang = "Hãng: ";
    String txtMau = "Màu: ";
    String txtGia = "Giá; ";
    String txtMoTa = "Mô tả";
    DTOPKTT pktt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_tin_pktt);

        Intent intent = getIntent();
        pktt = intent.getParcelableExtra(DTOPKTT.INTENT_TAG);

        getComponents();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (pktt == null) finish();
        else
        {
            setComponents(pktt);
        }
    }

    private void getComponents() {
        imgHinh = findViewById(R.id.imgHinh);
        tvTen = findViewById(R.id.tvTen);
        tvLoai = findViewById(R.id.tvLoai);
        tvHang = findViewById(R.id.tvHang);
        tvMau = findViewById(R.id.tvMau);
        tvGia = findViewById(R.id.tvGia);
        tvMoTa = findViewById(R.id.tvMoTa);
    }

    @SuppressLint("SetTextI18n")
    private void setComponents(@NonNull DTOPKTT pktt) {
        Glide.with(this).load(pktt.Hinh).into(imgHinh);
        tvTen.setText(pktt.Ten);
        tvLoai.setText(txtLoai + pktt.Loai);
        tvHang.setText(txtHang + pktt.Hang);
        tvMau.setText(txtMau + pktt.Mau);
        tvGia.setText(txtGia + ZUltility.formatMoney(String.valueOf(pktt.Gia)));
        tvMoTa.setText(txtMoTa + pktt.MoTa);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}