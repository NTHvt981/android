package com.example.giaodientest.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.giaodientest.Adapter.AdapterPKTT;
import com.example.giaodientest.DTO.DTOCTDH;
import com.example.giaodientest.DTO.DTODonHang;
import com.example.giaodientest.DTO.DTOKhachHang;
import com.example.giaodientest.DTO.DTOPKTT;
import com.example.giaodientest.DialogXacNhan;
import com.example.giaodientest.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class ActivityThongTinGiaoHang extends AppCompatActivity
        implements View.OnClickListener,
        AdapterPKTT.OnPkttListener,
        DialogXacNhan.DialogXacNhanListener {
    RecyclerView rvPhuKien;
    EditText edtTen;
    EditText edtSDT;
    EditText edtDiaChi;
    Button btnThanhToan;
    ImageView imgTen;
    ImageView imgSDT;
    ImageView imgDiaChi;

    ArrayList<DTOPKTT> lsPKTT;
    AdapterPKTT adapterPKTT;

    //for firebase
//    DatabaseReference referenceCTDH;
//    DatabaseReference referenceDonHang;
    CollectionReference referenceCTDH;
    CollectionReference referenceDonHang;
    CollectionReference referenceKhachHang;

    //Giá trị
    String MaDonHang;
    long TongCong = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_tin_giao_hang);

        setComponents();
        setEvent();

        getPKTTList();
        getSetUserData();

        rvPhuKien.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(getBaseContext());
        manager.setOrientation(RecyclerView.HORIZONTAL);
        rvPhuKien.setLayoutManager(manager);
        adapterPKTT = new AdapterPKTT(
                getBaseContext(),
                lsPKTT,
                this
        );
        rvPhuKien.setAdapter(adapterPKTT);
    }

    private void getSetUserData() {
        referenceKhachHang.document(
                Objects.requireNonNull(FirebaseAuth.getInstance().getUid())
        ).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                DTOKhachHang khachHang = documentSnapshot.toObject(DTOKhachHang.class);
                edtTen.setText(khachHang.HoTen);
                edtSDT.setText(khachHang.SoDienThoai);
                edtDiaChi.setText(khachHang.DiaChi);
            }
        });
    }

    private void setComponents() {
        rvPhuKien = findViewById(R.id.rvPKTT);
        edtTen = findViewById(R.id.edtTen);
        edtSDT = findViewById(R.id.edtSDT);
        edtDiaChi = findViewById(R.id.edtDiaChi);
        btnThanhToan = findViewById(R.id.btnThanhToan);
        imgTen = findViewById(R.id.imgTen);
        imgSDT = findViewById(R.id.imgSDT);
        imgDiaChi = findViewById(R.id.imgDiaChi);

        //khởi tạo firebase
        referenceDonHang = FirebaseFirestore.getInstance().collection("DonHang");
        referenceCTDH = FirebaseFirestore.getInstance().collection("ChiTietDonHang");
        referenceKhachHang = FirebaseFirestore.getInstance().collection("KhachHang");

        MaDonHang = referenceDonHang.document().getId();
    }

    private void setEvent() {
        btnThanhToan.setOnClickListener(this);
        imgTen.setOnClickListener(this);
        imgSDT.setOnClickListener(this);
        imgDiaChi.setOnClickListener(this);
    }

    private void getPKTTList() {
        Intent intent = getIntent();
        lsPKTT = intent.getParcelableArrayListExtra("danh sách phụ kiện");
        if (lsPKTT == null || lsPKTT.size() == 0)
        {
            Toast.makeText(getBaseContext(),
                    "Không có phụ kiện thời trang ActivityThongTinGiaoHang",
                    Toast.LENGTH_LONG).show();
            finish();
        }
    }

    public ArrayList<DTOCTDH> convertToCTDH(
            ArrayList<DTOPKTT> lspktt, String maDH)
    {
        ArrayList<DTOCTDH> lsctdh = new ArrayList<>(0);

        for (DTOPKTT pktt: lspktt) {
            DTOCTDH dh = new DTOCTDH();

            String macthd = referenceCTDH.document().getId();

            dh.Ma = macthd;
            dh.MaDonHang = maDH;
            dh.MaPhuKien = pktt.Ma;
            dh.Ten = pktt.Ten;
            dh.Hang = pktt.Hang;
            dh.Hinh = pktt.Hinh;
            dh.Gia = pktt.Gia;
            dh.Loai = pktt.Loai;
            dh.Mau = pktt.Mau;
            dh.MoTa = pktt.MoTa;

            lsctdh.add(dh);
        }

        return lsctdh;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnThanhToan:
                thanhToan();
                break;
            case R.id.imgTen:
                flipEditText(edtTen);
                break;
            case R.id.imgSDT:
                flipEditText(edtSDT);
                break;
            case R.id.imgDiaChi:
                flipEditText(edtDiaChi);
                break;
            default:
                break;
        }
    }

    private void thanhToan() {
        String ten = edtTen.getText().toString();
        String sdt = edtSDT.getText().toString();
        String diaChi = edtDiaChi.getText().toString();

        if (!hopLeKhong(ten, sdt, diaChi)) return;

        DialogXacNhan dialog = new DialogXacNhan(ten, sdt, diaChi);
        dialog.show(getSupportFragmentManager(), "Xác nhận");
    }

    private boolean hopLeKhong(String ten, String sdt, String diaChi) {
        if (ten.trim().isEmpty())
        {
            Toast.makeText(getBaseContext(),
                    "Họ tên trống!",
                    Toast.LENGTH_LONG).show();
            return false;
        }
        if (sdt.trim().isEmpty())
        {
            Toast.makeText(getBaseContext(),
                    "Số điện thoại trống!",
                    Toast.LENGTH_LONG).show();
            return false;
        }
        if (diaChi.trim().isEmpty())
        {
            Toast.makeText(getBaseContext(),
                    "Địa chỉ trống!",
                    Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    @Override
    public void chapNhan() {
        ArrayList<DTOCTDH> lsCTDH = convertToCTDH(lsPKTT, MaDonHang);

        for (DTOCTDH ctdh: lsCTDH) {
//            referenceCTDH.document(ctdh.Ma).set(ctdh);
            referenceCTDH.document(MaDonHang).collection("ChiTietDonHang").document(ctdh.Ma).set(ctdh);

            //Cập nhật tổng cộng
            TongCong = TongCong + ctdh.Gia;
        }


        DTODonHang donHang = new DTODonHang();
        //get mã khách hàng
        try {
            donHang.MaKhachHang = FirebaseAuth.getInstance().getUid();
        }
        catch (Exception ex)
        {
            Toast.makeText(this, "Quý khách chưa đặt hàng", Toast.LENGTH_LONG).show();
            finish();
        }

        //get thời gian hiện tại vd (10:15:00 3:5:2020)
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss / dd-MM-yyyy", Locale.getDefault());
        donHang.ThoiGianDatHang = sdf.format(Calendar.getInstance().getTime());
        donHang.Ma = MaDonHang;
        donHang.TenKhachHang = edtTen.getText().toString();
        donHang.SoDienThoai = edtSDT.getText().toString();
        donHang.DiaChi = edtDiaChi.getText().toString();
        donHang.TinhTrang = DTODonHang.DA_DAT;
        donHang.TongCong = TongCong;

//        referenceDonHang.child(MaDonHang).setValue(donHang);
        referenceDonHang.document(donHang.Ma)
                .set(donHang)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getBaseContext(),
                                "Đặt hàng thành công!",
                                Toast.LENGTH_LONG).show();

                        setResult(RESULT_OK);
                        finishActivity(ZUltility.DON_HANG_REQUEST);
                        finish();
                    }
                });
    }

    @Override
    public void onHinhClick(int position) {}
    @Override
    public void onTenClick(int position) {}
    @Override
    public void onHinhLongClick(int position) { }

    private void flipEditText(EditText editText) {
        if (editText.isEnabled())
            editText.setEnabled(false);
        else
            editText.setEnabled(true);
    }

    @Override
    public void finish()
    {
        super.finish();
        overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_top);
    }
}