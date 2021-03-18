package com.thisthat.thisthat.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hbb20.CountryCodePicker;
import com.thisthat.thisthat.MainActivity;
import com.thisthat.thisthat.R;
import com.thisthat.thisthat.ThemCategories;
import com.thisthat.thisthat.models.ContactModel;
import com.thisthat.thisthat.models.GetUserModel;
import com.thisthat.thisthat.networking.RetrofitClient;
import com.thisthat.thisthat.utils.SharedPreferencesConfig;

import java.lang.reflect.Array;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {
    Activity activity;
    ArrayList<ContactModel>arrayList;
    public MainAdapter(Activity activity,ArrayList<ContactModel>arrayList){
        this.activity = activity;
        this.arrayList = arrayList;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ContactModel contactModel = arrayList.get(position);
        holder.tvNumber.setText(contactModel.getNumber());
        holder.tvName.setText(contactModel.getName());
        holder.ccp.registerCarrierNumberEditText(holder.phone);
        holder.phone.setText(contactModel.getNumber());
        holder.ccp.detectSIMCountry(true);
        holder.sharedPreferencesConfig = new SharedPreferencesConfig(activity);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName,tvNumber;
        CountryCodePicker ccp;
        ProgressBar progressBar;
        EditText phone;
        SharedPreferencesConfig sharedPreferencesConfig;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvNumber = itemView.findViewById(R.id.tv_number);
            ccp = itemView.findViewById(R.id.ccp);
            progressBar = itemView.findViewById(R.id.progress);
            phone = itemView.findViewById(R.id.phone);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String phone = ccp.getFullNumberWithPlus();
                    if (phone.equals(sharedPreferencesConfig.readClientsPhone())){
                        Toast.makeText(activity, "You can not re-evaluate yourself", Toast.LENGTH_SHORT).show();
                    }else {
                        getUser();
                    }
                }
            });
        }

        private void getUser() {
        String phone = ccp.getFullNumberWithPlus();
        progressBar.setVisibility(View.VISIBLE);
        Call<GetUserModel> call = RetrofitClient.getInstance(activity)
                .getApiConnector()
                .getuser(phone);
        call.enqueue(new Callback<GetUserModel>() {
            @Override
            public void onResponse(Call<GetUserModel> call, Response<GetUserModel> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    if (response.body().getComplete()<1){
                        AlertDialog.Builder al = new AlertDialog.Builder(activity);
                        al.setTitle("Info")
                                .setMessage("It seems your friend has not evaluated themselves in at least one category.\nTell your friend to complete each category or at least lifestyle/personality category")
                                .setPositiveButton("Cool", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                });
                        AlertDialog alertDialog = al.create();
                        alertDialog.setCancelable(false);
                        alertDialog.show();
                    }else if (response.body().getComplete() >=1){
                        sharedPreferencesConfig.saveFriend(phone);
                        Intent intent = new Intent(activity, ThemCategories.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("FRIEND",Integer.toString(1));
                        activity.startActivity(intent);
                    }


                } else if (response.code() == 404){
                    AlertDialog.Builder al = new AlertDialog.Builder(activity);
                    al.setTitle("Info")
                            .setMessage("It seems your friend has not registered yet. Invite them and enjoy the fun")
                            .setPositiveButton("Invite", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent intent = new Intent(Intent.ACTION_SEND);
                                    intent.setType("text/plain");
                                    String shareBody = "You are missing out the fun. Join me now\n" +
                                            "Download ThisThat App now at https://play.google.com/store/apps/details?id=" + activity.getPackageName();
                                    intent.putExtra(Intent.EXTRA_SUBJECT,activity.getString(R.string.app_name));
                                    intent.putExtra(Intent.EXTRA_TEXT, shareBody);
                                    activity.startActivity(Intent.createChooser(intent, "Share via"));
                                    dialogInterface.dismiss();
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });
                    AlertDialog alertDialog = al.create();
                    alertDialog.setCancelable(false);
                    alertDialog.show();
                }
                else {
                    Toast.makeText(activity, "Server error,retry", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<GetUserModel> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(activity, "Network error. Check your connection", Toast.LENGTH_LONG).show();
            }
        });
    }
    }
}
