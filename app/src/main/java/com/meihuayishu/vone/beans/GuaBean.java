package com.meihuayishu.vone.beans;

import android.os.Parcel;
import android.os.Parcelable;

/**
 *
 */
public class GuaBean implements Parcelable {

    private int shang ;
    private int xia ;
    private int dong;

    public GuaBean(int shang, int xia, int dong) {
        this.shang = shang;
        this.xia = xia;
        this.dong = dong;
    }

    public GuaBean() {

    }

    public void setShang(int shang) {
        this.shang = shang;
    }

    public void setXia(int xia) {
        this.xia = xia;
    }

    public void setDong(int dong) {
        this.dong = dong;
    }

    public int getShang() {
        return shang;
    }

    public int getXia() {
        return xia;
    }

    public int getDong() {
        return dong;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(shang);
        dest.writeInt(xia);
        dest.writeInt(dong);
    }

    public static final Parcelable.Creator<GuaBean> CREATOR = new Creator<GuaBean>() {
        @Override
        public GuaBean createFromParcel(Parcel source) {
            GuaBean guaBean = new GuaBean();
            guaBean.setShang(source.readInt());
            guaBean.setXia(source.readInt());
            guaBean.setDong(source.readInt());

            return guaBean;
        }

        @Override
        public GuaBean[] newArray(int size) {
            return new GuaBean[size];
        }
    };

    @Override
    public String toString() {
        return "GuaBean{" +
                "shang=" + shang +
                ", xia=" + xia +
                ", dong=" + dong +
                '}';
    }
}
