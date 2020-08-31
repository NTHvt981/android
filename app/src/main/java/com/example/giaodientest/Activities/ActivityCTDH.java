package com.example.giaodientest.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.giaodientest.Adapter.AdapterCTDH;
import com.example.giaodientest.DTO.DTOCTDH;
import com.example.giaodientest.DTO.DTODonHang;
import com.example.giaodientest.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ActivityCTDH extends AppCompatActivity {
    RecyclerView rvCTDH;
    EditText edtTen;
    EditText edtSdt;
    EditText edtDiaChi;
    TextView tvGia;
    Button btnQuayLai;
    Button btnHuy;

    AdapterCTDH adapter;
    List<DTOCTDH> ctdhList;
    CollectionReference refCthd;
    DocumentReference refDonHang;
    DTODonHang donHang;

    String tvTextGia = "Tổng cộng: 0 VNĐ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ctdh);

        setComponents();

        Intent data = getIntent();
        donHang = data.getParcelableExtra("đơn hàng");
        refDonHang = FirebaseFirestore.getInstance()
                .collection("DonHang")
                .document(donHang.Ma);
        refCthd = FirebaseFirestore.getInstance()
                .collection("ChiTietDonHang")
                .document(donHang.Ma)
                .collection("ChiTietDonHang");

        ctdhList = new ArrayList<>(0);
        rvCTDH.setHasFixedSize(true);
        rvCTDH.setLayoutManager(new LinearLayoutManager(getBaseContext()));

        adapter = new AdapterCTDH(getBaseContext(), ctdhList);
        rvCTDH.setAdapter(adapter);
    }

    private void updateUI(DTODonHang donHang)
    {
        edtTen.setText(donHang.TenKhachHang);
        edtSdt.setText(donHang.SoDienThoai);
        edtDiaChi.setText(donHang.DiaChi);
        tvGia.setText(
                tvTextGia.replace("0",
                        ZUltility.formatMoney(String.valueOf(donHang.TongCong))
                )
        );
    }

    private void setComponents() {
        rvCTDH = findViewById(R.id.rvCTDH);
        edtTen = findViewById(R.id.edtTen);
        edtSdt = findViewById(R.id.edtSdt);
        edtDiaChi = findViewById(R.id.edtDiaChi);
        tvGia = findViewById(R.id.tvGia);
        btnQuayLai = findViewById(R.id.btnQuayLai);
        btnHuy = findViewById(R.id.btnHuy);
    }

    @Override
    protected void onStart() {
        super.onStart();

        refCthd.get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                ctdhList.clear();
                if (task.isSuccessful())
                {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        ctdhList.add( document.toObject(DTOCTDH.class));
                    }
                    adapter.notifyDataSetChanged();
                }
                else
                {
                    FirebaseFirestoreException exception = (FirebaseFirestoreException) task.getException();
                    //do that
                    Toast.makeText(getBaseContext(),
                            ZUltility.getNotifyFirebaseExeption(exception),
                            Toast.LENGTH_LONG).show();
                }
                }
            });

        refDonHang.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
            DTODonHang donHang = documentSnapshot.toObject(DTODonHang.class);

            if (donHang.TinhTrang == DTODonHang.DA_HUY)
            {
                Toast.makeText(getBaseContext(), "Đơn hàng đã bị hủy", Toast.LENGTH_LONG).show();
                finish();
            }

            updateUI(donHang);
            }
        });
    }

    public void quayLai(View view) {
        finish();
    }

    public void huy(View view) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setPositiveButton("Có",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        refDonHang.update("TinhTrang", DTODonHang.DA_HUY);
                        finish();
                    }
                })
                .setNegativeButton("Không",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                .setTitle("Xác nhận")
                .setMessage("Bạn có đồng ý hủy đơn hàng?");

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_top);
    }
}