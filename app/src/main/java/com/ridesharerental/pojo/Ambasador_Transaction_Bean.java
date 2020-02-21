package com.ridesharerental.pojo;

/**
 * Created by user65 on 2/20/2018.
 */

public class Ambasador_Transaction_Bean
{

    String id;
    String rank;
    String level;
    String commission_percent;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getCommission_percent() {
        return commission_percent;
    }

    public void setCommission_percent(String commission_percent) {
        this.commission_percent = commission_percent;
    }

    public String getCommission() {
        return commission;
    }

    public void setCommission(String commission) {
        this.commission = commission;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDrivername() {
        return drivername;
    }

    public void setDrivername(String drivername) {
        this.drivername = drivername;
    }

    String commission;
    String type;
    String dateAdded;
    String status;
    String drivername;
}
