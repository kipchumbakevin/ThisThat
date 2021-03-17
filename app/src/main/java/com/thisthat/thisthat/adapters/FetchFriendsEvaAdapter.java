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

import java.util.ArrayList;

public class FetchFriendsEvaAdapter extends RecyclerView.Adapter<FetchFriendsEvaAdapter.ViewHolder> {
    private final Context mContext;
    private final ArrayList<FetchResultsModel> mArrayList;
    private final LayoutInflater mLayoutInflator;

    public FetchFriendsEvaAdapter(Context context, ArrayList<FetchResultsModel>arrayList){
        mContext = context;
        mArrayList = arrayList;
        mLayoutInflator = LayoutInflater.from(mContext);
    }
    @NonNull
    @Override
    public FetchFriendsEvaAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mLayoutInflator.inflate(R.layout.see_results,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FetchFriendsEvaAdapter.ViewHolder holder, int position) {
        FetchResultsModel fetchResultsModel = mArrayList.get(position);
        holder.tv_a.setText(fetchResultsModel.getCategory().getOptionA());
        holder.tv_b.setText(fetchResultsModel.getCategory().getOptionB());
        if (fetchResultsModel.getSelf().getSelfChoice().equals(fetchResultsModel.getCategory().getOptionA())){
            holder.selfNumber.setText(fetchResultsModel.getSelf().getSelfPhone());
            holder.selfPick.setText(": Option A");
        }else {
            holder.selfNumber.setText(fetchResultsModel.getSelf().getSelfPhone());
            holder.selfPick.setText(": Option B");
        }
        if (fetchResultsModel.getEvaluatorChoice().equals(fetchResultsModel.getCategory().getOptionA())){
            holder.friend.setText("Your choice: Option A");
        }else {
            holder.friend.setText("Your choice: Option B");
        }
        try {
            Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(holder.selfNumber.getText().toString()));
            Cursor cursor = mContext.getContentResolver().query(uri,new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME},
                    null,null,null);
            if (cursor != null){
                if (cursor.moveToFirst()){
                    String nameC = cursor.getString(0);
                    holder.selfNumber.setText(nameC);
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
        TextView tv_a,tv_b,friend,selfNumber,selfPick;
        ImageView correct,wrong;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_a = itemView.findViewById(R.id.tv_a);
            tv_b = itemView.findViewById(R.id.tv_b);
            friend = itemView.findViewById(R.id.self);
            selfNumber = itemView.findViewById(R.id.friendNumber);
            selfPick = itemView.findViewById(R.id.friendPick);
            correct = itemView.findViewById(R.id.correct);
            wrong = itemView.findViewById(R.id.wrong);
        }
    }
}
