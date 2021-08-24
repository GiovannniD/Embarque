package com.example.embarque;

import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.embarque.databinding.ActivityInicioBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Inicio extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityInicioBinding binding;
    public LinkedHashMap<String, String> Rutas= new LinkedHashMap<String, String>();
    private String keyVehiculo,keyRuta,noPlaca;
    TextView  mTextView;
    EditText usuario;
    OkHttpClient client = new OkHttpClient();
    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setting conf = new setting();
        binding = ActivityInicioBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            //your codes here

        }
     //   Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG).show();
        ArrayList<String> data= new ArrayList<String>();
        ArrayList<String> data2= new ArrayList<String>();
        String json =bowlingJson("Jesse", "Jake","");

        try {

            String response = conf.api("http://"+conf.Link()+"/api/AppMobile/RutaEntrega", json);
            //  obj = new JSONObject(response);
            JSONArray arr = new JSONArray(response);
           // Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
            for (int i = 0; i < arr.length(); i++)
            {
                JSONObject e = arr.getJSONObject(i);
                data.add(e.getString("desRuta")); //this adds an element to the list.
                Rutas.put(e.getString("desRuta"),e.getString("keyRutaEntrega"));

                //  Toast.makeText(getApplicationContext(), LoadSucursal.get(arr.getJSONObject(i).getString("ip")), Toast.LENGTH_LONG).show();
            }

        } catch (IOException | JSONException e) {
            Log.d("error", e.getMessage());
            Toast.makeText(getApplicationContext(), e.getMessage().toString(), Toast.LENGTH_LONG).show();
        }
        mTextView = (TextView) findViewById(R.id.text);
        usuario = (EditText) findViewById(R.id.usuario);



        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.spinner_selected, data);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

        Spinner spinner = findViewById(R.id.spinner1);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //  Toast.makeText(getApplicationContext(),parent.getItemAtPosition(position).toString(),Toast.LENGTH_LONG).show();
                keyRuta=parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
       /* NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_inicio);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);*/

       /* binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                //Toast.makeText(getApplicationContext(), "hola", Toast.LENGTH_LONG).show();
            }
        });*/
    }
    String post(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
                .url(url)
                 .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    String bowlingJson(String keyRuta, String keyVehiculo,String Fecha) {
      /*  return "{'winCondition':'HIGH_SCORE',"
                + "'name':'Bowling',"
                + "'round':4,"
                + "'lastSaved':1367702411696,"
                + "'dateStarted':1367702378785,"
                + "'players':["
                + "{'name':'" + player1 + "','history':[10,8,6,7,8],'color':-13388315,'total':39},"
                + "{'name':'" + player2 + "','history':[6,10,5,10,10],'color':-48060,'total':41}"
                + "]}";*/

        return "{'idStatusRoutes':'2',"
                + "'keyRuta':'"+keyRuta+"',"
                + "'keyVehiculo':'"+keyVehiculo+"',"
                + "'routeDate':'"+Fecha+"'"
                + "}";
    }

    public void iniciar(View view) {
if(!usuario.getText().toString().isEmpty()){

}else{

    Snackbar.make(view, "Ingrese un nombre de usuario", Snackbar.LENGTH_LONG)
            .setAction("Action", null).show();
}

    }
   /* @Override
 public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_inicio);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }*/
}