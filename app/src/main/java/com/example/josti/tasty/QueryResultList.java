package com.example.josti.tasty;

import java.util.ArrayList;
/**
 * Created by hackerman on 11/04/16.
 */
public class QueryResultList {

    private ArrayList<String> names;
    private ArrayList<String> descriptions;
    private ArrayList<String> links;

    public ArrayList<String> getNames() {
        return names;
    }

    public void setNames(ArrayList<String> names) {
        this.names = names;
    }

    public ArrayList<String> getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(ArrayList<String> descriptions) {
        this.descriptions = descriptions;
    }

    public ArrayList<String> getLinks() {
        return links;
    }

    public void setLinks(ArrayList<String> links) {
        this.links = links;
    }
}
