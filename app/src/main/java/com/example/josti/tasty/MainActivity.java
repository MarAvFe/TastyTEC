package com.example.josti.tasty;

import android.content.Context;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;
import android.widget.TextView;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    ArrayList<String> dataDescription = new ArrayList<String>();
    ArrayList<String> dataName = new ArrayList<String>();
    AdapterListView adapterListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView lv = (ListView) findViewById(R.id.listView);
        loadRecipeDefault();
        adapterListView = new AdapterListView(this, R.layout.recipe_item, dataName);
        lv.setAdapter(adapterListView);

        Button buttonNew = (Button) findViewById(R.id.buttonNew);
        buttonNew.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dataName.clear();
                dataDescription.clear();
                for (int i = 0; i < 55; i++) {
                    dataName.add("New " + i);
                    dataDescription.add("adsadasda sadasdads a" + i);
                }
                adapterListView.notifyDataSetInvalidated();
            }
        });
        Button buttonFavorite = (Button) findViewById(R.id.buttonFavorite);
        buttonFavorite.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dataName.clear();
                dataDescription.clear();
                for (int i = 0;i < 55; i ++){
                    dataName.add("Favorite " + i);
                    dataDescription.add("adsadasda sadasdads a" + i);
                }
                adapterListView.notifyDataSetInvalidated();
            }
        });
        Button buttonTop = (Button) findViewById(R.id.buttonTop);
        buttonTop.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dataName.clear();
                dataDescription.clear();
                for (int i = 0;i < 55; i ++){
                    dataName.add("Top " + i);
                    dataDescription.add("adsadasda sadasdads a" + i);
                }
                adapterListView.notifyDataSetInvalidated();
            }
        });



        /*
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, "List item was clicked at " + position, Toast.LENGTH_SHORT).show();
            }

        });
        */

    }

    private void loadRecipeDefault(){
        for (int i = 0;i < 55; i ++){
            dataName.add("Name " + i);
            dataDescription.add("adsadasda sadasdads adadsad asdadasad adasdadsada dsa" + i);
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
                    Toast.makeText(getContext(), dataName.get(position), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this,Main2Activity.class);
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


    private class HttpRequestTask extends AsyncTask<Void, Void, wsConsume> {
        @Override
        protected wsConsume doInBackground(Void... params) {
            try {
                final String url = "http://192.168.10.140:5003/androidTest";
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                wsConsume wsReturn = restTemplate.getForObject(url, wsConsume.class);
                Log.v("HttpReq","Got vals from WS");
                return wsReturn;
            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage(), e);
            }

            return null;
        }

        

    }


}
