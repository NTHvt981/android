package com.example.giaodientest.DTO;

import android.os.Parcel;
import android.os.Parcelable;

public class DTOQuanAo implements Parcelable {
    public String Ma;
    public String MaKhachHang;
    public String HinhAnh;
    public String MaAo;
    public String MaQuan;
    public String MaNon;
    public String MaGiay;
    public String MaBalo;
//    tên quần áo bao gồm tên tất cả các phụ kiện thời trang
    public String TenToanBoPKTT;

    public static final String INTENT_TAG = "Quần áo";

    public DTOQuanAo() {}
    public DTOQuanAo(Parcel source) {
        Ma = source.readString();
        MaKhachHang = source.readString();
        HinhAnh = source.readString();
        MaAo = source.readString();
        MaQuan = source.readString();
        MaNon = source.readString();
        MaGiay = source.readString();
        MaBalo = source.readString();
        TenToanBoPKTT = source.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeString(Ma);
        dest.writeString(MaKhachHang);
        dest.writeString(HinhAnh);
        dest.writeString(MaAo);
        dest.writeString(MaQuan);
        dest.writeString(MaNon);
        dest.writeString(MaGiay);
        dest.writeString(MaBalo);
        dest.writeString(TenToanBoPKTT);
    }


    public static final Creator<DTOQuanAo> CREATOR =
            new Creator<DTOQuanAo>() {
                @Override
                public DTOQuanAo[] newArray(int size) {
                    return new DTOQuanAo[size];
                }

                @Override
                public DTOQuanAo createFromParcel(Parcel source) {
                    return new DTOQuanAo(source);
                }
            };
}
