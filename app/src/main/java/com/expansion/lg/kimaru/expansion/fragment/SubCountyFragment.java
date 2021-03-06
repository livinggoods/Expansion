package com.expansion.lg.kimaru.expansion.fragment;

/**
 * Created by kimaru on 3/11/17.
 */

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.expansion.lg.kimaru.expansion.R;
import com.expansion.lg.kimaru.expansion.activity.MainActivity;
import com.expansion.lg.kimaru.expansion.activity.SessionManagement;
import com.expansion.lg.kimaru.expansion.dbhelpers.VillageListAdapter;
import com.expansion.lg.kimaru.expansion.mzigos.Mapping;
import com.expansion.lg.kimaru.expansion.mzigos.SubCounty;
import com.expansion.lg.kimaru.expansion.mzigos.Village;
import com.expansion.lg.kimaru.expansion.other.DividerItemDecoration;
import com.expansion.lg.kimaru.expansion.other.GpsTracker;
import com.expansion.lg.kimaru.expansion.tables.VillageTable;

import java.util.ArrayList;
import java.util.List;

import io.fabric.sdk.android.Fabric;

// to show list in Gmail Mode

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SubCountyFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SubCountyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SubCountyFragment extends Fragment  {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    TextView textshow;

    // to show list in Gmail Mode
    private List<Village> villages = new ArrayList<>();
    private RecyclerView recyclerView;
    private VillageListAdapter rAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ActionMode actionMode;
    private ActionModeCallback actionModeCallback;


    SessionManagement session;
    Mapping mapping;
    SubCounty subCounty;


    // I cant seem to get the context working
    Context mContext = getContext();
    Activity activity = getActivity();



    public SubCountyFragment() {
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
    public static SubCountyFragment newInstance(String param1, String param2) {
        SubCountyFragment fragment = new SubCountyFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_communityunits, container, false);
        textshow = (TextView) v.findViewById(R.id.textShow);
        //session Management
        session = new SessionManagement(getContext());
        mapping = session.getSavedMapping();
        subCounty = session.getSavedSubCounty();
        MainActivity.CURRENT_TAG =MainActivity.TAG_SUBCOUNTY;
        MainActivity.backFragment = new MapViewFragment();



        // ============RecyclerView View starts here =======================
        // RecyclerView View.

        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener( new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // onRefresh action here
                Toast.makeText(getContext(), "Refreshing the list", Toast.LENGTH_SHORT).show();
            }
        });

        rAdapter = new VillageListAdapter(this.getContext(), villages, new VillageListAdapter.VillageListAdapterListener() {
            @Override
            public void onIconClicked(int position) {
                if (actionMode == null) {
//                    actionMode = startSupportActionMode(actionModeCallback);
                    Toast.makeText(getContext(), "An Icon is clicked "+ position, Toast.LENGTH_SHORT).show();
                }

//                toggleSelection(position);
            }

            @Override
            public void onIconImportantClicked(int position) {
                Toast.makeText(getContext(), "An iconImportant is clicked", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onMessageRowClicked(int position) {
                // read the message which removes bold from the row
                Village village = villages.get(position);

                village.setRead(true);
                villages.set(position, village);
                rAdapter.notifyDataSetChanged();
            }

            @Override
            public void onRowLongClicked(int position) {
                // When one long presses a registration, we give them a chance to
                // Interview the selected applicant

                //extract the clicked recruitment



            }

        });
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(rAdapter);
        actionModeCallback = new ActionModeCallback();
        swipeRefreshLayout.post(
                new Runnable(){
                    @Override
                    public void run(){
                        getVillages();
                    }
                }
        );

//        actionModeCallback = new ActionMode().Callback;


        //===========Gmail View Ends here ============================
        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        String county = mapping.getCounty();
        String subCountyName = subCounty.getSubCountyName();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(county + " - "+ subCountyName + " Sub County");
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


    // ===================================== Gmail View Methods ====================================
    private void enableActionMode(int position) {
        if (actionMode == null) {
            Toast.makeText(getContext(), "Values of Enabled", Toast.LENGTH_SHORT).show();
        }
        Toast.makeText(getContext(), "Values of Enabled", Toast.LENGTH_SHORT).show();
        toggleSelection(position);
    }

    private void toggleSelection(int position) {
        rAdapter.toggleSelection(position);
        int count = rAdapter.getSelectedItemCount();

        if (count == 0) {
            actionMode.finish();
        } else {
            actionMode.setTitle(String.valueOf(count));
            actionMode.invalidate();
        }
    }
    /*
    *  Choose a random
    *  Color
     */
    private int getRandomMaterialColor(String typeColor) {
        int returnColor = Color.GRAY;
        int arrayId = getResources().getIdentifier("mdcolor_" + typeColor, "array", getContext().getPackageName());

        if (arrayId != 0) {
            TypedArray colors = getResources().obtainTypedArray(arrayId);
            int index = (int) (Math.random() * colors.length());
            returnColor = colors.getColor(index, Color.GRAY);
            colors.recycle();
        }
        return returnColor;
    }


    private class ActionModeCallback implements ActionMode.Callback {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.menu_action_mode, menu);

            // disable swipe refresh if action mode is enabled
            swipeRefreshLayout.setEnabled(false);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_delete:
                    // delete all the selected messages
                    deleteMessages();
                    mode.finish();
                    return true;

                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            rAdapter.clearSelections();
            swipeRefreshLayout.setEnabled(true);
            actionMode = null;
            recyclerView.post(new Runnable() {
                @Override
                public void run() {
                    rAdapter.resetAnimationIndex();
                    // mAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    // deleting the messages from recycler view
    private void deleteMessages() {
        rAdapter.resetAnimationIndex();
        List<Integer> selectedItemPositions =
                rAdapter.getSelectedItems();
        for (int i = selectedItemPositions.size() - 1; i >= 0; i--) {
            rAdapter.removeData(selectedItemPositions.get(i));
        }
        rAdapter.notifyDataSetChanged();
    }
    private void getVillages() {
        swipeRefreshLayout.setRefreshing(true);

        villages.clear();

        try {
            // get CUs
            VillageTable villageTable = new VillageTable(getContext());
            List<Village> villageList = new ArrayList<>();

            villageList = villageTable.getVillageData();
            for (Village village:villageList){
                village.setColor(getRandomMaterialColor("400"));
                villages.add(village);
            }
            rAdapter.notifyDataSetChanged();
            swipeRefreshLayout.setRefreshing(false);
        } catch (Exception error){
            Toast.makeText(getContext(), "No subcounty  found", Toast.LENGTH_SHORT).show();

            textshow.setText(" No subcounty recorded ");
        }
        swipeRefreshLayout.setRefreshing(false);
    }

    //====================================== End Gmail Methods======================================

}
