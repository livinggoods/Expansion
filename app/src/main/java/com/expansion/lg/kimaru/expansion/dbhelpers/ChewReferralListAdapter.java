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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.expansion.lg.kimaru.expansion.R;
import com.expansion.lg.kimaru.expansion.mzigos.ChewReferral;
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

public class ChewReferralListAdapter extends RecyclerView.Adapter<ChewReferralListAdapter.ListHolder>{
    private Context mContext;
    private List<ChewReferral> chewReferrals;
    private ChewReferralListAdapterListener listener;
    private SparseBooleanArray selectedItems;

    private ChewReferralTable chewReferralTable;
    private ChewReferral chewReferral;

    //array to perform multiple actions at once
    private SparseBooleanArray animationItemsIndex;
    private boolean reverseAllActions = false;

    Resources res;

    // index is used to animate only the selected row
    // @TODO: Get a better soln for selected items
    private static int currentSelectedIndex = -1;

    public class ListHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        public TextView name, subject, message, iconText, timestamp;
        public ImageView iconImp, imgProfile;
        public LinearLayout registrationContainser;
        public RelativeLayout iconContainer, iconBack, iconFront;

        public ListHolder (View view){
            super(view);
            name = (TextView) view.findViewById(R.id.from);
            subject = (TextView) view.findViewById(R.id.txt_primary);
            message = (TextView) view.findViewById(R.id.txt_secondary);
            iconText = (TextView) view.findViewById(R.id.icon_text);
            timestamp = (TextView) view.findViewById(R.id.timestamp);
            iconBack = (RelativeLayout) view.findViewById(R.id.icon_back);
            iconFront = (RelativeLayout) view.findViewById(R.id.icon_front);
            iconImp = (ImageView) view.findViewById(R.id.icon_star);
            imgProfile = (ImageView) view.findViewById(R.id.icon_profile);
            registrationContainser = (LinearLayout) view.findViewById(R.id.message_container);
            iconContainer = (RelativeLayout) view.findViewById(R.id.icon_container);
            view.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View view){
            listener.onRowLongClicked(getAdapterPosition());
            view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
            return true;
        }
    }
    public ChewReferralListAdapter(Context mContext, List<ChewReferral> chewReferrals, ChewReferralListAdapterListener listener){
        this.mContext = mContext;
        this.chewReferrals = chewReferrals;
        this.listener = listener;
        selectedItems = new SparseBooleanArray();
        animationItemsIndex = new SparseBooleanArray();
        this.res = mContext.getResources();
        this.chewReferralTable = new ChewReferralTable(mContext);
    }

    @Override
    public ListHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.registration_list_row, parent, false);
        return new ListHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ListHolder holder, final int position){
        ChewReferral chewReferral = chewReferrals.get(position);

        //// displaying text view data
        // Display the applicant Name

        holder.name.setText(chewReferral.getName());
        holder.subject.setText(chewReferral.getPhone());
        holder.message.setText(chewReferral.getTitle());
        holder.timestamp.setText(chewReferral.getRecruitmentId());

        // displaying the first letter of From in icon text
        holder.iconText.setText(String.valueOf(chewReferral.getName().substring(0,1)));

        // change the row state to activated
        holder.itemView.setActivated(selectedItems.get(position, false));

        //change the fontstyle depending on message read status (change this to whether passed or not)
        applyReadStatus(holder, chewReferral);

        // handle message star
        applyImportant(holder, chewReferral);

        // handle icon animation
        applyIconAnimation(holder, position);

        // display profile image
        applyProfilePicture(holder, chewReferral);

        // apply click events
        applyClickEvents(holder, position);
    }

    private void applyClickEvents(ListHolder holder, final int position){
        holder.iconContainer.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                listener.onIconClicked(position);
            }
        });
        holder.iconImp.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                listener.onIconImportantClicked(position);
            }
        });
        holder.registrationContainser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onMessageRowClicked(position);
            }
        });
        holder.registrationContainser.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View view){
                listener.onRowLongClicked(position);
                view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
                return true;
            }
        });
    }
    private void applyProfilePicture(ListHolder holder, ChewReferral chewReferral) {
        holder.imgProfile.setImageResource(R.drawable.bg_circle);
        holder.imgProfile.setColorFilter(chewReferral.getColor());
        holder.iconText.setVisibility(View.VISIBLE);
    }

    private void applyIconAnimation(ListHolder holder, int position) {
        if (selectedItems.get(position, false)) {
            holder.iconFront.setVisibility(View.GONE);
            resetIconYAxis(holder.iconBack);
            holder.iconBack.setVisibility(View.VISIBLE);
            holder.iconBack.setAlpha(1);
            if (currentSelectedIndex == position) {
                FlipAnimator.flipView(mContext, holder.iconBack, holder.iconFront, true);
                resetCurrentIndex();
            }
        } else {
            holder.iconBack.setVisibility(View.GONE);
            resetIconYAxis(holder.iconFront);
            holder.iconFront.setVisibility(View.VISIBLE);
            holder.iconFront.setAlpha(1);
            if ((reverseAllActions && animationItemsIndex.get(position, false)) || currentSelectedIndex == position) {
                FlipAnimator.flipView(mContext, holder.iconBack, holder.iconFront, false);
                resetCurrentIndex();
            }
        }
    }


    // As the views will be reused, sometimes the icon appears as
    // flipped because older view is reused. Reset the Y-axis to 0
    private void resetIconYAxis(View view) {
        if (view.getRotationY() != 0) {
            view.setRotationY(0);
        }
    }

    public void resetAnimationIndex() {
        reverseAllActions = false;
        animationItemsIndex.clear();
    }


    private void applyImportant(ListHolder holder, ChewReferral chewReferral) {
        holder.iconImp.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_star_black_24dp));
        holder.iconImp.setColorFilter(ContextCompat.getColor(mContext, R.color.icon_tint_selected));
    }

    private void applyReadStatus(ListHolder holder, ChewReferral chewReferral) {
        holder.name.setTypeface(null, Typeface.NORMAL);
        holder.subject.setTypeface(null, Typeface.NORMAL);
        holder.name.setTextColor(ContextCompat.getColor(mContext, R.color.subject));
        holder.subject.setTextColor(ContextCompat.getColor(mContext, R.color.message));
    }

    @Override
    public int getItemCount() {
        return chewReferrals.size();
    }

    public void toggleSelection(int pos) {
        currentSelectedIndex = pos;
        if (selectedItems.get(pos, false)) {
            selectedItems.delete(pos);
            animationItemsIndex.delete(pos);
        } else {
            selectedItems.put(pos, true);
            animationItemsIndex.put(pos, true);
        }
        notifyItemChanged(pos);
    }

    public void clearSelections() {
        reverseAllActions = true;
        selectedItems.clear();
        notifyDataSetChanged();
    }

    public int getSelectedItemCount() {
        return selectedItems.size();
    }

    public List<Integer> getSelectedItems() {
        List<Integer> items =
                new ArrayList<>(selectedItems.size());
        for (int i = 0; i < selectedItems.size(); i++) {
            items.add(selectedItems.keyAt(i));
        }
        return items;
    }

    public void removeData(int position) {
        chewReferrals.remove(position);
        resetCurrentIndex();
    }

    private void resetCurrentIndex() {
        currentSelectedIndex = -1;
    }

    public interface ChewReferralListAdapterListener {
        void onIconClicked(int position);

        void onIconImportantClicked(int position);

        void onMessageRowClicked(int position);

        void onRowLongClicked(int position);
    }
}