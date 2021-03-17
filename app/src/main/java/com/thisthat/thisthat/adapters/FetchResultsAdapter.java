package com.thisthat.thisthat.adapters;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.thisthat.thisthat.R;
import com.thisthat.thisthat.models.FetchResultsModel;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class FetchResultsAdapter extends RecyclerView.Adapter<FetchResultsAdapter.ViewHolder> {
    private final Context mContext;
    private final ArrayList<FetchResultsModel> mArrayList;
    private final LayoutInflater mLayoutInflator;

    public FetchResultsAdapter(Context context, ArrayList<FetchResultsModel>arrayList){
        mContext = context;
        mArrayList = arrayList;
        mLayoutInflator = LayoutInflater.from(mContext);
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mLayoutInflator.inflate(R.layout.see_results,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FetchResultsModel fetchResultsModel = mArrayList.get(position);
        holder.tv_a.setText(fetchResultsModel.getCategory().getOptionA());
        holder.tv_b.setText(fetchResultsModel.getCategory().getOptionB());
        if (fetchResultsModel.getSelf().getSelfChoice().equals(fetchResultsModel.getCategory().getOptionA())){
            holder.self.setText("Your choice: Option A");
        }else {
            holder.self.setText("Your choice: Option B");
        }
        if (fetchResultsModel.getEvaluatorChoice().equals(fetchResultsModel.getCategory().getOptionA())){
            holder.friendNumber.setText(fetchResultsModel.getEvaluatorPhone());
            holder.friendPick.setText(": option A");
        }else {
            holder.friendNumber.setText(fetchResultsModel.getEvaluatorPhone());
            holder.friendPick.setText(": option B");
        }
        try {
            Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(holder.friendNumber.getText().toString()));
            Cursor cursor = mContext.getContentResolver().query(uri,new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME},
                    null,null,null);
            if (cursor != null){
                if (cursor.moveToFirst()){
                    String nameC = cursor.getString(0);
                    holder.friendNumber.setText(nameC);
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        if (fetchResultsModel.getEvaluatorChoice().equals(fetchResultsModel.getSelf().getSelfChoice())){
            holder.correct.setVisibility(View.VISIBLE);
        }else if (!fetchResultsModel.getEvaluatorChoice().equals(fetchResultsModel.getSelf().getSelfChoice())){
            holder.wrong.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_a,tv_b,self,friendNumber,friendPick;
        ImageView correct,wrong;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_a = itemView.findViewById(R.id.tv_a);
            tv_b = itemView.findViewById(R.id.tv_b);
            self = itemView.findViewById(R.id.self);
            friendNumber = itemView.findViewById(R.id.friendNumber);
            friendPick = itemView.findViewById(R.id.friendPick);
            correct = itemView.findViewById(R.id.correct);
            wrong = itemView.findViewById(R.id.wrong);
        }
    }
}
