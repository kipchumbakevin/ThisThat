package com.thisthat.thisthat.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.thisthat.thisthat.AnswerNever;
import com.thisthat.thisthat.R;
import com.thisthat.thisthat.ThemCategories;
import com.thisthat.thisthat.WouldYou;
import com.thisthat.thisthat.models.GetUserModel;
import com.thisthat.thisthat.models.MessagesModel;
import com.thisthat.thisthat.models.NeverModel;
import com.thisthat.thisthat.networking.RetrofitClient;
import com.thisthat.thisthat.utils.SharedPreferencesConfig;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NeverAdapter extends RecyclerView.Adapter<NeverAdapter.ViewHolder> {
    private final Context mContext;
    private final ArrayList<NeverModel> mNeverArray;
    private final LayoutInflater mLayoutInflator;
    SharedPreferencesConfig sharedPreferencesConfig;

    public NeverAdapter(Context context, ArrayList<NeverModel>arrayList){
        mContext = context;
        mNeverArray = arrayList;
        mLayoutInflator = LayoutInflater.from(mContext);
        sharedPreferencesConfig = new SharedPreferencesConfig(mContext);
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = mLayoutInflator.inflate(R.layout.would,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NeverModel neverModel = mNeverArray.get(position);
        holder.title.setText("Scenario: "+neverModel.getId());
        holder.id = Integer.toString(neverModel.getId());



    }

    @Override
    public int getItemCount() {
        return mNeverArray.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        String id;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, AnswerNever.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("ID", id);
                    mContext.startActivity(intent);
                }
            });


        }
    }
}
