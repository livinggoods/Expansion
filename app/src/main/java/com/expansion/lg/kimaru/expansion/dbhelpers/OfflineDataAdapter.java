package com.expansion.lg.kimaru.expansion.dbhelpers;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.SparseBooleanArray;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.expansion.lg.kimaru.expansion.R;
import com.expansion.lg.kimaru.expansion.mzigos.ChewReferral;
import com.expansion.lg.kimaru.expansion.mzigos.OfflineEntity;
import com.expansion.lg.kimaru.expansion.mzigos.Registration;
import com.expansion.lg.kimaru.expansion.other.CircleTransform;
import com.expansion.lg.kimaru.expansion.other.FlipAnimator;
import com.expansion.lg.kimaru.expansion.tables.ChewReferralTable;
import com.expansion.lg.kimaru.expansion.tables.RegistrationTable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kimaru on 5/12/17.
 */

public class OfflineDataAdapter extends RecyclerView.Adapter<OfflineDataAdapter.ListHolder>{
    private Context mContext;
    public List<OfflineEntity> data;

    public class ListHolder extends RecyclerView.ViewHolder {

        public TextView tvName;
        public TextView tvSyncTime;
        public TextView tvAllRecordCount;
        public TextView tvPendingRecords;
        Button btnSyncRecords;
        public ListHolder (View view){
            super(view);
            this.tvName = (TextView) view.findViewById(R.id.tv_entity_name);
            this.tvSyncTime = (TextView) view.findViewById(R.id.tv_entity_last_sync_time);
            this.tvAllRecordCount = (TextView) view.findViewById(R.id.tv_all_records);
            this.tvPendingRecords = (TextView) view.findViewById(R.id.tv_offline_records);
        }
    }
    public OfflineDataAdapter(Context mContext, List<OfflineEntity> data){
        this.mContext = mContext;
        this.data = data;
    }

    @Override
    public ListHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_item_entity, parent, false);
        return new ListHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ListHolder holder, final int position){
        final OfflineEntity datum = data.get(position);

        holder.tvName.setText(datum.getName());
        holder.tvSyncTime.setText(datum.getLastSync() + "");
        holder.tvAllRecordCount.setText(datum.getAllRecords()+"");
        holder.tvPendingRecords.setText(datum.getPendingRecords()+"");
    }


    @Override
    public int getItemCount() {
        return data.size();
    }
}