package com.example.giaodientest.DTO;

import android.os.Parcel;
import android.os.Parcelable;

public class DTODonHang implements Parcelable {
    public String Ma;
    public String TenKhachHang;
    public String SoDienThoai;
    public String DiaChi;
    public String MaKhachHang;
    public String ThoiGianDatHang;
    public String TinhTrang;
    public long TongCong;

    public static final String DA_HUY = "Đã huỷ";
    public static final String DA_DAT = "Đặt hàng thành công";
    public static final String DA_TIEP_NHAN = "Đã tiếp nhận đơn hàng";
    public static final String DANG_VAN_CHUYEN = "Đang vận chuyển";
    public static final String GIAO_THANH_CONG = "Giao hàng thành công";

    public DTODonHang() {}
    public DTODonHang(Parcel source) {
        Ma = source.readString();
        TenKhachHang = source.readString();
        SoDienThoai = source.readString();
        DiaChi = source.readString();
        MaKhachHang = source.readString();
        ThoiGianDatHang = source.readString();
        TinhTrang = source.readString();
        TongCong = source.readLong();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Ma);
        dest.writeString(TenKhachHang);
        dest.writeString(SoDienThoai);
        dest.writeString(DiaChi);
        dest.writeString(MaKhachHang);
        dest.writeString(ThoiGianDatHang);
        dest.writeString(TinhTrang);
        dest.writeLong(TongCong);
    }

    public static final Creator<DTODonHang> CREATOR =
            new Creator<DTODonHang>() {
                @Override
                public DTODonHang[] newArray(int size) {
                    return new DTODonHang[size];
                }

                @Override
                public DTODonHang createFromParcel(Parcel source) {
                    return new DTODonHang(source);
                }
            };
}
