package com.example.giaodientest.DTO;


public class DTONguoiDung {
    public String Ma;
    public String Email;
    public VaiTro CoVaiTro;

    public interface VaiTro {
        public boolean KhachHang = true;
        public boolean QuanTri = false;
    }
}
