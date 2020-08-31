package com.example.giaodientest;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class DialogXacNhan extends AppCompatDialogFragment {
    private String Ten;
    private String SoDienThoai;
    private String DiaChi;

    private TextView tvTen;
    private TextView tvSdt;
    private TextView tvDiaChi;

    String tvTextTen = "Người đặt hàng: ";
    String tvTextSdt = "Số điện thoại: ";
    String tvTextDiaChi = "Địa chỉ: ";

    private DialogXacNhanListener listener;

    public DialogXacNhan(String ten, String sdt, String diaChi)
    {
        Ten = ten;
        SoDienThoai = sdt;
        DiaChi = diaChi;
    }

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.layout_dialog_xac_nhan, null);


        //set component reference
        tvTen = view.findViewById(R.id.tvTen);
        tvSdt = view.findViewById(R.id.tvSdt);
        tvDiaChi = view.findViewById(R.id.tvDiaChi);

        //set value in textview
        tvTen.setText(tvTextTen + this.Ten);
        tvSdt.setText(tvTextSdt + this.SoDienThoai);
        tvDiaChi.setText(tvTextDiaChi + this.DiaChi);



        builder.setView(view).setTitle("Xác nhận")
                .setNegativeButton("Huỷ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.chapNhan();
                    }
                });

        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (DialogXacNhanListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement context listener");
        }
    }

    public interface DialogXacNhanListener
    {
        void chapNhan();
    }
}
