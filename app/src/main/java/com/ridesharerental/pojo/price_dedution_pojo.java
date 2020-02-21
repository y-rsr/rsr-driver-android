package com.ridesharerental.pojo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by user129 on 5/30/2018.
 */
public class price_dedution_pojo implements Parcelable {


    String id;
    String price_per_day;
    String payable_amount;
    String text;
    String selectflag;

    public String getSelectflag() {
        return selectflag;
    }

    public void setSelectflag(String selectflag) {
        this.selectflag = selectflag;
    }




    public price_dedution_pojo() {

        id = "";
        price_per_day = "";
        payable_amount = "";
        text = "";
        selectflag="";

    }

    public price_dedution_pojo(Parcel in) {

        id = in.readString();
        price_per_day = in.readString();
        payable_amount = in.readString();
        text = in.readString();
        selectflag = in.readString();

    }


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPrice_per_day() {
        return price_per_day;
    }

    public void setPrice_per_day(String price_per_day) {
        this.price_per_day = price_per_day;
    }

    public String getPayable_amount() {
        return payable_amount;
    }

    public void setPayable_amount(String payable_amount) {
        this.payable_amount = payable_amount;
    }


    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(price_per_day);
        dest.writeString(payable_amount);
        dest.writeString(text);
        dest.writeString(selectflag);

    }


    public static final Parcelable.Creator<price_dedution_pojo> CREATOR = new Parcelable.Creator<price_dedution_pojo>() {
        public price_dedution_pojo createFromParcel(Parcel in) {
            return new price_dedution_pojo(in);
        }

        public price_dedution_pojo[] newArray(int size) {
            return new price_dedution_pojo[size];
        }
    };


}
