package com.example.josti.tasty;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import java.util.ArrayList;

public class Main2Activity extends YouTubeBaseActivity{

    private YouTubePlayerView youTubePlayerView;
    private YouTubePlayer.OnInitializedListener onInitializedListener;
    String compartirUrl;
    String listIngredients;
    String listSteps;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Bundle extras = getIntent().getExtras();
        TextView title = (TextView) findViewById(R.id.recipeTitle);
        title.setText(extras.getString("RecipeName"));

        compartirUrl="http://www.youtube.com";


        ArrayList pasos = new ArrayList<String>();
        ArrayList ingredientes = new ArrayList<String>();



        pasos.add("paso1");
        pasos.add("paso2");
        pasos.add("paso3");
        pasos.add("paso4");
        ingredientes.add("ing1");
        ingredientes.add("ing2");
        ingredientes.add("ing3");
        ingredientes.add("ing4");
        ingredientes.add("ing1");
        ingredientes.add("ing2");
        ingredientes.add("ing3");
        ingredientes.add("ing4");
        ingredientes.add("ing1");
        ingredientes.add("ing2");
        ingredientes.add("ing3");
        ingredientes.add("ing4");
        ingredientes.add("ing1");
        ingredientes.add("ing2");
        ingredientes.add("ing3");
        ingredientes.add("ing4");

     RecipeForShow receta = new RecipeForShow("Nombre de la receta", "descripcion de la receta", "23", "0M9cypF0Pyk", pasos, ingredientes);

        listIngredients = retornarIngredientes(ingredientes);
        listSteps = retornarPasos(pasos);

        TextView tituloReceta = (TextView)findViewById(R.id.recipeTitle);
        ImageButton btnCompartir;

        tituloReceta.setText(receta.getName());
        btnCompartir = (ImageButton)findViewById(R.id.btnCompartir);

       TextView tvIngredients;
        tvIngredients = (TextView)findViewById(R.id.ingredientes);
        tvIngredients.setText(listIngredients);

        TextView tvSteps;
        tvSteps = (TextView)findViewById(R.id.preparation);
        tvSteps.setText(listSteps);



        btnCompartir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, "Sharing URL");
                i.putExtra(Intent.EXTRA_TEXT,compartirUrl);
                startActivity(Intent.createChooser(i, "Share URL"));

            }
        });

        youTubePlayerView = (YouTubePlayerView) findViewById(R.id.view_player);
        onInitializedListener = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                youTubePlayer.cueVideo("2eXCKFMzg2w");
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        };

        youTubePlayerView.initialize(Config.getAPIkey,onInitializedListener);


    }

    public String retornarIngredientes(ArrayList<String> arreglo) {
        String res="";
        for(String s : arreglo){
            res+="-"+s+"\n";
        }
        return res;
    }

    public String retornarPasos(ArrayList<String> arreglo) {
        String res="";
        for(String s : arreglo){
            res+=s+"\n\n";
        }
        return res;
    }
}
