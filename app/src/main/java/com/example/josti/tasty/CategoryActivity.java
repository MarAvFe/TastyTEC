package com.example.josti.tasty;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import android.view.KeyEvent;

public class CategoryActivity extends AppCompatActivity {
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
    private String categoryName;
    private static ArrayList<String> parames = new ArrayList<String>();


    private static String hostIp = MainActivity.getHostIp();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        Toolbar toolbar;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        Bundle extras = getIntent().getExtras();
        categoryName = extras.getString("Category");
        getSupportActionBar().setTitle(categoryName);

        lv = (ListView) findViewById(R.id.listViewCat);

        refreshArrays(5);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    public void refreshArrays(int type){
        switch(type){
            case 0: // All
                parames = new ArrayList<String>();
                parames.add("getRecipes?param=0");
                new HttpRequestTaskResultList().execute();
                break;
            case 5: // Category
                parames = new ArrayList<String>();
                parames.add("getRecipes?param=5&criteria="+categoryName);
                new HttpRequestTaskResultList().execute();
                break;
        }
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

    private class AdapterListView extends ArrayAdapter<String> implements AdapterView.OnItemClickListener{

        private int layout;
        private ArrayList<String> arr;
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
            if (convertView == null){
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView =  inflater.inflate(layout,parent,false);
                ViewHolder viewHolder = new ViewHolder();
                viewHolder.thumbnail = (ImageView) convertView.findViewById(R.id.thumbnail);
                viewHolder.title = (TextView) convertView.findViewById(R.id.title);
                viewHolder.description = (TextView) convertView.findViewById(R.id.description);
                convertView.setTag(viewHolder);
                disableEditText(viewHolder.title);
            }

            mainViewHolder = (ViewHolder) convertView.getTag();
            convertView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(CategoryActivity.this,ActivityRecipe.class);
                    intent.putExtra("RecipeName",dataName.get(position));
                    startActivity(intent);
                }
            });
            mainViewHolder.title.setText(dataName.get(position));
            mainViewHolder.description.setText(dataDescription.get(position));
            mainViewHolder.description.setLines(2);
            mainViewHolder.description.setMaxLines(3);
            return convertView;
        }

        @Override
        public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {
            Toast.makeText(CategoryActivity.this, "Triggered3-pos:" + position, Toast.LENGTH_SHORT).show();
        }
    }

    public class ViewHolder{
        ImageView thumbnail;
        TextView title;
        TextView description;
        Button button;
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
                Log.e("CategoryActivity", e.getMessage(), e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(QueryResultList retorno) {
            if(retorno !=  null){
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
                new LoadRecipesThumbs().execute();
                queriesReady();
            }else{
                Toast.makeText(CategoryActivity.this, "Error de conexión. Compruebe el HostIP", Toast.LENGTH_LONG).show();
            }
        }
    }

    /* ****** LoadRecipesThumbs ****** */
    private class LoadRecipesThumbs extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {
            for(String s : dataLink){
                try{
                    url = new URL("http://img.youtube.com/vi/" + s + "/1.jpg");
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
            if(retorno){
                for(int i = 0; i < arr.size(); i++){
                    try{
                        arr.get(i).thumbnail.setImageBitmap(dataThumbs.get(i));
                    }catch (IndexOutOfBoundsException e){
                    }
                }
            }else{
                Toast.makeText(CategoryActivity.this, "Error al obtener las imagenes", Toast.LENGTH_LONG).show();
            }
        }
    }


    private String getParams4WS(){
        String resultado = "";
        for (String s : parames)
            resultado += s;
        return resultado;
    }

    private CategoryActivity getContext(){
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
