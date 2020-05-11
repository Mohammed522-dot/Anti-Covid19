package com.example.makank.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.makank.Alert;
import com.example.makank.LoadingDialog;
import com.example.makank.R;
import com.example.makank.data.model.Request;
import com.example.makank.data.network.ApiClient;
import com.example.makank.data.network.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.makank.SharedPref.SHARED_PREF_NAME;
import static com.example.makank.SharedPref.USER_ID;
import static com.example.makank.SharedPref.mCtx;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.RequestViewHolder> {
    private List<Request> requestList;
    private Context context;
    private Boolean accept;
    LoadingDialog loadingDialog;
    Alert alert;

    public RequestAdapter(List<Request> requestList, Context context) {
        this.context = context;
        this.requestList = requestList;
    }

    public void setRequestList(List<Request> requestList) {
        this.requestList = new ArrayList<>();
        this.requestList = requestList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RequestAdapter.RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.rquest_item, parent, false);

        return new RequestAdapter.RequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestAdapter.RequestViewHolder holder, int position) {
        //final Request item = requestList.get(position);
        holder.sender.setText("أرسل إاليك : " + requestList.get(position).getSender_name());
        holder.owner_id.setText(Integer.toString(requestList.get(position).getOwner_id()));
        holder.conf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accept = true;
                SharedPreferences sharedPreference = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
                final String my_id = sharedPreference.getString(USER_ID, "id");
                final int owner_id = requestList.get(position).getOwner_id();
//                Toast.makeText(context, accept + "", Toast.LENGTH_SHORT).show();

                postRequest(my_id, owner_id, accept);
            }
        });
        holder.reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accept = false;
                SharedPreferences sharedPreference = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
                final String my_id = sharedPreference.getString(USER_ID, "id");
                final int owner_id = requestList.get(position).getOwner_id();
                postRequest(my_id, owner_id, accept);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (requestList != null) {
            return requestList.size();
        } else {
            return 0;
        }
    }

    public class RequestViewHolder extends RecyclerView.ViewHolder {
        TextView sender, owner_id, txtId, txtInfo;
        Button conf, reject;
        Typeface typeface;

        public RequestViewHolder(@NonNull View itemView) {
            super(itemView);

            sender = itemView.findViewById(R.id.sender_name);
            owner_id = itemView.findViewById(R.id.own_id);
            conf = itemView.findViewById(R.id.conf_btn);
            reject = itemView.findViewById(R.id.reject_btn);
            txtId = itemView.findViewById(R.id.txt2);
            txtInfo = itemView.findViewById(R.id.txt7);
            typeface = Typeface.createFromAsset(context.getAssets(), "fonts/Hacen-Algeria.ttf");
            sender.setTypeface(typeface);
            owner_id.setTypeface(typeface);
            conf.setTypeface(typeface);
            reject.setTypeface(typeface);
            txtId.setTypeface(typeface);
            txtInfo.setTypeface(typeface);
        }
    }

    private void postRequest(String my_id, int owner_id, Boolean accept) {

       /* final ProgressDialog progressDoalog;
        progressDoalog = new ProgressDialog(context);
        progressDoalog.setMax(100);
        progressDoalog.setMessage("loading....");*/
        ApiInterface apiService = ApiClient.getRetrofitClient().create(ApiInterface.class);
        Call<Request> call = apiService.getRequest(my_id, owner_id, accept);
        //  progressDoalog.show();
        loadingDialog = new LoadingDialog((Activity) context);
        alert = new Alert((Activity) context);
        loadingDialog.startLoadingDialog();
        call.enqueue(new Callback<Request>() {
            @Override
            public void onResponse(Call<Request> call, Response<Request> response) {

                if (response.isSuccessful()) {
                    loadingDialog.dismissDialog();
                    if (response.body().equals(true))
                    //  progressDoalog.dismiss();
                    alert.showAlertSuccess("تم قبول الطلب");
                    else
                        alert.showAlertSuccess("تم رفض الطلب");
                    //Toast.makeText(context, "don", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Request> call, Throwable t) {
                //   progressDoalog.dismiss();
                loadingDialog.dismissDialog();
                // Toast.makeText(context, "خطاء في النظام الخارجي", Toast.LENGTH_SHORT).show();
                alert.showAlertError("خطاء في النظام الخارجي");

            }
        });
    }
}
