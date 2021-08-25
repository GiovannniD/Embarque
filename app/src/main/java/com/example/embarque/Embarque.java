package com.example.embarque;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.StrictMode;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.embarque.databinding.ActivityEmbarqueBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import okhttp3.OkHttpClient;

public class Embarque extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityEmbarqueBinding binding;
    private String keyVehiculo,keyRuta,noPlaca;
    public LinkedHashMap<String, String> Rutas= new LinkedHashMap<String, String>();
    TextView  mTextView;
    EditText paquete;
    SharedPreferences settings;
    OkHttpClient client = new OkHttpClient();
    setting conf = new setting();
    ImageButton button;
    Button enlace;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityEmbarqueBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

         button = (ImageButton) findViewById(R.id.actualizar);
        enlace = (Button) findViewById(R.id.enlace);
        paquete = (EditText) findViewById(R.id.paquete);
        imageBtn();
        textChanged();
        /*Spannable buttonLabel = new SpannableString("agregar enlace");
        buttonLabel.setSpan(new ImageSpan(getApplicationContext(), android.R.drawable.ic_popup_sync,
                ImageSpan.ALIGN_BOTTOM), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        enlace.setText(buttonLabel);*/

       /* NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_embarque);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);*/
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

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    String bowlingJson(String usuario) {

        return "{'nombreUsuario':'"+usuario+"',"
                + "'KeyUser':'0',"
                + "}";
    }
   /* @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_embarque);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }*/
    private  void imageBtn(){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "test", Toast.LENGTH_SHORT).show();
            }
        });
    }
   public void agregarEnlace(View view) {


   }

   private void textChanged(){
       // Capture Text in EditText
       paquete.addTextChangedListener(new TextWatcher() {

           @Override
           public void afterTextChanged(Editable arg0) {
               // TODO Auto-generated method stub


           }

           @Override
           public void beforeTextChanged(CharSequence arg0, int arg1,
                                         int arg2, int arg3) {
               // TODO Auto-generated method stub
           }

           @Override
           public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                     int arg3) {
               // TODO Auto-generated method stub
               if(paquete.getText().toString().isEmpty())
               {
                 //  Toast.makeText(getApplicationContext(), "hola", Toast.LENGTH_LONG).show();
               }else{

               }
           }
       });
   }
}