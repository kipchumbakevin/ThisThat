package com.thisthat.thisthat.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.thisthat.thisthat.R;
import com.thisthat.thisthat.WouldYou;
import com.thisthat.thisthat.models.WouldYouRatherModel;

import java.util.ArrayList;

public class WouldYouRatherAdapter extends RecyclerView.Adapter<WouldYouRatherAdapter.WouldHolder> {
    private final ArrayList<WouldYouRatherModel> mWouldArrayList;
    private final Context mContext;
    private final LayoutInflater mLayoutInflator;

    public WouldYouRatherAdapter(Context context, ArrayList<WouldYouRatherModel>arrayList){
        mWouldArrayList = arrayList;
        mContext = context;
        mLayoutInflator = LayoutInflater.from(mContext);
    }
    @NonNull
    @Override
    public WouldHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mLayoutInflator.inflate(R.layout.would,parent,false);
        return new WouldHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WouldHolder holder, int position) {
        WouldYouRatherModel wouldYouRatherModel = mWouldArrayList.get(position);
        holder.title.setText("Scenario: "+wouldYouRatherModel.getId());
        holder.a = wouldYouRatherModel.getOptionA();
        holder.b = wouldYouRatherModel.getOptionB();
        holder.pA = Integer.toString(wouldYouRatherModel.getPickA());
        holder.id = Integer.toString(wouldYouRatherModel.getId());
        holder.pB = Integer.toString(wouldYouRatherModel.getPickB());
        holder.t = Integer.toString(wouldYouRatherModel.getTotal());
    }

    @Override
    public int getItemCount() {
        return mWouldArrayList.size();
    }

    public class WouldHolder extends RecyclerView.ViewHolder {
        TextView title;
        String id,a,b,pA,pB,t;
        public WouldHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, WouldYou.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("ID",id);
                    mContext.startActivity(intent);
                }
            });
        }
    }
}
