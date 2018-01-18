package com.expansion.lg.kimaru.expansion.fragment;
/**
 * Created by kimaru on 3/11/17.
 */

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.expansion.lg.kimaru.expansion.R;
import com.expansion.lg.kimaru.expansion.activity.MainActivity;
import com.expansion.lg.kimaru.expansion.activity.SessionManagement;
import com.expansion.lg.kimaru.expansion.mzigos.CommunityUnit;
import com.expansion.lg.kimaru.expansion.mzigos.LinkFacility;
import com.expansion.lg.kimaru.expansion.mzigos.Mapping;
import com.expansion.lg.kimaru.expansion.mzigos.SubCounty;
import com.expansion.lg.kimaru.expansion.tables.CommunityUnitTable;
import com.expansion.lg.kimaru.expansion.tables.LinkFacilityTable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NewCommunityUnitFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewCommunityUnitFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewCommunityUnitFragment extends Fragment implements OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Fragment backFragment = null;
    CommunityUnit editingCommunityUnit = null;

    private OnFragmentInteractionListener mListener;

    EditText editName, editAreaChiefName, editAreaChiefPhone, editWard, editComment;
    EditText editPrivateFacilityForAct, editPrivateFacilityForMrdt, editNumberOfChvs, editChvHouseHold;
    EditText editNumberOfHouseHolds, editMohPopulation, editPopulationDensity, editNumberOfVillages;
    EditText editDistanceToBranch, editTransportCost, editDistanceToMainRoad, editDistanceToHealthFacility;
    EditText editDistributors, editCHVsTrained, editPriceofAct, editPriceOfMrdt, editChiefPopulation, editChiefChvHouseHold;

    Spinner editEconomicStatus, spinnerLinkFacility;

    RadioGroup editPresenceOfFactories, editPresenceEstates, editPresenceOfTraderMarket, editPresenceOfSuperMarket;
    RadioGroup editNgosGivingFreeDrugs, editCHVsTrainedGroup;
    LinkFacility linkFacility = null;

    Button buttonSave, buttonList;


    private int mYear, mMonth, mDay;
    static final int DATE_DIALOG_ID = 100;

    SessionManagement session;
    Mapping mapping;
    SubCounty subCounty;
    String name, email;
    HashMap<String, String> user;


    Double latitude = 0D;
    Double longitude = 0D;

    List<LinkFacility> linkFacilityList = new ArrayList<LinkFacility>();
    List<String> linkFacilities = new ArrayList<String>();

    public NewCommunityUnitFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegistrationsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewCommunityUnitFragment newInstance(String param1, String param2) {
        NewCommunityUnitFragment fragment = new NewCommunityUnitFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_new_community_unit, container, false);
        MainActivity.CURRENT_TAG =MainActivity.TAG_NEW_COMMUNITY_UNIT;
        if (backFragment == null){
            MainActivity.backFragment = new CommunityUnitsFragment();
        }else{
            MainActivity.backFragment = backFragment;
        }

        session = new SessionManagement(getContext());
        mapping = session.getSavedMapping();
        subCounty = session.getSavedSubCounty();
        user = session.getUserDetails();

        //Initialize the UI Components

        editName = (EditText) v.findViewById(R.id.editName);
        editAreaChiefName = (EditText) v.findViewById(R.id.editAreaChiefName);
        editAreaChiefPhone = (EditText) v.findViewById(R.id.editAreaChiefPhone);
        editWard = (EditText) v.findViewById(R.id.editWard);
        editEconomicStatus = (Spinner) v.findViewById(R.id.editEconomicStatus);

        editPrivateFacilityForAct = (EditText) v.findViewById(R.id.editPrivateFacilityForAct);
        editPrivateFacilityForMrdt = (EditText) v.findViewById(R.id.editPrivateFacilityForMrdt);
        editNumberOfChvs = (EditText) v.findViewById(R.id.editNumberOfChvs);
        editChvHouseHold = (EditText) v.findViewById(R.id.editChvHouseHold);
        editPriceofAct = (EditText) v.findViewById(R.id.editPriceOfMrdt);
        editPriceOfMrdt = (EditText) v.findViewById(R.id.editPriceofAct);

        editChiefPopulation = (EditText) v.findViewById(R.id.editChiefPopulation);
        editChiefChvHouseHold = (EditText) v.findViewById(R.id.editChiefChvHouseHold);


        editNumberOfHouseHolds = (EditText) v.findViewById(R.id.editNumberOfHouseHolds);
        editMohPopulation = (EditText) v.findViewById(R.id.editMohPopulation);
        editPopulationDensity = (EditText) v.findViewById(R.id.editPopulationDensity);
        editNumberOfVillages = (EditText) v.findViewById(R.id.editNumberOfVillages);

        editDistanceToBranch = (EditText) v.findViewById(R.id.editDistanceToBranch);
        editTransportCost = (EditText) v.findViewById(R.id.editTransportCost);
        editDistanceToMainRoad = (EditText) v.findViewById(R.id.editDistanceToMainRoad);
        editDistanceToHealthFacility = (EditText) v.findViewById(R.id.editDistanceToHealthFacility);
        editComment = (EditText) v.findViewById(R.id.editComment);

        spinnerLinkFacility = (Spinner) v.findViewById(R.id.spinnerLinkFacility);
        editDistributors = (EditText) v.findViewById(R.id.editDistributors);
        // editCHVsTrained = (EditText) v.findViewById(R.id.editCHVsTrained);
        editCHVsTrainedGroup = (RadioGroup) v.findViewById(R.id.editCHVsTrainedGroup);


        editPresenceOfFactories = (RadioGroup) v.findViewById(R.id.editPresenceOfFactories);
        editPresenceEstates = (RadioGroup) v.findViewById(R.id.editPresenceEstates);
        editPresenceOfTraderMarket = (RadioGroup) v.findViewById(R.id.editPresenceOfTraderMarket);
        editPresenceOfSuperMarket = (RadioGroup) v.findViewById(R.id.editPresenceOfSuperMarket);
        editNgosGivingFreeDrugs = (RadioGroup) v.findViewById(R.id.editNgosGivingFreeDrugs);

        buttonList = (Button) v.findViewById(R.id.buttonList);
        buttonList.setOnClickListener(this);

        buttonSave = (Button) v.findViewById(R.id.buttonSave);
        buttonSave.setOnClickListener(this);

        // populate the link Facilities;
        linkFacilityList.clear();
        linkFacilities.clear();
        addLinkFacilities();
        ArrayAdapter<String> linkFacilityAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, linkFacilities);
        linkFacilityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLinkFacility.setAdapter(linkFacilityAdapter);
        //lets set the selected
        if (linkFacility != null){
            int x = 0;
            for (LinkFacility e : linkFacilityList) {
                if (e.getId().equalsIgnoreCase(linkFacility.getId())){
                    spinnerLinkFacility.setSelection(x, true);
                    break;
                }
                x++;
            }
            // hide the spinner
            TextInputLayout linkFacilityView = (TextInputLayout) v.findViewById(R.id.linkFacility);
            linkFacilityView.setVisibility(View.INVISIBLE);
            // Toast.makeText(getContext(), "ID "+linkFacility.getId(), Toast.LENGTH_SHORT).show();
        }
        setUpEditingMode();

        return v;
    }
    public void addLinkFacilities() {
        LinkFacilityTable linkFacilityTable = new LinkFacilityTable(getContext());

        linkFacilityList = linkFacilityTable.getLinkFacilityBySubCounty(subCounty.getId());
        for (LinkFacility lFacility: linkFacilityList){
            linkFacilities.add(lFacility.getFacilityName());
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.editDob:
                DialogFragment newFragment = new DatePickerFragment().newInstance(R.id.editDob);
                newFragment.show(getFragmentManager(), "DatePicker");
                break;

            case R.id.buttonList:
                CommunityUnitsFragment communityUnitsFragment = new CommunityUnitsFragment();
                communityUnitsFragment.linkFacility = linkFacility;
                Fragment fragment = communityUnitsFragment;
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left,
                        android.R.anim.slide_out_right);
                fragmentTransaction.replace(R.id.frame, fragment, "subcounties");
                fragmentTransaction.commitAllowingStateLoss();
                break;

            case R.id.editRelocated:
                DialogFragment dateMovedFragment = new DatePickerFragment().newInstance(R.id.editRelocated);
                dateMovedFragment.show(getFragmentManager(), "Datepicker");
                break;
            case R.id.buttonSave:
                Long currentDate =  new Date().getTime();
                CommunityUnitTable communityUnitTable = new CommunityUnitTable(getContext());
                // check if the field for Chiefs population exists or not.
                if (!communityUnitTable.isFieldExist(CommunityUnitTable.CHVS_HOUSEHOLDS_AS_PER_CHIEF)){
                    communityUnitTable.addChiefsFields();
                }
                if (!communityUnitTable.isFieldExist(CommunityUnitTable.COMMENT)){
                    communityUnitTable.addChiefsFields();
                }



//                "Private facility \n" +
//                        " • (Capture)- Name of Private  Facility\n" +
//                        "                  - Price of ACT at private Facility\n" +
//                        " • MRDT- same as above\n" +
//                        "\n" +
//                        " • No of Households per CHVs as per- Ast Chief\n" +
//                        " •  Population as per Ast Chief\n" +
//                        "In the Chief's \n" +
//                        " \n" +
//                        "Link Partners with the CU-\n" +
//                        "Make it possible to capture partners ina specific CU\n" +
//                        "Comments box- alpha -numeric\n" +
//                        "Partner save function is not working\n" +
//                        "List section does not capture partners instead it lists all CUs"
                String name = editName.getText().toString();
                String areaChiefName = editAreaChiefName.getText().toString();
                String areaChiefPhone = editAreaChiefPhone.getText().toString();
                String ward = editWard.getText().toString();
                String economicStatus = String.valueOf(editEconomicStatus.getSelectedItemPosition());
                String comments = editComment.getText().toString();

                String privateFacilityForAct = editPrivateFacilityForAct.getText().toString();
                String privateFacilityForMrdt = editPrivateFacilityForMrdt.getText().toString();

                Long numberOfChvs = Long.valueOf(editNumberOfChvs.getText().toString().trim()
                        .equalsIgnoreCase("") ? "0" : editNumberOfChvs.getText().toString());

                Long chvHouseHold = Long.valueOf(editChvHouseHold.getText().toString().trim()
                        .equalsIgnoreCase("") ? "0" : editChvHouseHold.getText().toString());

                Long numberOfHouseHolds = Long.valueOf(editNumberOfHouseHolds.getText().toString().trim()
                        .equalsIgnoreCase("") ? "0" : editNumberOfHouseHolds.getText().toString());

                Long mohPopulation = Long.valueOf(editMohPopulation.getText().toString()
                        .equalsIgnoreCase("") ? "0" : editMohPopulation.getText().toString());

                Long populationDensity = Long.valueOf(editPopulationDensity.getText().toString()
                        .equalsIgnoreCase("") ? "0" : editPopulationDensity.getText().toString());
                Long numberOfVillages = Long.valueOf(editNumberOfVillages.getText().toString()
                        .equalsIgnoreCase("") ? "0" : editNumberOfVillages.getText().toString());

                Long distanceToBranch = Long.valueOf(editDistanceToBranch.getText().toString()
                        .equalsIgnoreCase("") ? "0" : editDistanceToBranch.getText().toString());

                Long transportCost = Long.valueOf(editTransportCost.getText().toString()
                        .equalsIgnoreCase("") ? "0" : editTransportCost.getText().toString());

                Long actPrice = Long.valueOf(editPriceofAct.getText().toString()
                        .equalsIgnoreCase("") ? "0" : editPriceofAct.getText().toString());

                Long mrdtPrice = Long.valueOf(editPriceOfMrdt.getText().toString()
                        .equalsIgnoreCase("") ? "0" : editPriceOfMrdt.getText().toString());

                Long distanceToMainRoad = Long.valueOf(editDistanceToMainRoad.getText().toString()
                        .equalsIgnoreCase("") ? "0" : editDistanceToMainRoad.getText().toString());
                Long distanceToHealthFacility = Long.valueOf(editDistanceToHealthFacility.getText().toString()
                        .equalsIgnoreCase("") ? "0" : editDistanceToHealthFacility.getText().toString());
                String linkFacilityId;
                if (linkFacility != null){
                    linkFacilityId = linkFacility.getId();
                }else{
                    Integer lFacility = spinnerLinkFacility.getSelectedItemPosition();
                    if (linkFacilityList.size() != 0 && lFacility != -1) {
                        linkFacilityId = linkFacilityList.get(spinnerLinkFacility.getSelectedItemPosition()).getId();
                    }else{
                        linkFacilityId = "";
                    }
                }

                Long chiefPopulation = Long.valueOf(editChiefPopulation.getText().toString()
                        .equalsIgnoreCase("") ? "0" : editChiefPopulation.getText().toString());
                Long chiefChvsHousehold = Long.valueOf(editChiefChvHouseHold.getText().toString()
                        .equalsIgnoreCase("") ? "0" : editChiefChvHouseHold.getText().toString());


                Long distributors = Long.valueOf(editDistributors.getText().toString()
                        .equalsIgnoreCase("") ? "0" : editDistributors.getText().toString());

                // boolean cHVsTrained = (editCHVsTrained.getText().toString() == "Yes");

                Integer chvsTrainedInICCm = editCHVsTrainedGroup.getCheckedRadioButtonId();
                RadioButton selectedIccmOption =(RadioButton) editCHVsTrainedGroup.findViewById(chvsTrainedInICCm);
                boolean cHVsTrained = (selectedIccmOption.getText().toString().equalsIgnoreCase("Yes"));


                Integer factoriesPresent = editPresenceOfFactories.getCheckedRadioButtonId();
                RadioButton selectedFactoryOption =(RadioButton) editPresenceOfFactories.findViewById(factoriesPresent);
                Long presenceOfFactories = 0L;
                if (selectedFactoryOption.getText().toString().equalsIgnoreCase("More than 2")){
                    presenceOfFactories = 3L;
                }else if (selectedFactoryOption.getText().toString().equalsIgnoreCase("More than 2")){
                    presenceOfFactories = 2L;
                }else{
                    presenceOfFactories = 0L;
                }
                //boolean presenceOfFactories = (selectedFactoryOption.getText().toString().equalsIgnoreCase("Yes"));

                Integer estatesPresent = editPresenceEstates.getCheckedRadioButtonId();
                RadioButton selectedEstateOption =(RadioButton) editPresenceEstates.findViewById(estatesPresent);
                boolean presenceEstates = (selectedEstateOption.getText().toString().equalsIgnoreCase("Yes"));

                Integer tradeMarketsPresent = editPresenceOfTraderMarket.getCheckedRadioButtonId();
                RadioButton selectedTradeMarketOption =(RadioButton) editPresenceOfTraderMarket.findViewById(tradeMarketsPresent);
                boolean presenceOfTraderMarket = (selectedTradeMarketOption.getText().toString().equalsIgnoreCase("Yes"));


                Integer superMarketPresent = editPresenceOfSuperMarket.getCheckedRadioButtonId();
                RadioButton selectedSuperMarketOption =(RadioButton) editPresenceOfSuperMarket.findViewById(superMarketPresent);
                boolean presenceOfSuperMarket = (selectedSuperMarketOption.getText().toString().equalsIgnoreCase("Yes"));


                Integer ngosDrugs = editNgosGivingFreeDrugs.getCheckedRadioButtonId();
                RadioButton ngosGivingFreeDrugsSelected =(RadioButton) editNgosGivingFreeDrugs.findViewById(ngosDrugs);
                boolean ngosGivingFreeDrugs = (ngosGivingFreeDrugsSelected.getText().toString().equalsIgnoreCase("Yes"));

                // Do some validations

                if (name.toString().trim().equals("")){
                    Toast.makeText(getContext(), "Enter name of the Community Unit", Toast.LENGTH_SHORT).show();
                    editName.requestFocus();
                }

                else if (areaChiefName.toString().trim().equals("") && mapping != null && backFragment == null){
                    Toast.makeText(getContext(), "Enter the name of the Chief", Toast.LENGTH_SHORT).show();
                    editAreaChiefName.requestFocus();
                }

                else if(areaChiefPhone.toString().trim().equals("") && mapping != null && backFragment == null){
                    Toast.makeText(getContext(), "Enter the phone contact details of the chief", Toast.LENGTH_SHORT).show();
                    editAreaChiefPhone.requestFocus();
                } else{
                    Toast.makeText(getContext(), "Validating and saving", Toast.LENGTH_SHORT).show();
                    String id = UUID.randomUUID().toString();
                    if (editingCommunityUnit != null){
                        id = editingCommunityUnit.getId();
                    }
                    long userId = Long.valueOf(user.get(SessionManagement.KEY_USERID));
                    String country = user.get(SessionManagement.KEY_USER_COUNTRY);
                    String mappingId = "";
                    if (mapping != null){
                        mappingId = mapping.getId();
                    }

                    CommunityUnit communityUnit = new CommunityUnit();

                    communityUnit.setId(id);
                    communityUnit.setCommunityUnitName(name);
                    communityUnit.setMappingId(mappingId);
                    communityUnit.setLat(latitude);
                    communityUnit.setLon(longitude);
                    communityUnit.setCountry(country);
                    communityUnit.setSubCountyId(subCounty.getId());
                    communityUnit.setLinkFacilityId(linkFacilityId);
                    communityUnit.setAreaChiefName(areaChiefName);
                    communityUnit.setAreaChiefPhone(areaChiefPhone);
                    communityUnit.setWard(ward);
                    communityUnit.setEconomicStatus(economicStatus);
                    communityUnit.setPrivateFacilityForAct(privateFacilityForAct);
                    communityUnit.setPrivateFacilityForMrdt(privateFacilityForMrdt);
                    communityUnit.setNameOfNgoDoingIccm("");
                    communityUnit.setNameOfNgoDoingMhealth("");
                    communityUnit.setDateAdded(currentDate);
                    communityUnit.setAddedBy(userId);
                    communityUnit.setNumberOfChvs(numberOfChvs);
                    communityUnit.setHouseholdPerChv(chvHouseHold);
                    communityUnit.setNumberOfVillages(numberOfVillages);
                    communityUnit.setDistanceToBranch(distanceToBranch);
                    communityUnit.setTransportCost(transportCost);
                    communityUnit.setDistanceTOMainRoad(distanceToMainRoad);
                    communityUnit.setNoOfHouseholds(numberOfHouseHolds);
                    communityUnit.setMohPoplationDensity(mohPopulation);
                    communityUnit.setEstimatedPopulationDensity(populationDensity);
                    communityUnit.setDistanceTONearestHealthFacility(distanceToHealthFacility);
                    communityUnit.setActLevels(0);
                    communityUnit.setActPrice(actPrice);
                    communityUnit.setMrdtLevels(0);
                    communityUnit.setMrdtPrice(mrdtPrice);
                    communityUnit.setNoOfDistibutors(distributors);
                    communityUnit.setChvsTrained(cHVsTrained);
                    communityUnit.setPresenceOfEstates(presenceEstates);
                    communityUnit.setPresenceOfFactories(presenceOfFactories);
                    communityUnit.setPresenceOfHostels(presenceEstates);
                    communityUnit.setTraderMarket(presenceOfTraderMarket);
                    communityUnit.setLargeSupermarket(presenceOfSuperMarket);
                    communityUnit.setNgosGivingFreeDrugs(ngosGivingFreeDrugs);
                    communityUnit.setNgoDoingIccm(false);
                    communityUnit.setNgoDoingMhealth(false);
                    communityUnit.setPopulationAsPerChief(chiefPopulation);
                    communityUnit.setChvsHouseholdsAsPerChief(chiefChvsHousehold);
                    communityUnit.setComment(comments);


                    long cid = communityUnitTable.addCommunityUnitData(communityUnit);
                    if (cid != -1){
                        Toast.makeText(getContext(), "Community Unit saved successfully", Toast.LENGTH_SHORT).show();
                        editName.setText("");
                        editName.requestFocus();
                        editAreaChiefName.setText("");
                        editAreaChiefPhone.setText("");
                        editWard.setText("");

                        editPrivateFacilityForAct.setText("");
                        editPrivateFacilityForMrdt.setText("");
                        editNumberOfChvs.setText("");
                        editChvHouseHold.setText("");

                        editNumberOfHouseHolds.setText("");
                        editMohPopulation.setText("");
                        editPopulationDensity.setText("");
                        editNumberOfVillages.setText("");

                        editDistanceToBranch.setText("");
                        editTransportCost.setText("");
                        editDistanceToMainRoad.setText("");
                        editDistanceToHealthFacility.setText("");

                        editDistributors.setText("");
                        editPriceOfMrdt.setText("");
                        editPriceofAct.setText("");

                        editChiefPopulation.setText("");
                        editChiefChvHouseHold.setText("");
                        editComment.setText("");

                    }
                    if (backFragment != null){
                        RecruitmentViewFragment recruitmentViewFragment = new RecruitmentViewFragment();
                        fragment = recruitmentViewFragment;
                        fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                                android.R.anim.fade_out);
                        fragmentTransaction.replace(R.id.frame, fragment, MainActivity.TAG_NEW_RECRUITMENT);
                        fragmentTransaction.commitAllowingStateLoss();
                    }
                }

        }
    }

    public void setUpEditingMode(){
        if (editingCommunityUnit != null){
            editName.setText(editingCommunityUnit.getCommunityUnitName());
            editName.requestFocus();
            editAreaChiefName.setText(editingCommunityUnit.getAreaChiefName());
            editAreaChiefPhone.setText(editingCommunityUnit.getAreaChiefPhone());
            editWard.setText(editingCommunityUnit.getWard());
            editPrivateFacilityForAct.setText(editingCommunityUnit.getPrivateFacilityForAct());
            editPrivateFacilityForMrdt.setText(editingCommunityUnit.getPrivateFacilityForMrdt());
            editNumberOfChvs.setText(String.valueOf(editingCommunityUnit.getNumberOfChvs()));
            editChvHouseHold.setText(String.valueOf(editingCommunityUnit.getHouseholdPerChv()));
            editNumberOfHouseHolds.setText(String.valueOf(editingCommunityUnit.getNoOfHouseholds()));
            editMohPopulation.setText(String.valueOf(editingCommunityUnit.getMohPoplationDensity()));
            editPopulationDensity.setText(String.valueOf(
                    editingCommunityUnit.getEstimatedPopulationDensity()));
            editNumberOfVillages.setText(String.valueOf(editingCommunityUnit.getNumberOfVillages()));
            editDistanceToBranch.setText(String.valueOf(editingCommunityUnit.getDistanceToBranch()));
            editTransportCost.setText(String.valueOf(editingCommunityUnit.getTransportCost()));
            editDistanceToMainRoad.setText(String.valueOf(
                    editingCommunityUnit.getDistanceTOMainRoad()));
            editDistanceToHealthFacility.setText(String.valueOf(
                    editingCommunityUnit.getDistanceTONearestHealthFacility()));
            editDistributors.setText(String.valueOf(editingCommunityUnit.getNoOfDistibutors()));
            editPriceofAct.setText(String.valueOf(editingCommunityUnit.getActPrice()));
            editPriceOfMrdt.setText(String.valueOf(editingCommunityUnit.getMrdtPrice()));
            editChiefPopulation.setText(String.valueOf(editingCommunityUnit.getPopulationAsPerChief()));
            editChiefChvHouseHold.setText(String.valueOf(editingCommunityUnit.getChvsHouseholdsAsPerChief()));
            editComment.setText(editingCommunityUnit.getComment());


            // set radio Buttons
            //mReadEnglish.check(editingRegistration.getReadEnglish().equals(1) ? R.id.radioCanReadEnglish : R.id.radioCannotReadEnglish);
            editCHVsTrainedGroup.clearCheck();
            editCHVsTrainedGroup.check(editingCommunityUnit.isChvsTrained() ? R.id.trainedYes : R.id.trainedNo);

            editPresenceOfFactories.clearCheck();
            if (editingCommunityUnit.presenceOfFactories().equals(0L)){
                editPresenceOfFactories.check(R.id.cFactory1);
            }else if (editingCommunityUnit.presenceOfFactories().equals(2L)){
                editPresenceOfFactories.check(R.id.cFactory2);
            }else {
                editPresenceOfFactories.check(R.id.cFactory3);
            }

            editPresenceEstates.clearCheck();
            editPresenceEstates.check(editingCommunityUnit.isPresenceOfEstates()? R.id.estate1 : R.id.estate2);

            editPresenceOfTraderMarket.clearCheck();
            editPresenceOfTraderMarket.check(editingCommunityUnit.isTraderMarket() ? R.id.marketYes : R.id.marketNo);


            editPresenceOfSuperMarket.clearCheck();
            editPresenceOfSuperMarket.check(editingCommunityUnit.isLargeSupermarket() ? R.id.superMarketYes : R.id.SuperMarketNo);

            editNgosGivingFreeDrugs.clearCheck();
            editNgosGivingFreeDrugs.check(editingCommunityUnit.isNgosGivingFreeDrugs() ? R.id.drugsYes : R.id.drugsNo);

            editEconomicStatus.setSelection(Integer.valueOf(editingCommunityUnit.getEconomicStatus()));
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
