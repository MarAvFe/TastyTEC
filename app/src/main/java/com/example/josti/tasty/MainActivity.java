package com.example.josti.tasty;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.EditText;
import android.widget.AdapterView.OnItemClickListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils.TruncateAt;
import android.content.DialogInterface;
import android.app.AlertDialog;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.MalformedURLException;
import java.io.IOException;



public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private ArrayList<String> dataDescription = new ArrayList<String>();
    private ArrayList<String> dataName = new ArrayList<String>();
    private ArrayList<String> dataLink = new ArrayList<String>();
    private ArrayList<Bitmap> dataThumbs = new ArrayList<Bitmap>();
    private ArrayList<ViewHolder> arr = new ArrayList<ViewHolder>();
    private ListView lv;
    private URL url;
    private Bitmap bmp;
    private ViewHolder viewHolder;
    private AdapterListView adapterListView;
    private String recipeSearch;
    private static ArrayList<String> parames = new ArrayList<String>();
    private String category;
    private static String hostIp = "192.168.10.148"; // La IP del host del WS. HINT: ifconfig | ipconfig
    //private static String hostIp = "192.168.10.149"; // La IP del host del WS. HINT: ifconfig | ipconfig

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
        refreshArrays(1);

        //category = ;

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
        Log.i("QuerReady","Passing by");
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
                parames = new ArrayList<String>();
                parames.add("getRecipes?param=4&criteria="+recipeSearch);
                new HttpRequestTaskResultList().execute();
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
            refreshArrays(1);
            return true;
        }

        if(id == R.id.search){
            final EditText input = new EditText(MainActivity.this);
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Search Recipe")
                    .setMessage("Type something. No spaces.")
                    .setView(input)
                    .setPositiveButton("Search", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            recipeSearch=input.getText().toString();
                            Log.v("search",recipeSearch);
                            refreshArrays(4);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            // Do nothing
                        }
                    }).show();
        }

        return super.onOptionsItemSelected(item);
    }
    
    private class AdapterListView extends ArrayAdapter<String> implements AdapterView.OnItemClickListener{

        private int layout;
        public AdapterListView(Context context, int resource, ArrayList<String> objects) {
            super(context, resource, objects);
            layout = resource;
        }

        @Override
        public boolean areAllItemsEnabled() {return true;}

        @Override
        public boolean isEnabled(int arg0) {return true;}

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            ViewHolder mainViewHolder = null;
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(layout, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.thumbnail = (ImageView) convertView.findViewById(R.id.thumbnail);
                viewHolder.title = (TextView) convertView.findViewById(R.id.title);
                viewHolder.description = (TextView) convertView.findViewById(R.id.description);
                convertView.setTag(viewHolder);
                disableEditText(viewHolder.title);
                //viewHolder.thumbnail.setImageBitmap(dataThumbs.get(position));
            }

            mainViewHolder = (ViewHolder) convertView.getTag();
            convertView.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this,ActivityRecipe.class);
                    intent.putExtra("RecipeName",dataName.get(position));
                    startActivity(intent);
                }
            });
            mainViewHolder.title.setText(dataName.get(position));
            mainViewHolder.description.setText(dataDescription.get(position));
            mainViewHolder.description.setLines(2);
            mainViewHolder.description.setMaxLines(3);
            arr.add(mainViewHolder);
            return convertView;
        }

        @Override
        public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {
            Toast.makeText(MainActivity.this, "Triggered3-pos:"+position, Toast.LENGTH_SHORT).show();
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

        if (id == R.id.New) {
            refreshArrays(1);

        } else if (id == R.id.Favortie) {
            refreshArrays(2);

        } else if (id == R.id.Top) {
            refreshArrays(3);

        } else if (id == R.id.WenServiceIP) {
            final EditText input = new EditText(MainActivity.this);
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Change HostIp")
                    .setMessage("Type new Ip Address (" + MainActivity.getHostIp() + ")")
                    .setView(input)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            MainActivity.setHostIp( input.getText().toString().replace(" ","") );
                            refreshArrays(1);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            // Do nothing
                        }
                    }).show();
        }
        else if (id == R.id.nav_drinks){
            Intent intent = new Intent(MainActivity.this,CategoryActivity.class);
            intent.putExtra("Category","Drinks");
            startActivity(intent);
        }

        else if (id == R.id.nav_desserts){
            Intent intent = new Intent(MainActivity.this,CategoryActivity.class);
            intent.putExtra("Category","Desserts");
            startActivity(intent);
        }

        else if (id == R.id.nav_diners){
            Intent intent = new Intent(MainActivity.this,CategoryActivity.class);
            intent.putExtra("Category","Dinners");
            startActivity(intent);
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
                //Log.e("ResLiFail", e.getMessage(), e);
                Log.e("RESLIST", "Error de Conexión manejada");
            }
            return null;
        }

        @Override
        protected void onPostExecute(QueryResultList retorno) {
            if(retorno != null){
                QueryResultList results = new QueryResultList();
                results.setNames(retorno.getNames());
                results.setDescriptions(retorno.getDescriptions());
                results.setLinks(retorno.getLinks());

                dataName.clear();
                dataDescription.clear();
                dataLink.clear();
                dataThumbs.clear();
                for(int i = 0; i < results.getNames().size(); i++){
                    dataName.add(results.getNames().get(i));
                    dataDescription.add(results.getDescriptions().get(i));
                    dataLink.add(results.getLinks().get(i));
                }
                queriesReady();
                new LoadRecipesThumbs().execute();
            }else{
                Toast.makeText(MainActivity.this, "Error de conexión. Compruebe el HostIP", Toast.LENGTH_LONG).show();
            }
        }
    }

    /* ****** LoadRecipesThumbs ****** */
    private class LoadRecipesThumbs extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {
            dataThumbs.clear();
            for(String s : dataLink){
                try{
                    url = new URL("http://img.youtube.com/vi/" + s + "/1.jpg");// ''http://img.youtube.com/vi/ZrvAjkDqDYM/1.jpg
                    bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    dataThumbs.add(bmp);
                }catch(MalformedURLException ex){
                    return false;
                }catch(IOException e){
                    return false;
                }
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean retorno) {
                for(int i = 0; i < dataThumbs.size(); i++){
                    arr.get(i).thumbnail.setImageBitmap(dataThumbs.get(i));
                }
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

    private void disableEditText(TextView editText2) {
        EditText editText = (EditText) editText2;
        editText.setFocusable(false);
        editText.setEnabled(true);
        editText.setCursorVisible(false);
        editText.setKeyListener(null);
    }

}
