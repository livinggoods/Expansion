package com.expansion.lg.kimaru.expansion.fragment;
/**
 * Created by kimaru on 3/11/17.
 */

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.telephony.PhoneNumberUtils;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.expansion.lg.kimaru.expansion.R;
import com.expansion.lg.kimaru.expansion.activity.MainActivity;
import com.expansion.lg.kimaru.expansion.activity.SessionManagement;
import com.expansion.lg.kimaru.expansion.mzigos.CommunityUnit;
import com.expansion.lg.kimaru.expansion.mzigos.IccmComponent;
import com.expansion.lg.kimaru.expansion.mzigos.Parish;
import com.expansion.lg.kimaru.expansion.mzigos.PartnerActivity;
import com.expansion.lg.kimaru.expansion.mzigos.Partners;
import com.expansion.lg.kimaru.expansion.mzigos.SubCounty;
import com.expansion.lg.kimaru.expansion.mzigos.Village;
import com.expansion.lg.kimaru.expansion.other.UtilFunctions;
import com.expansion.lg.kimaru.expansion.tables.IccmComponentTable;
import com.expansion.lg.kimaru.expansion.tables.PartnerActivityTable;
import com.expansion.lg.kimaru.expansion.tables.PartnersTable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import io.fabric.sdk.android.Fabric;
import okhttp3.internal.Util;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NewPartnerActivityFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewPartnerActivityFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewPartnerActivityFragment extends Fragment implements OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Fragment backFragment = null;

    private OnFragmentInteractionListener mListener;

    Spinner selectPartner;
    EditText partnerActivityComment;
    RadioGroup editIsDoingIccm, editIsGivingFreeMedicine, editIsGivingStipend, editIsDoingMhealth;
    LinearLayout parentLayout;
    List<Partners> partnersList = new ArrayList<Partners>();
    List<String> partnerNames = new ArrayList<String>();
    List<IccmComponent> iccmComponentList = new ArrayList<IccmComponent>();

    JSONArray jsonArray;
    JSONObject jsonResults;


    Button buttonSave, buttonList;
    public PartnerActivity editingPartnerActivity = null;
    public Village village = null;
    public Parish parish = null;
    public SubCounty subCounty = null;
    public CommunityUnit communityUnit = null;

    EditText txtIccmComments, txtMedicineComments, txtStipendComments, txtMhealthComments;

    LinearLayout layoutQuestions;

    static final int DATE_DIALOG_ID = 100;

    SessionManagement session;
    HashMap<String, String> user;
    String country;


    public NewPartnerActivityFragment() {
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
    public static NewPartnerActivityFragment newInstance(String param1, String param2) {
        NewPartnerActivityFragment fragment = new NewPartnerActivityFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(getContext(), new Crashlytics());
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        String jsonStr = UtilFunctions.loadFromAsset(getContext(), "partner_activity.json");
        try {
            jsonArray = new JSONArray(jsonStr);
        } catch (JSONException ex) {
            ex.printStackTrace();
            Crashlytics.log(ex.toString());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_new_partner_activity, container, false);
        MainActivity.CURRENT_TAG = MainActivity.TAG_NEW_RECRUITMENT;
        if (backFragment == null) {
            MainActivity.backFragment = new PartnerActivityFragment();
        } else {
            MainActivity.backFragment = backFragment;
        }
        session = new SessionManagement(getContext());
        user = session.getUserDetails();
        country = user.get(SessionManagement.KEY_USER_COUNTRY);

        selectPartner = (Spinner) v.findViewById(R.id.selectPartner);
        partnerActivityComment = (EditText) v.findViewById(R.id.editComment);
        editIsDoingIccm = (RadioGroup) v.findViewById(R.id.editIsDoingIccm);
        editIsGivingFreeMedicine = (RadioGroup) v.findViewById(R.id.editIsGivingFreeMedicine);
        editIsGivingStipend = (RadioGroup) v.findViewById(R.id.editIsGivingStipend);
        editIsDoingMhealth = (RadioGroup) v.findViewById(R.id.editIsDoingMhealth);
        parentLayout = (LinearLayout) v.findViewById(R.id.check_add_layout);

        txtIccmComments = (EditText) v.findViewById(R.id.txt_iccm_comments);
        txtMedicineComments = (EditText) v.findViewById(R.id.txt_medicine_comments);
        txtMhealthComments = (EditText) v.findViewById(R.id.txt_mhealth_comments);
        txtStipendComments = (EditText) v.findViewById(R.id.txt_stipend_comments);

        layoutQuestions = (LinearLayout) v.findViewById(R.id.layout_additional_questions);

        editIsDoingIccm.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.editIsDoingIccmYes) {
                    txtIccmComments.setVisibility(View.VISIBLE);
                } else {
                    txtIccmComments.setVisibility(View.GONE);
                    txtIccmComments.setText("");
                }
            }
        });

        editIsGivingFreeMedicine.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.editIsGivingFreeMedicineYes) {
                    txtMedicineComments.setVisibility(View.VISIBLE);
                } else {
                    txtMedicineComments.setVisibility(View.GONE);
                    txtMedicineComments.setText("");
                }
            }
        });

        editIsGivingStipend.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.editIsGivingStipendYes) {
                    txtStipendComments.setVisibility(View.VISIBLE);
                } else {
                    txtStipendComments.setVisibility(View.GONE);
                    txtStipendComments.setText("");
                }
            }
        });

        editIsDoingMhealth.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.editIsDoingMhealthYes) {
                    txtMhealthComments.setVisibility(View.VISIBLE);
                } else {
                    txtMhealthComments.setVisibility(View.GONE);
                    txtMhealthComments.setText("");
                }
            }
        });

        addPartners();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, partnerNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectPartner.setAdapter(adapter);
        selectPartner.setOnItemSelectedListener(onSelectedPartnerListener);

        IccmComponentTable iccmComponentTable = new IccmComponentTable(getContext());
        iccmComponentList = iccmComponentTable.getIccmComponentData();
        for (IccmComponent component : iccmComponentList) {
            CheckBox checkBox = new CheckBox(getContext());
            checkBox.setId(component.getId());
            checkBox.setText(component.getComponentName());

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            params.gravity = Gravity.CENTER_VERTICAL;


            LinearLayout.LayoutParams checkParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            checkParams.gravity = Gravity.CENTER_VERTICAL;
            parentLayout.addView(checkBox, params);
        }
        setUpEditingMode();

        buttonList = (Button) v.findViewById(R.id.buttonList);
        buttonList.setOnClickListener(this);

        buttonSave = (Button) v.findViewById(R.id.buttonSave);
        buttonSave.setOnClickListener(this);

        setupAdditionalQuestions();


        return v;
    }

    AdapterView.OnItemSelectedListener onSelectedPartnerListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, final long id) {
            String selectedItem = partnerNames.get(position);
            if (selectedItem.equalsIgnoreCase("Add New")) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Add a New Partner");
                LinearLayout layout = new LinearLayout(getContext());
                layout.setOrientation(LinearLayout.VERTICAL);
                final EditText editTextPartnerName = new EditText(getContext());
                editTextPartnerName.setHint("Partner Name");
                editTextPartnerName.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
                layout.addView(editTextPartnerName);

                final EditText editTextPartnerContactPerson = new EditText(getContext());
                editTextPartnerContactPerson.setHint("Contact person");
                editTextPartnerContactPerson.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
                layout.addView(editTextPartnerContactPerson);

                final EditText editTextPartnerContactPersonPhone = new EditText(getContext());
                editTextPartnerContactPersonPhone.setHint("Contact person phone");
                editTextPartnerContactPersonPhone.setInputType(InputType.TYPE_CLASS_PHONE);
                layout.addView(editTextPartnerContactPersonPhone);

                final EditText editTextComment = new EditText(getContext());
                editTextComment.setHint("Comment");
                editTextComment.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                layout.addView(editTextComment);


                builder.setView(layout);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String partnerName = editTextPartnerName.getText().toString();
                        if (partnerName.equalsIgnoreCase("")) {
                            Toast.makeText(getContext(), "Facility Name is required", Toast.LENGTH_LONG).show();
                            return;
                        }
                        // save the Link Facility
                        String uuid = UUID.randomUUID().toString();
                        Partners partner = new Partners();
                        partner.setPartnerID(uuid);
                        partner.setPartnerName(partnerName);

                        partner.setContactPerson(editTextPartnerContactPerson.getText().toString());
                        partner.setContactPersonPhone(editTextPartnerContactPersonPhone.getText().toString());
                        //partner.setParent("");
                        partner.setMappingId(session.getSavedMapping().getId());
                        partner.setCountry(session.getUserDetails().get(SessionManagement.KEY_USER_COUNTRY));
                        partner.setComment(editTextComment.getText().toString());
                        partner.setSynced(0);
                        partner.setArchived(false);
                        partner.setDateAdded(new Date().getTime());
                        partner.setAddedBy(Long.valueOf(session.getUserDetails().get(SessionManagement.KEY_USERID)));
                        new PartnersTable(getContext()).addData(partner);

                        // clear select
                        partnersList.clear();
                        partnerNames.clear();
                        addPartners();
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                                android.R.layout.simple_spinner_item, partnerNames);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        selectPartner.setAdapter(adapter);

                        //lets set the selected
                        int x = 0;
                        for (Partners e : partnersList) {
                            if (e.getPartnerID().equalsIgnoreCase(uuid)) {
                                selectPartner.setSelection(x, true);
                                break;
                            }
                            x++;
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    public void addPartners() {
        PartnersTable partnersTable = new PartnersTable(getContext());
        partnersList = partnersTable.getPartnersData();
        for (Partners partner : partnersList) {
            partnerNames.add(partner.getPartnerName());
        }
        if (partnerNames.size() == 0) {
            partnerNames.add("  ");
        }
        partnerNames.add("Add New");
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonList:
                Fragment fragment = new PartnerActivityFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, MainActivity.TAG_NEW_RECRUITMENT);
                fragmentTransaction.commitAllowingStateLoss();
                break;
            case R.id.buttonSave:
                // set date as integers
                Long currentDate = new Date().getTime();

                // Generate the uuid
                String id;
                if (editingPartnerActivity != null) {
                    id = editingPartnerActivity.getId();
                } else {
                    id = UUID.randomUUID().toString();
                }

                PartnerActivity partnerActivity = new PartnerActivity();

                partnerActivity.setId(id);

                if (partnersList.size() != 0) {
                    partnerActivity.setPartnerId(partnersList.get(selectPartner.getSelectedItemPosition()).getPartnerID());
                }

                partnerActivity.setCountry(session.getUserDetails().get(SessionManagement.KEY_USER_COUNTRY));
                partnerActivity.setCounty(session.getSavedMapping().getCounty());

                if (subCounty != null) {
                    partnerActivity.setSubcounty(subCounty.getId());
                    partnerActivity.setCounty(subCounty.getCountyID());
                }

                if (parish != null) {
                    partnerActivity.setParish(parish.getId());
                }
                if (village != null) {
                    partnerActivity.setVillage(village.getId());
                }

                if (communityUnit != null) {
                    partnerActivity.setCommunityUnit(communityUnit.getId());
                    partnerActivity.setSubcounty(communityUnit.getSubCountyId());
                }

                partnerActivity.setMappingId(session.getSavedMapping().getId());
                partnerActivity.setComment(partnerActivityComment.getText().toString());


                partnerActivity.setDoingIccm(getSelectedRadioItemValue(editIsDoingIccm).equalsIgnoreCase("Yes"));
                partnerActivity.setGivingFreeDrugs(getSelectedRadioItemValue(editIsGivingFreeMedicine).equalsIgnoreCase("Yes"));
                partnerActivity.setGivingStipend(getSelectedRadioItemValue(editIsGivingStipend).equalsIgnoreCase("Yes"));
                partnerActivity.setDoingMhealth(getSelectedRadioItemValue(editIsDoingMhealth).equalsIgnoreCase("Yes"));
                partnerActivity.setDateAdded(new Date().getTime());
                partnerActivity.setAddedBy(Long.valueOf(session.getUserDetails().get(SessionManagement.KEY_USERID)));

                String iccmComment = txtIccmComments.getText().toString();
                String medicineComment = txtMedicineComments.getText().toString();
                String stipendComment = txtStipendComments.getText().toString();
                String mHealthComment = txtMhealthComments.getText().toString();

                JSONObject other = new JSONObject();


                try {
                    other.put("iccm_comment", iccmComment);
                    other.put("medicine_comment", medicineComment);
                    other.put("stipend_comment", stipendComment);
                    other.put("mhealth_comment", mHealthComment);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                try {
                    if (!validateExtraFields()) {
                        Log.e("JSON", jsonResults.toString());
                        throw new Exception("Please check your input");
                    }

                    other = UtilFunctions.mergeJSONObjects(other, jsonResults);

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    Crashlytics.log(e.toString());
                    return;
                }

                JSONObject activities = new JSONObject();
                String partnerActivities = "";

                partnerActivity.setSynced(0);
                partnerActivity.setOther(other.toString());

                ArrayList<Integer> listOfSelectedCheckBoxId = new ArrayList<>();
                for (int i = 0; i < parentLayout.getChildCount(); i++) {
                    CheckBox checkbox = (CheckBox) parentLayout.getChildAt(i);
                    try {
                        activities.put(String.valueOf(checkbox.getId()), checkbox.isChecked());
                    } catch (Exception e) {
                    }
                    if (checkbox.isChecked()) {
                        listOfSelectedCheckBoxId.add(checkbox.getId());
                    }
                }
                partnerActivities = activities.toString();
                partnerActivity.setActivities(partnerActivities);

                PartnerActivityTable partnerActivityTable = new PartnerActivityTable(getContext());
                long statusId = partnerActivityTable.addData(partnerActivity);
                if (statusId == -1) {
                    Toast.makeText(getContext(), "Could not save the partner", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Saved successfully", Toast.LENGTH_SHORT).show();

                    // Clear boxes
                    // Go back to Subcounty View...
                }

        }
    }

    public String getSelectedRadioItemValue(RadioGroup radioGroup) {
        Integer selectedButton = radioGroup.getCheckedRadioButtonId();
        RadioButton selectedRadioButton = (RadioButton) radioGroup.findViewById(selectedButton);
        String selectedValue = selectedRadioButton.getText().toString();
        return selectedValue;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
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

    public void setUpEditingMode() {
        if (editingPartnerActivity != null) {
            int x = 0;
            for (Partners e : partnersList) {
                if (e.getPartnerID().equalsIgnoreCase(editingPartnerActivity.getPartnerId())) {
                    selectPartner.setSelection(x, true);
                    break;
                }
                x++;
            }

            editIsDoingIccm.clearCheck();
            editIsDoingIccm.check(editingPartnerActivity.isDoingIccm() ?
                    R.id.editIsDoingIccmYes : R.id.editIsDoingIccmNo);

            editIsGivingFreeMedicine.clearCheck();
            editIsGivingFreeMedicine.check(editingPartnerActivity.isGivingFreeDrugs() ?
                    R.id.editIsGivingFreeMedicineYes : R.id.editIsGivingFreeMedicineNo);

            editIsGivingStipend.clearCheck();
            editIsGivingStipend.check(editingPartnerActivity.isGivingStipend() ?
                    R.id.editIsGivingStipendYes : R.id.editIsGivingStipendNo);

            editIsDoingMhealth.clearCheck();
            editIsDoingMhealth.check(editingPartnerActivity.isDoingMhealth() ?
                    R.id.editIsDoingMhealthYes : R.id.editIsDoingMhealthNo);

            try {
                JSONObject activities = new JSONObject(editingPartnerActivity.getActivities());
                Iterator<?> keys = activities.keys();
                while (keys.hasNext()) {
                    String key = (String) keys.next();
                    Integer iccm = Integer.valueOf(key);

                    Log.d("Tremap", iccm + " -- " + activities.getString(key));
                    CheckBox checkbox = (CheckBox) parentLayout.findViewById(iccm);
                    checkbox.setChecked(activities.getBoolean(String.valueOf(key)));
                }
            } catch (JSONException je) {
            }

            String other = editingPartnerActivity.getOther();
            Log.e("OTHER", other);
            try {
                jsonResults = new JSONObject(other);

                txtIccmComments.setText(jsonResults.getString("iccm_comment"));
                txtMedicineComments.setText(jsonResults.getString("medicine_comment"));
                txtStipendComments.setText(jsonResults.getString("stipend_comment"));
                txtMhealthComments.setText(jsonResults.getString("mhealth_comment"));

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    if (obj.getString("type").equals("label")) {
                        continue;
                    }

                    obj.put("value", jsonResults.get(obj.getString("name")));
                    jsonArray.put(i, obj);
                }

            } catch (JSONException ex) {
                ex.printStackTrace();
                Crashlytics.log(ex.toString());
            }
        }
    }

    /**
     * Adds additional questions
     */
    private void setupAdditionalQuestions() {

        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = jsonArray.getJSONObject(i);

                View questionView = getQuestionView(json);

                if (questionView != null)
                    layoutQuestions.addView(questionView);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Crashlytics.log(e.toString());
        }

    }

    private View getQuestionView(JSONObject json) throws JSONException {

        String type = json.getString("type");

        switch (type) {
            case "label":
                return createLabelField(json);
            case "input":
                return createInputField(json);
            case "radio":
                return createRadioButtons(json);
            default:
                return null;
        }
    }

    private LinearLayout getContainer() {

        LinearLayout container = new LinearLayout(getContext());
        container.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        container.setOrientation(LinearLayout.VERTICAL);

        float scale = getResources().getDisplayMetrics().density;

        int paddingLeft = (int) (scale * 10 + 0.5f);
        int paddingRight = (int) (scale * 0 + 0.5f);
        int paddingTop = (int) (scale * 0 + 0.5f);
        int paddingBottom = (int) (scale * 0 + 0.5f);

        container.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);

        return container;
    }

    private TextView getQuestionLabel(String title) {
        TextView label = new TextView(getContext());
        label.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        label.setText(title);

        float scale = getResources().getDisplayMetrics().density;

        int paddingLeft = (int) (scale * 0 + 0.5f);
        int paddingRight = (int) (scale * 0 + 0.5f);
        int paddingTop = (int) (scale * 16 + 0.5f);
        int paddingBottom = (int) (scale * 2 + 0.5f);

        label.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
        label.setTextColor(getResources().getColor(android.R.color.primary_text_light));

        return label;
    }

    private View createRadioButtons(JSONObject json) throws JSONException {
        String title = json.getString("title");
        String value = json.getString("value");
        String name = json.getString("name");
        JSONArray options = json.getJSONArray("options");

        RadioGroup input = new RadioGroup(getContext());
        input.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        input.setOrientation(RadioGroup.HORIZONTAL);
        input.setTag(name);

        for (int i = 0; i < options.length(); i++) {
            JSONObject optionObj = options.getJSONObject(i);
            String option = optionObj.getString("option");

            RadioButton radioButton = new RadioButton(getContext());
            radioButton.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            radioButton.setText(option);
            radioButton.setTag(value);

            input.addView(radioButton);

            if (option.equals(value)) {
                input.check(radioButton.getId());
            }
        }


        LinearLayout container = getContainer();
        container.addView(getQuestionLabel(title));
        container.addView(input);

        return container;
    }

    private LinearLayout createInputField(JSONObject json) throws JSONException {
        String title = json.getString("title");
        String value = json.getString("value");
        String name = json.getString("name");
        String constraint = json.getString("constraint");

        EditText input = new EditText(getContext());
        input.setTag(name);
        input.setHint(title);
        input.setText(value);

        switch (constraint) {
            case "number":
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                break;
            case "tel":
                input.setInputType(InputType.TYPE_CLASS_PHONE);
                break;
            default:
                input.setInputType(InputType.TYPE_CLASS_TEXT);
        }

        LinearLayout container = getContainer();
        container.addView(getQuestionLabel(title));
        container.addView(input);
        return container;
    }

    private View createLabelField(JSONObject json) throws JSONException {
        float scale = getResources().getDisplayMetrics().density;

        int paddingLeft = (int) (scale * 0 + 0.5f);
        int paddingRight = (int) (scale * 0 + 0.5f);
        int paddingTop = (int) (scale * 32 + 0.5f);
        int paddingBottom = (int) (scale * 0 + 0.5f);

        TextView label = new TextView(getContext());
        label.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        label.setText(json.getString("title").toUpperCase());
        label.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
        label.setTypeface(label.getTypeface(), Typeface.BOLD);
        return label;
    }

    private boolean validateExtraFields() throws JSONException {
        boolean isValid = true;

        jsonResults = new JSONObject();

        for (int i = 0; i < jsonArray.length(); i++) {

            JSONObject json = jsonArray.getJSONObject(i);
            String name = json.getString("name");
            String type = json.getString("type");
            boolean required = json.getBoolean("required");
            if (type.equals("label"))
                continue;

            String value = getValue(name, type);

            if (required && value.equals("")) {
                Log.e("Is Valid", "FALSe");
                Log.e("name", name + ": " + value);
                isValid = false;
            }

            jsonResults.put(name, value);

        }

        return isValid;
    }

    private String getValue(String name, String type) {

        switch (type) {

            case "input":
                EditText editText = (EditText) layoutQuestions.findViewWithTag(name);
                return editText.getText().toString();

            case "radio":
                RadioGroup radioGroup = (RadioGroup) layoutQuestions.findViewWithTag(name);
                int selected = radioGroup.getCheckedRadioButtonId();
                if (selected != -1) {
                    RadioButton radioButton = (RadioButton) layoutQuestions.findViewById(selected);
                    return radioButton.getText().toString();
                }
                return "";

            default:
                return "";
        }
    }
}
