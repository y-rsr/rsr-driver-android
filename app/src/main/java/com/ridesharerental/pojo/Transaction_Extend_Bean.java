package com.ridesharerental.pojo;

/**
 * Created by user65 on 1/10/2018.
 */

public class Transaction_Extend_Bean
{
    public String getNo_of_days() {
        return no_of_days;
    }

    public void setNo_of_days(String no_of_days) {
        this.no_of_days = no_of_days;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }

    String no_of_days;
    String total_amount;
    String dateAdded;

    public String getLablel() {
        return lablel;
    }

    public void setLablel(String lablel) {
        this.lablel = lablel;
    }

    String lablel;
}
