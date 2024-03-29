package com.example.makank.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.makank.Alert;
import com.example.makank.LoadingDialog;
import com.example.makank.R;
import com.example.makank.data.network.ApiClient;
import com.example.makank.data.network.ApiInterface;
import com.example.makank.data.model.Disease;
import com.example.makank.adapter.DiseaseAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.makank.SharedPref.SHARED_PREF_NAME;
import static com.example.makank.SharedPref.USER_ID;
import static com.example.makank.SharedPref.mCtx;

public class DiseaseActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<Disease> diseases;
    private DiseaseAdapter adapter;
    int d_id;
    Typeface typeface;
    LoadingDialog loadingDialog;
    Alert alert;
    Toolbar toolbar;
    TextView disease_i, info_Insert;
    private androidx.appcompat.widget.AppCompatButton btnGetSelected;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disease);
        toolbar = findViewById(R.id.toolbar_id);
        setSupportActionBar(toolbar);
        this.btnGetSelected = findViewById(R.id.don_all);
        this.recyclerView = findViewById(R.id.disease_recycler);

//        getSupportActionBar().setTitle("Multiple Selection");
        disease_i = findViewById(R.id.disease_id);
        info_Insert = findViewById(R.id.infoInsert);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        adapter = new DiseaseAdapter(this, diseases);
        recyclerView.setAdapter(adapter);
        diseases = new ArrayList<>();
        //createList();

        fetchWeatherDetails();
        alert = new Alert(this);
        loadingDialog = new LoadingDialog(this);

        typeface = Typeface.createFromAsset(this.getAssets(), "fonts/Hacen-Algeria.ttf");
        btnGetSelected.setTypeface(typeface);
        disease_i.setTypeface(typeface);
        info_Insert.setTypeface(typeface);
        btnGetSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (adapter.getSelected().size() > 0) {
                    ArrayList<Integer> arr = new ArrayList<>();
                    for (int i = 0; i < adapter.getSelected().size(); i++) {
                        arr.add(adapter.getSelected().get(i).getId());
//                        stringBuilder.append("\n");
                    }
                    showToast(arr);
                } else {

                    //showToast("No Selection");
                    Intent intent = new Intent(DiseaseActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    private void fetchWeatherDetails() {
        //  final ProgressDialog progressDoalog;
      /*  progressDoalog = new ProgressDialog(DiseaseActivity.this);
        progressDoalog.setMax(100);
        progressDoalog.setMessage("loading....");
//        progressDoalog.setTitle("ProgressDialog bar example");
        progressDoalog.show();
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);*/
//        loadingDialog.startLoadingDialog();

        ApiInterface apiService = ApiClient.getRetrofitClient().create(ApiInterface.class);
        Call<List<Disease>> call = apiService.getDisease();

        call.enqueue(new Callback<List<Disease>>() {
            @Override
            public void onResponse(Call<List<Disease>> call, Response<List<Disease>> response) {
                // progressDoalog.dismiss();
//               loadingDialog.dismissDialog();
                diseases = response.body();
                adapter.setDiseases(diseases);

            }

            @Override
            public void onFailure(Call<List<Disease>> call, Throwable t) {
                //progressDoalog.dismiss();
               // loadingDialog.dismissDialog();
                Log.d("TAG", "Response = " + t.toString());
            }
        });

    }

    private void createList() {
        diseases = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Disease disease = new Disease();
            disease.setName("المرض " + (i + 1));
            disease.setId(i + 1);
            // for example to show at least one selection
//            if (i == 0) {
//                disease.setChecked(true);
//            }
            diseases.add(disease);
        }
        adapter.setDiseases(diseases);
    }

    SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

    private void showToast(ArrayList<Integer> des) {
        //  Toast.makeText(this, des + "", Toast.LENGTH_SHORT).show();
        final ProgressDialog progressDoalog;
      /*  progressDoalog = new ProgressDialog(DiseaseActivity.this);
        progressDoalog.setMax(100);
        progressDoalog.setMessage("loading....");
        progressDoalog.show();*/
        loadingDialog.startLoadingDialog();
        final String id = sharedPreferences.getString(USER_ID, "id");
        ApiInterface apiService = ApiClient.getRetrofitClient().create(ApiInterface.class);
        Call<Disease> call = apiService.getDiseaseRegi(id, des);
        call.enqueue(new Callback<Disease>() {
            @Override
            public void onResponse(Call<Disease> call, Response<Disease> response) {

                if (response.isSuccessful()) {
                    //  progressDoalog.dismiss();
                    loadingDialog.dismissDialog();

                    // Toast.makeText(DiseaseActivity.this, "done", Toast.LENGTH_SHORT).show();
                    alert.showAlertSuccess("");
                    Intent intent = new Intent(DiseaseActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                }

            }

            @Override
            public void onFailure(Call<Disease> call, Throwable t) {
                // progressDoalog.dismiss();
             //   loadingDialog.dismissDialog();

                // Toast.makeText(DiseaseActivity.this, "خطاء في النظام الخارجي" + t, Toast.LENGTH_SHORT).show();
                alert.showAlertError("تــأكد من إتصالك بالإنترنت");

            }
        });
    }

}
