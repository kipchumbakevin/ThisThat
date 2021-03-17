package com.thisthat.thisthat.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.thisthat.thisthat.Celeb;
import com.thisthat.thisthat.R;
import com.thisthat.thisthat.models.AllCelebsModel;

import java.util.ArrayList;

public class FriendCelebAdapter extends RecyclerView.Adapter<FriendCelebAdapter.AllCelebViewHolder> {
    private final Context mContext;
    private final ArrayList<AllCelebsModel> mArrayList;
    private final LayoutInflater mLayoutInflator;

    public FriendCelebAdapter(Context context, ArrayList<AllCelebsModel>arrayList){
        mContext = context;
        mArrayList = arrayList;
        mLayoutInflator = LayoutInflater.from(mContext);
    }
    @NonNull
    @Override
    public FriendCelebAdapter.AllCelebViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mLayoutInflator.inflate(R.layout.would,parent,false);
        return new AllCelebViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendCelebAdapter.AllCelebViewHolder holder, int position) {
        AllCelebsModel allCelebsModel = mArrayList.get(position);
        holder.title.setText("Celeb: "+allCelebsModel.getId());
        holder.id = Integer.toString(allCelebsModel.getId());
    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }

    public class AllCelebViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        String id;
        public AllCelebViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, Celeb.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("FRIEND",Integer.toString(1));
                    intent.putExtra("ID",id);
                    mContext.startActivity(intent);
                }
            });
        }
    }
}
