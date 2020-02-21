package com.ridesharerental.pojo;

import java.io.Serializable;

/**
 * Created by user65 on 1/4/2018.
 */

public class Fliter_Bean implements Serializable
{
    String id;

    public boolean isselected() {
        return isselected;
    }

    public void setIsselected(boolean isselected) {
        this.isselected = isselected;
    }

    boolean isselected=false;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    String name;
}
