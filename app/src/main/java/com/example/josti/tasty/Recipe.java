package com.example.josti.tasty;

import java.util.ArrayList;

/**
 * Created by hackerman on 11/04/16.
 */
public class Recipe {

    private String Name;
    private String description;
    private String linkYT;
    private String shared;

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLinkYT() {
        return linkYT;
    }

    public void setLinkYT(String linkYT) {
        this.linkYT = linkYT;
    }

    public String getShared() {
        return shared;
    }

    public void setShared(String shared) {
        this.shared = shared;
    }
}
