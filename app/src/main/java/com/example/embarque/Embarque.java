package com.example.embarque;

import static com.example.embarque.Inicio.JSON;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.StrictMode;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
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
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Embarque extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityEmbarqueBinding binding;
    private String keyVehiculo,keyRuta,noPlaca;
    public LinkedHashMap<String, String> Rutas= new LinkedHashMap<String, String>();
    TextView  mTextView,conteo;
    EditText paquete,enlaceText;
    SharedPreferences settings;
    OkHttpClient client = new OkHttpClient();
    setting conf = new setting();
    ImageButton button;
    Button enlace;
    Spinner spinner;
  //  private TextInputLayout mNumeroProductoLabel;
   // private TextInputEditText mNumeroProducto;
    private int caso=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityEmbarqueBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

         button = (ImageButton) findViewById(R.id.actualizar);
        enlace = (Button) findViewById(R.id.enlace);
        paquete = (EditText) findViewById(R.id.paquete);
        conteo = (TextView) findViewById(R.id.conteo);
        settings = this.getSharedPreferences("Sersa_Embarque", Context.MODE_PRIVATE);
        paquete.findFocus();
        imageBtn();
        textChanged();
        agregarEnlace();
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
        mTextView = (TextView) findViewById(R.id.text);
        spinner = findViewById(R.id.spinner1);
        //   Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG).show();

        llenarCombo();
       /* binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }
private void llenarCombo(){
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
           // Rutas.clear();
        //    Toast.makeText(getApplicationContext(), e.getString("desRuta"), Toast.LENGTH_LONG).show();
            data.add(e.getString("desRuta")); //this adds an element to the list.
            Rutas.put(e.getString("desRuta"),e.getString("keyRutaEntrega"));

            //  Toast.makeText(getApplicationContext(), LoadSucursal.get(arr.getJSONObject(i).getString("ip")), Toast.LENGTH_LONG).show();
        }

    } catch (IOException | JSONException e) {
      //  Log.d("error", e.getMessage());
        Toast.makeText(getApplicationContext(), e.getMessage().toString(), Toast.LENGTH_LONG).show();
    }





    ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.spinner_selected, data);
    adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);


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
        findViewById(R.id.loadingPanel).setVisibility(View.GONE);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  Toast.makeText(getApplicationContext(), "test", Toast.LENGTH_SHORT).show();
                llenarCombo();
            }
        });
    }
   public void agregarEnlace() {
       enlace.setOnClickListener(new View.OnClickListener() {
           public void onClick(View v) {
               AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                       Embarque.this);

               // set title
               alertDialogBuilder.setTitle("Agregar un enlace");
               alertDialogBuilder.setCancelable(false);
               View mView = getLayoutInflater().inflate(R.layout.dialogo, null);
               enlaceText = (EditText) mView.findViewById(R.id.enlaceText);
               //mNumeroProductoLabel  = (TextInputLayout) findViewById(R.id.til_numeroproducto);
               alertDialogBuilder.setView(mView)
                       // set dialog message

                       //.setMessage("Click yes to exit!")
                       .setCancelable(false)
                       .setPositiveButton("Hecho",new DialogInterface.OnClickListener() {
                           public void onClick(DialogInterface dialog,int id) {

                               if (!TextUtils.isEmpty(enlaceText.getText().toString())){
                                    caso=0;
                                   try {
                                       postwithParameters("http://"+conf.Link()+"/api/AppMobile/GuardarEnlace", jsonRuta(enlaceText.getText().toString()));
                                   } catch (IOException e) {
                                       e.printStackTrace();
                                       Toast.makeText(getApplicationContext(), e.getMessage().toString(), Toast.LENGTH_LONG).show();
                                   }


                               }else {
                                   Snackbar.make(getWindow().getDecorView().getRootView(), "El nombre del enlace no puede estar vacio!", Snackbar.LENGTH_LONG)
                                           .setAction("Action", null).show();

                               }

                           }
                       })
                       .setNegativeButton("Cancelar",new DialogInterface.OnClickListener() {
                           public void onClick(DialogInterface dialog,int id) {
                               // if this button is clicked, just close
                               // the dialog box and do nothing
                               dialog.cancel();
                           }
                       });

               // create alert dialog
               AlertDialog alertDialog = alertDialogBuilder.create();

               // show it
               alertDialog.show();
           }
       });


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
               if(!paquete.getText().toString().isEmpty())
               {
                 //  Toast.makeText(getApplicationContext(), jsonPaquete(), Toast.LENGTH_LONG).show();
                   try {

                       findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
                       caso=1;
                       postwithParameters("http://"+conf.Link()+"/api/AppMobile/GuardarEmbarque", jsonPaquete());
                   } catch (IOException e) {
                       e.printStackTrace();
                       Toast.makeText(getApplicationContext(), e.getMessage().toString(), Toast.LENGTH_LONG).show();
                   }
                   //  Toast.makeText(getApplicationContext(), "hola", Toast.LENGTH_LONG).show();
               }else{

               }
           }
       });
   }
private String jsonPaquete(){

   // Toast.makeText(getApplicationContext(), "Ruta: "+Rutas.size(), Toast.LENGTH_LONG).show();

    return "{'KeyRutaEntrega':'"+Rutas.get(keyRuta)+"',"
            + "'KeyVehiculo' :"+settings.getString("keyVehiculo","")+","
            + "'KeyCodigo' : '"+paquete.getText().toString().trim()+"',"
            + "'KeyUsuario' : "+settings.getString("keyUsuario","")+","
            + "'Msg' :'0'"
            + "}";
}

    private String jsonRuta(String Ruta){



        return "   {\n" +
                "        \"keyRutaEntrega\": 0,\n" +
                "        \"desRuta\": \""+Ruta+"\",\n" +
                "        \"msg\": \"\",\n" +
                "        \"code\": 0\n" +
                "    }";
    }
    void postwithParameters(String url, String json) throws IOException{

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120,TimeUnit.SECONDS)

                .build();
        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        client.newCall(request)
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(final Call call, IOException e) {
                        // Error

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // For the example, you can show an error dialog or a toast
                                // on the main UI thread
                           //     findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(), "Vuelva a intentarlo, " + e.getMessage(), Toast.LENGTH_LONG).show();
                           //     guardar.setEnabled(true);

                             //   setting.tempSavePackage.put(codigo_paquete,json);
                              //  settings.edit().putString("paquete_pendientes",gson.toJson(setting.tempSavePackage)).apply();
                                findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                                paquete.setText("");

                              //  setResult(1);
                               // finish();


                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {
                        String res = response.body().string();
                        //   findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // For the example, you can show an error dialog or a toast
                                // on the main UI thread
                                findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                              //  setResult(1);
                               // finish();
                                try {
                                    JSONArray arr = new JSONArray(res);
                                    // Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
                                    for (int i = 0; i < arr.length(); i++)
                                    {
                                        JSONObject e = arr.getJSONObject(i);
                                        if(caso==1){
                                        if (Integer.parseInt(e.getString("code")) == 0) {
                                            conteo.setText(e.getString("count"));
                                            caso=0;
                                        }
                                    }else{
                                            llenarCombo();}
                                     Toast.makeText(getApplicationContext(), e.getString("msg"), Toast.LENGTH_LONG).show();
                                    }
                                    paquete.setText("");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                    }
                });
    }
}