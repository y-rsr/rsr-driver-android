package com.ridesharerental.pojo;

import java.io.Serializable;

/**
 * Created by user65 on 8/10/2017.
 */

public class Search_Bean implements Serializable
{
    public String getLoc_name() {
        return loc_name;
    }

    public void setLoc_name(String loc_name) {
        this.loc_name = loc_name;
    }

    public String getSel_value() {
        return sel_value;
    }

    public void setSel_value(String sel_value) {
        this.sel_value = sel_value;
    }

    String loc_name;
    String sel_value;
}
