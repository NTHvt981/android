package com.example.giaodientest.Fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.giaodientest.DTO.DTOKhachHang;
import com.example.giaodientest.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.SuccessContinuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;


public class FragmentCaNhan extends Fragment implements View.OnClickListener {
    FirebaseAuth auth;
    FirebaseFirestore db;

    EditText edtTen;
    EditText edtSdt;
    EditText edtDiaChi;
    ImageView imgHinh;
    ProgressBar pbUploading;
    Button btnSuaLuu;
    Button btnDangXuat;

    DTOKhachHang khachHang;
    Uri uriProfileImage;
    String profileImageUrl;
    Context context;

    public FragmentCaNhan(@NonNull Context context)
    {
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        getKhachHang();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_ca_nhan, container, false);
        getComponents(root);
        setEvents();
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (khachHang != null) setComponents(khachHang);
    }

    private void getKhachHang() {
        db.collection("KhachHang")
                .document(Objects.requireNonNull(auth.getUid()))
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful())
                        {
                            khachHang = task.getResult().toObject(DTOKhachHang.class);
                            if (khachHang == null)
                                khachHang = new DTOKhachHang();
                            else
                                setComponents(khachHang);
                        }
                        else
                            Toast.makeText(context, "Xảy ra lỗi trong việc lấy thông tin khách hàng",
                                    Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void getComponents(View root) {
        edtTen = root.findViewById(R.id.edtTen);
        edtSdt = root.findViewById(R.id.edtSdt);
        edtDiaChi = root.findViewById(R.id.edtDiaChi);
        imgHinh = root.findViewById(R.id.imgHinh);
        pbUploading = root.findViewById(R.id.pbLoading);
        btnSuaLuu = root.findViewById(R.id.btnSuaLuu);
        btnDangXuat = root.findViewById(R.id.btnDangXuat);
    }

    private void setComponents(DTOKhachHang khachHang) {
        edtTen.setText(khachHang.HoTen);
        edtSdt.setText(khachHang.SoDienThoai);
        edtDiaChi.setText(khachHang.DiaChi);
        if (khachHang.HinhAnh != null)
            Glide.with(context).load(khachHang.HinhAnh).into(imgHinh);
    }

    private void setEvents() {
        btnSuaLuu.setOnClickListener(this);
        btnDangXuat.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btnSuaLuu:
                suaLuu();
                break;
            case R.id.btnDangXuat:
                dangXuat();
                break;
            default:
                break;
        }
    }

    private void suaLuu() {
        if (getString(R.string.user_edit).equals(btnSuaLuu.getText())) {
            btnSuaLuu.setText(R.string.user_save);
            edit();
        }
        else if (getString(R.string.user_save).equals(btnSuaLuu.getText())) {
            btnSuaLuu.setText(R.string.user_edit);
            save();
        }
    }

    private void save() {
        edtTen.setEnabled(false);
        edtSdt.setEnabled(false);
        edtDiaChi.setEnabled(false);

        if (!checkValue()) return;

        khachHang.HoTen = edtTen.getText().toString();
        khachHang.SoDienThoai = edtSdt.getText().toString();
        khachHang.DiaChi = edtDiaChi.getText().toString();

        saveToFireStore(khachHang);
    }
    private void saveToFireStore(DTOKhachHang kh)
    {
        pbUploading.setVisibility(View.VISIBLE);
        db.collection("KhachHang")
            .document(Objects.requireNonNull(auth.getUid()))
            .set(kh)
            .addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    pbUploading.setVisibility(View.INVISIBLE);
                    if (task.isSuccessful())
                        Toast.makeText(context, "Lưu người dùng thành công", Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(context, "Có lỗi khi lưu người dùng", Toast.LENGTH_LONG).show();
                }
            });
    }

    private void edit() {
        edtTen.setEnabled(true);
        edtSdt.setEnabled(true);
        edtDiaChi.setEnabled(true);
    }

    private boolean checkValue() {
        if (edtTen.getText().toString().isEmpty())
        {
            Toast.makeText(context, "Tên không để trống", Toast.LENGTH_LONG).show();
            return false;
        }
        if (edtSdt.getText().toString().isEmpty())
        {
            Toast.makeText(context, "SĐT không để trống", Toast.LENGTH_LONG).show();
            return false;
        }
        if (edtDiaChi.getText().toString().isEmpty())
        {
            Toast.makeText(context, "Địa chỉ không để trống", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private void dangXuat() {
//        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
//        alertDialogBuilder.setPositiveButton("Có",
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        auth.signOut();
//                    }
//                })
//                .setNegativeButton("Không",
//                        new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//
//                            }
//                        })
//                .setTitle("Bạn có muốn đăng xuất");
//
//        AlertDialog alertDialog = alertDialogBuilder.create();
//        alertDialog.show();
        auth.signOut();
    }
}