package com.ridesharerental.ambasador.holder;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by user65 on 2/16/2018.
 */

public interface TreeElementI extends Serializable
{
    public void addChild(TreeElementI child);
    public String getId();
    public void setId(String id);
    public String getOutlineTitle();
    public void setOutlineTitle(String outlineTitle);
    public boolean isHasParent();
    public void setHasParent(boolean hasParent);
    public boolean isHasChild();
    public void setHasChild(boolean hasChild);
    public int getLevel();
    public void setLevel(int level);
    public boolean isExpanded();
    public void setExpanded(boolean expanded);
    public ArrayList<TreeElementI> getChildList();
    public TreeElementI getParent();
    public void setParent(TreeElementI parent);

}
