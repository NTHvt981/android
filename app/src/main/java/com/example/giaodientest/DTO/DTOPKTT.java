package com.example.giaodientest.DTO;

import android.os.Parcel;
import android.os.Parcelable;

public class DTOPKTT implements Parcelable {
    public String Ma;
    public String Ten;
    public String Hinh;
    public long Gia;
    public String Loai;
    public String MoTa;
    public String Mau;
    public String Hang;
    public int SoLuong;

    public static final String PK_NON = "Nón";
    public static final String PK_BALO = "Ba lô";
    public static final String PK_AO = "Áo";
    public static final String PK_QUAN = "Quần";
    public static final String PK_GIAY = "Giày";

    public static final String INTENT_TAG = "Phụ kiện thời trang";

    public DTOPKTT() {}

    public DTOPKTT(Parcel source) {
        Ma = source.readString();
        Ten = source.readString();
        Hinh = source.readString();
        Loai = source.readString();
        Gia = source.readLong();
        Mau = source.readString();
        MoTa = source.readString();
        SoLuong = source.readInt();
        Hang = source.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Ma);
        dest.writeString(Ten);
        dest.writeString(Hinh);
        dest.writeString(Loai);
        dest.writeLong(Gia);
        dest.writeString(Mau);
        dest.writeString(MoTa);
        dest.writeInt(SoLuong);
        dest.writeString(Hang);
    }

    public static final Creator<DTOPKTT> CREATOR =
            new Creator<DTOPKTT>() {
        @Override
        public DTOPKTT[] newArray(int size) {
            return new DTOPKTT[size];
        }

        @Override
        public DTOPKTT createFromParcel(Parcel source) {
            return new DTOPKTT(source);
        }
    };
}
