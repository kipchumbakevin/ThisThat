package com.thisthat.thisthat.adapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.thisthat.thisthat.CheckResults;
import com.thisthat.thisthat.R;
import com.thisthat.thisthat.models.FetchContactListModel;
import com.thisthat.thisthat.utils.SharedPreferencesConfig;

import java.util.ArrayList;

public class ThemAdapter extends RecyclerView.Adapter<ThemAdapter.ViewHolder> {
    private final Context mContext;
    private final ArrayList<FetchContactListModel> mArrayList;
    private final LayoutInflater mLayoutInflator;

    public ThemAdapter(Context context, ArrayList<FetchContactListModel> arrayList) {
        mContext = context;
        mArrayList = arrayList;
        mLayoutInflator = LayoutInflater.from(mContext);
    }

    @NonNull
    @Override
    public ThemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mLayoutInflator.inflate(R.layout.view_contacts, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ThemAdapter.ViewHolder holder, int position) {
        FetchContactListModel fetchResultsModel = mArrayList.get(position);
        SharedPreferencesConfig sharedPreferencesConfig = new SharedPreferencesConfig(mContext);
        if (fetchResultsModel.getEvaluateePhone().equals(sharedPreferencesConfig.readClientsPhone())) {
            holder.tvName.setText(fetchResultsModel.getEvaluatorPhone());
            holder.tvNumber.setText(fetchResultsModel.getEvaluatorPhone());
            holder.friendPhone = fetchResultsModel.getEvaluatorPhone();
        }else {
            holder.tvName.setText(fetchResultsModel.getEvaluateePhone());
            holder.tvNumber.setText(fetchResultsModel.getEvaluateePhone());
            holder.friendPhone = fetchResultsModel.getEvaluateePhone();
        }
        holder.cat = Integer.toString(fetchResultsModel.getSelfId());
        try {
            Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(holder.tvName.getText().toString()));
            Cursor cursor = mContext.getContentResolver().query(uri,new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME},
                    null,null,null);
            if (cursor != null){
                if (cursor.moveToFirst()){
                    String nameC = cursor.getString(0);
                    holder.tvName.setText(nameC);
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvNumber;
        String friendPhone,cat;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvNumber = itemView.findViewById(R.id.tv_number);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, CheckResults.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("FRIENDPHONE",friendPhone);
                    intent.putExtra("CAT",cat);
                    intent.putExtra("ME",Integer.toString(2));
                    mContext.startActivity(intent);
                }
            });
        }
    }
}