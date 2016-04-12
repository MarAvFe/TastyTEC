package com.example.josti.tasty;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;

public class ActivityRecipe extends YouTubeBaseActivity{

    private YouTubePlayerView youTubePlayerView;
    private YouTubePlayer.OnInitializedListener onInitializedListener;
    private String compartirUrl;
    private String listIngredients;
    private String listSteps;
    private String recipeName;
    private QueryRecipe receta;
    private ArrayList pasos, ingredientes;
    private TextView tvSteps, tvIngredients, tituloReceta;
    private ImageButton btnCompartir;
    private String hostIp = MainActivity.getHostIp();


    private ShowRecipe fullRecipe ;
    private static ArrayList<String> parames = new ArrayList<String>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Bundle extras = getIntent().getExtras();
        TextView title = (TextView) findViewById(R.id.recipeTitle);
        recipeName = extras.getString("RecipeName");
        title.setText(recipeName);

        Log.v("Recipe NAME", recipeName);
        // Cada una de estas es una petición de información al WS
        new HttpRequestTaskRecipe().execute();
        new HttpRequestTaskIngredientes().execute();
        new HttpRequestTaskSteps().execute();

        tvSteps = (TextView)findViewById(R.id.preparation);
        tvIngredients = (TextView)findViewById(R.id.ingredientes);
        btnCompartir = (ImageButton)findViewById(R.id.btnCompartir);
        tituloReceta = (TextView)findViewById(R.id.recipeTitle);

    }

    /*
    Las acciones del constructor que requieran de la consulta de datos del WS
    se pasan a este hilo queriesReady, para agruparlas y asegurarse que la
    llamada a tales acciones se haga una vez que esté toda la información
    consultada y no a medio palo para evitar inconsistencias
     */
    private void queriesReady(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                fullRecipe = new ShowRecipe(
                        receta.getName(),
                        receta.getDescription(),
                        receta.getShared(),
                        receta.getLinkYT(),
                        ingredientes,
                        pasos
                );
                Log.v("RECETAENLISTADA", fullRecipe.toString());
                listSteps = retornarPasos(fullRecipe.getSteps());
                listIngredients = retornarIngredientes(fullRecipe.getIngredients());
                tituloReceta.setText(fullRecipe.getName());
                tvIngredients.setText(listIngredients);
                tvSteps.setText(listSteps);
                compartirUrl="https://www.youtube.com/watch?v=" + fullRecipe.getLink();
                btnCompartir.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(Intent.ACTION_SEND);
                        i.setType("text/plain");
                        i.putExtra(Intent.EXTRA_SUBJECT, "Sharing URL");
                        i.putExtra(Intent.EXTRA_TEXT, compartirUrl);
                        startActivity(Intent.createChooser(i, "Share URL"));
                    }
                });
                youTubePlayerView = (YouTubePlayerView) findViewById(R.id.view_player);
                onInitializedListener = new YouTubePlayer.OnInitializedListener() {
                    @Override
                    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                        youTubePlayer.cueVideo(fullRecipe.getLink());
                        Log.v("VideoURL", fullRecipe.getLink());
                    }

                    @Override
                    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

                    }
                };

                youTubePlayerView.initialize(Config.getAPIkey,onInitializedListener);
            }
        });
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

    public ShowRecipe getReceta() {
        return fullRecipe;
    }

    public void setReceta(ShowRecipe receta) {
        this.fullRecipe = receta;
    }

    private String getParams4WS(){
        String resultado = "";
        for (String s : parames)
            resultado += s;
        return resultado;
    }

    /* ****** RECIPE ****** */
    private class HttpRequestTaskRecipe extends AsyncTask<Void, Void, QueryRecipe> {
        @Override
        protected QueryRecipe doInBackground(Void... params) {
            try {
                parames = new ArrayList<String>();
                parames.add("getRecipe?nombre=" + recipeName);
                final String url = "http://" + hostIp + ":5003/" + getParams4WS();
                Log.v("ADRR", url);
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                Log.v("HttpReq","Getting vals from RecipeWS...");
                QueryRecipe retorno = restTemplate.getForObject(url, QueryRecipe.class);
                Log.v("HttpReq","Got vals from WS");
                return retorno;
            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage(), e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(QueryRecipe retorno) {
            receta = new QueryRecipe();
            receta.setName(retorno.getName());
            receta.setDescription(retorno.getDescription());
            receta.setLinkYT(retorno.getLinkYT());
            receta.setShared(retorno.getShared());
        }
    }

    /* ****** INGREDIENTES ****** */
    private class HttpRequestTaskIngredientes extends AsyncTask<Void, Void, QueryIngredientes> {
        @Override
        protected QueryIngredientes doInBackground(Void... params) {
            try {
                parames = new ArrayList<String>();
                parames.add("getIngredients?nombre=" + recipeName);
                final String url = "http://" + hostIp + ":5003/" + getParams4WS();
                Log.v("ADRR", url);
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                Log.v("HttpReq","Getting vals from IngreWS...");
                QueryIngredientes retorno = restTemplate.getForObject(url, QueryIngredientes.class);
                Log.v("HttpReq","Got vals from WS");
                Log.v("HttpReq","Got:" + retorno.getLista().toString());
                return retorno;
            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage(), e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(QueryIngredientes retorno) {
            ingredientes = new ArrayList<String>();
            ingredientes = retorno.getLista();
        }
    }

    /* ****** STEPS ****** */
    private class HttpRequestTaskSteps extends AsyncTask<Void, Void, QuerySteps> {
        @Override
        protected QuerySteps doInBackground(Void... params) {
            try {
                parames = new ArrayList<String>();
                parames.add("getSteps?nombre=" + recipeName);
                final String url = "http://" + hostIp + ":5003/" + getParams4WS();
                Log.v("ADRR", url);
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                Log.v("HttpReq","Getting vals from StepsWS...");
                QuerySteps retorno = restTemplate.getForObject(url, QuerySteps.class);
                Log.v("HttpReq","Got vals from WS");
                return retorno;
            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage(), e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(QuerySteps retorno) {
            pasos = new ArrayList<String>();
            ArrayList<String> Apasos = new ArrayList<String>();
            for(int i = 0; i < retorno.getPasos().size(); i++){
                Apasos.add(retorno.getNumPaso().get(i) + " " + retorno.getPasos().get(i));
            }
            Log.v("Paso",Apasos.toString());
            pasos = Apasos;
            queriesReady();
        }
    }


}
