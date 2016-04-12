package com.example.josti.tasty;

import java.util.ArrayList;

/**
 * Created by hackerman on 11/04/16.
 */
public class RecipeForShow {

    private String Name;
    private String Description;
    private String Shared;
    private String link;
    private ArrayList<String> steps;
    private ArrayList<String> ingredients;

    /*
        ArrayList pasos = new ArrayList<String>();
        ArrayList Ingredientes = new ArrayList<String>();

        pasos.add("paso1");
        pasos.add("paso2");
        pasos.add("paso3");
        pasos.add("paso4");
        Ingredientes.add("ing1");
        Ingredientes.add("ing2");
        Ingredientes.add("ing3");
        Ingredientes.add("ing4");

    * RecipeForShow receta = RecipeForShow("Nombre de la receta", "descripcion de la receta", "23", "0M9cypF0Pyk", pasos, Ingredientes);
    * */

    public RecipeForShow(String name, String description, String shared, String link, ArrayList<String> steps, ArrayList<String> ingredients) {
        Name = name;
        Description = description;
        Shared = shared;
        this.link = "https://www.youtube.com/watch?v=" + link;
        this.steps = steps;
        this.ingredients = ingredients;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

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
