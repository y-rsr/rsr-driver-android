package com.ridesharerental.pojo;

import org.json.JSONArray;

/**
 * Created by user65 on 1/10/2018.
 */

public class My_Transaction_Bean
{
    String booking_no;
    String no_of_days;
    String total_amount;

    public String getBooking_no() {
        return booking_no;
    }

    public void setBooking_no(String booking_no) {
        this.booking_no = booking_no;
    }

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

    public String getExtended() {
        return extended;
    }

    public void setExtended(String extended) {
        this.extended = extended;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }

    public String getRental_cost() {
        return rental_cost;
    }

    public void setRental_cost(String rental_cost) {
        this.rental_cost = rental_cost;
    }

    public String getCarInfo() {
        return carInfo;
    }

    public void setCarInfo(String carInfo) {
        this.carInfo = carInfo;
    }

    public JSONArray getExtendDetails() {
        return extendDetails;
    }

    public void setExtendDetails(JSONArray extendDetails) {
        this.extendDetails = extendDetails;
    }

    String extended;
    String dateAdded;
    String rental_cost;
    String carInfo;
    JSONArray extendDetails;
}
