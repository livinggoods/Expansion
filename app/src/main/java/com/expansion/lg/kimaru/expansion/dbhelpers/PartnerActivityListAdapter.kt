package com.expansion.lg.kimaru.expansion.dbhelpers

import android.content.Context
import android.content.res.Resources
import android.graphics.Typeface
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.util.Log
import android.util.SparseBooleanArray
import android.view.HapticFeedbackConstants
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView

import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.expansion.lg.kimaru.expansion.R
import com.expansion.lg.kimaru.expansion.mzigos.PartnerActivity
import com.expansion.lg.kimaru.expansion.mzigos.Partners
import com.expansion.lg.kimaru.expansion.other.CircleTransform
import com.expansion.lg.kimaru.expansion.other.DisplayDate
import com.expansion.lg.kimaru.expansion.other.FlipAnimator
import com.expansion.lg.kimaru.expansion.tables.PartnerActivityTable
import com.expansion.lg.kimaru.expansion.tables.PartnersTable

import java.util.ArrayList

/**
 * Created by kimaru on 3/12/17.
 */

class PartnerActivityListAdapter(private val mContext: Context, private val partnerActivities: MutableList<PartnerActivity>, private val listener: PartnerActivityListAdapterListener) : RecyclerView.Adapter<PartnerActivityListAdapter.ListHolder>() {
    private val selectedItems: SparseBooleanArray

    //array to perform multiple actions at once
    private val selectedItemsIndex: SparseBooleanArray
    private var reverseAllActions = false

    internal var res: Resources

    val selectedItemCount: Int
        get() = selectedItems.size()

    inner class ListHolder(view: View) : RecyclerView.ViewHolder(view), View.OnLongClickListener {
        var from: TextView
        var subject: TextView
        var message: TextView
        var iconText: TextView
        var timestamp: TextView
        var iconImp: ImageView
        var imgProfile: ImageView
        var registrationContainser: LinearLayout
        var iconContainer: RelativeLayout
        var iconBack: RelativeLayout
        var iconFront: RelativeLayout

        init {
            from = view.findViewById<View>(R.id.from) as TextView
            subject = view.findViewById<View>(R.id.txt_primary) as TextView
            message = view.findViewById<View>(R.id.txt_secondary) as TextView
            iconText = view.findViewById<View>(R.id.icon_text) as TextView
            timestamp = view.findViewById<View>(R.id.timestamp) as TextView
            iconBack = view.findViewById<View>(R.id.icon_back) as RelativeLayout
            iconFront = view.findViewById<View>(R.id.icon_front) as RelativeLayout
            iconImp = view.findViewById<View>(R.id.icon_star) as ImageView
            imgProfile = view.findViewById<View>(R.id.icon_profile) as ImageView
            registrationContainser = view.findViewById<View>(R.id.message_container) as LinearLayout
            iconContainer = view.findViewById<View>(R.id.icon_container) as RelativeLayout
            view.setOnLongClickListener(this)
        }

        override fun onLongClick(view: View): Boolean {
            listener.onRowLongClicked(adapterPosition)
            view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
            return true
        }
    }

    init {
        selectedItems = SparseBooleanArray()
        selectedItemsIndex = SparseBooleanArray()
        this.res = mContext.resources
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.registration_list_row, parent, false)
        return ListHolder(itemView)
    }

    override fun onBindViewHolder(holder: ListHolder, position: Int) {
        val partnerActivity = partnerActivities[position]
        val partnerActivityTable = PartnerActivityTable(mContext)
        Log.d("=====", "================================================================")
        Log.d("Tremap", partnerActivity.partnerId)
        Log.d("=====", "================================================================")

        val partner = PartnersTable(mContext).getPartnerById(partnerActivity.partnerId)

        //// displaying text view data
        holder.from.text = partner!!.partnerName
        holder.subject.text = partner.contactPerson
        holder.message.text = partner.contactPersonPhone

        holder.timestamp.text = DisplayDate(partner.dateAdded).dateAndTime()

        // displaying the first letter of From in icon text
        holder.iconText.text = partner.partnerName.substring(0, 1)

        // change the row state to activated
        holder.itemView.isActivated = selectedItems.get(position, false)

        //change the fontstyle depending on message read status (change this to whether passed or not)
        applyReadStatus(holder, partnerActivity)

        // handle message star
        applyImportant(holder, partnerActivity)

        // handle icon animation
        applyIconAnimation(holder, position)

        // display profile image
        applyProfilePicture(holder, partner)

        // apply click events
        applyClickEvents(holder, position)
    }

    private fun applyClickEvents(holder: ListHolder, position: Int) {
        holder.iconContainer.setOnClickListener { listener.onIconClicked(position) }
        holder.iconImp.setOnClickListener { listener.onIconImportantClicked(position) }
        holder.registrationContainser.setOnClickListener { listener.onMessageRowClicked(position) }
        holder.registrationContainser.setOnLongClickListener { view ->
            listener.onRowLongClicked(position)
            view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
            true
        }
    }

    private fun applyProfilePicture(holder: ListHolder, partner: Partners) {
        holder.imgProfile.setImageResource(R.drawable.bg_circle)
        holder.imgProfile.setColorFilter(partner.color)
        holder.iconText.visibility = View.VISIBLE
    }

    private fun applyIconAnimation(holder: ListHolder, position: Int) {
        if (selectedItems.get(position, false)) {
            holder.iconFront.visibility = View.GONE
            resetIconYAxis(holder.iconBack)
            holder.iconBack.visibility = View.VISIBLE
            holder.iconBack.alpha = 1f
            if (currentSelectedIndex == position) {
                FlipAnimator.flipView(mContext, holder.iconBack, holder.iconFront, true)
                resetCurrentIndex()
            }
        } else {
            holder.iconBack.visibility = View.GONE
            resetIconYAxis(holder.iconFront)
            holder.iconFront.visibility = View.VISIBLE
            holder.iconFront.alpha = 1f
            if (reverseAllActions && selectedItemsIndex.get(position, false) || currentSelectedIndex == position) {
                FlipAnimator.flipView(mContext, holder.iconBack, holder.iconFront, false)
                resetCurrentIndex()
            }
        }
    }


    // As the views will be reused, sometimes the icon appears as
    // flipped because older view is reused. Reset the Y-axis to 0
    private fun resetIconYAxis(view: View) {
        if (view.rotationY != 0f) {
            view.rotationY = 0f
        }
    }

    fun resetAnimationIndex() {
        reverseAllActions = false
        selectedItemsIndex.clear()
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    private fun applyImportant(holder: ListHolder, partnerActivity: PartnerActivity) {
        if (partnerActivity.isDoingIccm) {
            holder.iconImp.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_star_black_24dp))
            holder.iconImp.setColorFilter(ContextCompat.getColor(mContext, R.color.icon_tint_selected))
        } else {
            holder.iconImp.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_star_border_black_24dp))
            holder.iconImp.setColorFilter(ContextCompat.getColor(mContext, R.color.icon_tint_normal))
        }
    }

    private fun applyReadStatus(holder: ListHolder, partnerActivity: PartnerActivity) {
        if (partnerActivity.isDoingMhealth) {
            holder.from.setTypeface(null, Typeface.NORMAL)
            holder.subject.setTypeface(null, Typeface.NORMAL)
            holder.from.setTextColor(ContextCompat.getColor(mContext, R.color.subject))
            holder.subject.setTextColor(ContextCompat.getColor(mContext, R.color.message))
        } else {
            holder.from.setTypeface(null, Typeface.BOLD)
            holder.subject.setTypeface(null, Typeface.BOLD)
            holder.from.setTextColor(ContextCompat.getColor(mContext, R.color.from))
            holder.subject.setTextColor(ContextCompat.getColor(mContext, R.color.subject))
        }
    }

    override fun getItemCount(): Int {
        return partnerActivities.size
    }

    fun toggleSelection(pos: Int) {
        currentSelectedIndex = pos
        if (selectedItems.get(pos, false)) {
            selectedItems.delete(pos)
            selectedItemsIndex.delete(pos)
        } else {
            selectedItems.put(pos, true)
            selectedItemsIndex.put(pos, true)
        }
        notifyItemChanged(pos)
    }

    fun clearSelections() {
        reverseAllActions = true
        selectedItems.clear()
        notifyDataSetChanged()
    }

    fun getSelectedItems(): List<Int> {
        val items = ArrayList<Int>(selectedItems.size())
        for (i in 0 until selectedItems.size()) {
            items.add(selectedItems.keyAt(i))
        }
        return items
    }

    fun removeData(position: Int) {
        partnerActivities.removeAt(position)
        resetCurrentIndex()
    }

    private fun resetCurrentIndex() {
        currentSelectedIndex = -1
    }

    interface PartnerActivityListAdapterListener {
        fun onIconClicked(position: Int)

        fun onIconImportantClicked(position: Int)

        fun onMessageRowClicked(position: Int)

        fun onRowLongClicked(position: Int)
    }

    companion object {

        // index is used to animate only the selected row
        // @TODO: Get a better soln for selected items
        private var currentSelectedIndex = -1
    }
}
