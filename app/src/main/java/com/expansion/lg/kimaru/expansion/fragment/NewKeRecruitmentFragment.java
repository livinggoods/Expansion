package com.expansion.lg.kimaru.expansion.fragment;
/**
 * Created by kimaru on 3/11/17.
 */

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.expansion.lg.kimaru.expansion.R;
import com.expansion.lg.kimaru.expansion.activity.MainActivity;
import com.expansion.lg.kimaru.expansion.activity.SessionManagement;
import com.expansion.lg.kimaru.expansion.mzigos.KeCounty;
import com.expansion.lg.kimaru.expansion.mzigos.Recruitment;
import com.expansion.lg.kimaru.expansion.mzigos.SubCounty;
import com.expansion.lg.kimaru.expansion.tables.KeCountyTable;
import com.expansion.lg.kimaru.expansion.tables.RecruitmentTable;
import com.expansion.lg.kimaru.expansion.tables.SubCountyTable;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NewKeRecruitmentFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewKeRecruitmentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewKeRecruitmentFragment extends Fragment implements OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    EditText mName;
    Spinner mCounty;
    Spinner mSubCounty;

    List<KeCounty> keCountyList = new ArrayList<>();
    List<String> keCounties = new ArrayList<>();

    List<SubCounty> subCountyList = new ArrayList<>();
    List<String> subCounties = new ArrayList<>();


    Button buttonSave, buttonList;
    public Recruitment editingRecruitment = null;


    private int mYear, mMonth, mDay;
    static final int DATE_DIALOG_ID = 100;

    SessionManagement session;
    HashMap<String, String> user;

    String subCountyName, subCountyContactPerson, subCountyContactPhone;



    public NewKeRecruitmentFragment() {
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
    public static NewKeRecruitmentFragment newInstance(String param1, String param2) {
        NewKeRecruitmentFragment fragment = new NewKeRecruitmentFragment();
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
        View v =  inflater.inflate(R.layout.fragment_new_ke_recruitment, container, false);
        MainActivity.CURRENT_TAG =MainActivity.TAG_NEW_RECRUITMENT;
        MainActivity.backFragment = new RecruitmentsFragment();
        session = new SessionManagement(getContext());
        user = session.getUserDetails();
                //Initialize the UI Components
        mName = (EditText) v.findViewById(R.id.editRecruitmentName);
        // mCounty = (EditText) v.findViewById(R.id.editRecruitmentCounty);
        // mSubCounty = (EditText) v.findViewById(R.id.editRecruitmentSubCounty);

        mCounty = (Spinner) v.findViewById(R.id.selectCounty);
        mSubCounty = (Spinner) v.findViewById(R.id.selectSubCounty);

        keCountyList = new KeCountyTable(getContext()).getCounties();
        for (KeCounty k : keCountyList){
            keCounties.add(k.getCountyName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, keCounties);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mCounty.setAdapter(adapter);
        mCounty.setOnItemSelectedListener(onSelectedCountyListener);

        // Subcounties

        //in case we are editing
        setUpEditingMode();

        buttonList = (Button) v.findViewById(R.id.buttonList);
        buttonList.setOnClickListener(this);

        buttonSave = (Button) v.findViewById(R.id.buttonSave);
        buttonSave.setOnClickListener(this);

        return v;
    }

    //AdapterView.OnItemSelectedListener onSelectedChewListener = new AdapterView.OnItemSelectedListener() {
    AdapterView.OnItemSelectedListener onSelectedCountyListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {
            // get subcounties
            if (position > subCountyList.size() -1){
                // Show Dialog to add the Referral
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Add new Sub County");

                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                // Context context = mapView.getContext();
                LinearLayout layout = new LinearLayout(getContext());
                layout.setOrientation(LinearLayout.VERTICAL);

                final EditText subCName = new EditText(getContext());
                subCName.setHint("Sub County Name");
                subCName.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
                layout.addView(subCName);

                final EditText refName = new EditText(getContext());
                refName.setHint("Contact Person Name");
                refName.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
                layout.addView(refName);

                final EditText refPhone = new EditText(getContext());
                refPhone.setHint("Contact Person Phone");
                refPhone.setInputType(InputType.TYPE_CLASS_PHONE);
                layout.addView(refPhone);

                builder.setView(layout);

                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        subCountyName = subCName.getText().toString();
                        subCountyContactPerson = refName.getText().toString();
                        subCountyContactPhone = refPhone.getText().toString();
                        // we save the referral, refresh the list and rebind the Spinner, and set selected
                        String uuid = UUID.randomUUID().toString();
                        SubCounty subCounty = new SubCounty();
                        subCounty.setId(uuid);
                        subCounty.setContactPersonPhone(subCountyContactPerson);
                        subCounty.setSubCountyName(subCountyName);
                        subCounty.setContactPerson(subCountyContactPerson);
                        subCounty.setCountyID(String.valueOf(keCountyList.get(position).getId()));

                        SubCountyTable scTbl = new SubCountyTable(getContext());
                        scTbl.addData(subCounty);

                        // clear subcounties

                        subCountyList.clear();
                        subCounties.clear();

                        subCountyList = scTbl.getSubCountiesByCounty(keCountyList.get(position).getId());
                        for (SubCounty s : subCountyList){
                            subCounties.add(s.getSubCountyName());
                        }
                        subCounties.add("Add New");

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                                android.R.layout.simple_spinner_item, subCounties);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        mSubCounty.setAdapter(adapter);

                        //lets set the selected
                        int x = 0;
                        for (SubCounty s : subCountyList){
                            if (s.getId().equalsIgnoreCase(uuid)){
                                mSubCounty.setSelection(x, true);
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
            }else{
                //populate subcounties
                subCountyList.clear();
                subCounties.clear();
                SubCountyTable scTable = new SubCountyTable(getContext());
                subCountyList = scTable.getSubCountiesByCounty(keCountyList.get(position).getId());
                for (SubCounty s : subCountyList){
                    subCounties.add(s.getSubCountyName());
                }
                subCounties.add("Add New");

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                        android.R.layout.simple_spinner_item, subCounties);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mSubCounty.setAdapter(adapter);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.buttonSave:

                // set date as integers
                Toast.makeText(getContext(), "Validating and saving", Toast.LENGTH_SHORT).show();
                Long currentDate =  new Date().getTime();

                // Generate the uuid
                String id;
                if (editingRecruitment != null){
                    id = editingRecruitment.getId();
                }else {
                    id = UUID.randomUUID().toString();
                }
                String recruitmentName = mName.getText().toString();
                String recruitmentDistrict = "";
                String recruitmentDivision = "";
                String recruitmentSubCounty = subCountyList.get(mSubCounty.getSelectedItemPosition()).getId();
                Integer recruitmentCounty = keCountyList.get(mCounty.getSelectedItemPosition()).getId();
                String recruitmentComment = "";
                String recruitmentLat = "";
                String recruitmentLon = "";
                String country = user.get(SessionManagement.KEY_USER_COUNTRY);

                Integer recruitmentAddedBy = Integer.parseInt(user.get(SessionManagement.KEY_USERID));
                Long recruitmentDateAdded = currentDate;
                Integer recruitmentSync = 0;


                // Do some validations
                if (recruitmentName.toString().trim().equals("")){
                    mName.requestFocus();
                    Toast.makeText(getContext(), "Name cannot be blank", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Save Recruitment
                Recruitment recruitment;
                recruitment = new Recruitment(id, recruitmentName, recruitmentDistrict,
                        recruitmentSubCounty, recruitmentDivision, recruitmentLat,
                        recruitmentLon, recruitmentComment, recruitmentAddedBy,
                        recruitmentDateAdded, recruitmentSync, country, String.valueOf(recruitmentCounty));
                RecruitmentTable recruitmentTable = new RecruitmentTable(getContext());
                long statusId = recruitmentTable.addData(recruitment);

                if (statusId == -1){
                    Toast.makeText(getContext(), "Could not save recruitment", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getContext(), "Saved successfully", Toast.LENGTH_SHORT).show();

                    // Clear boxes
                    mName.setText("");
                    mCounty.setSelection(0);
                    mSubCounty.setSelection(0);

                    //set Focus
                    mName.requestFocus();
                }

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

    public void setUpEditingMode(){
        if (editingRecruitment != null){
            mName.setText(editingRecruitment.getName());
            int x = 0;
            for (KeCounty c : keCountyList){
                if (c.getId().equals(Integer.valueOf(editingRecruitment.getCounty()))){
                    mCounty.setSelection(x, true);
                    break;
                }
                x++;
            }
            x = 0;
            for (SubCounty sc : subCountyList){
                if (sc.getId().equalsIgnoreCase(editingRecruitment.getSubcounty())){
                    mSubCounty.setSelection(x, true);
                    break;
                }
                x++;
            }
        }
    }
}
