package com.thisthat.thisthat.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.thisthat.thisthat.AllOthersActivity;
import com.thisthat.thisthat.R;
import com.thisthat.thisthat.models.FetchAllModel;

import java.util.ArrayList;

public class FriendLifestyleAdapter extends RecyclerView.Adapter<FriendLifestyleAdapter.FetchViewHolder> {
    private final Context mContext;
    private final ArrayList<FetchAllModel> mArrayList;
    private final LayoutInflater mLayoutInflator;

    public FriendLifestyleAdapter(Context context, ArrayList<FetchAllModel> arrayList){
        mContext = context;
        mArrayList = arrayList;
        mLayoutInflator = LayoutInflater.from(mContext);
    }
    @NonNull
    @Override
    public FriendLifestyleAdapter.FetchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mLayoutInflator.inflate(R.layout.would,parent,false);
        return new FetchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendLifestyleAdapter.FetchViewHolder holder, int position) {
        FetchAllModel fetchAllModel = mArrayList.get(position);
        holder.title.setText("Lifestyle/Personality: "+fetchAllModel.getId());
        holder.id = Integer.toString(fetchAllModel.getId());
        holder.optionA = fetchAllModel.getOptionA();
        holder.optionB = fetchAllModel.getOptionB();
    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }

    public class FetchViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        String id,optionA,optionB;
        public FetchViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, AllOthersActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("ME",Integer.toString(1));
                    intent.putExtra("A",optionA);
                    intent.putExtra("FRIEND",Integer.toString(1));
                    intent.putExtra("B",optionB);
                    intent.putExtra("ID",id);
                    mContext.startActivity(intent);
                }
            });
        }
    }
}