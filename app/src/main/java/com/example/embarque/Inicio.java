package com.example.embarque;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.google.gson.Gson;

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
    public LinkedHashMap<String, String> Vehiculos= new LinkedHashMap<String, String>();
    private String keyVehiculo,keyRuta,noPlaca;
    TextView  mTextView;
    EditText usuario;
    SharedPreferences settings;
    OkHttpClient client = new OkHttpClient();
    setting conf = new setting();
    Gson g = new Gson();
    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityInicioBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        settings = this.getSharedPreferences("Sersa_Embarque", Context.MODE_PRIVATE);
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
        String json =bowlingJson("Jesse");

        try {

            String response = conf.api("http://"+conf.Link()+"/api/AppMobile/GetAllVehicles", json);
            //  obj = new JSONObject(response);
            JSONArray arr = new JSONArray(response);
           // Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
            for (int i = 0; i < arr.length(); i++)
            {
                JSONObject e = arr.getJSONObject(i);
                data.add(e.getString("noPlaca")); //this adds an element to the list.
                Vehiculos.put(e.getString("noPlaca"),e.getString("keyVehiculo"));

                //  Toast.makeText(getApplicationContext(), LoadSucursal.get(arr.getJSONObject(i).getString("ip")), Toast.LENGTH_LONG).show();
            }

        } catch (IOException | JSONException e) {
            Log.d("error", e.getMessage());
            Toast.makeText(getApplicationContext(), e.getMessage().toString(), Toast.LENGTH_LONG).show();
        }
        mTextView = (TextView) findViewById(R.id.text);
        usuario = (EditText) findViewById(R.id.paquete);



        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.spinner_selected, data);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

        Spinner spinner = findViewById(R.id.spinner1);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //  Toast.makeText(getApplicationContext(),parent.getItemAtPosition(position).toString(),Toast.LENGTH_LONG).show();
                noPlaca=parent.getItemAtPosition(position).toString();
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

    String bowlingJson(String usuario) {

        return "{'nombreUsuario':'"+usuario+"',"
                + "'KeyUser':'0',"
                + "}";
    }

    public void iniciar(View view) {
if(!usuario.getText().toString().isEmpty()){
    String nombreUsuario=usuario.getText().toString().trim();
    String json =bowlingJson(nombreUsuario);
    try {

        String response = post("http://"+conf.Link()+"/api/AppMobile/IniciarEmbarque", json);
        JSONArray arr = new JSONArray(response);
        // Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
        keyVehiculo= Vehiculos.get(noPlaca);

        for (int i = 0; i < arr.length(); i++) {
            JSONObject e = arr.getJSONObject(i);
            if(Integer.parseInt(e.getString("keyUsuario"))!=0){
            Snackbar.make(view, "Sesion iniciada", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            Intent intent = new Intent(Inicio.this, Embarque.class);
            settings.edit().putString("keyVehiculo",keyVehiculo).apply();
            settings.edit().putString("noPlaca",noPlaca).apply();
            settings.edit().putString("nombreUsuario",nombreUsuario).apply();
            settings.edit().putString("keyUsuario",e.getString("keyUsuario")).apply();
            startActivity(intent);
             finish();
            }
             Toast.makeText(getApplicationContext(), e.getString("msg"), Toast.LENGTH_LONG).show();
        }

    } catch (IOException | JSONException e) {
        Log.d("error", e.getMessage());
        Snackbar.make(view, e.getMessage().toString(), Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }
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