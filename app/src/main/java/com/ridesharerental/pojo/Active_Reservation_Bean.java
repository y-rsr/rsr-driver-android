package com.ridesharerental.pojo;

import org.json.JSONArray;

/**
 * Created by user65 on 1/9/2018.
 */

public class Active_Reservation_Bean
{
    public JSONArray getDocuments() {
        return documents;
    }

    public void setDocuments(JSONArray documents) {
        this.documents = documents;
    }

    public JSONArray documents;

    public JSONArray getBooking_documents() {
        return booking_documents;
    }

    public void setBooking_documents(JSONArray booking_documents) {
        this.booking_documents = booking_documents;
    }

    public JSONArray booking_documents;
    public String getCarId() {
        return carId;
    }

    public void setCarId(String carId) {
        this.carId = carId;
    }

    public String getPlat_no() {
        return plat_no;
    }

    public void setPlat_no(String plat_no) {
        this.plat_no = plat_no;
    }

    String carId;

    public String getCar_make() {
        return car_make;
    }

    public void setCar_make(String car_make) {
        this.car_make = car_make;
    }

    public String getCar_model() {
        return car_model;
    }

    public void setCar_model(String car_model) {
        this.car_model = car_model;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    String car_make;
    String car_model;
    String year;
    String status;
    String profile_pic;

    public String getExtended_date() {
        return extended_date;
    }

    public void setExtended_date(String extended_date) {
        this.extended_date = extended_date;
    }

    String extended_date;

    public String getV_no() {
        return v_no;
    }

    public void setV_no(String v_no) {
        this.v_no = v_no;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    String v_no;
    String notes;

    public String getExtended() {
        return extended;
    }

    public void setExtended(String extended) {
        this.extended = extended;
    }

    String extended;

    public String getAllow_extend() {
        return allow_extend;
    }

    public void setAllow_extend(String allow_extend) {
        this.allow_extend = allow_extend;
    }

    String allow_extend;
    public String getBooking_no() {
        return booking_no;
    }

    public void setBooking_no(String booking_no) {
        this.booking_no = booking_no;
    }

    String booking_no;
    public String getNo_of_days() {
        return no_of_days;
    }

    public void setNo_of_days(String no_of_days) {
        this.no_of_days = no_of_days;
    }

    String no_of_days;

    public String getVin_no() {
        return vin_no;
    }

    public void setVin_no(String vin_no) {
        this.vin_no = vin_no;
    }

    String vin_no;
    String plat_no;

    public String getOwnername() {
        return ownername;
    }

    public void setOwnername(String ownername) {
        this.ownername = ownername;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    String ownername;
    String street;

    public String getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }

    public String getDate_from() {
        return date_from;
    }

    public void setDate_from(String date_from) {
        this.date_from = date_from;
    }

    public String getDate_to() {
        return date_to;
    }

    public void setDate_to(String date_to) {
        this.date_to = date_to;
    }

    String date_from;
    String date_to;

    public String getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }

    String total_amount;

    public String getTimer_date() {
        return timer_date;
    }

    public void setTimer_date(String timer_date) {
        this.timer_date = timer_date;
    }

    String timer_date;

    public String getCar_image() {
        return car_image;
    }

    public void setCar_image(String car_image) {
        this.car_image = car_image;
    }

    String car_image;

    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

    String progress;

    public String getStr_timer_counting() {
        return str_timer_counting;
    }

    public void setStr_timer_counting(String str_timer_counting) {
        this.str_timer_counting = str_timer_counting;
    }

    String str_timer_counting;
}
