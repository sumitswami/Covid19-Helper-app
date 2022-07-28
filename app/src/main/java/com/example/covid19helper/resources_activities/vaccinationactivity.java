package com.example.covid19helper.resources_activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.covid19helper.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class vaccinationactivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{



    String baseURL = "https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/findByPin";
    private EditText areaPinCode;
    private Button forwardbtn;
    ProgressBar holdonprogress;
    private ArrayList<vaccinemodel> vaccinationcenters;
    private RecyclerView resultRecyclerView;
    String areapin,avldate;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vaccinationactivity);

        mapViews();
        OnClicksetup();
    }


    private void OnClicksetup() {
        forwardbtn.setOnClickListener(view -> {
            holdonprogress.setVisibility(View.VISIBLE);
            DialogFragment dp = new pickdate();
            dp.show(getSupportFragmentManager(),"pick a date");

        });
    }

    private void mapViews() {
        forwardbtn = findViewById(R.id.getresult);
        holdonprogress = findViewById(R.id.progress_circular);
        areaPinCode = findViewById(R.id.enterPincode);
        resultRecyclerView = findViewById(R.id.recyclerView);
        resultRecyclerView.setHasFixedSize(true);
        vaccinationcenters= new ArrayList<vaccinemodel>();


    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayofmonth) {
        Calendar k =Calendar.getInstance();
        k.set(Calendar.YEAR,year);
        k.set(Calendar.MONTH,month);
        k.set(Calendar.DAY_OF_MONTH,dayofmonth);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        dateFormat.setTimeZone(k.getTimeZone());
        String d = dateFormat.format(k.getTime());
        setup(d);


    }

    private void setup(String d) {
        avldate = d;
        fetchDatenow();
    }

    private void fetchDatenow() {
        vaccinationcenters.clear();
        areapin = areaPinCode.getText().toString();
        String url_api = baseURL + "?pincode=" + areapin + "&date=" +avldate;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url_api, response -> {
            try {
                JSONObject object = new JSONObject(response);
                JSONArray sessonArray = object.getJSONArray("sessions");
                for (int i = 0; i < sessonArray.length(); i++) {
                    JSONObject sesobject = sessonArray.getJSONObject(i);
                    vaccinemodel vaccineModel = new vaccinemodel();
                    vaccineModel.setVaccinationcenter(sesobject.getString("name"));
                    vaccineModel.setVaccinationcenteraddress(sesobject.getString("address"));
                    vaccineModel.setVaccinationCentertime(sesobject.getString("to"));
                    vaccineModel.setVaccinationTimings(sesobject.getString("from"));
                    vaccineModel.setVaccinationName(sesobject.getString("vaccine"));
                    vaccineModel.setVaccinationCharges(sesobject.getString("fee_type"));
                    vaccineModel.setVaccinationage(sesobject.getString("min_age_limit"));
                    vaccineModel.setVaccineavailable(sesobject.getString("available_capacity"));
                    vaccinationcenters.add(vaccineModel);
                }

                vaccinationinfoadapter vaccinationinfoAdapter = new vaccinationinfoadapter(getApplicationContext(), vaccinationcenters);
                resultRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                resultRecyclerView.setAdapter(vaccinationinfoAdapter);
                holdonprogress.setVisibility(View.INVISIBLE);


            } catch (Exception e) {
                e.printStackTrace();
            }
        }, error -> {
            holdonprogress.setVisibility(View.INVISIBLE);
            Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_SHORT).show();
        });
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);




    }
}
