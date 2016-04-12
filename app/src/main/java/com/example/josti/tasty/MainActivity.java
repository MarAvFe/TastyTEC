package com.example.josti.tasty;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.josti.tasty.Ingredientes;

import java.util.ArrayList;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;


public class MainActivity extends AppCompatActivity implements OnClickListener, NavigationView.OnNavigationItemSelectedListener {
        Button buttonMore;

    private String hostIp = "192.168.10.108";
    private ArrayList<String> params = new ArrayList<String>();
    //private enum returnType{//RECIPE: 0, RESULTADOS: 1};
    private int returnTypeSelected = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        buttonMore = (Button) findViewById(R.id.buttonMore);
        buttonMore.setOnClickListener(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new HttpRequestTaskSteps().execute();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //params.add("androidTest");
        //params.add("getRecipe?nombre=Recipe 2");
        //params.add("getIngredients?nombre=Recipe 2");
        params.add("getSteps?nombre=Recipe 2");
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.buttonMore:

                Intent intent1 = new Intent(MainActivity.this,Main2Activity.class);
                startActivity(intent1);
                break;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.content_main, container, false);
            return rootView;
        }
    }


    /* ****** RECIPE ****** */
    private class HttpRequestTaskRecipe extends AsyncTask<Void, Void, Recipe> {
        @Override
        protected Recipe doInBackground(Void... params) {
            try {

                final String url = "http://" + hostIp + ":5003/" + getParams4WS();
                Log.v("ADRR", url);
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                Log.v("HttpReq","Getting vals from WS...");
                Recipe retorno = restTemplate.getForObject(url, Recipe.class);
                Log.v("HttpReq","Got vals from WS");
                return retorno;
            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage(), e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Recipe retorno) {
            TextView wsReturnIdText = (TextView) findViewById(R.id.val1);
            TextView wsReturnContentText = (TextView) findViewById(R.id.val2);
            wsReturnIdText.setText(retorno.getName() + " " + retorno.getDescription());
            wsReturnContentText.setText(retorno.getLinkYT() + " " + retorno.getShared());
        }
    }

    /* ****** INGREDIENTES ****** */
    private class HttpRequestTaskIngredientes extends AsyncTask<Void, Void, Ingredientes> {
        @Override
        protected Ingredientes doInBackground(Void... params) {
            try {
                final String url = "http://" + hostIp + ":5003/" + getParams4WS();
                Log.v("ADRR", url);
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                Log.v("HttpReq","Getting vals from WS...");
                Ingredientes retorno = restTemplate.getForObject(url, Ingredientes.class);
                Log.v("HttpReq","Got vals from WS");
                return retorno;
            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage(), e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Ingredientes retorno) {
            TextView wsReturnIdText = (TextView) findViewById(R.id.val1);
            TextView wsReturnContentText = (TextView) findViewById(R.id.val2);
            wsReturnIdText.setText(retorno.getLista().get(2));
        }
    }

    /* ****** STEPS ****** */
    private class HttpRequestTaskSteps extends AsyncTask<Void, Void, Steps> {
        @Override
        protected Steps doInBackground(Void... params) {
            try {
                final String url = "http://" + hostIp + ":5003/" + getParams4WS();
                Log.v("ADRR", url);
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                Log.v("HttpReq","Getting vals from WS...");
                Steps retorno = restTemplate.getForObject(url, Steps.class);
                Log.v("HttpReq","Got vals from WS");
                return retorno;
            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage(), e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Steps retorno) {
            TextView wsReturnIdText = (TextView) findViewById(R.id.val1);
            TextView wsReturnContentText = (TextView) findViewById(R.id.val2);
            wsReturnIdText.setText(retorno.getPasos().get(2));
            wsReturnContentText.setText(retorno.getNumPaso().get(2));
        }
    }

    /* ****** RESULTADOS ****** */
    private class HttpRequestTaskResultados extends AsyncTask<Void, Void, Resultados> {
        @Override
        protected Resultados doInBackground(Void... params) {
            try {
                final String url = "http://" + hostIp + ":5003/" + getParams4WS();
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                Resultados retorno = restTemplate.getForObject(url, Resultados.class);
                Log.v("HttpReq","Got vals from WS");
                return retorno;
            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage(), e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Resultados retorno) {
            TextView wsReturnIdText = (TextView) findViewById(R.id.val1);
            TextView wsReturnContentText = (TextView) findViewById(R.id.val2);
            wsReturnIdText.setText(":V");
            wsReturnContentText.setText(":v");
        }
    }

    /* ****** wsCONSUME ****** */
    private class HttpRequestTaskWsConsume extends AsyncTask<Void, Void, wsConsume> {
        @Override
        protected wsConsume doInBackground(Void... params) {
            try {
                final String url = "http://" + hostIp + ":5003/" + getParams4WS();
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                wsConsume retorno = restTemplate.getForObject(url, wsConsume.class);
                Log.v("HttpReq","Got vals from WS");
                return retorno;
            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage(), e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(wsConsume retorno) {
            TextView wsReturnIdText = (TextView) findViewById(R.id.val1);
            TextView wsReturnContentText = (TextView) findViewById(R.id.val2);
            wsReturnIdText.setText(":V");
            wsReturnContentText.setText(":v");
        }
    }

    public String getParams4WS(){
        String resultado = "";
        for (String s : params)
            resultado += s;
        return resultado;
    }

}
