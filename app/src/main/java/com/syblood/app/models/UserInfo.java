package com.syblood.app.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 医生信息
 * <p>
 * Created by xw on 2016-7-30.
 */
public class UserInfo implements Parcelable
{
    /**
     * 用户ID
     */
    private String userId;

    /**
     * 手机号
     */
    private String mobileNo;

    /**
     * 密码
     */
    private String password;

    /**
     * 注册时间：YYYY-MM-DD
     */
    private String registerDate;

    /**
     * 消息推送：1打开，0关闭
     */
    private String cidOn;

    /**
     * 消息推送：1打开，0关闭
     */
    private String onLine;

    /**
     * 姓名
     */
    private String realName;

    /**
     * 头像地址
     */
    private String headPic;

    /**
     * 身份证号
     */
    private String idNumber;

    /**
     * 所属医院代码
     */
    private String hospitalCode;

    /**
     * 所属医院名称
     */
    private String hospitalName;

    /**
     * 所属科室（学科）代码
     */
    private String officeCode;

    /**
     * 所属科室名称
     */
    private String officeName;

    /**
     * 职称
     */
    private String titleCode;

    /**
     * 职称名称
     */
    private String titleName;

    /**
     * 擅长
     */
    private String goodAt;

    /**
     * 简介
     */
    private String introduction;

    /**
     * 医生相册：多个图片|分隔
     */
    private String doctorPic;

    protected UserInfo(Parcel in) {
        userId = in.readString();
        mobileNo = in.readString();
        password = in.readString();
        registerDate = in.readString();
        cidOn = in.readString();
        onLine = in.readString();
        realName = in.readString();
        headPic = in.readString();
        idNumber = in.readString();
        hospitalCode = in.readString();
        hospitalName = in.readString();
        officeCode = in.readString();
        officeName = in.readString();
        titleCode = in.readString();
        titleName = in.readString();
        goodAt = in.readString();
        introduction = in.readString();
        doctorPic = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userId);
        dest.writeString(mobileNo);
        dest.writeString(password);
        dest.writeString(registerDate);
        dest.writeString(cidOn);
        dest.writeString(onLine);
        dest.writeString(realName);
        dest.writeString(headPic);
        dest.writeString(idNumber);
        dest.writeString(hospitalCode);
        dest.writeString(hospitalName);
        dest.writeString(officeCode);
        dest.writeString(officeName);
        dest.writeString(titleCode);
        dest.writeString(titleName);
        dest.writeString(goodAt);
        dest.writeString(introduction);
        dest.writeString(doctorPic);

    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<UserInfo> CREATOR = new Creator<UserInfo>() {
        @Override
        public UserInfo createFromParcel(Parcel in) {
            return new UserInfo(in);
        }

        @Override
        public UserInfo[] newArray(int size) {
            return new UserInfo[size];
        }
    };


    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String doctorId)
    {
        this.userId = doctorId;
    }

    public String getMobileNo()
    {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo)
    {
        this.mobileNo = mobileNo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRegisterDate()
    {
        return registerDate;
    }

    public void setRegisterDate(String registerDate)
    {
        this.registerDate = registerDate;
    }

    public String getCidOn()
    {
        return cidOn;
    }

    public void setCidOn(String cidOn)
    {
        this.cidOn = cidOn;
    }

    public String getOnLine()
    {
        return onLine;
    }

    public void setOnLine(String onLine)
    {
        this.onLine = onLine;
    }

    public String getRealName()
    {
        return realName;
    }

    public void setRealName(String realName)
    {
        this.realName = realName;
    }

    public String getHeadPic()
    {
        return headPic;
    }

    public void setHeadPic(String headPic)
    {
        this.headPic = headPic;
    }

    public String getIdNumber()
    {
        return idNumber;
    }

    public void setIdNumber(String idNumber)
    {
        this.idNumber = idNumber;
    }

    public String getHospitalCode()
    {
        return hospitalCode;
    }

    public void setHospitalCode(String hospitalCode)
    {
        this.hospitalCode = hospitalCode;
    }

    public String getHospitalName()
    {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName)
    {
        this.hospitalName = hospitalName;
    }

    public String getOfficeCode()
    {
        return officeCode;
    }

    public void setOfficeCode(String officeCode)
    {
        this.officeCode = officeCode;
    }

    public String getOfficeName()
    {
        return officeName;
    }

    public void setOfficeName(String officeName)
    {
        this.officeName = officeName;
    }

    public String getTitleCode()
    {
        return titleCode;
    }

    public void setTitleCode(String titleCode)
    {
        this.titleCode = titleCode;
    }

    public String getTitleName()
    {
        return titleName;
    }

    public void setTitleName(String titleName)
    {
        this.titleName = titleName;
    }

    public String getGoodAt()
    {
        return goodAt;
    }

    public void setGoodAt(String goodAt)
    {
        this.goodAt = goodAt;
    }

    public String getIntroduction()
    {
        return introduction;
    }

    public void setIntroduction(String introduction)
    {
        this.introduction = introduction;
    }

    public String getDoctorPic()
    {
        return doctorPic;
    }

    public void setDoctorPic(String doctorPic)
    {
        this.doctorPic = doctorPic;
    }

    public UserInfo()
    {

    }
}
