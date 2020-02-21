package com.ridesharerental.ambasador;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by user65 on 2/16/2018.
 */

public class DataEntity implements Serializable {
    public String id = "";
    public String name = "";
    ArrayList<DataEntity> children = new ArrayList<>();}
