package com.hx.lib_hx.xls;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;

public class DevBean implements Parcelable{
    private HashMap<String,String> map;
    private String objId;

    public DevBean() {
        map = new HashMap<>();
    }

    protected DevBean(Parcel in) {
        objId = in.readString();
    }

    public static final Creator<DevBean> CREATOR = new Creator<DevBean>() {
        @Override
        public DevBean createFromParcel(Parcel in) {
            return new DevBean(in);
        }

        @Override
        public DevBean[] newArray(int size) {
            return new DevBean[size];
        }
    };

    public String getObjId() {
        return objId;
    }

    public void setObjId(String objId) {
        this.objId = objId;
    }

    public void put(String key, String value) {
        map.put(key, value);
    }

    public HashMap<String, String> getMap() {
        return map;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(objId);
    }
}
