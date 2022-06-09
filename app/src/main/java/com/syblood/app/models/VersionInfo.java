package com.syblood.app.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 版本信息对象
 * Created by xgd on 2016-02-27.
 */
public class VersionInfo implements Parcelable
{
    private String version = "";
    private String versionDate = "";
    private String versionMemo = "";
    private String appUrl = "";
    private String useFlag = "";

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getVersionDate() {
        return versionDate;
    }

    public void setVersionDate(String versionDate) {
        this.versionDate = versionDate;
    }

    public String getVersionMemo() {
        return versionMemo;
    }

    public void setVersionMemo(String versionMemo) {
        this.versionMemo = versionMemo;
    }

    public String getAppUrl() {
        return appUrl;
    }

    public void setAppUrl(String appUrl) {
        this.appUrl = appUrl;
    }

    public String getUseFlag() {
        return useFlag;
    }

    public void setUseFlag(String useFlag) {
        this.useFlag = useFlag;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(this.version);
        parcel.writeString(this.versionDate);
        parcel.writeString(this.versionMemo);
        parcel.writeString(this.appUrl);
        parcel.writeString(this.useFlag);
    }

    public VersionInfo(String version)
    {
        this.version=version;
    }

    public VersionInfo(Parcel in)
    {
        this.version = in.readString();
        this.versionDate = in.readString();
        this.versionMemo = in.readString();
        this.appUrl= in.readString();
        this.useFlag= in.readString();

    }

    public static final Creator<VersionInfo> CREATOR	= new Creator<VersionInfo>()
    {
        public VersionInfo createFromParcel(Parcel in)
        {
            return new VersionInfo(in);
        }

        public VersionInfo[] newArray(int size)
        {
            return new VersionInfo[size];
        }
    };
}
