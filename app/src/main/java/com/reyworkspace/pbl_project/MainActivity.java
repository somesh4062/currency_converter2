package com.reyworkspace.pbl_project;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonObject;
import com.reyworkspace.pbl_project.Retrofit.RetrofitBuilder;
import com.reyworkspace.pbl_project.Retrofit.RetrofitInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    public Button convert;
    public EditText precurren;
    public EditText second_value;
    public Spinner sp;
    public Spinner sp1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        convert=findViewById(R.id.button);
        precurren=findViewById(R.id.e2);
        second_value=findViewById(R.id.e1);
        sp=findViewById(R.id.sp);
        sp1=findViewById(R.id.sp1);

        List<String> l=new ArrayList<>();
        l.add(0,"INR");
        l.add(1,"MYR");
        l.add(2,"USD");
        l.add(3,"EUR");
        l.add(4,"CAD");
        l.add(5,"HKD");
        l.add(6,"ISK");
        l.add(7,"PHP");
        l.add(8,"DKK");
        l.add(9,"HUF");
        l.add(10,"CZK");
        l.add(11,"AUD");
        l.add(12,"RON");
        l.add(13,"SEK");
        l.add(14,"IDR");
        l.add(15,"BRL");
        l.add(16,"RUB");
        l.add(17,"HRK");
        l.add(18,"JPY");
        l.add(19,"THB");
        l.add(20,"CHF");
        l.add(21,"SGD");
        l.add(22,"PLN");
        l.add(23,"BGN");
        l.add(24,"TRY");
        l.add(25,"CNY");
        l.add(26,"NOK");
        l.add(27,"NZD");
        l.add(28,"MXN");
        l.add(29,"ILS");
        l.add(30,"KRW");




        ArrayAdapter<String> a =new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,l);
        a.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(a);
        sp1.setAdapter(a);


    convert.setOnClickListener(v -> {

        if(isConnected()){
            if(TextUtils.isEmpty(precurren.getText().toString())){
                precurren.setError("Invalid Input");
                Toast.makeText(getApplicationContext(),"Enter Value",Toast.LENGTH_SHORT).show();

            }else {
                RetrofitInterface retrofitInterface = RetrofitBuilder.getRetrofitInsatnce().create(RetrofitInterface.class);
                Call<JsonObject> call=retrofitInterface.getExchangeCurrency(sp.getSelectedItem().toString());
                call.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        JsonObject res= response.body();
                        JsonObject rates = res.getAsJsonObject("rates");
                        double currency= Double.parseDouble(precurren.getText().toString());
                        double multi = Double.parseDouble(rates.get(sp1.getSelectedItem().toString()).toString());
                        double result = currency * multi;
                        second_value.setText(String.valueOf(result));
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {

                    }
                });
            }
        }else {
            Intent intent= new Intent(getApplicationContext(),networkActivity.class);
            startActivity(intent);
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }



    });

    }

    public void exit(View view) {
        Intent intent=new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    public void reset(View view) {
        precurren.setText("");
        second_value.setText("");
    }


    public boolean isConnected(){
        boolean connected=false;
        try{
            ConnectivityManager cm=(ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo =cm.getActiveNetworkInfo();
            connected = networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
            return connected;
        }catch (Exception e){
            Log.e("Connectivity Error",e.getMessage());
        }

        return connected;
    }
}