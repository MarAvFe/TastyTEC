package com.example.josti.tasty;

import java.util.ArrayList;

/**
 * Created by hackerman on 11/04/16.
 */
public class ShowRecipe {

    private String Name;
    private String Description;
    private String Shared;
    private String link;
    private ArrayList<String> steps;
    private ArrayList<String> ingredients;

    public ShowRecipe(String name, String description, String shared, String link, ArrayList<String> steps, ArrayList<String> ingredients) {
        Name = name;
        Description = description;
        Shared = shared;
        this.link = link;
        this.steps = steps;
        this.ingredients = ingredients;
    }

    @Override
    public String toString() {
        return "ShowRecipe{" +
                "Name='" + Name + '\'' +
                ", Description='" + Description + '\'' +
                ", Shared='" + Shared + '\'' +
                ", link='" + link + '\'' +
                ", steps=" + steps +
                ", ingredients=" + ingredients +
                '}';
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

   /* for(String i : getSteps()){

        resulatdo += s;
    }*/


    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getShared() {
        return Shared;
    }

    public void setShared(String shared) {
        Shared = shared;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public ArrayList<String> getSteps() {
        return steps;
    }

    public void setSteps(ArrayList<String> steps) {
        this.steps = steps;
    }

    public ArrayList<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<String> ingredients) {
        this.ingredients = ingredients;
    }
}
