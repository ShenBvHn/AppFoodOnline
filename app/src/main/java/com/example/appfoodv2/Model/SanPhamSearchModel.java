package com.example.appfoodv2.Model;

import android.annotation.SuppressLint;
import android.os.Parcel;

import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;

@SuppressLint("ParcelCreator")
public class SanPhamSearchModel implements SearchSuggestion {
    private String id;

    private String idsp;
    private String tensp;
    private long giatien;
    private String hinhanh;
    private String loaisp;
    private String mota;
    private long soluong;
    private String hansudung;
    private long type;
    private String trongluong;
    private boolean mIsHistory = false;


    public boolean isIsHistory() {
        return mIsHistory;
    }

    public void setIsHistory(boolean mIsHistory) {
        this.mIsHistory = mIsHistory;
    }
    public SanPhamSearchModel() {

    }

    public SanPhamSearchModel(String id, String id_sp, String tensp, long giatien, String hinhanh, String loaisp, long soluong, String hansudung, long type, String trongluong) {
        this.id = id;
        this.tensp = tensp;
        this.giatien = giatien;
        this.hinhanh = hinhanh;
        this.loaisp = loaisp;
        this.soluong = soluong;
        this.hansudung = hansudung;
        this.type = type;
        this.trongluong = trongluong;
        this.idsp = id_sp;
    }


    public SanPhamSearchModel(String id, String tensp, long giatien, String hinhanh, String loaisp, String mota, long soluong, String hansudung,
                         long type, String trongluong) {
        this.id = id;
        this.tensp = tensp;
        this.giatien = giatien;
        this.hinhanh = hinhanh;
        this.loaisp = loaisp;
        this.mota = mota;
        this.soluong = soluong;
        this.hansudung = hansudung;
        this.type = type;
        this.trongluong = trongluong;
    }

    public void setType(long type) {
        this.type = type;
    }

    public void setTrongluong(String trongluong) {
        this.trongluong = trongluong;
    }

    public String getIdsp() {
        return idsp;
    }

    public long getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTensp() {
        return tensp;
    }

    public void setTensp(String tensp) {
        this.tensp = tensp;
    }

    public long getGiatien() {
        return giatien;
    }

    public void setGiatien(long giatien) {
        this.giatien = giatien;
    }

    public String getHinhanh() {
        return hinhanh;
    }

    public void setHinhanh(String hinhanh) {
        this.hinhanh = hinhanh;
    }

    public String getTrongluong() {
        return trongluong;
    }

    public String getLoaisp() {
        return loaisp;
    }

    public void setLoaisp(String loaisp) {
        this.loaisp = loaisp;
    }

    public String getMota() {
        return mota;
    }

    public void setMota(String mota) {
        this.mota = mota;
    }

    public long getSoluong() {
        return soluong;
    }

    public void setSoluong(long soluong) {
        this.soluong = soluong;
    }

    public String getHansudung() {
        return hansudung;
    }

    public void setHansudung(String hansudung) {
        this.hansudung = hansudung;
    }


    @Override
    public String getBody() {
        return tensp;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(idsp);
        dest.writeString(tensp);
        dest.writeLong(giatien);
        dest.writeInt(mIsHistory ? 1 : 0);
    }
}
