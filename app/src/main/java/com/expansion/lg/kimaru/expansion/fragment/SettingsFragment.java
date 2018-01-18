package com.expansion.lg.kimaru.expansion.fragment;
/**
 * Created by kimaru on 3/11/17.
 */

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.expansion.lg.kimaru.expansion.R;
import com.expansion.lg.kimaru.expansion.activity.MainActivity;
import com.expansion.lg.kimaru.expansion.activity.SessionManagement;

import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SettingsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    EditText editCloudUrl, editApiVersion, editApiSuffix, editApiPrefix,
            editPeerServer, editPeerServerPort;

    ToggleButton saveCloudUrl, saveApiVersion, saveApiPrefix, saveApiSuffix,
            savePeerServer, savePeerServerPort;
    static final int DATE_DIALOG_ID = 100;
    SessionManagement session;
    HashMap<String, String> user;

    public SettingsFragment() {
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
    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
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
        View v =  inflater.inflate(R.layout.fragment_settings, container, false);
        session = new SessionManagement(getContext());
        // Edittexts
        editCloudUrl = (EditText) v.findViewById(R.id.editCloudUrl);
        editApiVersion = (EditText) v.findViewById(R.id.editApiVersion);
        editApiSuffix = (EditText) v.findViewById(R.id.editApiSuffix);
        editApiPrefix = (EditText) v.findViewById(R.id.editApiPrefix);
        editPeerServer = (EditText) v.findViewById(R.id.editPeerServer);
        editPeerServerPort = (EditText) v.findViewById(R.id.editPeerServerPort);

        //buttons
        saveCloudUrl = (ToggleButton) v.findViewById(R.id.saveCloudUrl);
        saveApiVersion = (ToggleButton) v.findViewById(R.id.saveApiVersion);
        saveApiPrefix = (ToggleButton) v.findViewById(R.id.saveApiPrefix);
        saveApiSuffix = (ToggleButton) v.findViewById(R.id.saveApiSuffix);
        savePeerServer = (ToggleButton) v.findViewById(R.id.savePeerServer);
        savePeerServerPort = (ToggleButton) v.findViewById(R.id.savePeerServerPort);

        //listeners
        saveCloudUrl.setOnClickListener(this);
        saveApiVersion.setOnClickListener(this);
        saveApiPrefix.setOnClickListener(this);
        saveApiSuffix.setOnClickListener(this);
        savePeerServer.setOnClickListener(this);
        savePeerServerPort.setOnClickListener(this);
        setUpViewMode();
        return v;
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
            case R.id.saveCloudUrl:
                String cloudUrl = editCloudUrl.getText().toString();
                if (cloudUrl.trim().length() > 0) {
                    if (cloudUrl.substring(0,4).equalsIgnoreCase("http") ||
                            cloudUrl.substring(0,4).equalsIgnoreCase("https")){
                        session.saveCloudUrl(cloudUrl);
                        showMessage("Cloud URL Saved");
                    }else{
                        showMessage("Enter the correct URL starting with the scheme (http[s])");
                        return;
                    }
                }else{
                    showMessage("Cloud URL is empty");
                    return;
                }

                break;
            case R.id.saveApiVersion:
                String apiVersion = editApiVersion.getText().toString();
                if (apiVersion.trim().length() > 0){
                    session.saveApiVersion(apiVersion);
                    showMessage("Version saved successfully");
                }else{
                    showMessage("Unable to save empty Version");
                }
                break;

            case R.id.saveApiPrefix:
                String apiPrefix = editApiPrefix.getText().toString();
                if (apiPrefix.trim().length() > 0){
                    session.saveApiPrefix(apiPrefix);
                    showMessage("Prefix saved successfully");
                }else{
                    showMessage("Unable to save empty prefix");
                }
                break;
            case R.id.saveApiSuffix:
                String apiSuffix = editApiSuffix.getText().toString();
                if (apiSuffix.trim().length() > 0){
                    session.saveApiSuffix(apiSuffix);
                    showMessage("Suffix saved successfully");
                }else{
                    showMessage("Unable to save empty suffix");
                }
                break;

            case R.id.savePeerServer:
                String peerServer = editPeerServer.getText().toString();
                if (peerServer.trim().length() > 0){
                    if (peerServer.substring(0,4).equalsIgnoreCase("http") ||
                            peerServer.substring(0,4).equalsIgnoreCase("https")){
                        session.savePeerServer(peerServer);
                        showMessage("Peer Server saved successfully");
                    }else{
                        showMessage("Enter the correct URL starting with the scheme (http[s])");
                        return;
                    }
                }else{
                    showMessage("Unable to save empty peer server");
                }
                break;

            case R.id.savePeerServerPort:
                String peerServerPort = editPeerServerPort.getText().toString();
                if (peerServerPort.trim().length() > 0){
                    try{
                        int port = Integer.parseInt(peerServerPort);
                        session.savePeerServerPort(peerServerPort);
                        showMessage("Peer Server port saved successfully");
                    }catch (Exception e){
                        showMessage("Peer Server port must be an integer");
                    }

                }else{
                    showMessage("Unable to save empty peer server port");
                }
                break;
        }
        //reload this fragment

        Fragment fragment  = new SettingsFragment();
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                android.R.anim.fade_out);
        fragmentTransaction.replace(R.id.frame, fragment, MainActivity.CURRENT_TAG);

        fragmentTransaction.commitAllowingStateLoss();
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

    public void setUpViewMode(){
        editCloudUrl.setText(session.getCloudUrl());
        editApiVersion.setText(session.getApiVersion());
        editApiPrefix.setText(session.getApiPrefix());
        editApiSuffix.setText(session.getApiSuffix());
        editPeerServer.setText(session.getPeerServer());
        editPeerServerPort.setText(session.getPeerServerPort());
    }

    public void showMessage(String msg){
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }
}
