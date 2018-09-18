package com.expansion.lg.kimaru.expansion.fragment

/**
 * Created by kimaru on 3/11/17.
 */

import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.expansion.lg.kimaru.expansion.R
import com.expansion.lg.kimaru.expansion.activity.MainActivity
import com.expansion.lg.kimaru.expansion.activity.SessionManagement
import com.expansion.lg.kimaru.expansion.dbhelpers.ChewReferralListAdapter
import com.expansion.lg.kimaru.expansion.mzigos.CommunityUnit
import com.expansion.lg.kimaru.expansion.mzigos.PartnerActivity
import com.expansion.lg.kimaru.expansion.mzigos.Registration
import com.expansion.lg.kimaru.expansion.tables.*
import java.util.*

// to show list in Gmail Mode
class CommunityUnitViewFragment : Fragment(), View.OnClickListener {

    private val recyclerView: RecyclerView? = null
    private val rAdapter: ChewReferralListAdapter? = null
    var swipeRefreshLayout: SwipeRefreshLayout? = null
    var registrationDetails: RelativeLayout? = null

    var communityUnit: CommunityUnit? = null

    private val partnerActivities = ArrayList<PartnerActivity>()
    private val communityUnits = ArrayList<CommunityUnit>()
    private var mListView: ListView? = null
    private val cuListView: ListView? = null


    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null

    private var mListener: RecruitmentsFragment.OnFragmentInteractionListener? = null
    internal lateinit var textContactPerson: TextView
    internal lateinit var textContactPersonPhone: TextView
    internal lateinit var textCommunityUnitName: TextView
    internal lateinit var textLinkFacility: TextView

    internal lateinit var textCounty: TextView
    internal lateinit var textSubCounty: TextView
    internal lateinit var textWard: TextView
    internal lateinit var textEconomicStatus: TextView
    internal lateinit var textMrdtDetails: TextView
    internal lateinit var textActDetails: TextView

    lateinit var subject: TextView
    lateinit var message: TextView
    lateinit var iconText: TextView
    lateinit var timestamp: TextView
    lateinit var iconImp: ImageView
    lateinit var imgProfile: ImageView
    lateinit var registrationContainser: LinearLayout
    lateinit var iconContainer: RelativeLayout
    lateinit var iconBack: RelativeLayout
    lateinit var iconFront: RelativeLayout


    // AppCompatActivity a = new AppCompatActivity();
    internal lateinit var adapter: PartnerActivityAdapter

    internal lateinit var session: SessionManagement
    internal lateinit var user: HashMap<String, String>
    internal var registrations: List<Registration> = ArrayList()

    internal var onRecruitmentRegistrationSummaryClickListener: View.OnClickListener = View.OnClickListener {
        val fragment = RegistrationsFragment()
        val fragmentTransaction = activity!!.supportFragmentManager.beginTransaction()
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                android.R.anim.fade_out)
        fragmentTransaction.replace(R.id.frame, fragment, MainActivity.TAG_REGISTRATIONS)
        fragmentTransaction.commitAllowingStateLoss()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = arguments!!.getString(ARG_PARAM1)
            mParam2 = arguments!!.getString(ARG_PARAM2)

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.communityunit_content, container, false)

        MainActivity.CURRENT_TAG = MainActivity.TAG_RECRUITMENTS
        MainActivity.backFragment = CommunityUnitsFragment()
        //session Management
        session = SessionManagement(context!!)
        user = session.userDetails
        if (communityUnit == null) {
            communityUnit = session.savedCommunityUnit
        }

        getPartnerActivities()

        mListView = v.findViewById<View>(R.id.partner_list_view) as ListView
        //        String[] listItems = new String[partnerActivities.size()];
        //        for (int i = 0; i < partnerActivities.size(); i++){
        //            PartnerActivity partnerActivity = partnerActivities.get(i);
        //            Partners partners = new PartnersTable(getContext())
        //                    .getPartnerById(partnerActivity.getPartnerId());
        //            listItems[i] = partners.getPartnerName();
        //        }
        adapter = PartnerActivityAdapter(context!!, partnerActivities)
        mListView!!.adapter = adapter

        textContactPerson = v.findViewById<View>(R.id.textContactPerson) as TextView
        textContactPersonPhone = v.findViewById<View>(R.id.textContactPersonPhone) as TextView
        textCommunityUnitName = v.findViewById<View>(R.id.textCommunityUnitName) as TextView
        textLinkFacility = v.findViewById<View>(R.id.textLinkFacility) as TextView

        val subCounty = SubCountyTable(context!!)
                .getSubCountyById(communityUnit!!.subCountyId)

        textCounty = v.findViewById<View>(R.id.textCounty) as TextView
        try {
            textCounty.text = KeCountyTable(context!!)
                    .getCountyById(Integer.valueOf(subCounty!!.countyID))!!
                    .countyName
        } catch (e: Exception) {
            textCounty.text = ""
        }

        textSubCounty = v.findViewById<View>(R.id.textSubCounty) as TextView
        textSubCounty.text = subCounty!!.subCountyName

        textWard = v.findViewById<View>(R.id.textWard) as TextView
        textWard.text = communityUnit!!.ward

        textEconomicStatus = v.findViewById<View>(R.id.textEconomicStatus) as TextView
        textEconomicStatus.text = communityUnit!!.economicStatus

        textMrdtDetails = v.findViewById<View>(R.id.textMrdtDetails) as TextView
        textMrdtDetails.text = communityUnit!!.privateFacilityForMrdt + " " + communityUnit!!.mrdtPrice

        textActDetails = v.findViewById<View>(R.id.textActDetails) as TextView
        textActDetails.text = communityUnit!!.privateFacilityForAct + " " + communityUnit!!.actPrice

        textCommunityUnitName.text = communityUnit!!.communityUnitName
        textContactPerson.text = communityUnit!!.areaChiefName
        textContactPersonPhone.text = communityUnit!!.areaChiefPhone
        try {
            textLinkFacility.text = LinkFacilityTable(context!!)
                    .getLinkFacilityById(communityUnit!!.linkFacilityId)!!.facilityName
        } catch (e: Exception) {
            textLinkFacility.text = "Link Facility not found"
        }


        val partnerView = v.findViewById<View>(R.id.cuPartnerActivitiesView) as RelativeLayout
        partnerView.setOnClickListener(this)

        return v
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        if (mListener != null) {
            mListener!!.onFragmentInteraction(uri)
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html) for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    private fun getPartnerActivities() {
        partnerActivities.clear()
        try {
            val activities = PartnerActivityTable(context!!)
                    .getPartnerActivityByField(PartnerActivityTable.COMMUNITYUNIT, communityUnit!!.id)
            for (pa in activities) {
                pa.color = getRandomMaterialColor("400")
                partnerActivities.add(pa)

            }
        } catch (e: Exception) {
        }

    }


    private fun getRandomMaterialColor(typeColor: String): Int {
        var returnColor = Color.GRAY
        val arrayId = resources.getIdentifier("mdcolor_$typeColor", "array", context!!.packageName)

        if (arrayId != 0) {
            val colors = resources.obtainTypedArray(arrayId)
            val index = (Math.random() * colors.length()).toInt()
            returnColor = colors.getColor(index, Color.GRAY)
            colors.recycle()
        }
        return returnColor
    }

    override fun onClick(view: View) {
        val fragment: Fragment
        val fragmentTransaction: FragmentTransaction
        when (view.id) {
            R.id.addReferrals -> {
                val newChewReferralFragment = NewChewReferralFragment()
                newChewReferralFragment.createdFromRecruitment = true
                fragment = newChewReferralFragment
                fragmentTransaction = activity!!.supportFragmentManager.beginTransaction()
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out)
                fragmentTransaction.replace(R.id.frame, fragment, MainActivity.TAG_NEW_RECRUITMENT)
                fragmentTransaction.commitAllowingStateLoss()
            }
            R.id.cuPartnerActivitiesView -> {
                val newPartnerActivityFragment = NewPartnerActivityFragment()
                newPartnerActivityFragment.backFragment = CommunityUnitViewFragment()
                newPartnerActivityFragment.communityUnit = communityUnit
                fragment = newPartnerActivityFragment
                fragmentTransaction = activity!!.supportFragmentManager.beginTransaction()
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out)
                fragmentTransaction.replace(R.id.frame, fragment, MainActivity.TAG_NEW_RECRUITMENT)
                fragmentTransaction.commitAllowingStateLoss()
            }
        }
    }

    inner class PartnerActivityAdapter(private val _context: Context, private val values: List<PartnerActivity>) : ArrayAdapter<PartnerActivity>(context, -1, values) {
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val inflater = _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            //View rowView = inflater.inflate(R.layout.rowlayout, parent, false);
            val ps = values[position]
            val rowView = inflater.inflate(R.layout.rowlayout, parent, false)
            // name = (TextView) rowView.findViewById(R.id.from);
            subject = rowView.findViewById<View>(R.id.txt_primary) as TextView
            message = rowView.findViewById<View>(R.id.txt_secondary) as TextView
            iconText = rowView.findViewById<View>(R.id.icon_text) as TextView
            timestamp = rowView.findViewById<View>(R.id.timestamp) as TextView
            iconBack = rowView.findViewById<View>(R.id.icon_back) as RelativeLayout
            iconFront = rowView.findViewById<View>(R.id.icon_front) as RelativeLayout
            iconImp = rowView.findViewById<View>(R.id.icon_star) as ImageView
            imgProfile = rowView.findViewById<View>(R.id.icon_profile) as ImageView
            registrationContainser = rowView.findViewById<View>(R.id.message_container) as LinearLayout
            iconContainer = rowView.findViewById<View>(R.id.icon_container) as RelativeLayout
            try {
                val partnerActivity = values[position]
                val partner = PartnersTable(context).getPartnerById(partnerActivity.partnerId)

                subject.text = partner!!.partnerName
                subject.layoutParams.width = RecyclerView.LayoutParams.WRAP_CONTENT

                message.text = partner.contactPerson
                message.layoutParams.width = RecyclerView.LayoutParams.WRAP_CONTENT
                // timestamp.setText(chew.getRecruitmentId());
                // timestamp.getLayoutParams().width = RecyclerView.LayoutParams.MATCH_PARENT;
                if (!partner.partnerName.equals("", ignoreCase = true)) {
                    iconText.text = partner.partnerName.substring(0, 1)
                } else {
                    iconText.text = ""
                }

                imgProfile.setImageResource(R.drawable.bg_circle)
                imgProfile.setColorFilter(partner.color)
                iconText.visibility = View.VISIBLE
                iconImp.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_delete_white_24dp))
                iconImp.setColorFilter(ContextCompat.getColor(getContext(), R.color.icon_warning))
            } catch (e: Exception) {
                Log.d("TREMAP", "==><====>==><====>==><====>==><====>==><====>==><====>==><====>==><====>")
                Log.d("TREMAP", e.message)

            }

            /**
             * Enable the logic to delete
             *
             */
            //            iconImp.setOnLongClickListener(new View.OnLongClickListener() {
            //                @Override
            //                public boolean onLongClick(View v) {
            //                    // really delete the Item?
            //                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
            //                    alertDialog.setTitle("Confirm Delete");
            //                    alertDialog.setMessage("Are you sure you want to delete "+ chew.getName()+"?");
            //                    alertDialog.setIcon(R.drawable.ic_delete_white_24dp);
            //                    alertDialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            //                        @Override
            //                        public void onClick(DialogInterface dialog, int which) {
            //                            // Check if there are records dependent on this item
            //
            //                        }
            //                    });
            //                    alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            //                        @Override
            //                        public void onClick(DialogInterface dialog, int which) {
            //                            // Check if there are records dependent on this item
            //                            dialog.cancel();
            //                        }
            //                    });
            //                    alertDialog.show();
            //                    return false;
            //                }
            //            });

            return rowView
        }
    }

    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private val ARG_PARAM1 = "param1"
        private val ARG_PARAM2 = "param2"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment RegistrationsFragment.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String, param2: String): CommunityUnitViewFragment {
            val fragment = CommunityUnitViewFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }


}
