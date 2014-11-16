package edu.usf.EventExpress;

/**
 * Created by Varik on 11/11/2014.
 */
public class Friend {
    String name = null;
    Long userID = null;
    boolean selected = false;

    public Friend(Long userID, String name, boolean selected){
        super();
        this.userID = userID;
        this.name = name;
        this.selected = selected;
    }

    public Long getUserID(){
        return userID;
    }

    public String getName(){
        return name;
    }

    public boolean isSelected(){
        return selected;
    }

    public void setSelected(boolean selected){
        this.selected = selected;
    }
}
