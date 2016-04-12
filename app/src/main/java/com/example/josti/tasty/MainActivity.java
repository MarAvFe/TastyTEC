package com.example.josti.tasty;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private ArrayList<String> dataDescription = new ArrayList<String>();
    private ArrayList<String> dataName = new ArrayList<String>();
    private ListView lv;
    private AdapterListView adapterListView;
    private static ArrayList<String> parames = new ArrayList<String>();
    private static String hostIp = "192.168.43.211"; // La IP del host del WS. HINT: ifconfig | ipconfig

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        lv = (ListView) findViewById(R.id.listView);
        refreshArrays(0);

        Button buttonNew = (Button) findViewById(R.id.buttonNew);
        buttonNew.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshArrays(1);
            }
        });
        Button buttonFavorite = (Button) findViewById(R.id.buttonFavorite);
        buttonFavorite.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshArrays(2);
            }
        });
        Button buttonTop = (Button) findViewById(R.id.buttonTop);
        buttonTop.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshArrays(3);
            }
        });

    }

    private void queriesReady(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapterListView = new AdapterListView(getContext(), R.layout.recipe_item, dataName);
                lv.setAdapter(adapterListView);
            }
        });
    }

    private void loadRecipeDefault(){
        for (int i = 0;i < 55; i ++){
            dataName.add("Recipe " + i);
            dataDescription.add("adsadasda sadasdads adadsad asdadasad adasdadsada dsa" + i);
        }

    }

    public void refreshArrays(int type){
        switch(type){
            case 0: // All
                parames = new ArrayList<String>();
                parames.add("getRecipes?param=0");
                new HttpRequestTaskResultList().execute();
                break;
            case 1: // New
                parames = new ArrayList<String>();
                parames.add("getRecipes?param=1");
                new HttpRequestTaskResultList().execute();
                break;
            case 2: // Favorite
                parames = new ArrayList<String>();
                parames.add("getRecipes?param=2");
                new HttpRequestTaskResultList().execute();
                break;
            case 3: // Top
                parames = new ArrayList<String>();
                parames.add("getRecipes?param=3");
                new HttpRequestTaskResultList().execute();
                break;
            case 4: // Search
                break;
        }
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
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    
    private class AdapterListView extends ArrayAdapter<String> {
        private int layout;
        private ArrayList<String> arr;
        public AdapterListView(Context context, int resource, ArrayList<String> objects) {
            super(context, resource, objects);
            layout = resource;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder mainViewHolder = null;
            if (convertView == null){
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView =  inflater.inflate(layout,parent,false);
                ViewHolder viewHolder = new ViewHolder();
                viewHolder.thumbnail = (ImageView) convertView.findViewById(R.id.thumbnail);
                viewHolder.title = (TextView) convertView.findViewById(R.id.title);
                viewHolder.description = (TextView) convertView.findViewById(R.id.description);
                viewHolder.button = (Button) convertView.findViewById(R.id.buttonMore);
                convertView.setTag(viewHolder);
            }
            mainViewHolder = (ViewHolder) convertView.getTag();
            mainViewHolder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(getContext(), dataName.get(position), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this,ActivityRecipe.class);
                    intent.putExtra("RecipeName",dataName.get(position));
                    startActivity(intent);
                }
            });
            mainViewHolder.title.setText(dataName.get(position));
            mainViewHolder.description.setText(dataDescription.get(position));
            return convertView;
        }


    }

    public class ViewHolder{
        ImageView thumbnail;
        TextView title;
        TextView description;
        Button button;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            Toast.makeText(MainActivity.this, "Camera?", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_gallery) {
            Toast.makeText(MainActivity.this, "gallery?", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_slideshow) {
            Toast.makeText(MainActivity.this, "slideshow?", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_share) {
            Toast.makeText(MainActivity.this, "share?", Toast.LENGTH_SHORT).show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /* ****** RESULTADOS ****** */
    private class HttpRequestTaskResultList extends AsyncTask<Void, Void, QueryResultList> {
        @Override
        protected QueryResultList doInBackground(Void... params) {
            try {
                final String url = "http://" + hostIp + ":5003/" + getParams4WS();
                Log.v("ADRR", url);
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                Log.v("HttpReq","Getting vals from ResulWS...");
                QueryResultList retorno = restTemplate.getForObject(url, QueryResultList.class);
                Log.v("HttpReq","Got vals from WS");
                Log.v("HttpReq","Got:" + retorno.getNames().toString());
                return retorno;
            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage(), e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(QueryResultList retorno) {
            QueryResultList results = new QueryResultList();
            results.setNames(retorno.getNames());
            results.setDescriptions(retorno.getDescriptions());
            results.setLinks(retorno.getLinks());

            dataName.clear();
            dataDescription.clear();
            for(int i = 0; i < results.getNames().size(); i++){
                dataName.add(results.getNames().get(i));
                dataDescription.add(results.getDescriptions().get(i));
            }
            queriesReady();
        }
    }

    public static String getHostIp() {
        return hostIp;
    }

    public static void setHostIp(String hostIp) {
        MainActivity.hostIp = hostIp;
    }

    private String getParams4WS(){
        String resultado = "";
        for (String s : parames)
            resultado += s;
        return resultado;
    }

    private MainActivity getContext(){
        return this;
    };
}
